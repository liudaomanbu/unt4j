package org.caotc.unit4j.core.common.reflect.property.model;

/**
 * @author caotc
 * @date 2023-02-15
 * @since 1.0.0
 */
public class BridgeSampleTwo {
    public static class A<T> {
        public T getT(T args) {
            return args;
        }
    }

    public static class B extends A<String> {
        public String getT(String args) {
            return args;
        }
    }
}
