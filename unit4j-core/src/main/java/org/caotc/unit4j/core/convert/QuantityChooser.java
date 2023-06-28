package org.caotc.unit4j.core.convert;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Streams;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import org.caotc.unit4j.core.Configuration;
import org.caotc.unit4j.core.Quantity;
import org.caotc.unit4j.core.math.number.AbstractNumber;
import org.caotc.unit4j.core.math.number.BigInteger;
import org.caotc.unit4j.core.unit.Unit;

import java.util.Iterator;
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
  private static final QuantityChooser MIN_QUANTITY_CHOOSER = new QuantityChooser() {

      @Override
      protected @NonNull Quantity chooseInternal(@NonNull Stream<Quantity> quantities,
                                                 @NonNull Configuration configuration) {
          return quantities.min(configuration::compare).orElseThrow(AssertionError::new);
      }
  };

    /**
     * 最大值选择器
     */
    private static final QuantityChooser MAX_QUANTITY_CHOOSER = new QuantityChooser() {

        @Override
        protected @NonNull Quantity chooseInternal(@NonNull Stream<Quantity> quantities,
                                                   @NonNull Configuration configuration) {
            return quantities.max(configuration::compare).orElseThrow(AssertionError::new);
        }
    };

    /**
     * 平均数选择器
     */
    private static final QuantityChooser AVERAGE_QUANTITY_CHOOSER = new QuantityChooser() {

        @Override
        protected @NonNull Quantity chooseInternal(@NonNull Stream<Quantity> quantities,
                                                   @NonNull Configuration configuration) {
            ImmutableList<Quantity> quantityImmutableList = quantities.collect(ImmutableList.toImmutableList());
            Unit unit = quantityImmutableList.stream().findAny().map(Quantity::unit)
                    .orElseThrow(AssertionError::new);
            ImmutableList<AbstractNumber> values = quantityImmutableList.stream()
                    .map(data -> data.convertTo(unit, configuration))
                    .map(Quantity::value)
                    .collect(ImmutableList.toImmutableList());
            AbstractNumber sum = values.stream().reduce(AbstractNumber::add)
                    .orElseThrow(AssertionError::new);
            AbstractNumber average = sum.divide(BigInteger.valueOf(values.size()));
      return Quantity.create(average, unit);
        }
    };

    /**
     * 中位数选择器
     */
    private static final QuantityChooser MEDIAN_QUANTITY_CHOOSER = new QuantityChooser() {

        @Override
        protected @NonNull Quantity chooseInternal(@NonNull Stream<Quantity> quantities,
                                                   @NonNull Configuration configuration) {
            ImmutableList<Quantity> sortedQuantities = quantities.sorted(configuration::compare)
                    .collect(ImmutableList.toImmutableList());
            int medianIndex = sortedQuantities.size() / 2;

            //集合总数是否是偶数
            boolean sizeIsEven = (sortedQuantities.size() % 2) == 0;
            if (sizeIsEven) {
                //偶数时中位数为中位的两个数值的平均数
                return averageQuantityChooser().chooseInternal(
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
    public static QuantityChooser minQuantityChooser() {
        return MIN_QUANTITY_CHOOSER;
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
    public static QuantityChooser maxQuantityChooser() {
        return MAX_QUANTITY_CHOOSER;
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
    public static QuantityChooser averageQuantityChooser() {
        return AVERAGE_QUANTITY_CHOOSER;
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
    public static QuantityChooser medianQuantityChooser() {
        return MEDIAN_QUANTITY_CHOOSER;
    }

    /**
     * 从集合中选取目标对象
     *
     * @param quantities    对象集合
     * @param configuration 配置
     * @return 目标对象
     * @throws IllegalArgumentException 如果{@code quantities}参数为空集合
     * @author caotc
     * @date 2019-01-16
     * @since 1.0.0
     */
    @NonNull
    public final Quantity choose(@NonNull Iterable<Quantity> quantities,
                                 @NonNull Configuration configuration) {
        Preconditions.checkArgument(!Iterables.isEmpty(quantities), "can't choose from empty Iterable");
        return chooseInternal(Streams.stream(quantities), configuration);
    }

    /**
     * 从集合中选取目标对象
     *
     * @param quantities    对象集合
     * @param configuration 配置
     * @return 目标对象
     * @throws IllegalArgumentException 如果{@code quantities}参数为空集合
     * @author caotc
     * @date 2019-01-16
     * @since 1.0.0
     */
    @NonNull
    public final Quantity choose(@NonNull Iterator<Quantity> quantities,
                                 @NonNull Configuration configuration) {
        Preconditions.checkArgument(quantities.hasNext(), "can't choose from empty Iterator");
        return chooseInternal(Streams.stream(quantities), configuration);
    }

    /**
     * 从集合中选取目标对象
     *
     * @param quantities    对象集合
     * @param configuration 配置
     * @return 目标对象
     * @author caotc
     * @date 2019-01-16
     * @implNote 传入参数 {@link Stream< Quantity >}不可能为空流,无需参数校验
     * @since 1.0.0
     */
    @NonNull
  protected abstract Quantity chooseInternal(@NonNull Stream<Quantity> quantities,
                                             @NonNull Configuration configuration);
}