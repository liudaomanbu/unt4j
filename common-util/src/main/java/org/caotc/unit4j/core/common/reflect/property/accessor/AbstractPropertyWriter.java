/*
 * Copyright (C) 2020 the original author or authors.
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
import lombok.*;
import org.caotc.unit4j.core.common.reflect.Element;
import org.caotc.unit4j.core.common.reflect.Invokable;
import org.caotc.unit4j.core.common.reflect.MethodInvokable;
import org.caotc.unit4j.core.common.util.ReflectionUtil;

/**
 * @param <O> owner type
 * @param <P> property type
 * @author caotc
 * @date 2019-05-27
 * @since 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = false)
public abstract class AbstractPropertyWriter<O, P> extends AbstractPropertyElement<O, P> implements
        PropertyWriter<O, P> {

    protected AbstractPropertyWriter(
            @NonNull Element element) {
        super(element);
    }

    /**
     * 给传入对象的该属性设置传入的值
     *
     * @param object 设置属性值的对象
     * @param value 设置的属性值
     * @return {@code this}
     * @author caotc
     * @date 2019-05-28
     * @since 1.0.0
     */
    @Override
    @NonNull
    public final AbstractPropertyWriter<O, P> write(@NonNull O object, @NonNull P value) {
        if (!accessible()) {
            accessible(true);
        }
        writeInternal(object, value);
        return this;
    }

  /**
   * 给传入对象的该属性设置传入的值
   *
   * @param object 设置属性值的对象
   * @param value 设置的属性值
   * @author caotc
   * @date 2019-05-28
   * @since 1.0.0
   */
  protected abstract void writeInternal(@NonNull O object, @NonNull P value);

    /**
   * 属性名称
   *
   * @return 属性名称
   * @author caotc
   * @date 2019-05-27
   * @since 1.0.0
   */
  @Override
  @NonNull
  public abstract String propertyName();

  /**
   * 属性类型
   *
   * @return 属性类型
   * @author caotc
   * @date 2019-05-27
   * @since 1.0.0
   */
  @Override
  @NonNull
  public abstract TypeToken<? extends P> propertyType();

    @Override
    @NonNull
    public final <P1 extends P> PropertyWriter<O, P1> propertyType(
            @NonNull Class<P1> propertyType) {
        return propertyType(TypeToken.of(propertyType));
    }

    @SuppressWarnings("unchecked")
    @Override
    @NonNull
    public final <P1 extends P> PropertyWriter<O, P1> propertyType(
            @NonNull TypeToken<P1> propertyType) {
        Preconditions.checkArgument(propertyType.isSupertypeOf(propertyType())
                , "PropertyWriter is known propertyType %s,not %s ", propertyType(), propertyType);
        return (PropertyWriter<O, P1>) this;
    }

    @Override
    public final boolean isReader() {
        return false;
    }

    @Override
    public final boolean isWriter() {
        return true;
    }

    @Override
    public @NonNull <O1> PropertyWriter<O1, P> ownBy(@NonNull TypeToken<O1> ownerType) {
        if (!canOwnBy(ownerType)) {
            throw new IllegalArgumentException(String.format("%s is can not own by %s", this, ownerType));
        }
        return ownByInternal(ownerType);
    }

    @NonNull
    protected abstract <O1> PropertyWriter<O1, P> ownByInternal(@NonNull TypeToken<O1> ownerType);

    /**
     * set{@link Invokable}实现的属性设置器
     *
     * @author caotc
     * @date 2019-05-27
     * @since 1.0.0
     */
    @Value
    @EqualsAndHashCode(callSuper = true)
  @ToString(callSuper = false)
  public static class InvokablePropertyWriter<O, P> extends AbstractPropertyWriter<O, P> {

      /**
       * set方法
       */
      @NonNull
      MethodInvokable<O, ?> invokable;
        /**
         * set方法名称风格
         */
        @NonNull
        String propertyName;

      InvokablePropertyWriter(@NonNull MethodInvokable<O, ?> invokable,
                              @NonNull String propertyName) {
          super(invokable);
          Preconditions.checkArgument(ReflectionUtil.isPropertyWriter(invokable), "%s is not a PropertyWriter", invokable);
          this.invokable = invokable;
          this.propertyName = propertyName;
      }

      @Override
      @SneakyThrows
      public void writeInternal(@NonNull O obj, @NonNull P value) {
          invokable.invoke(obj, value);
      }

      @SuppressWarnings("unchecked")
      @Override
      public @NonNull TypeToken<? extends P> propertyType() {
          return (TypeToken<? extends P>) invokable.parameters().get(0).type();
      }

        @Override
        public TypeToken<O> ownerType() {
            return invokable.ownerType();
        }

        @Override
        public @NonNull <O1> PropertyWriter<O1, P> ownBy(@NonNull TypeToken<O1> ownerType) {
            return new InvokablePropertyWriter<>(invokable().ownBy(ownerType), propertyName());
        }

        @Override
        protected @NonNull <O1> PropertyWriter<O1, P> ownByInternal(@NonNull TypeToken<O1> ownerType) {
            return new InvokablePropertyWriter<>(invokable().ownBy(ownerType), propertyName());
        }
  }
}