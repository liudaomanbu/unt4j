package org.caotc.unit4j.core.convert;

import com.google.common.base.Preconditions;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.caotc.unit4j.core.Configuration;
import org.caotc.unit4j.core.Quantity;
import org.caotc.unit4j.core.unit.Unit;
import org.caotc.unit4j.core.unit.UnitGroup;

import java.util.Collection;
import java.util.function.BiFunction;

/**
 * 自动转换目标单位选择器
 *
 * @author caotc
 * @date 2018-03-28
 * @since 1.0.0
 **/
@Value
@Builder(toBuilder = true)
public class TargetUnitChooser {

    /**
     * 单个{@link Quantity}对象目标单位选择器
     */
    @NonNull
    BiFunction<Quantity, Configuration, Unit> quantityTargetUnitChooser;
    /**
     * {@link Quantity}对象集合目标单位选择器
     */
    @NonNull
    BiFunction<? super Iterable<Quantity>, Configuration, Unit> quantitiesTargetUnitChooser;

    /**
     * 目标单位选择器工厂方法
     *
     * @param quantityTargetUnitChooser   对象目标单位选择器
     * @param quantitiesTargetUnitChooser 对象集合目标单位选择器
     * @return 目标单位选择器
     * @author caotc
     * @date 2019-05-24
     * @since 1.0.0
     */
    @NonNull
    public static TargetUnitChooser create(
            @NonNull BiFunction<Quantity, Configuration, Unit> quantityTargetUnitChooser,
            @NonNull BiFunction<? super Iterable<Quantity>, Configuration, Unit> quantitiesTargetUnitChooser) {
        return builder().quantityTargetUnitChooser(quantityTargetUnitChooser)
                .quantitiesTargetUnitChooser(quantitiesTargetUnitChooser).build();
    }

    /**
     * 目标单位选择器工厂方法,对象集合由{@code quantityChooser}参数选择出目标对象后由对象目标单位选择器处理
     *
     * @param quantityTargetUnitChooser 对象目标单位选择器
     * @param quantityChooser         {@link Quantity}选择器
     * @return 目标单位选择器
     * @author caotc
     * @date 2019-05-24
     * @since 1.0.0
     */
    @NonNull
    public static TargetUnitChooser create(
            @NonNull BiFunction<Quantity, Configuration, Unit> quantityTargetUnitChooser,
            @NonNull QuantityChooser quantityChooser) {
        return create(quantityTargetUnitChooser, (quantities, configuration) -> quantityTargetUnitChooser
                .apply(quantityChooser.choose(quantities, configuration), configuration));
    }

    /**
     * 目标单位选择器工厂方法,对象集合由{@code quantityChooser}参数选择出目标对象后由对象目标单位选择器处理,目标单位由{@code
     * quantityConvertResultsChooser}参数从传入的{@link Quantity}对象所有可转换结果中选择
     *
     * @param quantityConvertResultsChooser 从传入的{@link Quantity}对象所有可转换结果中选择目标单位的选择器
     * @param quantityChooser             {@link Quantity}选择器
     * @return 目标单位选择器
     * @author caotc
     * @date 2019-05-24
     * @since 1.0.0
     */
    @NonNull
    public static TargetUnitChooser create(
            @NonNull QuantityChooser quantityConvertResultsChooser,
            @NonNull QuantityChooser quantityChooser) {
        BiFunction<Quantity, Configuration, Unit> quantityTargetUnitChooser = (quantity, configuration) -> {
            UnitGroup unitGroup = configuration.getUnitGroup(quantity.unit());
            return quantityConvertResultsChooser.chooseInternal(
                    unitGroup.units().stream().map(unit -> quantity.convertTo(unit, configuration)),
                    configuration).unit();
        };

        return create(quantityTargetUnitChooser, (quantities, configuration) -> quantityTargetUnitChooser
                .apply(quantityChooser.choose(quantities, configuration), configuration));
    }

    /**
     * 选择传入{@link Quantity}对象的目标{@link Unit}(使用传入的{@link Configuration}配置对象)
     *
     * @param quantity      需要自动转换的{@link Quantity}对象
     * @param configuration {@link Configuration}配置对象
     * @return 目标 {@link Unit}
     * @author caotc
     * @date 2019-01-14
     * @since 1.0.0
     */
    @NonNull
    public Unit targetUnit(@NonNull Quantity quantity,
                           @NonNull Configuration configuration) {
        return quantityTargetUnitChooser.apply(quantity, configuration);
    }

    /**
     * 选择参数{@link Quantity}对象集合的目标{@link Unit}(使用参数{@link Configuration}配置对象)
     *
     * @param quantities    需要自动转换的{@link Quantity}对象集合
     * @param configuration {@link Configuration}配置对象
     * @return 目标 {@link Unit}
     * @author caotc
     * @date 2019-05-24
     * @since 1.0.0
     */
    @NonNull
    public Unit targetUnit(@NonNull Collection<Quantity> quantities,
                           @NonNull Configuration configuration) {
        Preconditions.checkArgument(!quantities.isEmpty(), "empty collection can't auto toUnit");
        boolean isSameUnitType =
                quantities.stream().map(Quantity::unit).map(Unit::type).distinct().count() == 1;
        Preconditions
                .checkArgument(isSameUnitType, "all data must have same unitElement possibleTypes");
        return quantitiesTargetUnitChooser.apply(quantities, configuration);
    }

}
