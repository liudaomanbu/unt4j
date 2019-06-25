package org.caotc.unit4j.support.mybatis.sql;

import net.sf.jsqlparser.expression.AllComparisonExpression;
import net.sf.jsqlparser.expression.AnalyticExpression;
import net.sf.jsqlparser.expression.AnyComparisonExpression;
import net.sf.jsqlparser.expression.CaseExpression;
import net.sf.jsqlparser.expression.CastExpression;
import net.sf.jsqlparser.expression.CollateExpression;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;
import net.sf.jsqlparser.expression.DateValue;
import net.sf.jsqlparser.expression.DoubleValue;
import net.sf.jsqlparser.expression.ExpressionVisitor;
import net.sf.jsqlparser.expression.ExtractExpression;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.HexValue;
import net.sf.jsqlparser.expression.IntervalExpression;
import net.sf.jsqlparser.expression.JdbcNamedParameter;
import net.sf.jsqlparser.expression.JdbcParameter;
import net.sf.jsqlparser.expression.JsonExpression;
import net.sf.jsqlparser.expression.KeepExpression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.MySQLGroupConcat;
import net.sf.jsqlparser.expression.NextValExpression;
import net.sf.jsqlparser.expression.NotExpression;
import net.sf.jsqlparser.expression.NullValue;
import net.sf.jsqlparser.expression.NumericBind;
import net.sf.jsqlparser.expression.OracleHierarchicalExpression;
import net.sf.jsqlparser.expression.OracleHint;
import net.sf.jsqlparser.expression.Parenthesis;
import net.sf.jsqlparser.expression.RowConstructor;
import net.sf.jsqlparser.expression.SignedExpression;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.TimeKeyExpression;
import net.sf.jsqlparser.expression.TimeValue;
import net.sf.jsqlparser.expression.TimestampValue;
import net.sf.jsqlparser.expression.UserVariable;
import net.sf.jsqlparser.expression.ValueListExpression;
import net.sf.jsqlparser.expression.WhenClause;
import net.sf.jsqlparser.expression.operators.arithmetic.Addition;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseAnd;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseLeftShift;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseOr;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseRightShift;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseXor;
import net.sf.jsqlparser.expression.operators.arithmetic.Concat;
import net.sf.jsqlparser.expression.operators.arithmetic.Division;
import net.sf.jsqlparser.expression.operators.arithmetic.Modulo;
import net.sf.jsqlparser.expression.operators.arithmetic.Multiplication;
import net.sf.jsqlparser.expression.operators.arithmetic.Subtraction;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.Between;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExistsExpression;
import net.sf.jsqlparser.expression.operators.relational.GreaterThan;
import net.sf.jsqlparser.expression.operators.relational.GreaterThanEquals;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.IsNullExpression;
import net.sf.jsqlparser.expression.operators.relational.JsonOperator;
import net.sf.jsqlparser.expression.operators.relational.LikeExpression;
import net.sf.jsqlparser.expression.operators.relational.Matches;
import net.sf.jsqlparser.expression.operators.relational.MinorThan;
import net.sf.jsqlparser.expression.operators.relational.MinorThanEquals;
import net.sf.jsqlparser.expression.operators.relational.NotEqualsTo;
import net.sf.jsqlparser.expression.operators.relational.RegExpMatchOperator;
import net.sf.jsqlparser.expression.operators.relational.RegExpMySQLOperator;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.SubSelect;

/**
 * 表达式访问器接口的空实现
 *
 * @author caotc
 * @date 2019-06-07
 * @since 1.0.0
 */
public class AbstractExpressionVisitor implements ExpressionVisitor {

  @Override
  public void visit(BitwiseRightShift aThis) {

  }

  @Override
  public void visit(BitwiseLeftShift aThis) {

  }

  @Override
  public void visit(NullValue nullValue) {

  }

  @Override
  public void visit(Function function) {

  }

  @Override
  public void visit(SignedExpression signedExpression) {

  }

  @Override
  public void visit(JdbcParameter jdbcParameter) {

  }

  @Override
  public void visit(JdbcNamedParameter jdbcNamedParameter) {

  }

