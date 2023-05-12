package org.caotc.unit4j.core.unit;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import lombok.NonNull;
import lombok.experimental.NonFinal;
import org.caotc.unit4j.core.Component;
import org.caotc.unit4j.core.Identifiable;
import org.caotc.unit4j.core.unit.type.BaseUnitType;
import org.caotc.unit4j.core.unit.type.UnitType;

import java.util.Map;

/**
 * 单位
 *
 * @author caotc
 * @date 2018-03-27
 * @since 1.0.0
 **/
public abstract class Unit implements Identifiable, Component<Unit> {
    /**
     * //todo id合法性校验？
     *
     * @param id
     * @return
     */
    @NonNull
    public static BaseStandardUnit of(@NonNull String id, @NonNull BaseUnitType unitType) {
        return BaseStandardUnit.create(id, unitType);
    }

    @NonNull
    public static Builder builder() {
        return new Builder();
    }

    //TODO 尝试删除
    @NonNull
    public abstract Prefix prefix();

    /**
     * 单位类型
     *
     * @return 单位类型
     * @author caotc
     * @date 2019-01-11
     * @since 1.0.0
     */
    @NonNull
    public abstract UnitType type();

    /**
     * 重定基准,将所有非基本类型拆解合并,返回结果的类型Map中不存在非基本类型Key //TODO 组件只有BaseStandardUnit还是包括BasePrefixUnit？
     *
     * @return 等价于原对象但是组件仅为基本单位的单位
     * @author caotc
     * @date 2018-08-17
     * @since 1.0.0
     */
    @NonNull
    public abstract Unit rebase();

    /**
     * 获取组成该对象的单位组件与对应指数
     *
     * @return 组成该对象的单位组件与对应指数的Map
     * @author caotc
     * @date 2019-01-11
     * @since 1.0.0
     */
    @NonNull
    public abstract ImmutableMap<Unit, Integer> componentToExponents();

    @NonNull
    public ImmutableMap<UnitType, Dimension> typeToDimensionElementMap() {
        return componentToExponents().entrySet().stream().collect(ImmutableMap
                .toImmutableMap(entry -> entry.getKey().type(),
                        entry -> Dimension.create(entry.getKey(), entry.getValue())));
    }

    /**
     * 幂函数,<tt>(this<sup>n</sup>)</tt>
     *
     * @param exponent 指数
     * @return <tt>this<sup>n</sup></tt>
     * @author caotc
     * @date 2019-01-11
     * @since 1.0.0
     */
    @NonNull
    public abstract Unit power(int exponent);

    /**
     * 倒转
     *
     * @return 倒转所有单位组件的指数后的单位对象
     * @author caotc
     * @date 2019-05-27
     * @since 1.0.0
     */
    //TODO 考虑方法名
    @NonNull
    public abstract Unit inverse();

    /**
     * 乘法{@code this * multiplicand}
     *
     * @param multiplicand 被乘数
     * @return {@code this * multiplicand}
     * @author caotc
     * @date 2019-01-11
     * @since 1.0.0
     */
    @NonNull
    public Unit multiply(@NonNull Unit multiplicand) {
        //TODO 配置策略化，目前使用为最保守逻辑，单位不同就作为不同组件(考虑将同一类型单位转换为同一单位的逻辑)
        if (multiplicand instanceof BaseStandardUnit) {
            return multiply((BaseStandardUnit) multiplicand);
        }
        if (multiplicand instanceof BasePrefixUnit) {
            return multiply((BasePrefixUnit) multiplicand);
        }
        if (multiplicand instanceof CompositeStandardUnit) {
            return multiply((CompositeStandardUnit) multiplicand);
        }
        if (multiplicand instanceof CompositePrefixUnit) {
            return multiply((CompositePrefixUnit) multiplicand);
        }
        throw new IllegalArgumentException("never come");
    }

    /**
     * 乘法{@code this * multiplicand}
     *
     * @param multiplicand 被乘数
     * @return {@code this * multiplicand}
     * @author caotc
     * @date 2019-01-11
     * @since 1.0.0
     */
    @NonNull
    public abstract Unit multiply(@NonNull BaseStandardUnit multiplicand);

    /**
     * 乘法{@code this * multiplicand}
     *
     * @param multiplicand 被乘数
     * @return {@code this * multiplicand}
     * @author caotc
     * @date 2019-01-11
     * @since 1.0.0
     */
    @NonNull
    public abstract Unit multiply(@NonNull BasePrefixUnit multiplicand);

    /**
     * 乘法{@code this * multiplicand}
     *
     * @param multiplicand 被乘数
     * @return {@code this * multiplicand}
     * @author caotc
     * @date 2019-01-11
     * @since 1.0.0
     */
    @NonNull
    public abstract Unit multiply(@NonNull CompositeStandardUnit multiplicand);

    /**
     * 乘法{@code this * multiplicand}
     *
     * @param multiplicand 被乘数
     * @return {@code this * multiplicand}
     * @author caotc
     * @date 2019-01-11
     * @since 1.0.0
     */
    @NonNull
    public abstract Unit multiply(@NonNull CompositePrefixUnit multiplicand);

    /**
     * 除法{@code (this / divisor)}
     *
     * @param divisor 除数
     * @return {@code (this / divisor)}
     * @author caotc
     * @date 2019-05-27
     * @since 1.0.0
     */
    @NonNull
    public Unit divide(@NonNull Unit divisor) {
        return multiply(divisor.inverse());
    }

    public Builder toBuilder() {
        return builder().componentToExponents(componentToExponents());
    }

    public static class Builder {//todo
        @NonFinal
        Prefix prefix = Prefix.EMPTY;
        @NonFinal
        ImmutableMap.Builder<Unit, Integer> componentToExponents;

        Builder() {
        }

        public Builder prefix(@NonNull Prefix prefix) {
            this.prefix = prefix;
            return this;
        }

        public Builder standardUnit(@NonNull StandardUnit standardUnit) {
            return componentToExponent(standardUnit, 1);
        }

        public Builder componentToExponent(@NonNull Unit key, int value) {
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

        public Builder componentToExponents(@NonNull Map<? extends Unit, ? extends Integer> componentToExponents) {
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

        public Unit build() {
            ImmutableMap<Unit, Integer> componentToExponents = this.componentToExponents == null ? ImmutableMap.of() : this.componentToExponents.build();
            //只有一种类型且指数为1时就是本身
            if (componentToExponents.size() == 1) {
                Map.Entry<Unit, Integer> entry = Iterables.getOnlyElement(componentToExponents.entrySet());
                if (entry.getValue() == 1) {
                    Unit unit = entry.getKey();
                    if (!prefix.isEmpty()) {
                        if (!unit.prefix().isEmpty()) {
                            throw new IllegalStateException(String.format("unit:%s already has prefix,can not add prefix:%s", unit, prefix));
                        }
                        //todo cast?
                        unit = ((StandardUnit) unit).addPrefix(prefix);
                    }
                    return unit;
                }
            }
            CompositeStandardUnit result = new CompositeStandardUnit(componentToExponents);
            return prefix.isEmpty() ? result : result.addPrefix(prefix);
        }

        public String toString() {
            return "Unit.Builder(componentToExponents=" + this.componentToExponents + ")";
        }
    }
}
