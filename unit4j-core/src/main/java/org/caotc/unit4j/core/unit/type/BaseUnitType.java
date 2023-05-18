package org.caotc.unit4j.core.unit.type;

import com.google.common.collect.ImmutableMap;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;

/**
 * 基本单位类型 由于基本单位类型没有任何有意义的关联属性,纯粹由定义产生,因此仅拥有id属性,用以定义和区分基本单位类型
 *
 * @author caotc
 * @date 2018-04-14
 * @since 1.0.0
 **/
@Value
@ToString(callSuper = false)
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor(access = AccessLevel.MODULE)
public class BaseUnitType extends UnitType {
    @NonNull
    String id;

    @NonNull
    @Override
    public ImmutableMap<UnitType, Integer> componentToExponents() {
        return ImmutableMap.of(this, 1);
    }

    @NonNull
    @Override
    public UnitType rebase() {
        return this;
    }

}
