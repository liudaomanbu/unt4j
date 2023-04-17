package org.caotc.unit4j.support.fastjson;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import lombok.NonNull;
import lombok.Value;
import org.caotc.unit4j.core.Quantity;
import org.caotc.unit4j.core.math.number.AbstractNumber;
import org.caotc.unit4j.support.QuantityValueCodecConfig;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * 单独{@link Quantity#value()}对象在fastjson的序列化器
 *
 * @author caotc
 * @date 2019-04-24
 * @since 1.0.0
 */
@Value
public class QuantityValueSerializer implements ObjectSerializer {

    /**
     * {@link Quantity#value()}的序列化反序列化配置
     */
    @NonNull
    QuantityValueCodecConfig quantityValueCodecConfig;

    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType,
                      int features) throws IOException {
    AbstractNumber value = (AbstractNumber) object;
        serializer.write(quantityValueCodecConfig.createSerializeCommands(value));
  }
}
