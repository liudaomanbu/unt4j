package org.caotc.unit4j.core.unit.type;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.NonFinal;
import org.caotc.unit4j.core.Component;
import org.caotc.unit4j.core.Identifiable;

import java.util.Map;

/**
 * 单位类型接口
 *
 * @author caotc
 * @date 2018-04-14
 * @since 1.0.0
 **/
@AllArgsConstructor(access = AccessLevel.MODULE)
public abstract class UnitType implements Identifiable, Component<UnitType> {
    /**
     * todo id合法性校验？
     *
     * @param id
     * @return
     */
    @NonNull
    public static BaseUnitType of(@NonNull String id) {
        return new BaseUnitType(id);
    }

    @NonNull
    public static Builder builder() {
        return new Builder();
    }

    public boolean isEmpty() {
        return componentToExponents().isEmpty();
    }

    /**
     * 获取组成该对象的单位类型组件与对应指数
     *
     * @return 组成该对象的单位类型组件与对应指数的Map
     * @author caotc
     * @date 2019-01-11
     * @since 1.0.0
     */
    public abstract @NonNull ImmutableMap<@NonNull UnitType, @NonNull Integer> componentToExponents();

    /**
     * 重定基准,将所有非基本类型拆解合并,返回等价于原对象但是组件仅为基本单位类型的单位类型.
     *
     * @return 等价于原对象但是组件仅为基本单位类型的单位类型
     * @author caotc
     * @date 2018-08-17
     * @since 1.0.0
     */
    public abstract @NonNull UnitType rebase();

    public @NonNull UnitType simplify() {
        return simplify(true);
    }

    public abstract @NonNull UnitType simplify(boolean recursive);

    /**
     * 两个单位类型{@link #rebase()}后是否相等
     *
     * @param other 比较的单位类型
     * @return 两个单位类型{@link #rebase()}后是否相等
     * @author caotc
     * @date 2018-10-16
     * @see #rebase()
     * @since 1.0.0
     */
    public boolean rebaseEquals(@NonNull UnitType other) {
        return rebase().equals(other.rebase());
    }

    /**
     * 反转
     *
     * @return 组件不变, 指数取反的单位类型
     * @author caotc
     * @date 2019-01-11
     * @since 1.0.0
     */
    @NonNull
    public UnitType reciprocal() {
        if (isEmpty()) {
            return this;
        }
        /*
            当只有一个组件时,将-1次方简化.
            能满足X倒数两次后结果仍然为X,更符合直觉.
         */
        if (componentToExponents().size() == 1) {
            Map.Entry<@NonNull UnitType, @NonNull Integer> entry = Iterables.getOnlyElement(componentToExponents().entrySet());
            return builder().componentToExponent(entry.getKey(), -entry.getValue()).build();
        }
        return pow(-1);
    }

    @NonNull
    public UnitType pow(int exponent) {
        return UnitType.builder().componentToExponent(this, exponent).build();
    }

    /**
     * 乘法{@code this * multiplicand}
     *
     * @param multiplicand 被乘数
     * @return {@code this * multiplicand}
     * @author caotc
     * @date 2019-01-10
     * @since 1.0.0
     */
    @NonNull
    public UnitType multiply(@NonNull UnitType multiplicand) {
        if (isEmpty()) {
            return multiplicand;
        }
        if (multiplicand.isEmpty()) {
            return this;
        }
        if (equals(multiplicand)) {
            return builder().componentToExponent(this, 2).build();
        }
        if (componentToExponents().keySet().size() == 1 || multiplicand.componentToExponents().keySet().size() == 1) {
            //(LENGTH)*(LENGTH)=(LENGTH)²
            if (componentToExponents().keySet().equals(multiplicand.componentToExponents().keySet())) {
                return builder()
                        .componentToExponent(Iterables.getOnlyElement(componentToExponents().keySet()), Iterables.getOnlyElement(componentToExponents().values()) + Iterables.getOnlyElement(multiplicand.componentToExponents().values()))
                        .build();
            }
            /*
            处理如(FORCE_WEIGHT)*(FORCE_WEIGHT)⁻¹的情况.
            FORCE_WEIGHT的componentToExponents不是(FORCE_WEIGHT)¹而是(MASS)¹(LENGTH)¹(TIME)⁻²
            但是(FORCE_WEIGHT)⁻¹的componentToExponents是(FORCE_WEIGHT)⁻¹
            这种情况需要把(MASS)¹(LENGTH)¹(TIME)⁻²视为(FORCE_WEIGHT)¹处理
             */
            if (componentToExponents().keySet().size() == 1 && componentToExponents().containsKey(multiplicand)) {
                return builder()
                        .componentToExponent(multiplicand, componentToExponents().get(multiplicand) + 1)
                        .build();
            }
            if (multiplicand.componentToExponents().keySet().size() == 1 && multiplicand.componentToExponents().containsKey(this)) {
                return builder()
                        .componentToExponent(this, multiplicand.componentToExponents().get(this) + 1)
                        .build();
            }
        }

        return builder()
                .componentToExponent(this, 1)
                .componentToExponent(multiplicand, 1)
                .build();
    }

    /**
     * 除法{@code (this / divisor)}
     *
     * @param divisor 除数
     * @return {@code this / divisor}
     * @author caotc
     * @date 2019-01-10
     * @since 1.0.0
     */
    @NonNull
    public UnitType divide(@NonNull UnitType divisor) {
        return multiply(divisor.reciprocal());
    }

    @NonNull
    public Builder toBuilder() {
        return builder().componentToExponents(componentToExponents());
    }

    @ToString
    @NoArgsConstructor(access = AccessLevel.MODULE)
    public static class Builder {
        @NonFinal
        ImmutableMap.Builder<UnitType, Integer> componentToExponents;

        public Builder componentToExponent(@NonNull UnitType key, int value) {
            //过滤无意义的数据
            if (value == 0) {
                return this;
            }
            if (this.componentToExponents == null) {
                this.componentToExponents = ImmutableMap.builder();
            }
            this.componentToExponents.put(key, value);
            return this;
        }

        public Builder componentToExponents(@NonNull Map<? extends UnitType, ? extends Integer> componentToExponents) {
            componentToExponents.forEach(this::componentToExponent);
            return this;
        }

        public Builder clearComponentToExponents() {
            this.componentToExponents = null;
            return this;
        }

        public UnitType build() {
            ImmutableMap<UnitType, Integer> componentToExponents = this.componentToExponents == null ? ImmutableMap.of() : this.componentToExponents.build();
            //只有一种类型且指数为1时就是本身
            if (componentToExponents.size() == 1) {
                Map.Entry<UnitType, Integer> entry = Iterables.getOnlyElement(componentToExponents.entrySet());
                if (entry.getValue() == 1) {
                    return entry.getKey();
                }
            }

            //避免(N)¹变成((N)¹)¹之类的无意义操作
            if (componentToExponents.containsValue(1)) {
                componentToExponents = componentToExponents.entrySet().stream()
                        .map(entry -> {
                            if (entry.getValue() != 1 || entry.getKey().componentToExponents().size() != 1) {
                                return entry;
                            }
                            entry = Iterables.getOnlyElement(entry.getKey().componentToExponents().entrySet());
                            return Maps.immutableEntry(entry.getKey(), entry.getValue());
                        }).collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, Map.Entry::getValue, Integer::sum));
                //上面同key合并操作后可能出现指数为0,需要过滤
                componentToExponents = componentToExponents.entrySet().stream()
                        .filter(entry -> entry.getValue() != 0)
                        .collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, Map.Entry::getValue));
            }
            return new CompositeUnitType(componentToExponents);
        }
    }
}
