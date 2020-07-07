/*
 * Copyright (C) 2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.caotc.unit4j.support.mybatis.interceptor;

import com.google.common.collect.ImmutableList;
import lombok.Getter;
import lombok.NonNull;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.JdbcParameter;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.ItemsListVisitorAdapter;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.update.Update;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.caotc.unit4j.api.annotation.CodecStrategy;
import org.caotc.unit4j.core.common.base.CaseFormat;
import org.caotc.unit4j.core.common.reflect.property.ReadableProperty;
import org.caotc.unit4j.core.common.util.ReflectionUtil;
import org.caotc.unit4j.core.constant.StringConstant;
import org.caotc.unit4j.core.exception.ReadablePropertyNotFoundException;
import org.caotc.unit4j.support.AmountCodecConfig;
import org.caotc.unit4j.support.Unit4jProperties;
import org.caotc.unit4j.support.common.util.AmountUtil;
import org.caotc.unit4j.support.mybatis.sql.visitor.ParameterMappingMatchColumnInsertUpdateVisitor;
import org.caotc.unit4j.support.mybatis.util.PluginUtil;

import java.sql.Connection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Stream;

/**
 * @author caotc
 * @date 2019-05-31
 * @since 1.0.0
 */
@Intercepts({
        @Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class,
                Integer.class})
})
@Slf4j
public class InsertUpdateInterceptor implements Interceptor {

    private static final String STATEMENT_HANDLER_MAPPED_STATEMENT_FIELD_NAME = "delegate.mappedStatement";

    Unit4jProperties unit4jProperties = new Unit4jProperties()
            .setFieldNameSplitter(CaseFormat.LOWER_UNDERSCORE::split)
            .setFieldNameJoiner((valueFieldNameWords, objectFieldNameWords) -> CaseFormat.LOWER_UNDERSCORE
                    .join(Stream.concat(objectFieldNameWords.stream(), valueFieldNameWords.stream())));

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler handler = (StatementHandler) PluginUtil.processTarget(invocation.getTarget());
        MappedStatement mappedStatement = (MappedStatement) SystemMetaObject.forObject(handler)
                .getValue(STATEMENT_HANDLER_MAPPED_STATEMENT_FIELD_NAME);
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
        BoundSql boundSql = handler.getBoundSql();

        Statement parse = CCJSqlParserUtil.parse(boundSql.getSql());

