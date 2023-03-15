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
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Streams;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.select.Select;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.Configuration;
import org.caotc.unit4j.api.annotation.CodecStrategy;
import org.caotc.unit4j.core.Amount;
import org.caotc.unit4j.core.common.base.CaseFormat;
import org.caotc.unit4j.core.common.reflect.property.WritableProperty;
import org.caotc.unit4j.core.math.number.AbstractNumber;
import org.caotc.unit4j.core.unit.Unit;
import org.caotc.unit4j.support.AmountCodecConfig;
import org.caotc.unit4j.support.Unit4jProperties;
import org.caotc.unit4j.support.common.util.AmountUtil;
import org.caotc.unit4j.support.mybatis.constant.AmountPropertyConstant;
import org.caotc.unit4j.support.mybatis.sql.visitor.FlatSelectVisitor;
import org.caotc.unit4j.support.mybatis.util.PluginUtil;

import java.sql.Connection;
import java.util.Properties;
import java.util.stream.Collectors;
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
public class SelectInterceptor implements Interceptor {
    private static final String STATEMENT_HANDLER_MAPPED_STATEMENT_FIELD_NAME = "delegate.mappedStatement";
    private static final String MAPPED_STATEMENT_RESULT_MAPS_FIELD_NAME = "resultMaps";

    Unit4jProperties unit4jProperties = new Unit4jProperties()
            .setFieldNameSplitter(CaseFormat.LOWER_UNDERSCORE::split)
            .setFieldNameJoiner((valueFieldNameWords, objectFieldNameWords) -> CaseFormat.LOWER_UNDERSCORE
                    .join(Stream.concat(objectFieldNameWords.stream(), valueFieldNameWords.stream()).collect(Collectors.toList())));

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler handler = (StatementHandler) PluginUtil.processTarget(invocation.getTarget());
        MappedStatement mappedStatement = (MappedStatement) SystemMetaObject.forObject(handler)
                .getValue(STATEMENT_HANDLER_MAPPED_STATEMENT_FIELD_NAME);
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
        if (SqlCommandType.SELECT == sqlCommandType) {
            //多个resultMap时为存储过程,不处理
            if (mappedStatement.getResultMaps().size() == 1) {
                BoundSql boundSql = handler.getBoundSql();
                log.debug("original sql:{}", boundSql.getSql());

                Select select = (Select) CCJSqlParserUtil.parse(boundSql.getSql());

                ResultMap resultMap = mappedStatement.getResultMaps().get(0);
                log.debug("original resultMap type:{}", resultMap.getType());
                log.debug("original resultMap mappedProperties:{}", resultMap.getMappedProperties());
                log.debug("original resultMap mappedColumns:{}", resultMap.getMappedColumns());
                log.debug("original resultMap constructorResultMappings:{}", resultMap.getConstructorResultMappings());
                log.debug("original resultMap idResultMappings:{}", resultMap.getIdResultMappings());
                log.debug("original resultMap propertyResultMappings:{}", resultMap.getPropertyResultMappings());
                log.debug("original resultMap resultMappings:{}", resultMap.getResultMappings());


                ImmutableSet<String> resultMappingPropertyNames = resultMap.getResultMappings().stream()
                        .map(ResultMapping::getProperty)
                        .collect(ImmutableSet.toImmutableSet());

                ImmutableSet<WritableProperty<?, Amount>> writableAmountProperties = AmountUtil.writableAmountPropertyStreamFromClass(resultMap.getType())
                        .filter(writableProperty -> resultMappingPropertyNames.isEmpty() || resultMappingPropertyNames.contains(writableProperty.name()))
                        .collect(ImmutableSet.toImmutableSet());

                ImmutableSet<AmountCodecConfig> amountCodecConfigs = writableAmountProperties
                        .stream()
                        .map(unit4jProperties::createPropertyAmountCodecConfig)
                        .collect(ImmutableSet.toImmutableSet());

                ImmutableSet<ResultMapping> amountPropertyResultMappings = writableAmountProperties
                        .stream()
                        .flatMap(writableAmountProperty -> createAmountResultMapping(mappedStatement.getConfiguration(), writableAmountProperty))
                        .collect(ImmutableSet.toImmutableSet());

                ImmutableList<ResultMapping> resultMappings = Streams.concat(resultMap.getResultMappings().stream(), amountPropertyResultMappings.stream())
                        .collect(ImmutableList.toImmutableList());
                ResultMap newResultMap = new ResultMap.Builder(mappedStatement.getConfiguration(), resultMap.getId(), resultMap.getType(), resultMappings, resultMap.getAutoMapping())
                        .discriminator(resultMap.getDiscriminator()).build();


                mappedStatement.getConfiguration().newMetaObject(mappedStatement).setValue(MAPPED_STATEMENT_RESULT_MAPS_FIELD_NAME, ImmutableList.of(newResultMap));

                amountCodecConfigs.forEach(amountCodecConfig -> {
                    switch (amountCodecConfig.strategy()) {
                        case VALUE:
                            break;
                        case FLAT:
                            select.getSelectBody().accept(new FlatSelectVisitor(amountCodecConfig));
                            break;
                        case OBJECT:
                            throw new IllegalArgumentException(
                                    "database strategy can't use " + CodecStrategy.OBJECT);
                        default:
                            throw new IllegalArgumentException();
                    }
                });

                log.debug("new resultMap type:{}", resultMap.getType());
                log.debug("new resultMap mappedProperties:{}", resultMap.getMappedProperties());
                log.debug("new resultMap mappedColumns:{}", resultMap.getMappedColumns());
                log.debug("new resultMap constructorResultMappings:{}", resultMap.getConstructorResultMappings());
                log.debug("new resultMap idResultMappings:{}", resultMap.getIdResultMappings());
                log.debug("new resultMap propertyResultMappings:{}", resultMap.getPropertyResultMappings());
                log.debug("new resultMap resultMappings:{}", resultMap.getResultMappings());

                log.debug("new sql:{}", select.toString());
                SystemMetaObject.forObject(boundSql).setValue("sql", select.toString());
            }
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

    private Stream<ResultMapping> createAmountResultMapping(Configuration configuration, WritableProperty<?, ?> amountWritableProperty) {
        AmountCodecConfig amountCodecConfig = unit4jProperties.createPropertyAmountCodecConfig(amountWritableProperty);
        switch (amountCodecConfig.strategy()) {
            case OBJECT:
                throw new IllegalArgumentException(
                        "database strategy can't use " + CodecStrategy.OBJECT);
            case VALUE:
                //TODO outputName
                ResultMapping amountResultMapping = new ResultMapping.Builder(configuration, amountWritableProperty.name() + AmountPropertyConstant.DELIMITER + Amount.Fields.VALUE, amountCodecConfig.outputName(), AbstractNumber.class).build();
                return Stream.of(amountResultMapping);
            case FLAT:
                ResultMapping amountValueResultMapping = new ResultMapping.Builder(configuration, amountWritableProperty.name() + AmountPropertyConstant.DELIMITER + Amount.Fields.VALUE, amountCodecConfig.outputValueName(), AbstractNumber.class).build();
                ResultMapping amountUnitResultMapping = new ResultMapping.Builder(configuration, amountWritableProperty.name() + AmountPropertyConstant.DELIMITER + Amount.Fields.UNIT, amountCodecConfig.outputUnitName(), Unit.class).build();
                return Stream.of(amountValueResultMapping, amountUnitResultMapping);
            default:
                throw new IllegalStateException("Unexpected value: " + amountCodecConfig.strategy());
        }
    }
}
