package org.caotc.unit4j.core.unit.type;

import lombok.extern.slf4j.Slf4j;
import org.caotc.unit4j.core.unit.UnitTypes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Random;

@Slf4j
class UnitTypeTest {

    @Test
    void rebaseEquals() {
        Assertions.assertTrue(UnitTypes.LENGTH.rebaseEquals(UnitTypes.LENGTH));
        Assertions.assertTrue(UnitTypes.ENERGY_WORK_HEAT_QUANTITY.rebaseEquals(UnitTypes.FORCE_WEIGHT.multiply(UnitTypes.LENGTH)));
    }

    @Test
    void inverse() {
        Assertions.assertEquals(UnitType.builder().componentToExponent(UnitTypes.LENGTH, -1).build(), UnitTypes.LENGTH.inverse());
        Assertions.assertEquals(UnitTypes.NON, UnitTypes.NON.inverse());
        Assertions.assertEquals(UnitType.builder().componentToExponent(UnitTypes.LENGTH, -1)
                .componentToExponent(UnitTypes.MASS, -1)
                .build(), UnitType.builder().componentToExponent(UnitTypes.LENGTH, 1)
                .componentToExponent(UnitTypes.MASS, 1)
                .build().inverse());
        int value = new Random().nextInt();
        Assertions.assertEquals(UnitType.builder().componentToExponent(UnitTypes.LENGTH, -1).componentToExponent(UnitTypes.MASS, -value).build()
                , UnitType.builder().componentToExponent(UnitTypes.LENGTH, 1).componentToExponent(UnitTypes.MASS, value).build().inverse());
    }

    @Test
    void multiply() {
        Assertions.assertEquals(UnitType.builder().componentToExponent(UnitTypes.LENGTH, 2).build(), UnitTypes.LENGTH.multiply(UnitTypes.LENGTH));
        Assertions.assertEquals(UnitTypes.NON, UnitTypes.LENGTH.multiply(UnitType.builder().componentToExponent(UnitTypes.LENGTH, -1).build()));
        Assertions.assertEquals(UnitType.builder().componentToExponent(UnitTypes.LENGTH, 1)
                .componentToExponent(UnitTypes.MASS, 1)
                .build(), UnitTypes.LENGTH.multiply(UnitTypes.MASS));
        int value = new Random().nextInt();
        Assertions.assertEquals(UnitType.builder().componentToExponent(UnitTypes.LENGTH, 1).componentToExponent(UnitTypes.MASS, value).build()
                , UnitTypes.LENGTH.multiply(UnitType.builder().componentToExponent(UnitTypes.MASS, value).build()));
    }

    @Test
    void divide() {
        Assertions.assertEquals(UnitTypes.NON, UnitTypes.LENGTH.divide(UnitTypes.LENGTH));
        Assertions.assertEquals(UnitType.builder().componentToExponent(UnitTypes.LENGTH, 2).build(), UnitTypes.LENGTH.divide(UnitType.builder().componentToExponent(UnitTypes.LENGTH, -1).build()));
        Assertions.assertEquals(UnitType.builder().componentToExponent(UnitTypes.LENGTH, 1)
                .componentToExponent(UnitTypes.MASS, -1)
                .build(), UnitTypes.LENGTH.divide(UnitTypes.MASS));
        int value = new Random().nextInt();
        Assertions.assertEquals(UnitType.builder().componentToExponent(UnitTypes.LENGTH, 1).componentToExponent(UnitTypes.MASS, -value).build()
                , UnitTypes.LENGTH.divide(UnitType.builder().componentToExponent(UnitTypes.MASS, value).build()));
    }

}