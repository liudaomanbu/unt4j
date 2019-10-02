package org.caotc.unit4j.support.mybatis.sql.visitor;

import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.ItemsListVisitor;
import net.sf.jsqlparser.expression.operators.relational.MultiExpressionList;
import net.sf.jsqlparser.expression.operators.relational.NamedExpressionList;
import net.sf.jsqlparser.statement.select.SubSelect;

/**
 * @author caotc
 * @date 2019-09-26
 * @since 1.0.0
 */
@Slf4j
public class RemoveParameterItemsListVisitor implements ItemsListVisitor {

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
}
