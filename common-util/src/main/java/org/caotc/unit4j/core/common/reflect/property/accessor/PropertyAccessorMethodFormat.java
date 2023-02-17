package org.caotc.unit4j.core.common.reflect.property.accessor;

import lombok.NonNull;
import org.caotc.unit4j.core.common.reflect.Invokable;
import org.caotc.unit4j.core.common.reflect.MethodInvokable;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author caotc
 * @date 2023-01-30
 * @since 1.0.0
 */
public interface PropertyAccessorMethodFormat {
    /**
     * 检查传入方法是否是属于该命名风格的get方法
     *
     * @param method 需要检查的方法
     * @return 是否是属于该命名风格的get方法
     * @author caotc
     * @date 2019-05-23
     * @since 1.0.0
     */
    default boolean isPropertyReader(@NonNull Method method) {
        return isPropertyReader(Invokable.from(method));
    }

    /**
     * 检查传入方法是否是属于该命名风格的get方法
     *
     * @param invokable 需要检查的方法
     * @return 是否是属于该命名风格的get方法
     * @author caotc
     * @date 2019-05-23
     * @since 1.0.0
     */
    boolean isPropertyReader(@NonNull MethodInvokable<?, ?> invokable);

    /**
     * 检查传入方法是否是属于该命名风格的set方法
     *
     * @param method 需要检查的方法
     * @return 是否是属于该命名风格的set方法
     * @author caotc
     * @date 2019-05-23
     * @since 1.0.0
     */
    default boolean isPropertyWriter(@NonNull Method method) {
        return isPropertyWriter(Invokable.from(method));
    }

    /**
     * 检查传入方法是否是属于该命名风格的set方法
     *
     * @param invokable 需要检查的方法
     * @return 是否是属于该命名风格的set方法
     * @author caotc
     * @date 2019-05-23
     * @since 1.0.0
     */
    boolean isPropertyWriter(@NonNull MethodInvokable<?, ?> invokable);

    /**
     * 获取传入属性的该命名风格的get方法名
     *
     * @param field 属性
     * @return 该命名风格的get方法名
     * @author caotc
     * @date 2019-05-23
     * @since 1.0.0
     */
    @NonNull String propertyReaderMethodNameFromField(@NonNull Field field);

    /**
     * 获取传入get方法的对应的属性名称
     *
     * @param getMethod get方法
     * @return 对应的属性名称
     * @author caotc
     * @date 2019-05-23
     * @apiNote 传入的参数必须是该命名风格的get方法, 不可传入普通方法
     * @since 1.0.0
     */
    @NonNull
    default String propertyNameFromPropertyReader(@NonNull Method getMethod) {
        return propertyNameFromPropertyReader(Invokable.from(getMethod));
    }

    /**
     * 获取传入get方法的对应的属性名称
     *
     * @param getInvokable get方法
     * @return 对应的属性名称
     * @author caotc
     * @date 2019-05-23
     * @apiNote 传入的参数必须是该命名风格的get方法, 不可传入普通方法
     * @since 1.0.0
     */
    @NonNull String propertyNameFromPropertyReader(@NonNull Invokable<?, ?> getInvokable);

    /**
     * 获取传入属性的该命名风格的set方法名
     *
     * @param field 属性
     * @return 该命名风格的set方法名
     * @author caotc
     * @date 2019-05-23
     * @since 1.0.0
     */
    @NonNull String PropertyWriterNameFromField(@NonNull Field field);

    /**
     * 获取传入set方法的对应的属性名称
     *
     * @param setMethod set方法
     * @return 对应的属性名称
     * @author caotc
     * @date 2019-05-23
     * @apiNote 传入的参数必须是该命名风格的set方法, 不可传入普通方法
     * @since 1.0.0
     */
    @NonNull
    default String propertyNameFromPropertyWriter(@NonNull Method setMethod) {
        return propertyNameFromPropertyWriter(Invokable.from(setMethod));
    }

    /**
     * 获取传入set方法的对应的属性名称
     *
     * @param setInvokable get方法
     * @return 对应的属性名称
     * @author caotc
     * @date 2019-05-23
     * @apiNote 传入的参数必须是该命名风格的set方法, 不可传入普通方法
     * @since 1.0.0
     */
    @NonNull String propertyNameFromPropertyWriter(@NonNull Invokable<?, ?> setInvokable);

    @NonNull
    default String propertyName(@NonNull Method propertyAccessorMethod) {
        return propertyName(Invokable.from(propertyAccessorMethod));
    }

    @NonNull
    default String propertyName(@NonNull MethodInvokable<?, ?> propertyAccessorInvokable) {
        if (isPropertyReader(propertyAccessorInvokable)) {
            return propertyNameFromPropertyReader(propertyAccessorInvokable);
        }
        if (isPropertyWriter(propertyAccessorInvokable)) {
            return propertyNameFromPropertyWriter(propertyAccessorInvokable);
        }
        throw new IllegalArgumentException(String.format("%s is not a propertyAccessorInvokable", propertyAccessorInvokable));
    }
}
