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
import org.caotc.unit4j.support.SerializeCommands;

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
@MappedTypes(value = {SerializeCommands.class})
public class SerializeCommandsTypeHandler extends BaseTypeHandler<SerializeCommands> {

  @Override
  public void setNonNullParameter(PreparedStatement ps, int i, SerializeCommands parameter,
      JdbcType jdbcType)
      throws SQLException {
    if (Objects.isNull(jdbcType)) {
      ps.setObject(i, parameter);
    } else {
      ps.setObject(i, parameter, jdbcType.TYPE_CODE);
    }
  }

  @Override
  public SerializeCommands getNullableResult(ResultSet rs, String columnName) throws SQLException {
    return null;
  }

  @Override
  public SerializeCommands getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    return null;
  }

  @Override
  public SerializeCommands getNullableResult(CallableStatement cs, int columnIndex)
      throws SQLException {
    return null;
  }
}
