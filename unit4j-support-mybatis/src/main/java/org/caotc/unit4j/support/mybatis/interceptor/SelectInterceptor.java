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
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Streams;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.select.Select;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
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
import java.util.List;
import java.util.Properties;
import java.util.function.Function;
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
                    .join(Stream.concat(objectFieldNameWords.stream(), valueFieldNameWords.stream())));

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
                Class<?> type = resultMap.getType();
                ImmutableSet<String> propertyNames = resultMap.getResultMappings().stream().map(ResultMapping::getProperty).collect(ImmutableSet.toImmutableSet());

//                AmountUtil.writableAmountPropertyStreamFromClass(resultMap.getType())
//                        .filter(writableProperty -> propertyNames.isEmpty() || propertyNames.contains(writableProperty.name()))
//                        .forEach();
                ImmutableMap<String, ResultMapping> propertyToResultMappings = resultMap.getResultMappings().stream()
                        .collect(ImmutableMap.toImmutableMap(ResultMapping::getProperty, Function.identity()));

                ResultMapping dataValueResultMapping = new ResultMapping.Builder(mappedStatement.getConfiguration(), "data-value", "data_value", AbstractNumber.class).build();
                ResultMapping dataUnitResultMapping = new ResultMapping.Builder(mappedStatement.getConfiguration(), "data-unit", "data_unit", Unit.class).build();
                List<ResultMapping> resultMappings = Streams.concat(resultMap.getResultMappings().stream(), Stream.of(dataValueResultMapping, dataUnitResultMapping)).collect(Collectors.toList());
                ResultMap newResultMap = new ResultMap.Builder(mappedStatement.getConfiguration(), resultMap.getId(), resultMap.getType(), resultMappings, resultMap.getAutoMapping())
                        .discriminator(resultMap.getDiscriminator()).build();


                mappedStatement.getConfiguration().newMetaObject(mappedStatement).setValue(MAPPED_STATEMENT_RESULT_MAPS_FIELD_NAME, ImmutableList.of(newResultMap));

                log.debug("getType:{}", type);
                log.debug("getMappedProperties:{}", resultMap.getMappedProperties());
                log.debug("getMappedColumns:{}", resultMap.getMappedColumns());
                log.debug("getConstructorResultMappings:{}", resultMap.getConstructorResultMappings());
                log.debug("getIdResultMappings:{}", resultMap.getIdResultMappings());
                log.debug("getPropertyResultMappings:{}", resultMap.getPropertyResultMappings());
                log.debug("getResultMappings:{}", resultMappings);


                ImmutableSet<AmountCodecConfig> amountCodecConfigs = AmountUtil
                        .writableAmountPropertyStreamFromClass(Object.class)
                        .filter(writableProperty -> resultMappings.stream()
                                .map(ResultMapping::getProperty).anyMatch(writableProperty.name()::equals))
                        .map(unit4jProperties::createPropertyAmountCodecConfig)
                        .collect(ImmutableSet.toImmutableSet());


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
                log.debug("sql:{}", select.toString());
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
        ResultMapping amountValueResultMapping;
        ResultMapping amountUnitResultMapping;
        switch (amountCodecConfig.strategy()) {
            case OBJECT:
                throw new IllegalArgumentException(
                        "database strategy can't use " + CodecStrategy.OBJECT);
            case VALUE:
                amountValueResultMapping = new ResultMapping.Builder(configuration, amountWritableProperty.name() + AmountPropertyConstant.DELIMITER + Amount.Fields.VALUE, amountCodecConfig.outputName(), AbstractNumber.class).build();
                amountUnitResultMapping = new ResultMapping.Builder(configuration, amountWritableProperty.name() + AmountPropertyConstant.DELIMITER + Amount.Fields.UNIT, amountCodecConfig.outputUnitName(), Unit.class).build();
                break;
            case FLAT:
                amountValueResultMapping = new ResultMapping.Builder(configuration, amountWritableProperty.name() + AmountPropertyConstant.DELIMITER + Amount.Fields.VALUE, amountCodecConfig.outputValueName(), AbstractNumber.class).build();
                amountUnitResultMapping = new ResultMapping.Builder(configuration, amountWritableProperty.name() + AmountPropertyConstant.DELIMITER + Amount.Fields.UNIT, amountCodecConfig.outputUnitName(), Unit.class).build();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + amountCodecConfig.strategy());
        }
        return Stream.of(amountValueResultMapping, amountUnitResultMapping);
    }
}
