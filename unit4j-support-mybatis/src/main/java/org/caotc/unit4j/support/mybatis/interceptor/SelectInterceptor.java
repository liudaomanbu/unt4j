package org.caotc.unit4j.support.mybatis.interceptor;

import com.google.common.collect.ImmutableSet;
import java.sql.Connection;
import java.util.Properties;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.Select;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.caotc.unit4j.core.Amount;
import org.caotc.unit4j.core.common.base.CaseFormat;
import org.caotc.unit4j.core.common.reflect.WritableProperty;
import org.caotc.unit4j.core.common.util.ReflectionUtil;
import org.caotc.unit4j.support.AmountCodecConfig;
import org.caotc.unit4j.support.CodecStrategy;
import org.caotc.unit4j.support.Unit4jProperties;
import org.caotc.unit4j.support.annotation.AmountSerialize;
import org.caotc.unit4j.support.mybatis.sql.visitor.FlatSelectVisitor;

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

  Unit4jProperties unit4jProperties = new Unit4jProperties()
      .setFieldNameSplitter(CaseFormat.LOWER_UNDERSCORE::split)
      .setFieldNameJoiner((valueFieldNameWords, objectFieldNameWords) -> CaseFormat.LOWER_UNDERSCORE
          .join(Stream.concat(objectFieldNameWords.stream(), valueFieldNameWords.stream())));

  @Override
  public Object intercept(Invocation invocation) throws Throwable {
    StatementHandler handler = (StatementHandler) invocation.getTarget();
    MappedStatement mappedStatement = (MappedStatement) SystemMetaObject.forObject(handler)
        .getValue(STATEMENT_HANDLER_MAPPED_STATEMENT_FIELD_NAME);
    SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
    if (SqlCommandType.SELECT == sqlCommandType) {
      //多个resultMap时为存储过程,不处理
      if (mappedStatement.getResultMaps().size() == 1) {
        BoundSql boundSql = handler.getBoundSql();
        Statement parse = CCJSqlParserUtil.parse(boundSql.getSql());

        ResultMap resultMap = mappedStatement.getResultMaps().get(0);
        Class<?> type = resultMap.getType();
        ImmutableSet<? extends WritableProperty<?, ?>> writableProperties = ReflectionUtil
            .writablePropertiesFromClass(type);
        ImmutableSet<AmountCodecConfig> amountCodecConfigs = writableProperties.stream()
            .filter(writableProperty ->
                Amount.class.equals(writableProperty.propertyType().getRawType())
                    || writableProperty.annotation(AmountSerialize.class).isPresent())
            .map(writableProperty -> unit4jProperties
                .createAmountCodecConfig(writableProperty.propertyName(),
                    writableProperty.annotation(AmountSerialize.class)
                        .orElse(null))).collect(ImmutableSet.toImmutableSet());

        Select select = (Select) parse;

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

}
