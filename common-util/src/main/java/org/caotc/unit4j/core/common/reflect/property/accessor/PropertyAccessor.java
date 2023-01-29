package org.caotc.unit4j.core.common.reflect.property.accessor;

import com.google.common.reflect.TypeToken;
import lombok.NonNull;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

/**
 * @author caotc
 * @date 2023-01-29
 * @since 1.0.0
 */
public interface PropertyAccessor<T, R> extends PropertyReader<T, R>, PropertyWriter<T, R> {
    @NonNull
    static <T, R> PropertyAccessor<T, R> from(@NonNull Type ownerType, @NonNull Field field) {
        return PropertyElement.<T, R>from(ownerType, field).toAccessor();
    }

    @NonNull
    static <T, R> PropertyAccessor<T, R> from(@NonNull Class<T> ownerClass, @NonNull Field field) {
        return PropertyElement.<T, R>from(ownerClass, field).toAccessor();
    }

    /**
     * 工厂方法
     *
     * @param field 属性
     * @return 属性获取器
     * @author caotc
     * @date 2019-06-16
     * @since 1.0.0
     */
    @NonNull
    static <T, R> PropertyAccessor<T, R> from(@NonNull TypeToken<T> ownerType, @NonNull Field field) {
        return PropertyElement.<T, R>from(ownerType, field).toAccessor();
    }

    @Override
    @NonNull <R1 extends R> PropertyAccessor<T, R1> propertyType(@NonNull Class<R1> propertyType);

    @Override
    @NonNull <R1 extends R> PropertyAccessor<T, R1> propertyType(@NonNull TypeToken<R1> propertyType);

    @Override
    @NonNull PropertyAccessor<T, R> write(@NonNull T object, @NonNull R value);
}
