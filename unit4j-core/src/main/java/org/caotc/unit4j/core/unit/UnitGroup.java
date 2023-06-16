package org.caotc.unit4j.core.unit;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Singular;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.caotc.unit4j.core.Configuration;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import java.util.SortedSet;
import java.util.Spliterator;

/**
 * 单位组
 *
 * @author caotc
 * @date 2018-04-13
 * @since 1.0.0
 **/
@Value
@Slf4j
@Builder
public class UnitGroup implements SortedSet<Unit>, List<Unit> {

  /**
   * 以大小排序的单位集合
   */
  @NonNull
  @Singular
  ImmutableList<Unit> units;
  @NonNull
  Configuration configuration;
  @NonNull
  @Getter(lazy = true)
  ImmutableSortedSet<Unit> sortedUnits = ImmutableSortedSet.<Unit>orderedBy(configuration::compare).addAll(units).build();

  UnitGroup(@NonNull ImmutableList<Unit> units, @NonNull Configuration configuration) {
    this.units = units.stream().sorted(configuration::compare).collect(ImmutableList.toImmutableList());
    this.configuration = configuration;
    Preconditions.checkArgument(!units.isEmpty());
  }

  /**
   * 工厂方法
   *
   * @param standardUnit  标准单位
   * @param configuration 单位比较器
   * @return 该标准单位与所有国际单位制词头单位的单位组
   * @author caotc
   * @date 2019-05-27
   * @since 1.0.0
   */
  @NonNull
  public static UnitGroup createSiUnitGroup(@NonNull StandardUnit standardUnit,
                                            @NonNull Configuration configuration) {
    return builder().configuration(configuration)
            .unit(standardUnit)
            .units(Prefix.SI_UNIT_PREFIXES.stream().map(standardUnit::addPrefix)
                    .collect(ImmutableSet.toImmutableSet())).build();
  }

  /**
   * 工厂方法
   *
   * @param units         单位集合
   * @param configuration 单位比较器
   * @return 该单位集合的单位组
   * @author caotc
   * @date 2019-05-27
   * @since 1.0.0
   */
  @NonNull
  public static UnitGroup create(@NonNull Iterable<? extends Unit> units,
                                 @NonNull Configuration configuration) {
    return builder().configuration(configuration).units(units).build();
  }

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
    return Optional.ofNullable(sortedUnits().lower(unit));
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
    return Optional.ofNullable(sortedUnits().higher(unit));
  }

  @Override
  public Comparator<? super Unit> comparator() {
    return sortedUnits().comparator();
  }

  @Override
  public SortedSet<Unit> subSet(Unit fromElement, Unit toElement) {
    return sortedUnits().subSet(fromElement, toElement);
  }

  @Override
  public SortedSet<Unit> headSet(Unit toElement) {
    return sortedUnits().headSet(toElement);
  }

  @Override
  public SortedSet<Unit> tailSet(Unit fromElement) {
    return sortedUnits().tailSet(fromElement);
  }

  @Override
  @NonNull
  public Unit first() {
    return sortedUnits().first();
  }

  @Override
  @NonNull
  public Unit last() {
    return sortedUnits().last();
  }

  @Override
  public Spliterator<Unit> spliterator() {
    return sortedUnits().spliterator();
  }

  @Override
  public int size() {
    return sortedUnits().size();
  }

  @Override
  public boolean isEmpty() {
    return sortedUnits().isEmpty();
  }

  @Override
  public boolean contains(Object o) {
    return sortedUnits().contains(o);
  }

  @Override
  public Iterator<Unit> iterator() {
    return sortedUnits().iterator();
  }

  @Override
  public Object[] toArray() {
    return sortedUnits().toArray();
  }

  @SuppressWarnings("SuspiciousToArrayCall")
  @Override
  public <T> T[] toArray(@NonNull T[] a) {
    return sortedUnits().toArray(a);
  }

  @Deprecated
  @Override
  public boolean add(Unit unit) {
    return sortedUnits().add(unit);
  }

  @Deprecated
  @Override
  public boolean remove(Object o) {
    return sortedUnits().remove(o);
  }

  @Override
  public boolean containsAll(@NonNull Collection<?> c) {
    return sortedUnits().containsAll(c);
  }

  @Deprecated
  @Override
  public boolean addAll(@NonNull Collection<? extends Unit> c) {
    return sortedUnits().addAll(c);
  }

  @Deprecated
  @Override
  public boolean addAll(int index, Collection<? extends Unit> c) {
    return units().addAll(index, c);
  }

  @Deprecated
  @Override
  public boolean retainAll(@NonNull Collection<?> c) {
    return sortedUnits().retainAll(c);
  }

  @Deprecated
  @Override
  public boolean removeAll(@NonNull Collection<?> c) {
    return sortedUnits().removeAll(c);
  }

  @Deprecated
  @Override
  public void clear() {
    sortedUnits().clear();
  }

  @Override
  public Unit get(int index) {
    return units().get(index);
  }

  @Deprecated
  @Override
  public Unit set(int index, Unit element) {
    return units().set(index, element);
  }

  @Deprecated
  @Override
  public void add(int index, Unit element) {
    units().add(index, element);
  }

  @Deprecated
  @Override
  public Unit remove(int index) {
    return units().remove(index);
  }

  @Override
  public int indexOf(Object o) {
    return units().indexOf(o);//todo 二分查找
  }

  @Override
  public int lastIndexOf(Object o) {
    return units().lastIndexOf(o);//todo 二分查找
  }

  @Override
  public ListIterator<Unit> listIterator() {
    return units().listIterator();
  }

  @Override
  public ListIterator<Unit> listIterator(int index) {
    return units().listIterator(index);
  }

  @Override
  public List<Unit> subList(int fromIndex, int toIndex) {
    return units().subList(fromIndex,toIndex);
  }
}
