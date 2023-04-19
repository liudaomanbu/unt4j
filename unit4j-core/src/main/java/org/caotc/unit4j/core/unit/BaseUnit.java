package org.caotc.unit4j.core.unit;

import lombok.NonNull;
import org.caotc.unit4j.core.unit.type.BaseUnitType;

/**
 * 基本类型单位
 *
 * @author caotc
 * @date 2019-05-26
 * @since 1.0.0
 */
public abstract class BaseUnit extends Unit {
    @Override
    @NonNull
    public abstract BaseUnitType type();
}
