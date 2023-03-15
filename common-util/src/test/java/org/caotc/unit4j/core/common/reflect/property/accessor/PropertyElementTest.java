package org.caotc.unit4j.core.common.reflect.property.accessor;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;

@Slf4j
class PropertyElementTest {
    void test() {
//        Class<?> clazz = Class.forName("org.caotc.unit4j.core.common.reflect.property.accessor.PropertyElementTest$MyClass");
        Class<?> clazz1 = MyClass.class;
        System.out.println(clazz1.hashCode());
        Field field = null;
        try {
            field = clazz1.getDeclaredField("name");
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        System.out.println(field != null);

        try {
            Thread.sleep(10000); // 等待5秒，模拟类的动态更新
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

//        clazz = Class.forName("org.caotc.unit4j.core.common.reflect.property.accessor.PropertyElementTest$MyClass");
        Class<?> clazz2 = MyClass.class;
        System.out.println(clazz2.hashCode());
        System.out.println("clazz1==clazz2:" + (clazz1 == clazz2));
        System.out.println("clazz1.equals(clazz2):" + (clazz1.equals(clazz2)));
        field = null;
        try {
            field = clazz2.getDeclaredField("name");
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        System.out.println(field != null);
    }

    public static class MyClass {

    }
}