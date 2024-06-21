package org.caotc.unit4j.support.jackson;

import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.NopAnnotationIntrospector;
import com.fasterxml.jackson.databind.util.NameTransformer;
import lombok.NonNull;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.caotc.unit4j.api.annotation.QuantityCodecStrategy;
import org.caotc.unit4j.api.annotation.QuantitySerialize;
import org.caotc.unit4j.support.QuantityCodecConfig;
import org.caotc.unit4j.support.Unit4jProperties;

/**
 * @author caotc
 * @date 2024-06-03
 * @since 1.0.0
 */
@Value(staticConstructor = "of")
@Slf4j
public class AnnotationIntrospector extends NopAnnotationIntrospector {
    @NonNull
    Unit4jProperties unit4jProperties = new Unit4jProperties();

    @Override
    public Object findSerializer(Annotated a) {
        log.debug("findSerializer Annotated:{}", a);
        log.debug("findSerializer getName:{}", a.getName());
        QuantitySerialize annotation = a.getAnnotation(QuantitySerialize.class);
        if (annotation != null) {
            QuantityCodecConfig codecConfig = unit4jProperties.createPropertyQuantityCodecConfig(annotation);
            if (codecConfig.strategy() == QuantityCodecStrategy.FLAT) {
                //todo 与当前JsonUnwrapped注解处理一致,但是这样不符合直觉,不是根据当前上下文的属性名格式输出.是否需要修改?
                //todo 可能是方法,需要处理Name
                return QuantitySerializer.of(codecConfig, NameTransformer.simpleTransformer(a.getName(), ""));
            } else {
                return QuantitySerializer.of(codecConfig);
            }
        }
        return null;
    }

    @Override
    public NameTransformer findUnwrappingNameTransformer(AnnotatedMember a) {
        log.debug("findUnwrappingNameTransformer AnnotatedMember:{}", a);
        QuantitySerialize annotation = a.getAnnotation(QuantitySerialize.class);
        if (annotation != null && annotation.strategy() == QuantityCodecStrategy.FLAT) {
            //todo 与当前JsonUnwrapped注解处理一致,但是这样不符合直觉,不是根据当前上下文的属性名格式输出.是否需要修改?
            //todo 可能是方法,需要处理Name
            return NameTransformer.simpleTransformer(a.getName(), "");
        }
        return null;
    }
}
