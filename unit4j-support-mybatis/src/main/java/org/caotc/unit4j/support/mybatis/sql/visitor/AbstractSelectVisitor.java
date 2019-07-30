package org.caotc.unit4j.support.mybatis.sql.visitor;

import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.SelectVisitor;
import net.sf.jsqlparser.statement.select.SetOperationList;
import net.sf.jsqlparser.statement.select.WithItem;
import net.sf.jsqlparser.statement.values.ValuesStatement;

/**
 * select语句访问器空实现
 *
 * @author caotc
 * @date 2019-07-27
 * @since 1.0.0
 */
public abstract class AbstractSelectVisitor implements SelectVisitor {

  @Override
  public void visit(PlainSelect plainSelect) {

  }

  @Override
  public void visit(SetOperationList setOpList) {

  }

  @Override
  public void visit(WithItem withItem) {

  }

  @Override
  public void visit(ValuesStatement aThis) {

  }
}
