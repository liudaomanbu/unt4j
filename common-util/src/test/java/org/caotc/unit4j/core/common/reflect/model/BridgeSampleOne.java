package org.caotc.unit4j.core.common.reflect.model;

/**
 * @author caotc
 * @date 2023-02-15
 * @since 1.0.0
 */
public class BridgeSampleOne {
    public static class A<T> {
        public T getT() {
            return null;
        }
    }

    public static class B extends A<String> {
        public String getT() {
            return null;
        }
    }

    public static class C extends B {
        public String getT() {
            return null;
        }
    }
}
