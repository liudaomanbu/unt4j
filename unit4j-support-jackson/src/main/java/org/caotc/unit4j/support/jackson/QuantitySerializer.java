package org.caotc.unit4j.support.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import lombok.NonNull;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.caotc.unit4j.core.Quantity;
import org.caotc.unit4j.support.QuantityCodecConfig;

import java.io.IOException;

/**
 * {@link Quantity}在jackson中的序列化器 //TODO 考虑Spring环境时配置刷新问题
 *
 * @author caotc
 * @date 2019-04-24
 * @since 1.0.0
 */
@Value
@Slf4j
public class QuantitySerializer extends StdSerializer<Quantity> {

    /**
     * 序列化反序列化配置
     */
    @NonNull
    QuantityCodecConfig quantityCodecConfig;
    /**
     * 数值序列化器
     */
    @NonNull
    NumberSerializer numberSerializer;
    /**
     * 单位序列化器
     */
    @NonNull
    UnitSerializer unitSerializer;

    public QuantitySerializer(@NonNull QuantityCodecConfig quantityCodecConfig) {
        super(Quantity.class);
        this.quantityCodecConfig = quantityCodecConfig;
        this.numberSerializer = NumberSerializer.of(quantityCodecConfig.valueCodecConfig());
        unitSerializer = UnitSerializer.of(quantityCodecConfig.unitCodecConfig());
    }

    @Override
    public void serialize(Quantity value, JsonGenerator gen, SerializerProvider provider)
            throws IOException {
        log.debug("AmountSerializer");
        log.debug("value:{}", value);
        log.debug("gen:{}", gen);
        log.debug("provider:{}", provider);
    }

}
