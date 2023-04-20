package org.caotc.unit4j.core;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

/**
 * @author caotc
 * @date 2023-04-20
 * @since 1.0.0
 */
public class Test {
    public static void main(String[] args) {
//        Parent.Builder<?, ?> builder = Parent.builder();
        Child.Builder<?, ?> builder1 = Child.builder();
        Student.Builder<?, ?> builder2 = Student.builder();
//        builder.name1("");
//        builder1.name1("").name2("");
//        builder2.name1("").name2("").name3("");
    }
}

@Getter
@SuperBuilder
abstract class Parent {
//    String name1;
}

@Getter
@SuperBuilder
class Child extends Parent {
    String name2;
}

@Getter
@SuperBuilder
class Student extends Child {
    String name3;
}
