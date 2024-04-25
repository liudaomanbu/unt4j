package org.caotc.unit4j.api.annotation;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

@Slf4j
class WithUnitTest {

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.api.annotation.provider.WithUnitProvider#withUnitFieldAndValues")
    void isAnnotationPresent(Field withUnitField) {
        boolean exist = withUnitField.isAnnotationPresent(WithUnit.class);
        log.info("exist:{}", exist);
        Assertions.assertTrue(exist);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.api.annotation.provider.WithUnitProvider#withUnitGetMethodAndValues")
    void isAnnotationPresent(Method withUnitGetMethod) {
        boolean exist = withUnitGetMethod.isAnnotationPresent(WithUnit.class);
        log.info("exist:{}", exist);
        Assertions.assertTrue(exist);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.api.annotation.provider.WithUnitProvider#withUnitFieldAndValues")
    void getAnnotation(Field withUnitField, String withUnitValue) {
        WithUnit withUnitAnnotation = withUnitField.getAnnotation(WithUnit.class);
        log.info("withUnitAnnotation:{}", withUnitAnnotation);
        Assertions.assertNotNull(withUnitAnnotation);
        Assertions.assertEquals(withUnitValue, withUnitAnnotation.value());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.api.annotation.provider.WithUnitProvider#withUnitGetMethodAndValues")
    void getAnnotation(Method withUnitGetMethod, String withUnitValue) {
        WithUnit withUnitAnnotation = withUnitGetMethod.getAnnotation(WithUnit.class);
        log.info("withUnitAnnotation:{}", withUnitAnnotation);
        Assertions.assertNotNull(withUnitAnnotation);
        Assertions.assertEquals(withUnitValue, withUnitAnnotation.value());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.api.annotation.provider.WithUnitProvider#withUnitFieldAndValues")
    void getDeclaredAnnotation(Field withUnitField, String withUnitValue) {
        WithUnit withUnitAnnotation = withUnitField.getDeclaredAnnotation(WithUnit.class);
        log.info("withUnitAnnotation:{}", withUnitAnnotation);
        Assertions.assertNotNull(withUnitAnnotation);
        Assertions.assertEquals(withUnitValue, withUnitAnnotation.value());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.api.annotation.provider.WithUnitProvider#withUnitGetMethodAndValues")
    void getDeclaredAnnotation(Method withUnitGetMethod, String withUnitValue) {
        WithUnit withUnitAnnotation = withUnitGetMethod.getDeclaredAnnotation(WithUnit.class);
        log.info("withUnitAnnotation:{}", withUnitAnnotation);
        Assertions.assertNotNull(withUnitAnnotation);
        Assertions.assertEquals(withUnitValue, withUnitAnnotation.value());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.api.annotation.provider.WithUnitProvider#withUnitFieldAndValues")
    void getAnnotations(Field withUnitField, String withUnitValue) {
        Annotation[] annotations = withUnitField.getAnnotations();
        log.info("annotations:{}", Arrays.toString(annotations));
        Assertions.assertNotNull(annotations);
        Assertions.assertEquals(1, annotations.length);
        Annotation annotation = annotations[0];
        log.info("annotation:{}", annotation);
        Assertions.assertNotNull(annotation);
        Assertions.assertInstanceOf(WithUnit.class, annotation);
        WithUnit dataType = (WithUnit) annotation;
        Assertions.assertEquals(withUnitValue, dataType.value());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.api.annotation.provider.WithUnitProvider#withUnitGetMethodAndValues")
    void getAnnotations(Method withUnitGetMethod, String withUnitValue) {
        Annotation[] annotations = withUnitGetMethod.getAnnotations();
        log.info("annotations:{}", Arrays.toString(annotations));
        Assertions.assertNotNull(annotations);
        Assertions.assertEquals(1, annotations.length);
        Annotation annotation = annotations[0];
        log.info("annotation:{}", annotation);
        Assertions.assertNotNull(annotation);
        Assertions.assertInstanceOf(WithUnit.class, annotation);
        WithUnit dataType = (WithUnit) annotation;
        Assertions.assertEquals(withUnitValue, dataType.value());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.api.annotation.provider.WithUnitProvider#withUnitFieldAndValues")
    void getDeclaredAnnotations(Field withUnitField, String withUnitValue) {
        Annotation[] annotations = withUnitField.getDeclaredAnnotations();
        log.info("annotations:{}", Arrays.toString(annotations));
        Assertions.assertNotNull(annotations);
        Assertions.assertEquals(1, annotations.length);
        Annotation annotation = annotations[0];
        log.info("annotation:{}", annotation);
        Assertions.assertNotNull(annotation);
        Assertions.assertInstanceOf(WithUnit.class, annotation);
        WithUnit dataType = (WithUnit) annotation;
        Assertions.assertEquals(withUnitValue, dataType.value());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.api.annotation.provider.WithUnitProvider#withUnitGetMethodAndValues")
    void getDeclaredAnnotations(Method withUnitGetMethod, String withUnitValue) {
        Annotation[] annotations = withUnitGetMethod.getDeclaredAnnotations();
        log.info("annotations:{}", Arrays.toString(annotations));
        Assertions.assertNotNull(annotations);
        Assertions.assertEquals(1, annotations.length);
        Annotation annotation = annotations[0];
        log.info("annotation:{}", annotation);
        Assertions.assertNotNull(annotation);
        Assertions.assertInstanceOf(WithUnit.class, annotation);
        WithUnit dataType = (WithUnit) annotation;
        Assertions.assertEquals(withUnitValue, dataType.value());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.api.annotation.provider.WithUnitProvider#withUnitFieldAndValues")
    void getAnnotationsByType(Field withUnitField, String withUnitValue) {
        Annotation[] annotations = withUnitField.getAnnotationsByType(WithUnit.class);
        log.info("annotations:{}", Arrays.toString(annotations));
        Assertions.assertNotNull(annotations);
        Assertions.assertEquals(1, annotations.length);
        Annotation annotation = annotations[0];
        log.info("annotation:{}", annotation);
        Assertions.assertNotNull(annotation);
        Assertions.assertInstanceOf(WithUnit.class, annotation);
        WithUnit dataType = (WithUnit) annotation;
        Assertions.assertEquals(withUnitValue, dataType.value());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.api.annotation.provider.WithUnitProvider#withUnitGetMethodAndValues")
    void getAnnotationsByType(Method withUnitGetMethod, String withUnitValue) {
        Annotation[] annotations = withUnitGetMethod.getAnnotationsByType(WithUnit.class);
        log.info("annotations:{}", Arrays.toString(annotations));
        Assertions.assertNotNull(annotations);
        Assertions.assertEquals(1, annotations.length);
        Annotation annotation = annotations[0];
        log.info("annotation:{}", annotation);
        Assertions.assertNotNull(annotation);
        Assertions.assertInstanceOf(WithUnit.class, annotation);
        WithUnit dataType = (WithUnit) annotation;
        Assertions.assertEquals(withUnitValue, dataType.value());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.api.annotation.provider.WithUnitProvider#withUnitFieldAndValues")
    void getDeclaredAnnotationsByType(Field withUnitField, String withUnitValue) {
        Annotation[] annotations = withUnitField.getDeclaredAnnotationsByType(WithUnit.class);
        log.info("annotations:{}", Arrays.toString(annotations));
        Assertions.assertNotNull(annotations);
        Assertions.assertEquals(1, annotations.length);
        Annotation annotation = annotations[0];
        log.info("annotation:{}", annotation);
        Assertions.assertNotNull(annotation);
        Assertions.assertInstanceOf(WithUnit.class, annotation);
        WithUnit dataType = (WithUnit) annotation;
        Assertions.assertEquals(withUnitValue, dataType.value());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.api.annotation.provider.WithUnitProvider#withUnitGetMethodAndValues")
    void getDeclaredAnnotationsByType(Method withUnitGetMethod, String withUnitValue) {
        Annotation[] annotations = withUnitGetMethod.getDeclaredAnnotationsByType(WithUnit.class);
        log.info("annotations:{}", Arrays.toString(annotations));
        Assertions.assertNotNull(annotations);
        Assertions.assertEquals(1, annotations.length);
        Annotation annotation = annotations[0];
        log.info("annotation:{}", annotation);
        Assertions.assertNotNull(annotation);
        Assertions.assertInstanceOf(WithUnit.class, annotation);
        WithUnit dataType = (WithUnit) annotation;
        Assertions.assertEquals(withUnitValue, dataType.value());
    }
}

