package org.caotc.unit4j.support.mybatis;

import com.google.common.collect.ImmutableList;
import java.sql.Connection;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import lombok.NonNull;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitor;
import net.sf.jsqlparser.expression.JdbcParameter;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.ItemsListVisitor;
import net.sf.jsqlparser.expression.operators.relational.MultiExpressionList;
import net.sf.jsqlparser.expression.operators.relational.NamedExpressionList;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItem;
import net.sf.jsqlparser.statement.select.SubSelect;
import net.sf.jsqlparser.statement.update.Update;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
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
import org.caotc.unit4j.core.common.reflect.ReadableProperty;
import org.caotc.unit4j.core.common.util.ReflectionUtil;
import org.caotc.unit4j.core.constant.StringConstant;
import org.caotc.unit4j.core.exception.NeverHappenException;
import org.caotc.unit4j.support.AmountCodecConfig;
import org.caotc.unit4j.support.CodecStrategy;
import org.caotc.unit4j.support.SerializeCommand;
import org.caotc.unit4j.support.SerializeCommands;
import org.caotc.unit4j.support.Unit4jProperties;
import org.caotc.unit4j.support.annotation.AmountSerialize;
import org.caotc.unit4j.support.mybatis.sql.visitor.AbstractExpressionVisitor;
import org.caotc.unit4j.support.mybatis.sql.visitor.AbstractSelectItemVisitor;
import org.caotc.unit4j.support.mybatis.sql.visitor.AbstractSelectVisitor;
import org.caotc.unit4j.support.mybatis.sql.visitor.RecursionExpressionVisitor;

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
public class AmountInterceptor implements Interceptor {

