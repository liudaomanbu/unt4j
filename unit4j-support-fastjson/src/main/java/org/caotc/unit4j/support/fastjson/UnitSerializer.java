package org.caotc.unit4j.support.fastjson;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import lombok.NonNull;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.caotc.unit4j.core.unit.Unit;
import org.caotc.unit4j.support.UnitCodecConfig;
import org.caotc.unit4j.support.fastjson.util.CodecUtil;

import java.lang.reflect.Type;

/**
 * {@link org.caotc.unit4j.core.unit.Unit}在fastjson的序列化器
 *
 * @author caotc
 * @date 2019-04-24
 * @since 1.0.0
 */
@Value(staticConstructor = "of")
@Slf4j
public class UnitSerializer implements ObjectSerializer {

  @NonNull
  public static UnitSerializer of(@NonNull UnitCodecConfig codecConfig) {
    return of(codecConfig, codecConfig);
  }

  /**
   * {@link org.caotc.unit4j.core.unit.Unit}的序列化反序列化配置
   */
  @NonNull
  UnitCodecConfig codecConfig;
  @NonNull
  UnitCodecConfig propertyCodecConfig;

  @Override
  public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType,
                    int features) {
    log.debug("object:{},fieldName:{},fieldType:{},features:{}", object, fieldName, fieldType, features);
    Unit unit = (Unit) object;

    //是否作为属性
    UnitCodecConfig unitCodecConfig = CodecUtil.isProperty(serializer.getContext()) ? codecConfig() : propertyCodecConfig();
    String serialize = serialize(unitCodecConfig, unit);
    serializer.write(serialize);
  }

  static String serialize(@NonNull UnitCodecConfig unitCodecConfig, @NonNull Unit unit) {
    switch (unitCodecConfig.strategy()) {
      case AS_ALIAS:
        return unitCodecConfig.aliasSerializer().serialize(unit);
      case AS_ID:
      default:
        return unit.id();
    }
  }
}