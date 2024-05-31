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
    Unit4jProperties unit4jProperties = new Unit4jProperties();
    /**
     * 序列化反序列化配置
     */
    @NonNull
    QuantityCodecConfig codecConfig;
    @NonNull
    QuantityCodecConfig propertyCodecConfig;


    public QuantitySerializer(@NonNull QuantityCodecConfig codecConfig, @NonNull QuantityCodecConfig propertyCodecConfig) {
        super(Quantity.class);
        this.codecConfig = codecConfig;
        this.propertyCodecConfig = propertyCodecConfig;
        validate();
    }

    private void validate() {
        if (codecConfig().strategy() == QuantityCodecStrategy.FLAT) {
            throw new IllegalArgumentException(String.format("strategy %s only support property", QuantityCodecStrategy.FLAT));
        }
    }

    @Override
    public JsonSerializer<Quantity> unwrappingSerializer(NameTransformer unwrapper) {
        return this;
    }

    @Override
    public boolean isUnwrappingSerializer() {
//        return super.isUnwrappingSerializer();
        return true;
    }

    @Override
    public void serialize(Quantity value, JsonGenerator gen, SerializerProvider provider)
            throws IOException {
        log.error("serialize");
        gen.writeObjectField("unit", value.unit());
        gen.writeObjectField("value", value.value());

        JsonStreamContext sc = gen.getOutputContext();

        QuantityCodecConfig codecConfig;
        //是否作为属性
        if (sc.inRoot() || sc.inArray()) {
            codecConfig = codecConfig();
        } else {
            codecConfig = propertyCodecConfig();
        }

//        switch (codecConfig.strategy()) {
//            case FLAT:
//                List<String> propertyNameWords = caseFormat.split(propertyName);
//
//                /*
//                  fastjson中想要实现直接不输出该属性名,只能使用Filter机制.但是却没提供全局的Filter机制,必须按序列化目标Class注册.
//                  所以在FLAT策略时只能输出null值和逗号来代替不输出属性名.
//                 */
//                serializer.writeNull();
//                writer.write(",");
//                List<String> unitPropertyNameWords = ImmutableList.<String>builder().addAll(propertyNameWords).addAll(codecConfig.outputUnitNameWords()).build();
//                String unitPropertyName = caseFormat.join(unitPropertyNameWords);
//                writer.writeFieldName(unitPropertyName);
//                UnitSerializer.of(codecConfig.unitCodecConfig()).write(serializer, quantity.unit(), unitPropertyName, quantity.unit().getClass(), features);
//                writer.write(",");
//                List<String> valuePropertyNameWords = ImmutableList.<String>builder().addAll(propertyNameWords).addAll(codecConfig.outputValueNameWords()).build();
//                String valuePropertyName = caseFormat.join(valuePropertyNameWords);
//                writer.writeFieldName(valuePropertyName);
//                valueSerializer.write(serializer, quantity.value(), valuePropertyName, quantity.value().getClass(), features);
//                break;
//            case OBJECT:
//                writer.write("{");
//                unitPropertyName = caseFormat.join(codecConfig.outputUnitNameWords());
//                writer.writeFieldName(unitPropertyName);
//                UnitSerializer.of(codecConfig.unitCodecConfig()).write(serializer, quantity.unit(), unitPropertyName, quantity.unit().getClass(), features);
//                writer.write(",");
//                valuePropertyName = caseFormat.join(codecConfig.outputValueNameWords());
//                writer.writeFieldName(valuePropertyName);
//                valueSerializer.write(serializer, quantity.value(), valuePropertyName, quantity.value().getClass(), features);
//                writer.write("}");
//                break;
//            case AS_VALUE:
//            default:
//                valueSerializer.write(serializer, quantity.value(), null, null, features);
//        }
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property)
            throws JsonMappingException {
        log.error("ContextualSerializer");
        log.error("prov.getActiveView():{}", prov.getActiveView());
        log.error("property:{}", property);
        //TODO 待确认
        if (property != null) {
            if (Objects.equals(property.getType().getRawClass(), Quantity.class)) {
                QuantitySerialize quantitySerialize = property.getAnnotation(QuantitySerialize.class);
                //TODO 整个类生效
//        if (amountSerialize == null) {
//          amountSerialize = property.getContextAnnotation(AmountSerialize.class);
//        }
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
