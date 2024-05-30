package org.caotc.unit4j.support.jackson.provider;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.caotc.unit4j.core.unit.Unit;
import org.caotc.unit4j.core.unit.Units;

import java.util.stream.Stream;

/**
 * @author caotc
 * @date 2022-08-18
 * @since 1.0.0
 */
@UtilityClass
@Slf4j
public class UnitSerializerProvider {
    static Stream<Unit> units() {
        return Units.VALUES.stream();
    }
}