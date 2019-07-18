package org.caotc.unit4j.support.jackson;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import java.util.List;
import lombok.NonNull;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.caotc.unit4j.support.Unit4jProperties;

@Value
@Slf4j
public class Unit4jBeanSerializerModifier extends BeanSerializerModifier {

  @NonNull
  Unit4jProperties unit4jProperties;

  @Override
  public List<BeanPropertyWriter> changeProperties(SerializationConfig config,
      BeanDescription beanDesc, List<BeanPropertyWriter> beanProperties) {
    log.debug("changeProperties");
    log.debug("configuration:{}", config);
    log.debug("beanDesc:{}", beanDesc);
    log.debug("beanProperties:{}", beanProperties);
//    return beanProperties.stream().filter(beanPropertyWriter->{
//      AmountSerialize annotation = beanPropertyWriter.getAnnotation(AmountSerialize.class);
//      AmountCodecConfig amountCodecConfig = unit4jProperties
//          .createAmountCodecConfig(beanPropertyWriter.getName(), annotation);
//      return amountCodecConfig.strategy()!= AmountCodecStrategy.FLAT;
//    }).collect(Collectors.toList());
    return beanProperties;
  }

  @Override
  public JsonSerializer<?> modifySerializer(SerializationConfig config, BeanDescription beanDesc,
      JsonSerializer<?> serializer) {
    log.debug("modifySerializer");
    return super.modifySerializer(config, beanDesc, serializer);
  }

}
