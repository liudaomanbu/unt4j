package org.caotc.unit4j.core.common.reflect.property.accessor;

import com.google.common.base.Preconditions;
import com.google.common.reflect.TypeToken;
import lombok.NonNull;
import org.caotc.unit4j.core.common.reflect.FieldElement;
import org.caotc.unit4j.core.common.util.ReflectionUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

/**
 * todo 范型字母
 *
 * @author caotc
 * @date 2023-01-29
 * @since 1.0.0
 */
public interface PropertyAccessor<T, R> extends PropertyReader<T, R>, PropertyWriter<T, R> {
    @SuppressWarnings("unchecked")
    @NonNull
    static <T, R> PropertyAccessor<T, R> from(@NonNull Type ownerType, @NonNull Field field) {
        return from((TypeToken<T>) TypeToken.of(ownerType), field);
    }

    @NonNull
    static <T, R> PropertyAccessor<T, R> from(@NonNull Class<T> ownerClass, @NonNull Field field) {
        return from(TypeToken.of(ownerClass), field);
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
        Preconditions.checkArgument(ReflectionUtil.isPropertyElement(field), "%s is not a PropertyElement", field);
        return new AbstractPropertyAccessor.FieldElementPropertyAccessor<>(ownerType, FieldElement.of(field));
    }

    @Override
    @NonNull <R1 extends R> PropertyAccessor<T, R1> propertyType(@NonNull Class<R1> propertyType);

    @Override
    @NonNull <R1 extends R> PropertyAccessor<T, R1> propertyType(@NonNull TypeToken<R1> propertyType);

    @Override
    @NonNull PropertyAccessor<T, R> write(@NonNull T object, @NonNull R value);
}
