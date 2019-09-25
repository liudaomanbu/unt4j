package org.caotc.unit4j.support.mybatis.sql.visitor;

import lombok.NonNull;
import lombok.Value;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;

/**
 * @author caotc
 * @date 2019-09-19
 * @since 1.0.0
 */
@Value
public class ColumnRenameSelectItemVisitor extends AbstractSelectItemVisitor {

  @NonNull
  ColumnRenameExpressionVisitor columnRenameExpressionVisitor;

  public ColumnRenameSelectItemVisitor(@NonNull String originalColumnName,
      @NonNull String targetColumnName) {
    this.columnRenameExpressionVisitor = new ColumnRenameExpressionVisitor(originalColumnName,
        targetColumnName);
  }

  @Override
  public void visit(SelectExpressionItem selectExpressionItem) {
    Expression expression = selectExpressionItem.getExpression();
    expression.accept(columnRenameExpressionVisitor);
  }
}
