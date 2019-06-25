package org.caotc.unit4j.support.mybatis.sql;

import java.util.Collection;
import java.util.Optional;
import lombok.NonNull;
import lombok.Value;
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
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
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
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.ExpressionListItem;
import net.sf.jsqlparser.statement.select.FunctionItem;
import net.sf.jsqlparser.statement.select.OrderByElement;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SubSelect;

/**
 * 递归表达式访问器
 *
 * @author caotc
 * @date 2019-06-07
 * @since 1.0.0
 */
@Value(staticConstructor = "create")
public class RecursionExpressionVisitor implements ExpressionVisitor {

  /**
   * 被代理的表达式访问器
   */
  @NonNull
  ExpressionVisitor expressionVisitor;

  @Override
  public void visit(BitwiseRightShift aThis) {
    expressionVisitor.visit(aThis);
    aThis.getLeftExpression().accept(this);
    aThis.getRightExpression().accept(this);
  }

  @Override
  public void visit(BitwiseLeftShift aThis) {
    expressionVisitor.visit(aThis);
    aThis.getLeftExpression().accept(this);
    aThis.getRightExpression().accept(this);
  }

  @Override
  public void visit(NullValue nullValue) {
    expressionVisitor.visit(nullValue);
  }

  @Override
  public void visit(Function function) {
    expressionVisitor.visit(function);
    visit(function.getKeep());
    function.getAttribute().accept(this);
    function.getNamedParameters().getExpressions().forEach(expression -> expression.accept(this));
    function.getParameters().getExpressions().forEach(expression -> expression.accept(this));
  }

  @Override
  public void visit(SignedExpression signedExpression) {
    expressionVisitor.visit(signedExpression);
    signedExpression.getExpression().accept(this);
  }

  @Override
  public void visit(JdbcParameter jdbcParameter) {
    expressionVisitor.visit(jdbcParameter);
  }

  @Override
  public void visit(JdbcNamedParameter jdbcNamedParameter) {
    expressionVisitor.visit(jdbcNamedParameter);
  }

  @Override
  public void visit(DoubleValue doubleValue) {
    expressionVisitor.visit(doubleValue);
  }

  @Override
  public void visit(LongValue longValue) {
    expressionVisitor.visit(longValue);
  }

  @Override
  public void visit(HexValue hexValue) {
    expressionVisitor.visit(hexValue);
  }

  @Override
  public void visit(DateValue dateValue) {
    expressionVisitor.visit(dateValue);
  }

  @Override
  public void visit(TimeValue timeValue) {
    expressionVisitor.visit(timeValue);
  }

  @Override
  public void visit(TimestampValue timestampValue) {
    expressionVisitor.visit(timestampValue);
  }

  @Override
  public void visit(Parenthesis parenthesis) {
    expressionVisitor.visit(parenthesis);
    parenthesis.getExpression().accept(this);
  }

  @Override
  public void visit(StringValue stringValue) {
    expressionVisitor.visit(stringValue);
  }

  @Override
  public void visit(Addition addition) {
    expressionVisitor.visit(addition);
    addition.getLeftExpression().accept(this);
    addition.getRightExpression().accept(this);
  }

  @Override
  public void visit(Division division) {
    expressionVisitor.visit(division);
    division.getLeftExpression().accept(this);
    division.getRightExpression().accept(this);
  }

  @Override
  public void visit(Multiplication multiplication) {
    expressionVisitor.visit(multiplication);
    multiplication.getLeftExpression().accept(this);
    multiplication.getRightExpression().accept(this);
  }

  @Override
  public void visit(Subtraction subtraction) {
    expressionVisitor.visit(subtraction);
    subtraction.getLeftExpression().accept(this);
    subtraction.getRightExpression().accept(this);
  }

  @Override
  public void visit(AndExpression andExpression) {
    expressionVisitor.visit(andExpression);
    andExpression.getLeftExpression().accept(this);
    andExpression.getRightExpression().accept(this);
  }

  @Override
  public void visit(OrExpression orExpression) {
    expressionVisitor.visit(orExpression);
    orExpression.getLeftExpression().accept(this);
    orExpression.getRightExpression().accept(this);
  }

  @Override
  public void visit(Between between) {
    expressionVisitor.visit(between);
    between.getLeftExpression().accept(this);
    between.getBetweenExpressionStart().accept(this);
    between.getBetweenExpressionEnd().accept(this);
  }

  @Override
  public void visit(EqualsTo equalsTo) {
    expressionVisitor.visit(equalsTo);
    equalsTo.getLeftExpression().accept(this);
    equalsTo.getRightExpression().accept(this);
  }

  @Override
  public void visit(GreaterThan greaterThan) {
    expressionVisitor.visit(greaterThan);
    greaterThan.getLeftExpression().accept(this);
    greaterThan.getRightExpression().accept(this);
  }

