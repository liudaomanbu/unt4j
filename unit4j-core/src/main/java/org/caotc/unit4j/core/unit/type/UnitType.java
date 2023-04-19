package org.caotc.unit4j.core.unit.type;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.NonFinal;
import org.caotc.unit4j.core.Component;
import org.caotc.unit4j.core.Identifiable;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Stream;

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

    /**
     * 获取组成该对象的单位类型组件与对应指数
     *
     * @return 组成该对象的单位类型组件与对应指数的Map
     * @author caotc
     * @date 2019-01-11
     * @since 1.0.0
     */
    public abstract @NonNull ImmutableMap<UnitType, Integer> componentToExponents();

    /**
     * 重定基准,将所有非基本类型拆解合并,返回等价于原对象但是组件仅为基本单位类型的单位类型.
     *
     * @return 等价于原对象但是组件仅为基本单位类型的单位类型
     * @author caotc
     * @date 2018-08-17
     * @since 1.0.0
     */
    public abstract @NonNull UnitType rebase();

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
    public UnitType inverse() {
        return builder().componentToExponents(
                Maps.transformValues(componentToExponents(), exponent -> -exponent)).build();
    }

    /**
     * 乘法{@code this * multiplicand} //TODO 待移到父类
     *
     * @param multiplicand 被乘数
     * @return {@code this * multiplicand}
     * @author caotc
     * @date 2019-01-10
     * @since 1.0.0
     */
    @NonNull
    public UnitType multiply(@NonNull UnitType multiplicand) {
        ImmutableMap<UnitType, Integer> unitTypeToIndexMap = Stream
                .of(this.componentToExponents(), multiplicand.componentToExponents())
                .map(Map::entrySet)
                .flatMap(Collection::stream)
                .collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, Map.Entry::getValue, Integer::sum));
        return builder().componentToExponents(unitTypeToIndexMap).build();
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
        return multiply(divisor.inverse());
    }

    public Builder toBuilder() {
        return builder().componentToExponents(componentToExponents());
    }

    public static class Builder {
        @NonFinal
        private ImmutableMap.Builder<UnitType, Integer> componentToExponents;

        Builder() {
        }

        public Builder componentToExponent(@NonNull UnitType key, int value) {
            if (this.componentToExponents == null) {
                this.componentToExponents = ImmutableMap.builder();
            }
            //过滤无意义的数据
            if (value == 0) {
                return this;
            }
            this.componentToExponents.put(key, value);
            return this;
        }

        public Builder componentToExponents(@NonNull Map<? extends UnitType, ? extends Integer> componentToExponents) {
            if (this.componentToExponents == null) {
                this.componentToExponents = ImmutableMap.builder();
            }

            //过滤无意义的数据
            this.componentToExponents.putAll(Maps.filterValues(componentToExponents, (exponent) -> exponent != 0));
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
            return new CompositeUnitType(componentToExponents);
        }

        public String toString() {
            return "UnitType.Builder(componentToExponents=" + this.componentToExponents + ")";
        }
    }
}
