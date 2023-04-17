package org.caotc.unit4j.core.convert;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Streams;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;
import org.caotc.unit4j.core.Configuration;
import org.caotc.unit4j.core.Quantity;
import org.caotc.unit4j.core.math.number.AbstractNumber;
import org.caotc.unit4j.core.math.number.BigDecimal;
import org.caotc.unit4j.core.math.number.BigInteger;
import org.caotc.unit4j.core.unit.Unit;

import java.util.Comparator;
import java.util.Iterator;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * {@link Quantity}类的选择器,用于在集合中选取目标对象
 *
 * @author caotc
 * @date 2018-04-13
 * @since 1.0.0
 **/
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public abstract class QuantityChooser {

  /**
   * 最小值选择器
   */
  private static final QuantityChooser MIN_AMOUNT_CHOOSER = new QuantityChooser() {

    @Override
    protected @NonNull Quantity chooseInternal(@NonNull Stream<Quantity> amounts,
                                               @NonNull Configuration configuration) {
      return amounts.min(configuration::compare).orElseThrow(AssertionError::new);
    }
  };

  /**
   * 最大值选择器
   */
  private static final QuantityChooser MAX_AMOUNT_CHOOSER = new QuantityChooser() {

    @Override
    protected @NonNull Quantity chooseInternal(@NonNull Stream<Quantity> amounts,
                                               @NonNull Configuration configuration) {
      return amounts.min(configuration::compare).orElseThrow(AssertionError::new);
    }
  };

  /**
   * 平均数选择器
   */
  private static final QuantityChooser AVERAGE_AMOUNT_CHOOSER = new QuantityChooser() {

    @Override
    protected @NonNull Quantity chooseInternal(@NonNull Stream<Quantity> amounts,
                                               @NonNull Configuration configuration) {
      ImmutableList<Quantity> quantityImmutableList = amounts.collect(ImmutableList.toImmutableList());
      Unit unit = quantityImmutableList.stream().findAny().map(Quantity::unit)
              .orElseThrow(AssertionError::new);
      ImmutableList<AbstractNumber> values = quantityImmutableList.stream()
              .map(data -> data.convertTo(unit, Configuration.defaultInstance()))
              .map(Quantity::value).collect(ImmutableList.toImmutableList());
      AbstractNumber sum = values.stream().reduce(AbstractNumber::add)
              .orElseThrow(AssertionError::new);
      AbstractNumber average = sum.divide(BigInteger.valueOf(values.size()));
      return Quantity.create(average, unit);
    }
  };

  /**
   * 中位数选择器
   */
  private static final QuantityChooser MEDIAN_AMOUNT_CHOOSER = new QuantityChooser() {

    @Override
    protected @NonNull Quantity chooseInternal(@NonNull Stream<Quantity> amounts,
                                               @NonNull Configuration configuration) {
      ImmutableList<Quantity> sortedQuantities = amounts.sorted(configuration::compare)
              .collect(ImmutableList.toImmutableList());
      int medianIndex = sortedQuantities.size() / 2;

      //集合总数是否是偶数
      boolean sizeIsEven = (sortedQuantities.size() % 2) == 0;
      if (sizeIsEven) {
        //偶数时中位数为中位的两个数值的平均数
        return averageAmountChooser().chooseInternal(
                Stream.of(sortedQuantities.get(medianIndex - 1), sortedQuantities.get(medianIndex)),
                configuration);
      } else {
        return sortedQuantities.get(medianIndex);
      }
    }
  };

  /**
   * 最小值选择器
   *
   * @return 最小值选择器
   * @author caotc
   * @date 2019-05-23
   * @since 1.0.0
   */
  @NonNull
  public static QuantityChooser minAmountChooser() {
    return MIN_AMOUNT_CHOOSER;
  }

  /**
   * 最大值选择器
   *
   * @return 最大值选择器
   * @author caotc
   * @date 2019-05-23
   * @since 1.0.0
   */
  @NonNull
  public static QuantityChooser maxAmountChooser() {
    return MAX_AMOUNT_CHOOSER;
  }

  /**
   * 平均数选择器
   *
   * @return 平均数选择器
   * @author caotc
   * @date 2019-05-23
   * @apiNote 该选择器返回的 {@link Quantity}对象可能不存在于参数集合中,而是计算后生成的新对象
   * @since 1.0.0
   */
  @NonNull
  public static QuantityChooser averageAmountChooser() {
    return AVERAGE_AMOUNT_CHOOSER;
  }

  /**
   * 中位数选择器
   *
   * @return 中位数选择器
   * @author caotc
   * @date 2019-05-23
   * @apiNote 该选择器返回的 {@link Quantity}对象可能不存在于参数集合中,而是计算后生成的新对象
   * @since 1.0.0
   */
  @NonNull
  public static QuantityChooser medianAmountChooser() {
    return MEDIAN_AMOUNT_CHOOSER;
  }

  /**
   * 目标值选择器工厂方法
   *
   * @param targetValue 目标值
   * @return 目标值选择器
   * @author caotc
   * @date 2019-05-24
   * @since 1.0.0
   */
  @NonNull
  public static QuantityChooser createTargetValueAmountChooser(@NonNull AbstractNumber targetValue) {
    return new TargetValueQuantityChooser(targetValue);
  }

  /**
   * 目标值选择器工厂方法
   *
   * @param targetValue 目标值
   * @return 目标值选择器
   * @author caotc
   * @date 2019-05-24
   * @since 1.0.0
   */
  @NonNull
  public static QuantityChooser createTargetValueAmountChooser(long targetValue) {
    return new TargetValueQuantityChooser(BigInteger.valueOf(targetValue));
  }

  /**
   * 目标值选择器工厂方法
   *
   * @param targetValue 目标值
   * @return 目标值选择器
   * @author caotc
   * @date 2019-05-24
   * @since 1.0.0
   */
  @NonNull
  public static QuantityChooser createTargetValueAmountChooser(
          @NonNull java.math.BigInteger targetValue) {
    return new TargetValueQuantityChooser(BigInteger.valueOf(targetValue));
  }

  /**
   * 目标值选择器工厂方法
   *
   * @param targetValue 目标值
   * @return 目标值选择器
   * @author caotc
   * @date 2019-05-24
   * @since 1.0.0
   */
  @NonNull
  public static QuantityChooser createTargetValueAmountChooser(
          @NonNull java.math.BigDecimal targetValue) {
    return new TargetValueQuantityChooser(BigDecimal.valueOf(targetValue));
  }

  /**
   * 从集合中选取目标对象
   *
   * @param amounts       对象集合
   * @param configuration 配置
   * @return 目标对象
   * @throws IllegalArgumentException 如果{@code amounts}参数为空集合
   * @author caotc
   * @date 2019-01-16
   * @since 1.0.0
   */
  @NonNull
  public final Quantity choose(@NonNull Iterable<Quantity> amounts,
                               @NonNull Configuration configuration) {
    Preconditions.checkArgument(!Iterables.isEmpty(amounts), "can't choose from empty Iterable");
    return chooseInternal(Streams.stream(amounts), configuration);
  }

  /**
   * 从集合中选取目标对象
   *
   * @param amounts       对象集合
   * @param configuration 配置
   * @return 目标对象
   * @throws IllegalArgumentException 如果{@code amounts}参数为空集合
   * @author caotc
   * @date 2019-01-16
   * @since 1.0.0
   */
  @NonNull
  public final Quantity choose(@NonNull Iterator<Quantity> amounts,
                               @NonNull Configuration configuration) {
    Preconditions.checkArgument(amounts.hasNext(), "can't choose from empty Iterator");
    return chooseInternal(Streams.stream(amounts), configuration);
  }

  /**
   * 从集合中选取目标对象
   *
   * @param amounts 对象集合
   * @param configuration 配置
   * @return 目标对象
   * @author caotc
   * @date 2019-01-16
   * @implNote 传入参数 {@link Stream< Quantity >}不可能为空流,无需参数校验
   * @since 1.0.0
   */
  @NonNull
  protected abstract Quantity chooseInternal(@NonNull Stream<Quantity> amounts,
                                             @NonNull Configuration configuration);
}

/**
 * 目标值选择器,选择最接近目标值的对象
 *
 * @author caotc
 * @date 2019-05-24
 * @since 1.0.0
 */
@Value
class TargetValueQuantityChooser extends QuantityChooser {

  @NonNull
  AbstractNumber targetValue;

  @Override
  protected @NonNull Quantity chooseInternal(@NonNull Stream<Quantity> amounts,
                                             @NonNull Configuration configuration) {
    ImmutableMap<@NonNull AbstractNumber, Quantity> valueToAmounts = amounts.distinct()
            .collect(ImmutableMap.toImmutableMap(Quantity::value, Function.identity()));
    ImmutableSortedSet<AbstractNumber> sortedValues = valueToAmounts.keySet().stream()
        .collect(ImmutableSortedSet.toImmutableSortedSet(Comparator.naturalOrder()));
    return valueToAmounts.get(sortedValues.higher(targetValue));
  }
}