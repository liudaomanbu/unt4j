package org.caotc.unit4j.core.unit;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Value;

/**
 * @author caotc
 * @date 2023-05-17
 * @since 1.0.0
 */
@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@lombok.Builder
public class SimplifyConfig {
//    //(SECOND)(MINUTE)->60_(SECOND)² todo dimensionMerge ? name
//    @lombok.Builder.Default
//    boolean dimensionMerge=true;
//    //(10³_GRAM)(10³_METER)->10⁶_((GRAM)(METER)) todo prefixMerge ? name
//    @lombok.Builder.Default
//    boolean prefixMerge=true;
    //(10³_GRAM)⁻¹->10⁻³_(GRAM)⁻¹ todo prefixUnit ? name
    @lombok.Builder.Default
    boolean prefixUnit =true;
    @lombok.Builder.Default
    boolean recursive=true;

    @NonNull
    public static SimplifyConfig of(){
        return builder().build();
    }
}
