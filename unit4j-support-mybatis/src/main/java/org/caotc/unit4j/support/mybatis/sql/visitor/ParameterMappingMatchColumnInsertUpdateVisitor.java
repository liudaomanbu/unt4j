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

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import lombok.NonNull;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.JdbcParameter;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.StatementVisitorAdapter;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.update.Update;
import org.apache.ibatis.mapping.ParameterMapping;

/**
 * @author caotc
 * @date 2019-11-20
 * @since 1.0.0
 */
@Value(staticConstructor = "create")
@Slf4j
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
        for (int i = 0; i < jdbcParameterIndexs.size(); i++) {
            columnToParameterMappings
                .put(insert.getColumns().get(jdbcParameterIndexs.get(i)), parameterMappings.get(i));
        }
    }

    @Override
    public void visit(Update update) {
        log.debug("update:{}", update);
        List<Expression> expressions = update.getExpressions();
        List<Integer> jdbcParameterIndexs = Lists.newArrayList();
        for (int i = 0; i < expressions.size(); i++) {
            Expression expression = expressions.get(i);
            if (expression instanceof JdbcParameter) {
                jdbcParameterIndexs.add(i);
            }
        }

        //TODO 处理where的JdbcParameter
        for (int i = 0; i < jdbcParameterIndexs.size(); i++) {
            columnToParameterMappings
                    .put(update.getColumns().get(jdbcParameterIndexs.get(i)), parameterMappings.get(i));
        }
    }
}
