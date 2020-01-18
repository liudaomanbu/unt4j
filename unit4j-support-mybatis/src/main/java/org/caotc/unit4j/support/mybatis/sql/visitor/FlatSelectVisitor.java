/*
 * Copyright (C) 2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.caotc.unit4j.support.mybatis.sql.visitor;

import com.google.common.base.Preconditions;
import lombok.NonNull;
import lombok.Value;
import net.sf.jsqlparser.statement.select.PlainSelect;
import org.caotc.unit4j.api.annotation.CodecStrategy;
import org.caotc.unit4j.support.AmountCodecConfig;

/**
 * {@link CodecStrategy#FLAT}策略的查询语句访问器
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
