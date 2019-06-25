package org.caotc.unit4j.support.mybatis;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import lombok.Value;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.caotc.unit4j.core.Amount;
import org.caotc.unit4j.core.constant.UnitConstant;

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
@MappedTypes(value = {Amount.class})
public class AmountTypeHandler extends BaseTypeHandler<Amount> {

  @Override
  public void setNonNullParameter(PreparedStatement ps, int i, Amount parameter,
      JdbcType jdbcType)
      throws SQLException {
    if (Objects.isNull(jdbcType)) {
      ps.setObject(i, parameter);
    } else {
      ps.setObject(i, parameter, jdbcType.TYPE_CODE);
    }
  }

  @Override
  public Amount getNullableResult(ResultSet rs, String columnName)
      throws SQLException {
    //TODO 取不到类型，没有注解支持，无法知道取出来的值的单位
    return Amount.create(rs.getInt(columnName), UnitConstant.NON);
  }

  @Override
  public Amount getNullableResult(ResultSet rs, int columnIndex)
      throws SQLException {
    //TODO 取不到类型，没有注解支持，无法知道取出来的值的单位
    return Amount.create(rs.getInt(columnIndex), UnitConstant.NON);
  }

  @Override
  public Amount getNullableResult(CallableStatement cs, int columnIndex)
      throws SQLException {
    //TODO 取不到类型，没有注解支持，无法知道取出来的值的单位
    return Amount.create(cs.getInt(columnIndex), UnitConstant.NON);
  }

}
