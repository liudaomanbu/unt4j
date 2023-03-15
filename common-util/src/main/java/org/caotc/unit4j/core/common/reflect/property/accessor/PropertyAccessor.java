package org.caotc.unit4j.core.common.reflect.property.accessor;

import com.google.common.base.Preconditions;
import com.google.common.reflect.TypeToken;
import lombok.NonNull;
import org.caotc.unit4j.core.common.reflect.FieldElement;
import org.caotc.unit4j.core.common.util.ReflectionUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

/**
 * @param <O> owner type
 * @param <P> property type
 * @author caotc
 * @date 2023-01-29
 * @since 1.0.0
 */
public interface PropertyAccessor<O, P> extends PropertyReader<O, P>, PropertyWriter<O, P> {
    @SuppressWarnings("unchecked")
    @NonNull
    static <O, P> PropertyAccessor<O, P> from(@NonNull Type ownerType, @NonNull Field field) {
        return from((TypeToken<O>) TypeToken.of(ownerType), field);
    }

    @NonNull
    static <O, P> PropertyAccessor<O, P> from(@NonNull Class<O> ownerClass, @NonNull Field field) {
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
    static <O, P> PropertyAccessor<O, P> from(@NonNull TypeToken<O> ownerType, @NonNull Field field) {
        Preconditions.checkArgument(ReflectionUtil.isPropertyElement(field), "%s is not a PropertyElement", field);
        return new BasePropertyAccessor.FieldElementPropertyAccessor<>(ownerType, FieldElement.of(field));
    }

    @Override
    @NonNull <P1 extends P> PropertyAccessor<O, P1> propertyType(@NonNull Class<P1> propertyType);

    @Override
    @NonNull <P1 extends P> PropertyAccessor<O, P1> propertyType(@NonNull TypeToken<P1> propertyType);

    default @NonNull <O1> PropertyAccessor<O1, P> ownerType(@NonNull Class<O1> newOwnerType) {
        return this.ownerType(TypeToken.of(newOwnerType));
    }

    @Override
    @NonNull <O1> PropertyAccessor<O1, P> ownerType(@NonNull TypeToken<O1> newOwnerType);
}
