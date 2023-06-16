package org.caotc.unit4j.core.convert;

import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;
import org.caotc.unit4j.core.Configuration;
import org.caotc.unit4j.core.Quantity;
import org.caotc.unit4j.core.unit.UnitConstant;
import org.caotc.unit4j.core.unit.UnitGroup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Random;
import java.util.stream.Stream;

/**
 * @author caotc
 * @date 2019-01-16
 * @since 1.0.0
 **/
@Slf4j
class QuantityChooserTest {

    private static final Collection<Quantity> EMPTY_QUANTITIES = ImmutableList.of();
    private static final Collection<Quantity> QUANTITIES = ImmutableList
            .of(Quantity.create(BigDecimal.TEN, UnitConstant.DAY),
                    Quantity.create(BigDecimal.TEN, UnitConstant.SECOND),
                    Quantity.create(BigDecimal.TEN, UnitConstant.HOUR));

    private Configuration configuration = Configuration.defaultInstance();

    @RepeatedTest(100)
    void choose() {
        Random random = new Random();
        Provider.QuantityGroup quantityGroup = Provider.randomQuantityGroup(UnitGroup.createSiUnitGroup(UnitConstant.METER, Configuration.defaultInstance()), random.nextInt(100) + 1);
        log.debug("quantities:{}", quantityGroup.quantities());
        Quantity min = QuantityChooser.minQuantityChooser().choose(quantityGroup.quantities(), Configuration.defaultInstance());
        log.debug("min:{}", min);
        Assertions.assertEquals(quantityGroup.min(), min);
        Quantity max = QuantityChooser.maxQuantityChooser().choose(quantityGroup.quantities(), Configuration.defaultInstance());
        log.debug("max:{}", max);
        Assertions.assertEquals(quantityGroup.max(), max);
        Quantity median = QuantityChooser.medianQuantityChooser().choose(quantityGroup.quantities(), Configuration.defaultInstance());
        log.debug("median:{}", median);
        Assertions.assertEquals(0, Configuration.defaultInstance().compare(quantityGroup.median(), median));
        Quantity average = QuantityChooser.averageQuantityChooser().choose(quantityGroup.quantities(), Configuration.defaultInstance());
        log.debug("average:{}", average);
        Assertions.assertEquals(0, Configuration.defaultInstance().compare(quantityGroup.average(), average));

//        Quantity target = QuantityChooser.targetValueQuantityChooser(quantityGroup.quantities()).choose(quantityGroup.quantities(), Configuration.defaultInstance());
//        log.debug("average:{}", average);
//        Assertions.assertEquals(average,quantityGroup.average());
    }

    @Test
    void test() {
        Stream.of(QuantityChooser.minQuantityChooser(),
                        QuantityChooser.maxQuantityChooser()
                        , QuantityChooser.averageQuantityChooser(),
                        QuantityChooser.medianQuantityChooser())
                .forEach(this::choose);
    }

    void choose(QuantityChooser quantityChooser) {
        log.info("amountChooser:{}", quantityChooser);
        //检查空集合
        Assertions
                .assertThrows(IllegalArgumentException.class,
                        () -> quantityChooser.choose(EMPTY_QUANTITIES, Configuration.defaultInstance()));
        Quantity quantity = quantityChooser.choose(QUANTITIES, Configuration.defaultInstance());
        Assertions.assertNotNull(quantity);
        log.info("selected amount:{}", quantity);
    }
}