  @Override
  public void visit(
      GreaterThanEquals greaterThanEquals) {
    expressionVisitor.visit(greaterThanEquals);
    greaterThanEquals.getLeftExpression().accept(this);
    greaterThanEquals.getRightExpression().accept(this);
  }

  @Override
  public void visit(InExpression inExpression) {
    expressionVisitor.visit(inExpression);
    inExpression.getLeftExpression().accept(this);
  }

  @Override
  public void visit(
      IsNullExpression isNullExpression) {
    expressionVisitor.visit(isNullExpression);
    isNullExpression.getLeftExpression().accept(this);
  }

  @Override
  public void visit(LikeExpression likeExpression) {
    expressionVisitor.visit(likeExpression);
    likeExpression.getLeftExpression().accept(this);
    likeExpression.getRightExpression().accept(this);
  }

  @Override
  public void visit(MinorThan minorThan) {
    expressionVisitor.visit(minorThan);
    minorThan.getLeftExpression().accept(this);
    minorThan.getRightExpression().accept(this);
  }

  @Override
  public void visit(MinorThanEquals minorThanEquals) {
    expressionVisitor.visit(minorThanEquals);
    minorThanEquals.getLeftExpression().accept(this);
    minorThanEquals.getRightExpression().accept(this);
  }

  @Override
  public void visit(NotEqualsTo notEqualsTo) {
    expressionVisitor.visit(notEqualsTo);
    notEqualsTo.getLeftExpression().accept(this);
    notEqualsTo.getRightExpression().accept(this);
  }

  @Override
  public void visit(Column tableColumn) {
    expressionVisitor.visit(tableColumn);
    Optional.ofNullable(tableColumn.getTable()).map(Table::getPivot).ifPresent(pivot -> {
          pivot.getForColumns().forEach(this::visit);
          pivot.getFunctionItems().stream().map(FunctionItem::getFunction).forEach(this::visit);
          pivot.getMultiInItems().stream().map(ExpressionListItem::getExpressionList)
              .map(ExpressionList::getExpressions).flatMap(
              Collection::stream).forEach(expression -> expression.accept(this));
          pivot.getSingleInItems().stream().map(SelectExpressionItem::getExpression)
              .forEach(expression -> expression.accept(this));
        }
    );
  }

  @Override
  public void visit(SubSelect subSelect) {
    expressionVisitor.visit(subSelect);
    Optional.ofNullable(subSelect.getPivot()).ifPresent(pivot -> {
          pivot.getForColumns().forEach(this::visit);
          pivot.getFunctionItems().stream().map(FunctionItem::getFunction).forEach(this::visit);
          pivot.getMultiInItems().stream().map(ExpressionListItem::getExpressionList)
              .map(ExpressionList::getExpressions).flatMap(
              Collection::stream).forEach(expression -> expression.accept(this));
          pivot.getSingleInItems().stream().map(SelectExpressionItem::getExpression)
              .forEach(expression -> expression.accept(this));
        }
    );
  }

  @Override
  public void visit(CaseExpression caseExpression) {
    expressionVisitor.visit(caseExpression);
    caseExpression.getElseExpression().accept(this);
    caseExpression.getSwitchExpression().accept(this);
    caseExpression.getWhenClauses().forEach(this::visit);
  }

  @Override
  public void visit(WhenClause whenClause) {
    expressionVisitor.visit(whenClause);
    whenClause.getThenExpression().accept(this);
    whenClause.getWhenExpression().accept(this);
  }

  @Override
  public void visit(
      ExistsExpression existsExpression) {
    expressionVisitor.visit(existsExpression);
    existsExpression.getRightExpression().accept(this);
  }

  @Override
  public void visit(AllComparisonExpression allComparisonExpression) {
    expressionVisitor.visit(allComparisonExpression);
    visit(allComparisonExpression.getSubSelect());
  }

  @Override
  public void visit(AnyComparisonExpression anyComparisonExpression) {
    expressionVisitor.visit(anyComparisonExpression);
    visit(anyComparisonExpression.getSubSelect());
  }

  @Override
  public void visit(Concat concat) {
    expressionVisitor.visit(concat);
    concat.getLeftExpression().accept(this);
    concat.getRightExpression().accept(this);
  }

  @Override
  public void visit(Matches matches) {
    expressionVisitor.visit(matches);
    matches.getLeftExpression().accept(this);
    matches.getRightExpression().accept(this);
  }

  @Override
  public void visit(BitwiseAnd bitwiseAnd) {
    expressionVisitor.visit(bitwiseAnd);
    bitwiseAnd.getLeftExpression().accept(this);
    bitwiseAnd.getRightExpression().accept(this);
  }

  @Override
  public void visit(BitwiseOr bitwiseOr) {
    expressionVisitor.visit(bitwiseOr);
    bitwiseOr.getLeftExpression().accept(this);
    bitwiseOr.getRightExpression().accept(this);
  }

