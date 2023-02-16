package org.caotc.unit4j.core.common.reflect.model;

/**
 * @author caotc
 * @date 2023-02-15
 * @since 1.0.0
 */
public class BridgeSampleThree {
    static class A {
        public void foo() {
        }
    }

    public static class B extends A {

    }

    public static class C extends B {
        public void foo() {
        }
    }
}
