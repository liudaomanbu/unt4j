package org.caotc.unit4j.support.spring;

import java.math.RoundingMode;
import lombok.Value;
import lombok.experimental.Accessors;
import org.caotc.unit4j.core.Amount;
import org.caotc.unit4j.core.math.number.BigDecimal;
import org.caotc.unit4j.support.CodecStrategy;
import org.caotc.unit4j.support.annotation.AmountDeserialize;
import org.caotc.unit4j.support.annotation.AmountSerialize;
import org.caotc.unit4j.support.annotation.DataType;

@Value(staticConstructor = "create")
@Accessors(fluent = false, chain = true)
public class AmountField {

  public static AmountField create(Amount amount) {
    return create(amount, amount, amount, amount, amount.intValueExact(), amount.intValueExact(),
        amount.intValueExact());
  }

  Amount noAnnotationAmount;

  @AmountSerialize(strategy = CodecStrategy.VALUE, valueType = BigDecimal.class)
  Amount annotationValueAmount;

  @AmountSerialize(strategy = CodecStrategy.OBJECT, valueType = byte.class, roundingMode = RoundingMode.HALF_UP)
  Amount annotationObjectAmount;

  @AmountSerialize(strategy = CodecStrategy.FLAT, valueType = String.class)
  Amount annotationFlatAmount;

  @AmountSerialize(strategy = CodecStrategy.VALUE, targetUnitId = "SECOND")
  @AmountDeserialize(strategy = CodecStrategy.VALUE, targetUnitId = "MINUTE")
  Integer dbValue;

  @AmountSerialize(strategy = CodecStrategy.FLAT, targetUnitId = "MINUTE")
  @AmountDeserialize(strategy = CodecStrategy.VALUE, targetUnitId = "MINUTE")
  Integer jsonValue;

  @DataType("createTime")
  Integer dataTypeValue;
}
