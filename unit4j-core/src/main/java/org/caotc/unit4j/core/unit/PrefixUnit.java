package org.caotc.unit4j.core.unit;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import java.util.Optional;
import lombok.NonNull;
import org.caotc.unit4j.core.Configuration;
import org.caotc.unit4j.core.constant.StringConstant;
import org.caotc.unit4j.core.unit.type.UnitType;

/**
 * 有词头的单位
 *
 * @author caotc
 * @date 2019-05-27
 * @since 1.0.0
 */
public interface PrefixUnit extends Unit {

  /**
   * 词头与单位的id分隔符
   */
  String SEPARATOR = StringConstant.UNDERSCORE;

  /**
   * 该词头单位的标准单位
   *
   * @return 标准单位
   * @author caotc
   * @date 2019-05-27
   * @since 1.0.0
   */
  @NonNull
  StandardUnit standardUnit();

  @Override
  @NonNull
  default UnitType type() {
    return standardUnit().type();
  }

  @Override
  @NonNull
  default ImmutableMap<Unit, Integer> unitComponentToExponents() {
    return standardUnit().unitComponentToExponents();
  }

  @Override
  @NonNull
  default String id() {
    return composite(prefix().id(), standardUnit().id());
  }

  @Override
  @NonNull
  default ImmutableSet<Alias> aliasesFromConfiguration(@NonNull Configuration configuration) {
    return standardUnit().aliasesFromConfiguration(configuration).stream()
        .map(Alias::type)
        .map(aliasType -> aliasFromConfiguration(configuration, aliasType))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .collect(ImmutableSet.toImmutableSet());
  }

  @Override
  @NonNull
  default Optional<Alias> aliasFromConfiguration(@NonNull Configuration configuration,
      @NonNull Alias.Type aliasType) {
    return standardUnit().aliasFromConfiguration(configuration, aliasType)
        .map(Alias::value)
        .flatMap(standardUnitAlias -> prefix().aliasFromConfiguration(configuration, aliasType)
            .map(Alias::value)
            .map(prefixAlias -> composite(prefixAlias, standardUnitAlias)))
        .map(compositedAlias -> Alias.create(aliasType, compositedAlias));
  }

  /**
   * 组合该对象的词头和标准单位的id和别名的方法
   *
   * @param prefix 词头id或别名
   * @param standardUnit 准单位id或别名
   * @return 组合后的id或别名
   * @author caotc
   * @date 2019-05-27
   * @since 1.0.0
   */
  default String composite(@NonNull String prefix, @NonNull String standardUnit) {
    return StringConstant.UNDERLINE_JOINER.join(prefix, standardUnit);
  }
}
