package org.caotc.unit4j.support.mybatis.sql.visitor;

import java.util.List;
import lombok.NonNull;
import lombok.Value;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItem;

/**
 * @author caotc
 * @date 2019-09-19
 * @since 1.0.0
 */
@Value
public class ColumnAddSelectVisitor extends AbstractSelectVisitor {

  @NonNull
  Column column;

  public ColumnAddSelectVisitor(@NonNull String columnName) {
    this.column = new Column(columnName);
  }

  @Override
  public void visit(PlainSelect plainSelect) {
    List<SelectItem> selectItems = plainSelect.getSelectItems();
    selectItems.add(new SelectExpressionItem(column));
  }
}
