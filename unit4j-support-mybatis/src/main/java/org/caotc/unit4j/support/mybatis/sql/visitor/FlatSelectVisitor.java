package org.caotc.unit4j.support.mybatis.sql.visitor;

import com.google.common.base.Preconditions;
import lombok.NonNull;
import lombok.Value;
import net.sf.jsqlparser.statement.select.PlainSelect;
import org.caotc.unit4j.support.AmountCodecConfig;
import org.caotc.unit4j.support.CodecStrategy;

/**
 * {@link org.caotc.unit4j.support.CodecStrategy#FLAT}策略的查询语句访问器
 *
 * @author caotc
 * @date 2019-09-19
 * @since 1.0.0
 */
@Value
public class FlatSelectVisitor extends AbstractSelectVisitor {

  @NonNull
  ColumnRenameSelectVisitor columnRenameSelectVisitor;
  @NonNull
  ColumnAddSelectVisitor columnAddSelectVisitor;

  public FlatSelectVisitor(@NonNull AmountCodecConfig amountCodecConfig) {
    Preconditions.checkArgument(CodecStrategy.FLAT == amountCodecConfig.strategy());
    this.columnRenameSelectVisitor = new ColumnRenameSelectVisitor(amountCodecConfig.outputName()
        , amountCodecConfig.outputValueName());
    this.columnAddSelectVisitor = new ColumnAddSelectVisitor(amountCodecConfig.outputUnitName());
  }

  @Override
  public void visit(PlainSelect plainSelect) {
    plainSelect.accept(columnRenameSelectVisitor);
    plainSelect.accept(columnAddSelectVisitor);
  }
}
