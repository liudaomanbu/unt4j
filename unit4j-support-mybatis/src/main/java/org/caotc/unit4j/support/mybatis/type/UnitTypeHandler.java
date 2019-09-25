package org.caotc.unit4j.support.mybatis.type;

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
import org.caotc.unit4j.core.unit.BasePrefixUnit;
import org.caotc.unit4j.core.unit.BaseStandardUnit;
import org.caotc.unit4j.core.unit.CompositePrefixUnit;
import org.caotc.unit4j.core.unit.CompositeStandardUnit;
import org.caotc.unit4j.core.unit.Unit;

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
@MappedTypes(value = {Unit.class, BaseStandardUnit.class, BasePrefixUnit.class,
    CompositeStandardUnit.class, CompositePrefixUnit.class})
public class UnitTypeHandler extends BaseTypeHandler<Unit> {

  @Override
  public void setNonNullParameter(PreparedStatement ps, int i, Unit parameter, JdbcType jdbcType)
      throws SQLException {
    if (Objects.isNull(jdbcType)) {
      ps.setObject(i, parameter);
    } else {
      ps.setObject(i, parameter, jdbcType.TYPE_CODE);
    }
  }

  @Override
  public Unit getNullableResult(ResultSet rs, String columnName) throws SQLException {
    return null;
  }

  @Override
  public Unit getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    return null;
  }

  @Override
  public Unit getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
    return null;
  }
}
