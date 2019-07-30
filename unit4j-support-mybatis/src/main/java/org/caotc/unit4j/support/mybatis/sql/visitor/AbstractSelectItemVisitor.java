package org.caotc.unit4j.support.mybatis.sql.visitor;

import net.sf.jsqlparser.statement.select.AllColumns;
import net.sf.jsqlparser.statement.select.AllTableColumns;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItemVisitor;

/**
 * select项访问器空实现
 *
 * @author caotc
 * @date 2019-07-27
 * @since 1.0.0
 */
public abstract class AbstractSelectItemVisitor implements SelectItemVisitor {

  @Override
  public void visit(AllColumns allColumns) {

  }

  @Override
  public void visit(AllTableColumns allTableColumns) {

  }

  @Override
  public void visit(SelectExpressionItem selectExpressionItem) {

  }
}
