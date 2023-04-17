package org.caotc.unit4j.core.unit.convert;

import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;
import org.caotc.unit4j.core.Configuration;
import org.caotc.unit4j.core.Quantity;
import org.caotc.unit4j.core.constant.UnitConstant;
import org.caotc.unit4j.core.convert.QuantityChooser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collection;
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

    @Test
    void test() {
        Stream.of(QuantityChooser.minAmountChooser(),
                        QuantityChooser.maxAmountChooser()
                        , QuantityChooser.averageAmountChooser(),
                        QuantityChooser.medianAmountChooser())
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