package org.caotc.unit4j.support.fastjson;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import java.io.IOException;
import java.lang.reflect.Type;
import lombok.NonNull;
import lombok.Value;
import org.caotc.unit4j.core.unit.Unit;
import org.caotc.unit4j.support.UnitCodecConfig;

/**
 * {@link org.caotc.unit4j.core.unit.Unit}在fastjson的序列化器
 *
 * @author caotc
 * @date 2019-04-24
 * @since 1.0.0
 */
@Value
public class UnitSerializer implements ObjectSerializer {

  /**
   * {@link org.caotc.unit4j.core.unit.Unit}的序列化反序列化配置
   */
  @NonNull
  UnitCodecConfig unitCodecConfig;

  @Override
  public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType,
      int features) throws IOException {
    Unit unit = (Unit) object;
    serializer.write(unitCodecConfig.serialize(unit));
  }

}
