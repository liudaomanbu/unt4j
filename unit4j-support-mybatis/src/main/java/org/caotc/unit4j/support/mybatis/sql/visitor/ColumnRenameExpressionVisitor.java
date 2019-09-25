package org.caotc.unit4j.support.mybatis.sql.visitor;

import lombok.NonNull;
import lombok.Value;
import net.sf.jsqlparser.schema.Column;

/**
 * @author caotc
 * @date 2019-09-19
 * @since 1.0.0
 */
@Value
public class ColumnRenameExpressionVisitor extends AbstractExpressionVisitor {

  @NonNull
  String originalColumnName;
  @NonNull
  String targetColumnName;

  @Override
  public void visit(Column tableColumn) {
    if (originalColumnName.equals(tableColumn.getColumnName())) {
      tableColumn.setColumnName(targetColumnName);
    }
  }
}
