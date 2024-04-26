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
class QuantityDeserializeTest {

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.api.annotation.provider.QuantityDeserializeProvider#quantityFields")
    void isAnnotationPresent(Field quantityDeserializeField) {
        boolean exist = quantityDeserializeField.isAnnotationPresent(QuantityDeserialize.class);
        log.info("exist:{}", exist);
        Assertions.assertTrue(exist);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.api.annotation.provider.QuantityDeserializeProvider#quantitySetMethods")
    void isAnnotationPresent(Method quantityDeserializeSetMethod) {
        boolean exist = quantityDeserializeSetMethod.isAnnotationPresent(QuantityDeserialize.class);
        log.info("exist:{}", exist);
        Assertions.assertTrue(exist);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.api.annotation.provider.QuantityDeserializeProvider#quantityFields")
    void getAnnotation(Field quantityDeserializeField) {
        QuantityDeserialize quantityDeserializeAnnotation = quantityDeserializeField.getAnnotation(QuantityDeserialize.class);
        log.info("quantityDeserializeAnnotation:{}", quantityDeserializeAnnotation);
        Assertions.assertNotNull(quantityDeserializeAnnotation);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.api.annotation.provider.QuantityDeserializeProvider#quantitySetMethods")
    void getAnnotation(Method quantityDeserializeSetMethod) {
        QuantityDeserialize quantityDeserializeAnnotation = quantityDeserializeSetMethod.getAnnotation(QuantityDeserialize.class);
        log.info("quantityDeserializeAnnotation:{}", quantityDeserializeAnnotation);
        Assertions.assertNotNull(quantityDeserializeAnnotation);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.api.annotation.provider.QuantityDeserializeProvider#quantityFields")
    void getDeclaredAnnotation(Field quantityDeserializeField) {
        QuantityDeserialize quantityDeserializeAnnotation = quantityDeserializeField.getDeclaredAnnotation(QuantityDeserialize.class);
        log.info("quantityDeserializeAnnotation:{}", quantityDeserializeAnnotation);
        Assertions.assertNotNull(quantityDeserializeAnnotation);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.api.annotation.provider.QuantityDeserializeProvider#quantitySetMethods")
    void getDeclaredAnnotation(Method quantityDeserializeSetMethod) {
        QuantityDeserialize quantityDeserializeAnnotation = quantityDeserializeSetMethod.getDeclaredAnnotation(QuantityDeserialize.class);
        log.info("quantityDeserializeAnnotation:{}", quantityDeserializeAnnotation);
        Assertions.assertNotNull(quantityDeserializeAnnotation);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.api.annotation.provider.QuantityDeserializeProvider#quantityFields")
    void getAnnotations(Field quantityDeserializeField) {
        Annotation[] annotations = quantityDeserializeField.getAnnotations();
        log.info("annotations:{}", Arrays.toString(annotations));
        Assertions.assertNotNull(annotations);
        Assertions.assertEquals(1, annotations.length);
        Annotation annotation = annotations[0];
        log.info("annotation:{}", annotation);
        Assertions.assertNotNull(annotation);
        Assertions.assertInstanceOf(QuantityDeserialize.class, annotation);
        QuantityDeserialize dataType = (QuantityDeserialize) annotation;
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.api.annotation.provider.QuantityDeserializeProvider#quantitySetMethods")
    void getAnnotations(Method quantityDeserializeSetMethod) {
        Annotation[] annotations = quantityDeserializeSetMethod.getAnnotations();
        log.info("annotations:{}", Arrays.toString(annotations));
        Assertions.assertNotNull(annotations);
        Assertions.assertEquals(1, annotations.length);
        Annotation annotation = annotations[0];
        log.info("annotation:{}", annotation);
        Assertions.assertNotNull(annotation);
        Assertions.assertInstanceOf(QuantityDeserialize.class, annotation);
        QuantityDeserialize dataType = (QuantityDeserialize) annotation;
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.api.annotation.provider.QuantityDeserializeProvider#quantityFields")
    void getDeclaredAnnotations(Field quantityDeserializeField) {
        Annotation[] annotations = quantityDeserializeField.getDeclaredAnnotations();
        log.info("annotations:{}", Arrays.toString(annotations));
        Assertions.assertNotNull(annotations);
        Assertions.assertEquals(1, annotations.length);
        Annotation annotation = annotations[0];
        log.info("annotation:{}", annotation);
        Assertions.assertNotNull(annotation);
        Assertions.assertInstanceOf(QuantityDeserialize.class, annotation);
        QuantityDeserialize dataType = (QuantityDeserialize) annotation;
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.api.annotation.provider.QuantityDeserializeProvider#quantitySetMethods")
    void getDeclaredAnnotations(Method quantityDeserializeSetMethod) {
        Annotation[] annotations = quantityDeserializeSetMethod.getDeclaredAnnotations();
        log.info("annotations:{}", Arrays.toString(annotations));
        Assertions.assertNotNull(annotations);
        Assertions.assertEquals(1, annotations.length);
        Annotation annotation = annotations[0];
        log.info("annotation:{}", annotation);
        Assertions.assertNotNull(annotation);
        Assertions.assertInstanceOf(QuantityDeserialize.class, annotation);
        QuantityDeserialize dataType = (QuantityDeserialize) annotation;
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.api.annotation.provider.QuantityDeserializeProvider#quantityFields")
    void getAnnotationsByType(Field quantityDeserializeField) {
        Annotation[] annotations = quantityDeserializeField.getAnnotationsByType(QuantityDeserialize.class);
        log.info("annotations:{}", Arrays.toString(annotations));
        Assertions.assertNotNull(annotations);
        Assertions.assertEquals(1, annotations.length);
        Annotation annotation = annotations[0];
        log.info("annotation:{}", annotation);
        Assertions.assertNotNull(annotation);
        Assertions.assertInstanceOf(QuantityDeserialize.class, annotation);
        QuantityDeserialize dataType = (QuantityDeserialize) annotation;
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.api.annotation.provider.QuantityDeserializeProvider#quantitySetMethods")
    void getAnnotationsByType(Method quantityDeserializeSetMethod) {
        Annotation[] annotations = quantityDeserializeSetMethod.getAnnotationsByType(QuantityDeserialize.class);
        log.info("annotations:{}", Arrays.toString(annotations));
        Assertions.assertNotNull(annotations);
        Assertions.assertEquals(1, annotations.length);
        Annotation annotation = annotations[0];
        log.info("annotation:{}", annotation);
        Assertions.assertNotNull(annotation);
        Assertions.assertInstanceOf(QuantityDeserialize.class, annotation);
        QuantityDeserialize dataType = (QuantityDeserialize) annotation;
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.api.annotation.provider.QuantityDeserializeProvider#quantityFields")
    void getDeclaredAnnotationsByType(Field quantityDeserializeField) {
        Annotation[] annotations = quantityDeserializeField.getDeclaredAnnotationsByType(QuantityDeserialize.class);
        log.info("annotations:{}", Arrays.toString(annotations));
        Assertions.assertNotNull(annotations);
        Assertions.assertEquals(1, annotations.length);
        Annotation annotation = annotations[0];
        log.info("annotation:{}", annotation);
        Assertions.assertNotNull(annotation);
        Assertions.assertInstanceOf(QuantityDeserialize.class, annotation);
        QuantityDeserialize dataType = (QuantityDeserialize) annotation;
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.api.annotation.provider.QuantityDeserializeProvider#quantitySetMethods")
    void getDeclaredAnnotationsByType(Method quantityDeserializeSetMethod) {
        Annotation[] annotations = quantityDeserializeSetMethod.getDeclaredAnnotationsByType(QuantityDeserialize.class);
        log.info("annotations:{}", Arrays.toString(annotations));
        Assertions.assertNotNull(annotations);
        Assertions.assertEquals(1, annotations.length);
        Annotation annotation = annotations[0];
        log.info("annotation:{}", annotation);
        Assertions.assertNotNull(annotation);
        Assertions.assertInstanceOf(QuantityDeserialize.class, annotation);
        QuantityDeserialize dataType = (QuantityDeserialize) annotation;
    }
}

