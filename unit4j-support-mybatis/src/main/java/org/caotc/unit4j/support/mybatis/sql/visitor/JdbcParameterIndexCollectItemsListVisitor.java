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

import com.google.common.collect.Lists;
import java.util.List;
import lombok.Value;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.JdbcParameter;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.ItemsListVisitorAdapter;

/**
 * @author caotc
 * @date 2019-11-20
 * @since 1.0.0
 */
@Value(staticConstructor = "create")
public class JdbcParameterIndexCollectItemsListVisitor extends ItemsListVisitorAdapter {

  List<Integer> jdbcParameterIndexs = Lists.newArrayList();

  @Override
  public void visit(ExpressionList expressionList) {
    for (int i = 0; i < expressionList.getExpressions().size(); i++) {
      Expression expression = expressionList.getExpressions().get(i);
      if (expression instanceof JdbcParameter) {
        jdbcParameterIndexs.add(i);
      }
    }
  }
}
