package org.caotc.unit4j.core.common.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author caotc
 * @date 2022-08-17
 * @since 1.0.0
 */
public abstract class SuperClass {

    protected void toBeOverriden() {
        System.out.println("In super class.");

    }

}

class ImplClass extends SuperClass {

    public void toBeOverriden() {

        System.out.println("In sub class.");

    }

}

class Main {
    public static void main(String[] args) throws NoSuchMethodException,
            SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {

        SuperClass object = new ImplClass();

        Class<SuperClass> c = SuperClass.class;
        Method method = c.getDeclaredMethod("toBeOverriden");
        method.setAccessible(true);
        method.invoke(object);

        Arrays.stream(B.class.getDeclaredMethods()).forEach(System.out::println);
    }

    static class A {
        public void foo() {
        }
    }

    static class B extends A {

    }

    public static class C extends A {

    }

    public static class D extends A {
        public void foo() {
        }
    }
}
