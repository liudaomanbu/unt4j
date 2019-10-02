package org.caotc.unit4j.support.mybatis.model;

import lombok.Data;
import lombok.ToString;
import org.caotc.unit4j.core.Amount;
import org.caotc.unit4j.support.CodecStrategy;
import org.caotc.unit4j.support.annotation.AmountSerialize;

/**
 * `
 *
 * @author caotc
 * @date 2019-09-27
 * @since 1.0.0
 */
@Data
@ToString(callSuper = true)
public class TestAmount {

  Long id;

  @AmountSerialize(strategy = CodecStrategy.VALUE)
  Amount amountValue;

  @AmountSerialize(strategy = CodecStrategy.FLAT)
  Amount amountFlat;
}