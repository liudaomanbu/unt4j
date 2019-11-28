/*
 * Copyright (C) 2019 the original author or authors.
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
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import lombok.NonNull;
import lombok.Value;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.StatementVisitorAdapter;
import net.sf.jsqlparser.statement.insert.Insert;
import org.apache.ibatis.mapping.ParameterMapping;

/**
 * @author caotc
 * @date 2019-11-20
 * @since 1.0.0
 */
@Value(staticConstructor = "create")
public class ParameterMappingMatchColumnInsertUpdateVisitor extends StatementVisitorAdapter {

  @NonNull
  public static ParameterMappingMatchColumnInsertUpdateVisitor create(
      @NonNull List<ParameterMapping> parameterMappings) {
    return create(ImmutableList.copyOf(parameterMappings));
  }

  ImmutableList<ParameterMapping> parameterMappings;
  Map<Column, ParameterMapping> columnToParameterMappings = Maps.newHashMap();

  @Override
  public void visit(Insert insert) {
    JdbcParameterIndexCollectItemsListVisitor jdbcParameterIndexCollectItemsListVisitor = JdbcParameterIndexCollectItemsListVisitor
        .create();
    insert.getItemsList().accept(jdbcParameterIndexCollectItemsListVisitor);
    List<Integer> jdbcParameterIndexs = jdbcParameterIndexCollectItemsListVisitor
        .jdbcParameterIndexs();
    Preconditions.checkArgument(jdbcParameterIndexs.size() == parameterMappings.size());
    for (int i = 0; i < jdbcParameterIndexs.size(); i++) {
      columnToParameterMappings
          .put(insert.getColumns().get(jdbcParameterIndexs.get(i)), parameterMappings.get(i));
    }
  }
}