        if (SqlCommandType.INSERT == sqlCommandType
                || SqlCommandType.UPDATE == sqlCommandType) {
            log.debug("original sql:{}", parse);

            List<Column> columns = SqlCommandType.INSERT == sqlCommandType ? ((Insert) parse).getColumns()
                    : ((Update) parse).getColumns();
            log.debug("ParameterMappings:{}", boundSql.getParameterMappings());
            ParameterMappingMatchColumnInsertUpdateVisitor parameterMappingMatchColumnInsertUpdateVisitor = ParameterMappingMatchColumnInsertUpdateVisitor
                    .create(boundSql.getParameterMappings());
            parse.accept(parameterMappingMatchColumnInsertUpdateVisitor);
            parameterMappingMatchColumnInsertUpdateVisitor.columnToParameterMappings().entrySet().stream()
                    .map(entry -> SqlParam.create(entry.getValue(), entry.getKey(),
                            readableProperty(entry.getValue(), boundSql.getParameterObject(), mappedStatement)
                                    .orElseThrow(() ->
                                            ReadablePropertyNotFoundException.create(boundSql.getParameterObject().getClass(), entry.getValue().getProperty()))))
                    .filter(sqlParam -> AmountUtil.isAmountProperty(sqlParam.readableProperty))
                    .forEach(sqlParam -> {
                        //noinspection unchecked
                        AmountUtil
                                .readAmount((ReadableProperty<? super Object, ?>) sqlParam.readableProperty,
                                        boundSql.getParameterObject()).ifPresent(amount -> {
                            AmountCodecConfig amountCodecConfig = unit4jProperties
                                    .createPropertyAmountCodecConfig(sqlParam.readableProperty);

                            if (Objects.nonNull(amountCodecConfig.targetUnit()) && !amountCodecConfig
                                    .targetUnit().equals(amount.unit())) {
                                amount = amount.convertTo(amountCodecConfig.targetUnit());
                            }
                            boundSql.setAdditionalParameter(sqlParam.parameterMapping.getProperty()
                                    , amount.value(amountCodecConfig.valueCodecConfig().valueType(),
                                            amountCodecConfig.valueCodecConfig().mathContext()));

                            switch (amountCodecConfig.strategy()) {
                                case VALUE:
                                    break;
                                case FLAT:
                                    ParameterMapping valueParameterMapping = new ParameterMapping.Builder(mappedStatement.getConfiguration(), sqlParam.parameterMapping.getProperty(), amountCodecConfig.valueCodecConfig().valueType()).build();
                                    boundSql.getParameterMappings().set(boundSql.getParameterMappings().indexOf(sqlParam.parameterMapping), valueParameterMapping);

                                    sqlParam.column.setColumnName(amountCodecConfig.outputValueName());

                                    Column unitColumn = new Column(sqlParam.column.getTable(),
                                            amountCodecConfig.outputUnitName());
                                    if (!columns.contains(unitColumn)) {
                                        columns.add(unitColumn);
                                        if (SqlCommandType.INSERT == sqlCommandType) {
                                            ((Insert) parse).getItemsList().accept(new ItemsListVisitorAdapter() {
                                                @Override
                                                public void visit(ExpressionList expressionList) {
                                                    List<Expression> expressions = expressionList.getExpressions();
                                                    //TODO 参数顺序处理
                                                    expressions.add(new JdbcParameter(expressions.size() + 1, false));
                                                }
                                            });
                                        }
                                        if (SqlCommandType.UPDATE == sqlCommandType) {
                                            List<Expression> expressions = ((Update) parse).getExpressions();
                                            //TODO 参数顺序处理
                                            expressions.add(new JdbcParameter(expressions.size() + 1, false));
                                        }
                                    }

                                    ParameterMapping unitParameterMapping = new ParameterMapping.Builder(
                                            mappedStatement.getConfiguration(),
                                            //TODO 单位序列化配置
                                            sqlParam.directPropertyName() + unitColumn.getColumnName(), String.class)
                                            .build();
                                    //TODO 处理顺序
                                    boundSql.getParameterMappings().add(parameterMappingMatchColumnInsertUpdateVisitor.columnToParameterMappings().size(), unitParameterMapping);


                                    boundSql.setAdditionalParameter(amountCodecConfig.outputUnitName(),
                                            amount.unit().id());
                                    break;
                                case OBJECT:
                                    throw new IllegalArgumentException(
                                            "database strategy can't use " + CodecStrategy.OBJECT);
                                default:
                                    throw new IllegalArgumentException();
                            }
                        });
                    });
            log.debug("new sql:{}", parse);
            SystemMetaObject.forObject(boundSql).setValue("sql", parse.toString());

        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }

    private Optional<? extends ReadableProperty<?, ?>> readableProperty(
            @NonNull ParameterMapping parameterMapping,
            @NonNull Object parameterObject, @NonNull MappedStatement mappedStatement) {
        Class<?> clazz = parameterObject.getClass();
        String fieldName = parameterMapping.getProperty();
        if (parameterMapping.getProperty().contains(StringConstant.DOT)) {
            ImmutableList<String> propertyNames = ImmutableList
                    .copyOf(StringConstant.DOT_SPLITTER.split(parameterMapping.getProperty()));
            //获取复杂属性名的最后一层属性名之前的属性名
            String directPropertyName = StringConstant.DOT_JOINER
                    .join(propertyNames.subList(0, propertyNames.size() - 1));
            Object directParameterObject = mappedStatement.getConfiguration()
                    .newMetaObject(parameterObject).getValue(directPropertyName);
            if (Objects.isNull(directParameterObject)) {
                return Optional.empty();
            }
            clazz = directParameterObject.getClass();
            fieldName = propertyNames.get(propertyNames.size() - 1);
        }
        return ReflectionUtil
                .readablePropertyFromClass(clazz, fieldName);
    }

    @Value(staticConstructor = "create")
    private static class SqlParam {

        @NonNull
        ParameterMapping parameterMapping;
        @NonNull
        Column column;
        @NonNull
        ReadableProperty<?, ?> readableProperty;
        //复杂属性名的最后一层属性名之前的属性名
        @Getter(lazy = true)
        @NonNull
        String directPropertyName = generateDirectPropertyName();

        private String generateDirectPropertyName() {
            if (parameterMapping().getProperty().contains(StringConstant.DOT)) {
                ImmutableList<String> propertyNames = ImmutableList
                        .copyOf(StringConstant.DOT_SPLITTER.split(parameterMapping().getProperty()));
                return StringConstant.DOT_JOINER
                        .join(propertyNames.subList(0, propertyNames.size() - 1));
            }
            return StringConstant.EMPTY;
        }
    }
}
