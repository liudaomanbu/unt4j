package org.caotc.unit4j.core.unit;

import lombok.Value;

/**
 * @author caotc
 * @date 2023-05-17
 * @since 1.0.0
 */
@Value
public class SimplifyConfig {
    boolean prefixMerge;
    boolean dimensionMerge;
    //todo prefixUnit (10³_GRAM)⁻¹->10⁻³_(GRAM)⁻¹? name
    boolean prefixUnit;
    boolean recursive;
}
