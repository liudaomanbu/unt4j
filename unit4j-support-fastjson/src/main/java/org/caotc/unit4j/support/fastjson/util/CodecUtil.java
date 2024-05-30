package org.caotc.unit4j.support.fastjson.util;

import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.serializer.SerialContext;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.caotc.unit4j.core.common.base.CaseFormat;

import java.util.Collection;
import java.util.Objects;

/**
 * @author caotc
 * @date 2024-05-15
 * @since 1.0.0
 */
@UtilityClass
public class CodecUtil {
    private static final BiMap<PropertyNamingStrategy, CaseFormat> CASE_FORMAT_MAP = ImmutableBiMap.<PropertyNamingStrategy, CaseFormat>builder()
            .put(PropertyNamingStrategy.CamelCase, CaseFormat.LOWER_CAMEL)
            .put(PropertyNamingStrategy.PascalCase, CaseFormat.UPPER_CAMEL)
            .put(PropertyNamingStrategy.SnakeCase, CaseFormat.LOWER_UNDERSCORE)
            .put(PropertyNamingStrategy.KebabCase, CaseFormat.LOWER_HYPHEN)
            .build();

    @NonNull
    public static CaseFormat mapping(@NonNull PropertyNamingStrategy propertyNamingStrategy) {
        return CASE_FORMAT_MAP.get(propertyNamingStrategy);
    }

    public static boolean isProperty(SerialContext context) {
        return Objects.isNull(context) || context.object.getClass().isArray() || context.object instanceof Collection;
    }
}
