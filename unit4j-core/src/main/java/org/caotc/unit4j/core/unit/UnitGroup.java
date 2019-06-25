package org.caotc.unit4j.core.unit;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Optional;
import java.util.SortedSet;
import lombok.NonNull;
import lombok.Singular;
import lombok.Value;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

/**
 * 单位组
 *
 * @author caotc
 * @date 2018-04-13
 * @since 1.0.0
 **/
@Value
@Slf4j
public class UnitGroup implements SortedSet<Unit> {

  /**
   * 工厂方法
   *
   * @param standardUnit 标准单位
   * @param comparator 单位比较器
   * @return 该标准单位与所有国际单位制词头单位的单位组
   * @author caotc
   * @date 2019-05-27
   * @since 1.0.0
   */
  @NonNull
  public static UnitGroup createSiUnitGroup(@NonNull StandardUnit standardUnit,
      @NonNull Comparator<Unit> comparator) {
    return builder(comparator)
        .unit(standardUnit)
        .units(Prefix.SI_UNIT_PREFIXES.stream().map(standardUnit::addPrefix)
            .collect(ImmutableSet.toImmutableSet())).build();
  }

  /**
   * 工厂方法
   *
   * @param units 单位集合
   * @param comparator 单位比较器
   * @return 该单位集合的单位组
   * @author caotc
   * @date 2019-05-27
   * @since 1.0.0
   */
  @NonNull
  public static UnitGroup create(@NonNull Iterable<? extends Unit> units,
      @NonNull Comparator<Unit> comparator) {
    return builder(comparator).units(units).build();
  }

  /**
   * 以大小排序的单位集合
   */
  @NonNull
  @Singular
  ImmutableSortedSet<Unit> units;

  /**
   * 获取单位组中比传入单位小的最近单位
   *
   * @param unit 比较单位
   * @return 比传入单位小的最近单位
   * @author caotc
   * @date 2019-05-27
   * @since 1.0.0
   */
  @NonNull
  public Optional<Unit> lower(@NonNull Unit unit) {
    return Optional.ofNullable(this.units().lower(unit));
  }

  /**
   * 获取单位组中比传入单位大的最近单位
   *
   * @param unit 比较单位
   * @return 比传入单位大的最近单位
   * @author caotc
   * @date 2019-05-27
   * @since 1.0.0
   */
  @NonNull
  public Optional<Unit> higher(@NonNull Unit unit) {
    return Optional.ofNullable(this.units().higher(unit));
  }

  @Override
  public Comparator<? super Unit> comparator() {
    return units.comparator();
  }

  @Override
  public SortedSet<Unit> subSet(Unit fromElement, Unit toElement) {
    return units.subSet(fromElement, toElement);
  }

  @Override
  public SortedSet<Unit> headSet(Unit toElement) {
    return units.headSet(toElement);
  }

  @Override
  public SortedSet<Unit> tailSet(Unit fromElement) {
    return units.tailSet(fromElement);
  }

  @Override
  @NonNull
  public Unit first() {
    return units.first();
  }

  @Override
  @NonNull
  public Unit last() {
    return units.last();
  }

  @Override
  public int size() {
    return units.size();
  }

  @Override
  public boolean isEmpty() {
    return units.isEmpty();
  }

  @Override
  public boolean contains(Object o) {
    return units.contains(o);
  }

  @Override
  public Iterator<Unit> iterator() {
    return units.iterator();
  }

  @Override
  public Object[] toArray() {
    return units.toArray();
  }

  @SuppressWarnings("SuspiciousToArrayCall")
  @Override
  public <T> T[] toArray(@NonNull T[] a) {
    return units.toArray(a);
  }

  @Deprecated
  @Override
  public boolean add(Unit unit) {
    return units.add(unit);
  }

  @Deprecated
  @Override
  public boolean remove(Object o) {
    return units.remove(o);
  }

  @Override
  public boolean containsAll(@NonNull Collection<?> c) {
    return units.containsAll(c);
  }

  @Deprecated
  @Override
  public boolean addAll(@NonNull Collection<? extends Unit> c) {
    return units.addAll(c);
  }

  @Deprecated
  @Override
  public boolean retainAll(@NonNull Collection<?> c) {
    return units.retainAll(c);
  }

  @Deprecated
  @Override
  public boolean removeAll(@NonNull Collection<?> c) {
    return units.removeAll(c);
  }

  @Deprecated
  @Override
  public void clear() {
    units.clear();
  }

  private UnitGroup valid() {
    this.units()
        .forEach(
            unit -> this
                .units()
                .forEach(o -> Preconditions.checkArgument(unit.type().equals(o.type())
                    , "%s and %s can't convert,%s and %s are not type rebase equal",
                    unit, o, unit, o)));
    return this;
  }

  public static UnitGroupBuilder builder(@NonNull Comparator<Unit> comparator) {
    return new UnitGroupBuilder(comparator);
  }

  //TODO 自动化处理
  public static class UnitGroupBuilder {

    @NonFinal
    private ImmutableSortedSet.Builder<Unit> units;
    private final Comparator<Unit> comparator;

    UnitGroupBuilder(@NonNull Comparator<Unit> comparator) {
      this.comparator = comparator;
      this.units = ImmutableSortedSet.orderedBy(comparator);
    }

    public UnitGroup.UnitGroupBuilder unit(Unit compositePrefixUnit) {
      this.units.add(compositePrefixUnit);
      return this;
    }

    public UnitGroup.UnitGroupBuilder units(Iterable<? extends Unit> units) {
      this.units.addAll(units);
      return this;
    }

    public UnitGroup.UnitGroupBuilder clearUnits() {
      this.units = ImmutableSortedSet.orderedBy(comparator);
      return this;
    }

    public UnitGroup build() {
      ImmutableSortedSet<Unit> compositePrefixUnits =
          this.units == null ? ImmutableSortedSet.of() : this.units.build();
      return new UnitGroup(compositePrefixUnits).valid();
    }
  }

}
