package org.caotc.unit4j.core.unit.convert;

import java.math.BigDecimal;
import lombok.extern.slf4j.Slf4j;
import org.caotc.unit4j.core.Amount;
import org.caotc.unit4j.core.Configuration;
import org.caotc.unit4j.core.constant.UnitConstant;
import org.caotc.unit4j.core.convert.TargetUnitChooser;
import org.caotc.unit4j.core.unit.Unit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author caotc
 * @date 2019-01-18
 * @implSpec
 * @implNote
 * @since 1.0.0
 **/
@Slf4j
abstract class TargetCompositePrefixUnitChooserTest {

  TargetUnitChooser targetUnitChooser = targetUnitChooser();

  protected abstract TargetUnitChooser targetUnitChooser();

  @Test
  void findTargetUnit() {
    Unit targetCompositePrefixUnit = targetUnitChooser
        .targetUnitFromAmount(Amount.create(BigDecimal.TEN, UnitConstant.HOUR),
            Configuration.defaultInstance());
    Assertions.assertNotNull(targetCompositePrefixUnit);
    log.info("targetCompositePrefixUnit:{}", targetCompositePrefixUnit);
  }

}