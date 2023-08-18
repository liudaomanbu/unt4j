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

package org.caotc.unit4j.support.mybatis.type.handler;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.caotc.unit4j.core.math.number.BigFractionAdapter;
import org.caotc.unit4j.core.math.number.Number;
import org.caotc.unit4j.core.math.number.Numbers;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

/**
 * 如果没有该TypeHandler,会导致mybatis解析{@link org.apache.ibatis.mapping.BoundSql}时直接报错,无法在{@link
 * Interceptor}中对sql进行操作
 *
 * @author caotc
 * @date 2018-10-09
 * @implSpec
 * @implNote
 * @since 1.0.0
 **/
@Value
@MappedTypes(value = {Number.class, BigFractionAdapter.class})
@Slf4j
public class NumberTypeHandler extends BaseTypeHandler<Number> {

  @Override
  public void setNonNullParameter(PreparedStatement ps, int i, Number parameter,
      JdbcType jdbcType)
      throws SQLException {
    if (Objects.isNull(jdbcType)) {
      ps.setObject(i, parameter);
    } else {
      ps.setObject(i, parameter, jdbcType.TYPE_CODE);
    }
  }

  @Override
  public Number getNullableResult(ResultSet rs, String columnName) throws SQLException {
      //TODO 指定类型
    java.math.BigDecimal value = rs.getBigDecimal(columnName);
      log.debug("columnName:{},value:{}", columnName, value);
    return Numbers.valueOf(value);
  }

  @Override
  public Number getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    return Numbers.valueOf(rs.getBigDecimal(columnIndex));
  }

  @Override
  public Number getNullableResult(CallableStatement cs, int columnIndex)
      throws SQLException {
    return Numbers.valueOf(cs.getBigDecimal(columnIndex));
  }
}
