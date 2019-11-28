/*
 * Copyright (C) 2019 the original author or authors.
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

import java.sql.Statement;
import java.util.Collection;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.type.TypeHandlerRegistry;

/**
 * 结果集拦截器
 *
 * @author caotc
 * @date 2019-07-24
 * @since 1.0.0
 */
@Intercepts({
    @Signature(type = ResultSetHandler.class, method = "handleResultSets", args = {Statement.class})
})
@Slf4j
public class ResultSetHandlerInterceptor implements Interceptor {

  @Override
  public Object intercept(Invocation invocation) throws Throwable {
    ResultSetHandler handler = (ResultSetHandler) invocation.getTarget();
    TypeHandlerRegistry typeHandlerRegistry = (TypeHandlerRegistry) SystemMetaObject
        .forObject(handler)
        .getValue("typeHandlerRegistry");
    log.error("TypeHandlerRegistry:{}", typeHandlerRegistry);
    Statement statement = (Statement) invocation.getArgs()[0];
//    MappedStatement mappedStatement = (MappedStatement) SystemMetaObject.forObject(handler)
//        .getValue("mappedStatement");
//    ResultMap resultMap = mappedStatement.getResultMaps().get(0);
//    ResultMapping resultMapping = resultMap.getResultMappings().stream()
//        .filter(mapping -> mapping.getProperty().equals("amountFlat")).findFirst().orElse(null);
//    log.info("resultMapping:{}",resultMap);
//    SystemMetaObject.forObject(resultMapping).setValue("javaType", Amount.class);
//    log.info("resultMapping:{}",resultMap);
    Object result = invocation.proceed();
    log.error("resultSet:{}", statement.getResultSet());
    if (result instanceof Collection) {
      Collection<?> collection = (Collection<?>) result;

    }
    return result;
  }

  @Override
  public Object plugin(Object target) {
    return Plugin.wrap(target, this);
  }

  @Override
  public void setProperties(Properties properties) {

  }
}
