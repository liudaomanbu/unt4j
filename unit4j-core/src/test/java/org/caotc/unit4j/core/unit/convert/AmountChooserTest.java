package org.caotc.unit4j.core.unit.convert;

import com.google.common.collect.ImmutableList;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.caotc.unit4j.core.Amount;
import org.caotc.unit4j.core.Configuration;
import org.caotc.unit4j.core.constant.UnitConstant;
import org.caotc.unit4j.core.convert.AmountChooser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author caotc
 * @date 2019-01-16
 * @since 1.0.0
 **/
@Slf4j
class AmountChooserTest {

  private static final Collection<Amount> EMPTY_AMOUNTS = ImmutableList.of();
  private static final Collection<Amount> AMOUNTS = ImmutableList
      .of(Amount.create(BigDecimal.TEN, UnitConstant.DAY),
          Amount.create(BigDecimal.TEN, UnitConstant.SECOND),
          Amount.create(BigDecimal.TEN, UnitConstant.HOUR));

  private Configuration configuration = Configuration.defaultInstance();

  @Test
  void test() {
    Stream.of(AmountChooser.minAmountChooser(),
        AmountChooser.maxAmountChooser()
        , AmountChooser.averageAmountChooser(),
        AmountChooser.medianAmountChooser())
        .forEach(this::choose);
  }

  void choose(AmountChooser amountChooser) {
    log.info("amountChooser:{}", amountChooser);
    //检查空集合
    Assertions
        .assertThrows(IllegalArgumentException.class,
            () -> amountChooser.choose(EMPTY_AMOUNTS, Configuration.defaultInstance()));
    Amount amount = amountChooser.choose(AMOUNTS, Configuration.defaultInstance());
    Assertions.assertNotNull(amount);
    log.info("selected amount:{}", amount);
  }
}