package org.caotc.unit4j.support.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonStreamContext;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.NameTransformer;
import lombok.NonNull;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.caotc.unit4j.api.annotation.QuantityCodecStrategy;
import org.caotc.unit4j.api.annotation.QuantitySerialize;
import org.caotc.unit4j.core.Quantity;
import org.caotc.unit4j.support.QuantityCodecConfig;
import org.caotc.unit4j.support.Unit4jProperties;

import java.io.IOException;
import java.util.Objects;

/**
 * {@link Quantity}在jackson中的上下文序列化器. 为了实现不同类中的{@link Quantity}属性通过注解实现不同策略序列化 ,在jackson中需要通过{@link
 * ContextualSerializer}实现给每个属性返回不同的{@link QuantitySerializer}//TODO 考虑Spring环境时配置刷新问题
 *
 * @author caotc
 * @date 2019-04-24
 * @since 1.0.0
 */
@Value(staticConstructor = "of")
@Slf4j
public class QuantitySerializer extends StdSerializer<Quantity> implements
        ContextualSerializer {
    @NonNull
    public static QuantitySerializer of(@NonNull QuantityCodecConfig codecConfig) {
        return of(codecConfig, NameTransformer.NOP);
    }

    @NonNull
    Unit4jProperties unit4jProperties = new Unit4jProperties();
    /**
     * 序列化反序列化配置
     */
    @NonNull
    QuantityCodecConfig codecConfig;
    @NonNull
    NameTransformer nameTransformer;


    QuantitySerializer(@NonNull QuantityCodecConfig codecConfig, @NonNull NameTransformer nameTransformer) {
        super(Quantity.class);
        this.codecConfig = codecConfig;
        this.nameTransformer = nameTransformer;
    }

    @Override
    public JsonSerializer<Quantity> unwrappingSerializer(NameTransformer unwrapper) {
        return QuantitySerializer.of(codecConfig(), unwrapper);
    }

    @Override
    public boolean isUnwrappingSerializer() {
        return codecConfig().strategy() == QuantityCodecStrategy.FLAT;
    }

    @Override
    public void serialize(Quantity value, JsonGenerator gen, SerializerProvider provider)
            throws IOException {
        log.debug("serialize value:{},gen:{},provider:{}", value, gen, provider);

        JsonStreamContext sc = gen.getOutputContext();
        log.error("serialize currentName:{}", sc.getCurrentName());

        QuantityCodecConfig codecConfig = codecConfig();

        UnitSerializer unitSerializer = UnitSerializer.of(codecConfig.unitCodecConfig());
        NumberSerializer numberSerializer = NumberSerializer.of(codecConfig.valueCodecConfig());
        switch (codecConfig.strategy()) {
            case OBJECT:
                gen.writeStartObject(value);
                gen.writeFieldName(nameTransformer().transform("unit"));
                unitSerializer.serialize(value.unit(), gen, provider);
                gen.writeFieldName(nameTransformer().transform("value"));
                numberSerializer.serialize(value.value(), gen, provider);
//                gen.writeObjectField(nameTransformer().transform("unit"), value.unit());
//                gen.writeObjectField(nameTransformer().transform("value"), value.value());
                gen.writeEndObject();
                break;
            case FLAT:
                gen.writeFieldName(nameTransformer().transform("unit"));
                unitSerializer.serialize(value.unit(), gen, provider);
                gen.writeFieldName(nameTransformer().transform("value"));
                numberSerializer.serialize(value.value(), gen, provider);
//                gen.writeObjectField(nameTransformer().transform("unit"), value.unit());
//                gen.writeObjectField(nameTransformer().transform("value"), value.value());
                break;
            case AS_VALUE:
            default:
                gen.writeObject(value.value());
        }
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property)
            throws JsonMappingException {
        log.error("ContextualSerializer");

        log.error("property:{}", property);
        //TODO 待确认
        if (property != null) {
            log.error("property name:{},fullName:{},wrapperName:{}", property.getName(), property.getFullName(), property.getWrapperName());
            if (Objects.equals(property.getType().getRawClass(), Quantity.class)) {
                QuantitySerialize quantitySerialize = property.getAnnotation(QuantitySerialize.class);
                if (quantitySerialize != null) {
//                return  QuantitySerializer.of(
//                        unit4jProperties.createPropertyQuantityCodecConfig(ReflectionUtil
//                                .readablePropertyExact(prov.getActiveView(), property.getName())));
                    return this;
                } else {
//                return new QuantitySerializer(unit4jProperties.createQuantityCodecConfig());
                    return this;
                }
            }
            return prov.findValueSerializer(property.getType(), property);
        }
        return this;
    }
}