  @Override
  public void visit(BitwiseXor bitwiseXor) {
    expressionVisitor.visit(bitwiseXor);
    bitwiseXor.getLeftExpression().accept(this);
    bitwiseXor.getRightExpression().accept(this);
  }

  @Override
  public void visit(CastExpression cast) {
    expressionVisitor.visit(cast);
    cast.getLeftExpression().accept(this);
  }

  @Override
  public void visit(Modulo modulo) {
    expressionVisitor.visit(modulo);
    modulo.getLeftExpression().accept(this);
    modulo.getRightExpression().accept(this);
  }

  @Override
  public void visit(AnalyticExpression aexpr) {
    expressionVisitor.visit(aexpr);
    aexpr.getDefaultValue().accept(this);
    aexpr.getExpression().accept(this);
    visit(aexpr.getKeep());
    aexpr.getOffset().accept(this);
    aexpr.getPartitionExpressionList().getExpressions()
        .forEach(expression -> expression.accept(this));
    aexpr.getOrderByElements().stream().map(OrderByElement::getExpression)
        .forEach(expression -> expression.accept(this));
    aexpr.getWindowElement().getOffset().getExpression().accept(this);
    aexpr.getWindowElement().getRange().getStart().getExpression().accept(this);
    aexpr.getWindowElement().getRange().getEnd().getExpression().accept(this);
  }

  @Override
  public void visit(ExtractExpression eexpr) {
    expressionVisitor.visit(eexpr);
    eexpr.getExpression().accept(this);
  }

  @Override
  public void visit(IntervalExpression iexpr) {
    expressionVisitor.visit(iexpr);
    iexpr.getExpression().accept(this);
  }

  @Override
  public void visit(OracleHierarchicalExpression oexpr) {
    expressionVisitor.visit(oexpr);
    oexpr.getConnectExpression().accept(this);
    oexpr.getStartExpression().accept(this);
  }

  @Override
  public void visit(RegExpMatchOperator rexpr) {
    expressionVisitor.visit(rexpr);
    rexpr.getLeftExpression().accept(this);
    rexpr.getRightExpression().accept(this);
  }

  @Override
  public void visit(JsonExpression jsonExpr) {
    expressionVisitor.visit(jsonExpr);
    visit(jsonExpr.getColumn());
  }

  @Override
  public void visit(JsonOperator jsonExpr) {
    expressionVisitor.visit(jsonExpr);
    jsonExpr.getLeftExpression().accept(this);
    jsonExpr.getRightExpression().accept(this);
  }

  @Override
  public void visit(
      RegExpMySQLOperator regExpMySQLOperator) {
    expressionVisitor.visit(regExpMySQLOperator);
    regExpMySQLOperator.getLeftExpression().accept(this);
    regExpMySQLOperator.getRightExpression().accept(this);
  }

  @Override
  public void visit(UserVariable var) {
    expressionVisitor.visit(var);
  }

  @Override
  public void visit(NumericBind bind) {
    expressionVisitor.visit(bind);
  }

  @Override
  public void visit(KeepExpression aexpr) {
    expressionVisitor.visit(aexpr);
    aexpr.getOrderByElements().stream().map(OrderByElement::getExpression)
        .forEach(expression -> expression.accept(this));
  }

  @Override
  public void visit(MySQLGroupConcat groupConcat) {
    expressionVisitor.visit(groupConcat);
    groupConcat.getOrderByElements().stream().map(OrderByElement::getExpression)
        .forEach(expression -> expression.accept(this));
    groupConcat.getExpressionList().getExpressions().forEach(expression -> expression.accept(this));
  }

  @Override
  public void visit(ValueListExpression valueList) {
    expressionVisitor.visit(valueList);
    valueList.getExpressionList().getExpressions().forEach(expression -> expression.accept(this));
  }

  @Override
  public void visit(RowConstructor rowConstructor) {
    expressionVisitor.visit(rowConstructor);
    rowConstructor.getExprList().getExpressions().forEach(expression -> expression.accept(this));
  }

  @Override
  public void visit(OracleHint hint) {
    expressionVisitor.visit(hint);
  }

  @Override
  public void visit(TimeKeyExpression timeKeyExpression) {
    expressionVisitor.visit(timeKeyExpression);
  }

  @Override
  public void visit(DateTimeLiteralExpression literal) {
    expressionVisitor.visit(literal);
  }

  @Override
  public void visit(NotExpression aThis) {
    expressionVisitor.visit(aThis);
    aThis.getExpression().accept(this);
  }

  @Override
  public void visit(NextValExpression aThis) {
    expressionVisitor.visit(aThis);
  }

  @Override
  public void visit(CollateExpression aThis) {
    expressionVisitor.visit(aThis);
    aThis.getLeftExpression().accept(this);
  }
}
