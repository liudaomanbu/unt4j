package org.caotc.unit4j.support.mybatis;

import java.sql.Statement;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;

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
    Statement statement = (Statement) invocation.getArgs()[0];
    return invocation.proceed();
  }

  @Override
  public Object plugin(Object target) {
    return Plugin.wrap(target, this);
  }

  @Override
  public void setProperties(Properties properties) {

  }
}
