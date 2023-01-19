package org.caotc.unit4j.core.common.util.model;

/**
 * @author caotc
 * @date 2023-01-11
 * @since 1.0.0
 */
public class CompositeGetterObject {
    public Internal getInternal() {
        return new Internal();
    }

    public static class Internal {
        public Integer getValue() {
            return Integer.MAX_VALUE;
        }
    }
}



