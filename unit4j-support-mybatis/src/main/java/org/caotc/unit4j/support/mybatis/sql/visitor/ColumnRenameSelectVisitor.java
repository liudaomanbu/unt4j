package org.caotc.unit4j.support.mybatis.sql.visitor;

import java.util.List;
import lombok.NonNull;
import lombok.Value;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.SelectItem;

/**
 * @author caotc
 * @date 2019-09-19
 * @since 1.0.0
 */
@Value
public class ColumnRenameSelectVisitor extends AbstractSelectVisitor {

  @NonNull
  ColumnRenameSelectItemVisitor columnRenameSelectItemVisitor;

  public ColumnRenameSelectVisitor(@NonNull String originalColumnName,
      @NonNull String targetColumnName) {
    this.columnRenameSelectItemVisitor = new ColumnRenameSelectItemVisitor(originalColumnName,
        targetColumnName);
  }

  @Override
  public void visit(PlainSelect plainSelect) {
    List<SelectItem> selectItems = plainSelect.getSelectItems();
    selectItems.forEach(selectItem -> {
      selectItem.accept(columnRenameSelectItemVisitor);
    });
  }
}
