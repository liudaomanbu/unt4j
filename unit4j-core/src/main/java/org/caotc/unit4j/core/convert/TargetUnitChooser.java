package org.caotc.unit4j.core.convert;

import com.google.common.base.Preconditions;
import java.util.Collection;
import java.util.function.BiFunction;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.caotc.unit4j.core.Amount;
import org.caotc.unit4j.core.Configuration;
import org.caotc.unit4j.core.unit.Unit;
import org.caotc.unit4j.core.unit.UnitGroup;

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
   * 目标单位选择器工厂方法
   *
   * @param amountTargetUnitChooser 对象目标单位选择器
   * @param amountsTargetUnitChooser 对象集合目标单位选择器
   * @return 目标单位选择器
   * @author caotc
   * @date 2019-05-24
   * @since 1.0.0
   */
  @NonNull
  public static TargetUnitChooser create(
      @NonNull BiFunction<Amount, Configuration, Unit> amountTargetUnitChooser,
      @NonNull BiFunction<? super Iterable<Amount>, Configuration, Unit> amountsTargetUnitChooser) {
    return builder().amountTargetUnitChooser(amountTargetUnitChooser)
        .amountsTargetUnitChooser(amountsTargetUnitChooser).build();
  }

  /**
   * 目标单位选择器工厂方法,对象集合由{@code amountChooser}参数选择出目标对象后由对象目标单位选择器处理
   *
   * @param amountTargetUnitChooser 对象目标单位选择器
   * @param amountChooser {@link Amount}选择器
   * @return 目标单位选择器
   * @author caotc
   * @date 2019-05-24
   * @since 1.0.0
   */
  @NonNull
  public static TargetUnitChooser create(
      @NonNull BiFunction<Amount, Configuration, Unit> amountTargetUnitChooser,
      @NonNull AmountChooser amountChooser) {
    return create(amountTargetUnitChooser, (amounts, configuration) -> amountTargetUnitChooser
        .apply(amountChooser.choose(amounts, configuration), configuration));
  }

  /**
   * 目标单位选择器工厂方法,对象集合由{@code amountChooser}参数选择出目标对象后由对象目标单位选择器处理,目标单位由{@code
   * amountConvertResultsChooser}参数从传入的{@link Amount}对象所有可转换结果中选择
   *
   * @param amountConvertResultsChooser 从传入的{@link Amount}对象所有可转换结果中选择目标单位的选择器
   * @param amountChooser {@link Amount}选择器
   * @return 目标单位选择器
   * @author caotc
   * @date 2019-05-24
   * @since 1.0.0
   */
  @NonNull
  public static TargetUnitChooser create(
      @NonNull AmountChooser amountConvertResultsChooser,
      @NonNull AmountChooser amountChooser) {
    BiFunction<Amount, Configuration, Unit> amountTargetUnitChooser = (amount, configuration) -> {
      UnitGroup unitGroup = configuration.getUnitGroup(amount.unit());
      return amountConvertResultsChooser.chooseInternal(
          unitGroup.units().stream().map(unit -> amount.convertTo(unit, configuration)),
          configuration).unit();
    };

    return create(amountTargetUnitChooser, (amounts, configuration) -> amountTargetUnitChooser
        .apply(amountChooser.choose(amounts, configuration), configuration));
  }

  /**
   * 单个{@link Amount}对象目标单位选择器
   */
  @NonNull
  BiFunction<Amount, Configuration, Unit> amountTargetUnitChooser;
  /**
   * {@link Amount}对象集合目标单位选择器
   */
  @NonNull
  BiFunction<? super Iterable<Amount>, Configuration, Unit> amountsTargetUnitChooser;

  /**
   * 选择传入{@link Amount}对象的目标{@link Unit}(使用传入的{@link Configuration}配置对象)
   *
   * @param amount 需要自动转换的{@link Amount}对象
   * @param configuration {@link Configuration}配置对象
   * @return 目标 {@link Unit}
   * @author caotc
   * @date 2019-01-14
   * @since 1.0.0
   */
  @NonNull
  public Unit targetUnitFromAmount(@NonNull Amount amount,
      @NonNull Configuration configuration) {
    return amountTargetUnitChooser.apply(amount, configuration);
  }

  /**
   * 选择参数{@link Amount}对象集合的目标{@link Unit}(使用参数{@link Configuration}配置对象)
   *
   * @param amounts 需要自动转换的{@link Amount}对象集合
   * @param configuration {@link Configuration}配置对象
   * @return 目标 {@link Unit}
   * @author caotc
   * @date 2019-05-24
   * @since 1.0.0
   */
  @NonNull
  public Unit targetUnitFromAmounts(@NonNull Collection<Amount> amounts,
      @NonNull Configuration configuration) {
    Preconditions.checkArgument(!amounts.isEmpty(), "empty collection can't auto toUnit");
    boolean isSameUnitType =
        amounts.stream().map(Amount::unit).map(Unit::type).distinct().count() == 1;
    Preconditions
        .checkArgument(isSameUnitType, "all data must have same unitElement possibleTypes");
    return amountsTargetUnitChooser.apply(amounts, configuration);
  }

}
