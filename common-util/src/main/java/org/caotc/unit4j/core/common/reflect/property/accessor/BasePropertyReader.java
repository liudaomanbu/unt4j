/*
 * Copyright (C) 2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.caotc.unit4j.core.common.reflect.property.accessor;

import com.google.common.base.Preconditions;
import com.google.common.reflect.TypeToken;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;
import org.caotc.unit4j.core.common.reflect.Element;
import org.caotc.unit4j.core.common.reflect.Invokable;
import org.caotc.unit4j.core.common.reflect.MethodInvokable;
import org.caotc.unit4j.core.common.util.ReflectionUtil;

import java.util.Optional;

/**
 * @param <O> owner type
 * @param <P> property type
 * @author caotc
 * @date 2019-05-27
 * @since 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
public abstract class BasePropertyReader<O, P> extends BasePropertyElement<O, P> implements
        PropertyReader<O, P> {

    protected BasePropertyReader(@NonNull Element member) {
        super(member);
    }

    /**
     * 从传入的对象中获取该属性的值
     *
     * @param object 对象
     * @return 对象中该属性的值
     * @author caotc
     * @date 2019-05-27
     * @since 1.0.0
     */
    @Override
    @NonNull
    public final Optional<P> read(@NonNull O object) {
        if (!accessible()) {
            accessible(true);
        }
        return readInternal(object);
    }

    /**
     * 从传入的对象中获取该属性的值
     *
     * @param object 对象
     * @return 对象中该属性的值
     * @author caotc
     * @date 2019-05-27
     * @since 1.0.0
     */
    @NonNull
    protected abstract Optional<P> readInternal(@NonNull O object);

    @Override
    @NonNull
    public final <P1 extends P> PropertyReader<O, P1> propertyType(
            @NonNull Class<P1> propertyType) {
        return propertyType(TypeToken.of(propertyType));
    }

    @SuppressWarnings("unchecked")
    @Override
    @NonNull
    public final <P1 extends P> PropertyReader<O, P1> propertyType(
            @NonNull TypeToken<P1> propertyType) {
        Preconditions.checkArgument(propertyType.isSupertypeOf(propertyType())
                , "PropertyReader is known propertyType %s,not %s ", propertyType(), propertyType);
        return (PropertyReader<O, P1>) this;
    }

    @Override
    public final boolean isReader() {
        return true;
    }

    @Override
    public final boolean isWriter() {
        return false;
    }

    @Override
    public @NonNull <O1> PropertyReader<O1, P> ownerType(@NonNull TypeToken<O1> newOwnerType) {
        if (!checkOwnerType(newOwnerType)) {
            throw new IllegalArgumentException(String.format("%s is can not own by %s", this, newOwnerType));
        }
        return ownByInternal(newOwnerType);
    }

    @Override
    public String toString() {
        return String.format("PropertyReader(ownerType=%s,propertyName=%s,element=%s)", ownerType(), propertyName(), element());
    }

    @NonNull
    protected abstract <O1> PropertyReader<O1, P> ownByInternal(@NonNull TypeToken<O1> ownerType);

    /**
     * get{@link Invokable}实现的属性获取器
     *
     * @author caotc
     * @date 2019-05-27
     * @since 1.0.0
     */
    @Value
    @EqualsAndHashCode(callSuper = true)
    public static class InvokablePropertyReader<O, P> extends BasePropertyReader<O, P> {

        /**
         * 方法
         */
        @NonNull MethodInvokable<O, P> invokable;
        /**
         * 属性名称
         */
        @NonNull String propertyName;

        InvokablePropertyReader(@NonNull MethodInvokable<O, P> invokable,
                                @NonNull String propertyName) {
            super(invokable);
            Preconditions.checkArgument(ReflectionUtil.isPropertyReader(invokable), "%s is not a PropertyReader", invokable);
            this.invokable = invokable;
            this.propertyName = propertyName;
        }

        @NonNull
        @Override
        public Optional<P> readInternal(@NonNull O object) {
            try {
                return Optional.ofNullable(invokable.invoke(object));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        @SuppressWarnings("unchecked")
        @Override
        public @NonNull TypeToken<P> propertyType() {
            return (TypeToken<P>) invokable.returnType();
        }

        @Override
        public TypeToken<O> ownerType() {
            return invokable.ownerType();
        }

        @Override
        protected @NonNull <O1> PropertyReader<O1, P> ownByInternal(@NonNull TypeToken<O1> ownerType) {
            return new InvokablePropertyReader<>(invokable().ownerType(ownerType), propertyName());
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }
}