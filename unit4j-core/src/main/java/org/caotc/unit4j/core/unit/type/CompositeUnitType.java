package org.caotc.unit4j.core.unit.type;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Singular;
import lombok.ToString;
import lombok.Value;
import org.caotc.unit4j.core.common.util.Util;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 组合单位类型,由基本到位和其他组合单位类型组合而成
 *
 * @author caotc
 * @date 2018-04-14
 * @since 1.0.0
 **/
@Value
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = false)
public class CompositeUnitType extends UnitType {
    //todo
//    private static final Serializer<UnitType> ID_SERIALIZER = ComponentSerializer.<UnitType>builder()
//            .configuration(Configuration.defaultInstance())
//            .baseSerializer(UnitType::id)
//            .aliasUndefinedStrategy(AliasUndefinedStrategy.AUTO_COMPOSITE)
//            .build();
    /**
     * 单位类型组件与对应指数
     */
    @NonNull
    @Singular
    ImmutableMap<@NonNull UnitType, @NonNull Integer> componentToExponents;

    CompositeUnitType(@NonNull ImmutableMap<@NonNull UnitType, @NonNull Integer> componentToExponents) {
        this.componentToExponents = componentToExponents;
        validate();
    }

    @NonNull
    @Override
    public String id() {
        return Util.createCompositeId(this.componentToExponents());
    }

    @NonNull
    @Override
    public UnitType rebase() {
        return builder().componentToExponents(componentToExponents().entrySet().stream()
                        .map(entry -> entry.getKey().rebase().componentToExponents().entrySet().stream()
                                .collect(ImmutableMap.toImmutableMap(Entry::getKey, e -> e.getValue() * entry.getValue())))
                        .map(Map::entrySet)
                        .flatMap(Collection::stream)
                        .collect(ImmutableMap.toImmutableMap(Entry::getKey, Entry::getValue, Integer::sum)))
                .build();
    }

    public @NonNull UnitType simplify(boolean recursive) {
        return builder().componentToExponents(componentToExponents().entrySet().stream()
                        .map(entry -> {
                            UnitType unitType = builder()
                                    .componentToExponents(Maps.transformValues(entry.getKey().componentToExponents(), i -> i * entry.getValue()))
                                    .build();
                            if (recursive && unitType.componentToExponents().size() > 1) {//todo 封装为unit方法？
                                unitType = unitType.simplify(true);
                            }
                            return unitType;
                        })
                        .map(UnitType::componentToExponents)
                        .map(Map::entrySet)
                        .flatMap(Collection::stream)
                        .collect(ImmutableMap.toImmutableMap(Entry::getKey, Entry::getValue, Integer::sum)))
                .build();
    }

    private void validate() {
        if (componentToExponents().size() == 1 && Iterables.getOnlyElement(componentToExponents().values()) == 1) {
            throw new IllegalArgumentException(String.format("UnitType only and Exponent is 1.%s is not a CompositeUnitType", componentToExponents()));
        }
        if (componentToExponents().containsValue(0)) {
            throw new IllegalArgumentException(String.format("Exponent can not is 0.%s", componentToExponents()));
        }
    }
}
