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
class QuantitySerializeTest {

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.api.annotation.provider.QuantitySerializeProvider#quantityFields")
    void isAnnotationPresent(Field quantitySerializeField) {
        boolean exist = quantitySerializeField.isAnnotationPresent(QuantitySerialize.class);
        log.info("exist:{}", exist);
        Assertions.assertTrue(exist);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.api.annotation.provider.QuantitySerializeProvider#quantitySetMethods")
    void isAnnotationPresent(Method quantitySerializeGetMethod) {
        boolean exist = quantitySerializeGetMethod.isAnnotationPresent(QuantitySerialize.class);
        log.info("exist:{}", exist);
        Assertions.assertTrue(exist);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.api.annotation.provider.QuantitySerializeProvider#quantityFields")
    void getAnnotation(Field quantitySerializeField) {
        QuantitySerialize quantityDeserializeAnnotation = quantitySerializeField.getAnnotation(QuantitySerialize.class);
        log.info("quantityDeserializeAnnotation:{}", quantityDeserializeAnnotation);
        Assertions.assertNotNull(quantityDeserializeAnnotation);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.api.annotation.provider.QuantitySerializeProvider#quantitySetMethods")
    void getAnnotation(Method quantitySerializeGetMethod) {
        QuantitySerialize quantityDeserializeAnnotation = quantitySerializeGetMethod.getAnnotation(QuantitySerialize.class);
        log.info("quantityDeserializeAnnotation:{}", quantityDeserializeAnnotation);
        Assertions.assertNotNull(quantityDeserializeAnnotation);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.api.annotation.provider.QuantitySerializeProvider#quantityFields")
    void getDeclaredAnnotation(Field quantitySerializeField) {
        QuantitySerialize quantityDeserializeAnnotation = quantitySerializeField.getDeclaredAnnotation(QuantitySerialize.class);
        log.info("quantityDeserializeAnnotation:{}", quantityDeserializeAnnotation);
        Assertions.assertNotNull(quantityDeserializeAnnotation);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.api.annotation.provider.QuantitySerializeProvider#quantitySetMethods")
    void getDeclaredAnnotation(Method quantitySerializeGetMethod) {
        QuantitySerialize quantityDeserializeAnnotation = quantitySerializeGetMethod.getDeclaredAnnotation(QuantitySerialize.class);
        log.info("quantityDeserializeAnnotation:{}", quantityDeserializeAnnotation);
        Assertions.assertNotNull(quantityDeserializeAnnotation);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.api.annotation.provider.QuantitySerializeProvider#quantityFields")
    void getAnnotations(Field quantitySerializeField) {
        Annotation[] annotations = quantitySerializeField.getAnnotations();
        log.info("annotations:{}", Arrays.toString(annotations));
        Assertions.assertNotNull(annotations);
        Assertions.assertEquals(1, annotations.length);
        Annotation annotation = annotations[0];
        log.info("annotation:{}", annotation);
        Assertions.assertNotNull(annotation);
        Assertions.assertInstanceOf(QuantitySerialize.class, annotation);
        QuantitySerialize dataType = (QuantitySerialize) annotation;
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.api.annotation.provider.QuantitySerializeProvider#quantitySetMethods")
    void getAnnotations(Method quantitySerializeGetMethod) {
        Annotation[] annotations = quantitySerializeGetMethod.getAnnotations();
        log.info("annotations:{}", Arrays.toString(annotations));
        Assertions.assertNotNull(annotations);
        Assertions.assertEquals(1, annotations.length);
        Annotation annotation = annotations[0];
        log.info("annotation:{}", annotation);
        Assertions.assertNotNull(annotation);
        Assertions.assertInstanceOf(QuantitySerialize.class, annotation);
        QuantitySerialize dataType = (QuantitySerialize) annotation;
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.api.annotation.provider.QuantitySerializeProvider#quantityFields")
    void getDeclaredAnnotations(Field quantitySerializeField) {
        Annotation[] annotations = quantitySerializeField.getDeclaredAnnotations();
        log.info("annotations:{}", Arrays.toString(annotations));
        Assertions.assertNotNull(annotations);
        Assertions.assertEquals(1, annotations.length);
        Annotation annotation = annotations[0];
        log.info("annotation:{}", annotation);
        Assertions.assertNotNull(annotation);
        Assertions.assertInstanceOf(QuantitySerialize.class, annotation);
        QuantitySerialize dataType = (QuantitySerialize) annotation;
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.api.annotation.provider.QuantitySerializeProvider#quantitySetMethods")
    void getDeclaredAnnotations(Method quantitySerializeGetMethod) {
        Annotation[] annotations = quantitySerializeGetMethod.getDeclaredAnnotations();
        log.info("annotations:{}", Arrays.toString(annotations));
        Assertions.assertNotNull(annotations);
        Assertions.assertEquals(1, annotations.length);
        Annotation annotation = annotations[0];
        log.info("annotation:{}", annotation);
        Assertions.assertNotNull(annotation);
        Assertions.assertInstanceOf(QuantitySerialize.class, annotation);
        QuantitySerialize dataType = (QuantitySerialize) annotation;
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.api.annotation.provider.QuantitySerializeProvider#quantityFields")
    void getAnnotationsByType(Field quantitySerializeField) {
        Annotation[] annotations = quantitySerializeField.getAnnotationsByType(QuantitySerialize.class);
        log.info("annotations:{}", Arrays.toString(annotations));
        Assertions.assertNotNull(annotations);
        Assertions.assertEquals(1, annotations.length);
        Annotation annotation = annotations[0];
        log.info("annotation:{}", annotation);
        Assertions.assertNotNull(annotation);
        Assertions.assertInstanceOf(QuantitySerialize.class, annotation);
        QuantitySerialize dataType = (QuantitySerialize) annotation;
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.api.annotation.provider.QuantitySerializeProvider#quantitySetMethods")
    void getAnnotationsByType(Method quantitySerializeGetMethod) {
        Annotation[] annotations = quantitySerializeGetMethod.getAnnotationsByType(QuantitySerialize.class);
        log.info("annotations:{}", Arrays.toString(annotations));
        Assertions.assertNotNull(annotations);
        Assertions.assertEquals(1, annotations.length);
        Annotation annotation = annotations[0];
        log.info("annotation:{}", annotation);
        Assertions.assertNotNull(annotation);
        Assertions.assertInstanceOf(QuantitySerialize.class, annotation);
        QuantitySerialize dataType = (QuantitySerialize) annotation;
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.api.annotation.provider.QuantitySerializeProvider#quantityFields")
    void getDeclaredAnnotationsByType(Field quantitySerializeField) {
        Annotation[] annotations = quantitySerializeField.getDeclaredAnnotationsByType(QuantitySerialize.class);
        log.info("annotations:{}", Arrays.toString(annotations));
        Assertions.assertNotNull(annotations);
        Assertions.assertEquals(1, annotations.length);
        Annotation annotation = annotations[0];
        log.info("annotation:{}", annotation);
        Assertions.assertNotNull(annotation);
        Assertions.assertInstanceOf(QuantitySerialize.class, annotation);
        QuantitySerialize dataType = (QuantitySerialize) annotation;
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.api.annotation.provider.QuantitySerializeProvider#quantitySetMethods")
    void getDeclaredAnnotationsByType(Method quantitySerializeGetMethod) {
        Annotation[] annotations = quantitySerializeGetMethod.getDeclaredAnnotationsByType(QuantitySerialize.class);
        log.info("annotations:{}", Arrays.toString(annotations));
        Assertions.assertNotNull(annotations);
        Assertions.assertEquals(1, annotations.length);
        Annotation annotation = annotations[0];
        log.info("annotation:{}", annotation);
        Assertions.assertNotNull(annotation);
        Assertions.assertInstanceOf(QuantitySerialize.class, annotation);
        QuantitySerialize dataType = (QuantitySerialize) annotation;
    }
}