  @Override
  public void visit(DoubleValue doubleValue) {

  }

  @Override
  public void visit(LongValue longValue) {

  }

  @Override
  public void visit(HexValue hexValue) {

  }

  @Override
  public void visit(DateValue dateValue) {

  }

  @Override
  public void visit(TimeValue timeValue) {

  }

  @Override
  public void visit(TimestampValue timestampValue) {

  }

  @Override
  public void visit(Parenthesis parenthesis) {

  }

  @Override
  public void visit(StringValue stringValue) {

  }

  @Override
  public void visit(Addition addition) {

  }

  @Override
  public void visit(Division division) {

  }

  @Override
  public void visit(Multiplication multiplication) {

  }

  @Override
  public void visit(Subtraction subtraction) {

  }

  @Override
  public void visit(AndExpression andExpression) {

  }

  @Override
  public void visit(OrExpression orExpression) {

  }

  @Override
  public void visit(Between between) {

  }

  @Override
  public void visit(EqualsTo equalsTo) {

  }

  @Override
  public void visit(GreaterThan greaterThan) {

  }

  @Override
  public void visit(GreaterThanEquals greaterThanEquals) {

  }

  @Override
  public void visit(InExpression inExpression) {

  }

  @Override
  public void visit(IsNullExpression isNullExpression) {

  }

  @Override
  public void visit(LikeExpression likeExpression) {

  }

  @Override
  public void visit(MinorThan minorThan) {

  }

  @Override
  public void visit(MinorThanEquals minorThanEquals) {

  }

  @Override
  public void visit(NotEqualsTo notEqualsTo) {

  }

  @Override
  public void visit(Column tableColumn) {

  }

  @Override
  public void visit(SubSelect subSelect) {

  }

  @Override
  public void visit(CaseExpression caseExpression) {

  }

  @Override
  public void visit(WhenClause whenClause) {

  }

  @Override
  public void visit(ExistsExpression existsExpression) {

  }

  @Override
  public void visit(AllComparisonExpression allComparisonExpression) {

  }

  @Override
  public void visit(AnyComparisonExpression anyComparisonExpression) {

  }

  @Override
  public void visit(Concat concat) {

  }

  @Override
  public void visit(Matches matches) {

  }

  @Override
  public void visit(BitwiseAnd bitwiseAnd) {

  }

  @Override
  public void visit(BitwiseOr bitwiseOr) {

  }

  @Override
  public void visit(BitwiseXor bitwiseXor) {

  }

  @Override
  public void visit(CastExpression cast) {

  }

  @Override
  public void visit(Modulo modulo) {

  }

  @Override
  public void visit(AnalyticExpression aexpr) {

  }

  @Override
  public void visit(ExtractExpression eexpr) {

  }

  @Override
  public void visit(IntervalExpression iexpr) {

  }

  @Override
  public void visit(OracleHierarchicalExpression oexpr) {

  }

  @Override
  public void visit(RegExpMatchOperator rexpr) {

  }

  @Override
  public void visit(JsonExpression jsonExpr) {

  }

  @Override
  public void visit(JsonOperator jsonExpr) {

  }

  @Override
  public void visit(RegExpMySQLOperator regExpMySQLOperator) {

  }

  @Override
  public void visit(UserVariable var) {

  }

  @Override
  public void visit(NumericBind bind) {

  }

  @Override
  public void visit(KeepExpression aexpr) {

  }

  @Override
  public void visit(MySQLGroupConcat groupConcat) {

  }

  @Override
  public void visit(ValueListExpression valueList) {

  }

  @Override
  public void visit(RowConstructor rowConstructor) {

  }

  @Override
  public void visit(OracleHint hint) {

  }

  @Override
  public void visit(TimeKeyExpression timeKeyExpression) {

  }

  @Override
  public void visit(DateTimeLiteralExpression literal) {

  }

  @Override
  public void visit(NotExpression aThis) {

  }

  @Override
  public void visit(NextValExpression aThis) {

  }

  @Override
  public void visit(CollateExpression aThis) {

  }
}