  private static final String STATEMENT_HANDLER_MAPPED_STATEMENT_FIELD_NAME = "delegate.mappedStatement";
  private static final ItemsListVisitor ADD_PARAMETER_ITEMS_LIST_VISITOR = new ItemsListVisitor() {

    @Override
    public void visit(SubSelect subSelect) {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void visit(ExpressionList expressionList) {
      expressionList.getExpressions().add(new JdbcParameter());
    }

    @Override
    public void visit(NamedExpressionList namedExpressionList) {
      log.debug("namedExpressionList:{}", namedExpressionList);
    }

    @Override
    public void visit(MultiExpressionList multiExprList) {
      throw new UnsupportedOperationException("Not supported yet.");
    }
  };
  private static final ItemsListVisitor REMOVE_PARAMETER_ITEMS_LIST_VISITOR = new ItemsListVisitor() {

    @Override
    public void visit(SubSelect subSelect) {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void visit(ExpressionList expressionList) {
      expressionList.getExpressions().remove(0);
    }

    @Override
    public void visit(NamedExpressionList namedExpressionList) {
      log.debug("namedExpressionList:{}", namedExpressionList);
    }

    @Override
    public void visit(MultiExpressionList multiExprList) {
      throw new UnsupportedOperationException("Not supported yet.");
    }
  };

  private static final ExpressionVisitor REMOVE_EXPRESSION_VISITOR = new AbstractExpressionVisitor() {

  };

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
    BoundSql boundSql = handler.getBoundSql();
    log.debug("原sql:{}", boundSql.getSql());
    log.debug("getParameterObject:{}", boundSql.getParameterObject());
    log.debug("getParameterMappings:{}", boundSql.getParameterMappings());

    Statement parse = CCJSqlParserUtil.parse(boundSql.getSql());

    final List<Column> columns;

    if (SqlCommandType.INSERT.equals(sqlCommandType)) {
      Insert insert = (Insert) parse;
      columns = insert.getColumns();
    } else if (SqlCommandType.UPDATE.equals(sqlCommandType)) {
      Update update = (Update) parse;
      columns = update.getColumns();
      Expression where = update.getWhere();
      log.warn("update where:{}", where);
      where.accept(RecursionExpressionVisitor.create(new AbstractExpressionVisitor() {
        @Override
        public void visit(EqualsTo equalsTo) {
          super.visit(equalsTo);
        }
      }));
    } else if (SqlCommandType.SELECT.equals(sqlCommandType)) {
      List<ResultMap> resultMaps = mappedStatement.getResultMaps();
      Select select = (Select) parse;
      select.getSelectBody().accept(new AbstractSelectVisitor() {
        @Override
        public void visit(PlainSelect plainSelect) {
          List<SelectItem> selectItems = plainSelect.getSelectItems();
          selectItems.forEach(selectItem -> {
            selectItem.accept(new AbstractSelectItemVisitor() {
              @Override
              public void visit(SelectExpressionItem selectExpressionItem) {
                log.debug("SelectExpressionItem:{}", selectExpressionItem);
                Expression expression = selectExpressionItem.getExpression();
                expression.accept(new AbstractExpressionVisitor() {
                  @Override
                  public void visit(Column tableColumn) {
                    if ("doctor_team_id".equals(tableColumn.getColumnName())) {
                      tableColumn.setColumnName("doctor_team_id_new");
                    }
                  }
                });
              }
            });
          });
          selectItems.add(new SelectExpressionItem(new Column("new_sytfhfshjsyj")));
        }
      });
      return invocation.proceed();
//      columns=null;
    } else {
      return invocation.proceed();
    }

    ImmutableList<SqlParam> sqlParams = Optional.ofNullable(boundSql.getParameterMappings())
        .map(parameterMappings -> createSqlParams(parameterMappings, columns))
        .orElseGet(ImmutableList::of);
    sqlParams.stream().filter(sqlParam -> {
      ReadableProperty<?, Object> propertyReader = readableProperty(sqlParam.parameterMapping,
          boundSql.getParameterObject(), mappedStatement);
      return propertyReader.annotation(AmountSerialize.class).isPresent() || propertyReader
          .propertyType().getRawType()
          .equals(Amount.class);
    }).forEach(sqlParam -> {
      ParameterMapping parameterMapping = sqlParam.parameterMapping;
      String property = parameterMapping.getProperty();
      Amount amount = (Amount) mappedStatement.getConfiguration()
          .newMetaObject(boundSql.getParameterObject()).getValue(property);
      AmountCodecConfig amountCodecConfig = unit4jProperties
          .createAmountCodecConfig(sqlParam.column.getColumnName(),
              readableProperty(sqlParam.parameterMapping, boundSql.getParameterObject(),
                  mappedStatement).annotation(AmountSerialize.class)
                  .orElse(null));
      SerializeCommands serializeCommands = amountCodecConfig
          .serializeCommandsFromAmount(amount);
      execute(parse, serializeCommands, boundSql, sqlParam, mappedStatement);
    });
    SystemMetaObject.forObject(boundSql).setValue("sql", parse.toString());
    log.debug("修改后sql:{}", boundSql.getSql());
    return invocation.proceed();
  }

  private void execute(@NonNull Statement statement, @NonNull SerializeCommands serializeCommands,
      @NonNull BoundSql boundSql, @NonNull SqlParam sqlParam,
      @NonNull MappedStatement mappedStatement) {
    for (SerializeCommand serializeCommand : serializeCommands) {
      switch (serializeCommand.type()) {
        case REMOVE_ORIGINAL_FIELD:
          sqlParam.removeFieldName(statement, boundSql);
          break;
        case WRITE_VALUE:
          if (serializeCommand.fieldValue() instanceof SerializeCommands) {
            execute(statement, (SerializeCommands) serializeCommand.fieldValue(), boundSql,
                sqlParam, mappedStatement);
          } else {
            sqlParam.setValue(boundSql, mappedStatement, serializeCommand.fieldValue());
          }
          break;
        case WRITE_FIELD:
          sqlParam.addFieldName(statement, boundSql, serializeCommand.fieldName(),
              serializeCommand.fieldValue(), mappedStatement);
          if (serializeCommand.fieldValue() instanceof SerializeCommands) {
            execute(statement, (SerializeCommands) serializeCommand.fieldValue(), boundSql,
                sqlParam, mappedStatement);
          } else {
            boundSql.setAdditionalParameter(serializeCommand.fieldName(),
                serializeCommand.fieldValue());
          }
          break;
        case WRITE_FIELD_SEPARATOR:
          break;
        /**
         * 只有 {@link CodecStrategy#OBJECT}才会产生以下指令,DB序列化暂时不支持此策略
         */
        case START_OBJECT:
        case END_OBJECT:
        default:
          throw NeverHappenException.instance();
      }
    }
  }

  @Override
  public Object plugin(Object target) {
    return Plugin.wrap(target, this);
  }

  @Override
  public void setProperties(Properties properties) {

  }

  private ReadableProperty<?, Object> readableProperty(@NonNull ParameterMapping parameterMapping,
      @NonNull Object parameterObject, @NonNull MappedStatement mappedStatement) {
    if (parameterMapping.getProperty().contains(StringConstant.DOT)) {
      ImmutableList<String> propertyNames = ImmutableList
          .copyOf(StringConstant.DOT_SPLITTER.split(parameterMapping.getProperty()));
      //获取复杂属性名的最后一层属性名之前的属性名
      String directPropertyName = StringConstant.DOT_JOINER
          .join(propertyNames.subList(0, propertyNames.size() - 1));
      Object directParameterObject = mappedStatement.getConfiguration()
          .newMetaObject(parameterObject).getValue(directPropertyName);
      return ReflectionUtil
          .readablePropertyFromClass(directParameterObject.getClass(),
              propertyNames.get(propertyNames.size() - 1)).orElseThrow(
              NeverHappenException::instance);
    } else {
      return ReflectionUtil
          .readablePropertyFromClass(parameterObject.getClass(),
              parameterMapping.getProperty())
          .orElseThrow(NeverHappenException::instance);
    }
  }

  private ImmutableList<SqlParam> createSqlParams(@NonNull List<ParameterMapping> parameterMappings,
      @NonNull List<Column> columns) {
    return IntStream.range(0, columns.size())
        .mapToObj(index -> SqlParam
            .create(parameterMappings.get(index),
                columns.size() > index ? columns.get(index) : null, index))
        .collect(ImmutableList.toImmutableList());
  }

  @Value(staticConstructor = "create")
  private static class SqlParam {

    @NonNull
    ParameterMapping parameterMapping;
    Column column;
    int jdbcParameterIndex;

    private void removeFieldName(@NonNull Statement statement, @NonNull BoundSql boundSql) {
      boundSql.getParameterMappings().remove(parameterMapping);
      if (statement instanceof Insert) {
        Insert insert = (Insert) statement;
        insert.getColumns().remove(column);
        insert.getItemsList().accept(REMOVE_PARAMETER_ITEMS_LIST_VISITOR);
      }
      if (statement instanceof Update) {
        Update update = (Update) statement;
        update.getColumns().remove(column);
        update.getExpressions().add(new JdbcParameter());
      }
      if (statement instanceof Delete) {
        Delete delete = (Delete) statement;
//        delete.getWhere().accept();
      }
    }

    private void setValue(@NonNull BoundSql boundSql, @NonNull MappedStatement mappedStatement,
        @NonNull Object value) {
      boundSql.setAdditionalParameter(parameterMapping.getProperty(), value);
      boundSql.getParameterMappings().set(jdbcParameterIndex,
          new ParameterMapping.Builder(mappedStatement.getConfiguration(),
              parameterMapping.getProperty(), value.getClass()).build());
    }

    private void addFieldName(@NonNull Statement statement, @NonNull BoundSql boundSql,
        @NonNull String fieldName, @NonNull Object fieldValue,
        @NonNull MappedStatement mappedStatement) {
      if (statement instanceof Insert) {
        Insert insert = (Insert) statement;
        insert.getColumns().add(new Column(fieldName));
        insert.getItemsList().accept(ADD_PARAMETER_ITEMS_LIST_VISITOR);
      }
      if (statement instanceof Update) {
        Update update = (Update) statement;
        update.getColumns().add(new Column(fieldName));
        update.getExpressions().add(new JdbcParameter());
      }
      boundSql.getParameterMappings().add(
          new ParameterMapping.Builder(mappedStatement.getConfiguration(),
              fieldName, fieldValue.getClass())
              .build());
    }
  }
}
