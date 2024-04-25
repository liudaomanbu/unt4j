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
class DataTypeTest {

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.api.annotation.provider.DataTypeProvider#dataTypeFieldAndValues")
    void isAnnotationPresent(Field dataTypeField) {
        boolean exist = dataTypeField.isAnnotationPresent(DataType.class);
        log.info("exist:{}", exist);
        Assertions.assertTrue(exist);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.api.annotation.provider.DataTypeProvider#dataTypeGetMethodAndValues")
    void isAnnotationPresent(Method dataTypeGetMethod) {
        boolean exist = dataTypeGetMethod.isAnnotationPresent(DataType.class);
        log.info("exist:{}", exist);
        Assertions.assertTrue(exist);
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.api.annotation.provider.DataTypeProvider#dataTypeFieldAndValues")
    void getAnnotation(Field dataTypeField, String dataTypeValue) {
        DataType dataTypeAnnotation = dataTypeField.getAnnotation(DataType.class);
        log.info("dataTypeAnnotation:{}", dataTypeAnnotation);
        Assertions.assertNotNull(dataTypeAnnotation);
        Assertions.assertEquals(dataTypeValue, dataTypeAnnotation.value());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.api.annotation.provider.DataTypeProvider#dataTypeGetMethodAndValues")
    void getAnnotation(Method dataTypeGetMethod, String dataTypeValue) {
        DataType dataTypeAnnotation = dataTypeGetMethod.getAnnotation(DataType.class);
        log.info("dataTypeAnnotation:{}", dataTypeAnnotation);
        Assertions.assertNotNull(dataTypeAnnotation);
        Assertions.assertEquals(dataTypeValue, dataTypeAnnotation.value());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.api.annotation.provider.DataTypeProvider#dataTypeFieldAndValues")
    void getDeclaredAnnotation(Field dataTypeField, String dataTypeValue) {
        DataType dataTypeAnnotation = dataTypeField.getDeclaredAnnotation(DataType.class);
        log.info("dataTypeAnnotation:{}", dataTypeAnnotation);
        Assertions.assertNotNull(dataTypeAnnotation);
        Assertions.assertEquals(dataTypeValue, dataTypeAnnotation.value());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.api.annotation.provider.DataTypeProvider#dataTypeGetMethodAndValues")
    void getDeclaredAnnotation(Method dataTypeGetMethod, String dataTypeValue) {
        DataType dataTypeAnnotation = dataTypeGetMethod.getDeclaredAnnotation(DataType.class);
        log.info("dataTypeAnnotation:{}", dataTypeAnnotation);
        Assertions.assertNotNull(dataTypeAnnotation);
        Assertions.assertEquals(dataTypeValue, dataTypeAnnotation.value());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.api.annotation.provider.DataTypeProvider#dataTypeFieldAndValues")
    void getAnnotations(Field dataTypeField, String dataTypeValue) {
        Annotation[] annotations = dataTypeField.getAnnotations();
        log.info("annotations:{}", Arrays.toString(annotations));
        Assertions.assertNotNull(annotations);
        Assertions.assertEquals(1, annotations.length);
        Annotation annotation = annotations[0];
        log.info("annotation:{}", annotation);
        Assertions.assertNotNull(annotation);
        Assertions.assertInstanceOf(DataType.class, annotation);
        DataType dataType = (DataType) annotation;
        Assertions.assertEquals(dataTypeValue, dataType.value());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.api.annotation.provider.DataTypeProvider#dataTypeGetMethodAndValues")
    void getAnnotations(Method dataTypeGetMethod, String dataTypeValue) {
        Annotation[] annotations = dataTypeGetMethod.getAnnotations();
        log.info("annotations:{}", Arrays.toString(annotations));
        Assertions.assertNotNull(annotations);
        Assertions.assertEquals(1, annotations.length);
        Annotation annotation = annotations[0];
        log.info("annotation:{}", annotation);
        Assertions.assertNotNull(annotation);
        Assertions.assertInstanceOf(DataType.class, annotation);
        DataType dataType = (DataType) annotation;
        Assertions.assertEquals(dataTypeValue, dataType.value());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.api.annotation.provider.DataTypeProvider#dataTypeFieldAndValues")
    void getDeclaredAnnotations(Field dataTypeField, String dataTypeValue) {
        Annotation[] annotations = dataTypeField.getDeclaredAnnotations();
        log.info("annotations:{}", Arrays.toString(annotations));
        Assertions.assertNotNull(annotations);
        Assertions.assertEquals(1, annotations.length);
        Annotation annotation = annotations[0];
        log.info("annotation:{}", annotation);
        Assertions.assertNotNull(annotation);
        Assertions.assertInstanceOf(DataType.class, annotation);
        DataType dataType = (DataType) annotation;
        Assertions.assertEquals(dataTypeValue, dataType.value());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.api.annotation.provider.DataTypeProvider#dataTypeGetMethodAndValues")
    void getDeclaredAnnotations(Method dataTypeGetMethod, String dataTypeValue) {
        Annotation[] annotations = dataTypeGetMethod.getDeclaredAnnotations();
        log.info("annotations:{}", Arrays.toString(annotations));
        Assertions.assertNotNull(annotations);
        Assertions.assertEquals(1, annotations.length);
        Annotation annotation = annotations[0];
        log.info("annotation:{}", annotation);
        Assertions.assertNotNull(annotation);
        Assertions.assertInstanceOf(DataType.class, annotation);
        DataType dataType = (DataType) annotation;
        Assertions.assertEquals(dataTypeValue, dataType.value());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.api.annotation.provider.DataTypeProvider#dataTypeFieldAndValues")
    void getAnnotationsByType(Field dataTypeField, String dataTypeValue) {
        Annotation[] annotations = dataTypeField.getAnnotationsByType(DataType.class);
        log.info("annotations:{}", Arrays.toString(annotations));
        Assertions.assertNotNull(annotations);
        Assertions.assertEquals(1, annotations.length);
        Annotation annotation = annotations[0];
        log.info("annotation:{}", annotation);
        Assertions.assertNotNull(annotation);
        Assertions.assertInstanceOf(DataType.class, annotation);
        DataType dataType = (DataType) annotation;
        Assertions.assertEquals(dataTypeValue, dataType.value());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.api.annotation.provider.DataTypeProvider#dataTypeGetMethodAndValues")
    void getAnnotationsByType(Method dataTypeGetMethod, String dataTypeValue) {
        Annotation[] annotations = dataTypeGetMethod.getAnnotationsByType(DataType.class);
        log.info("annotations:{}", Arrays.toString(annotations));
        Assertions.assertNotNull(annotations);
        Assertions.assertEquals(1, annotations.length);
        Annotation annotation = annotations[0];
        log.info("annotation:{}", annotation);
        Assertions.assertNotNull(annotation);
        Assertions.assertInstanceOf(DataType.class, annotation);
        DataType dataType = (DataType) annotation;
        Assertions.assertEquals(dataTypeValue, dataType.value());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.api.annotation.provider.DataTypeProvider#dataTypeFieldAndValues")
    void getDeclaredAnnotationsByType(Field dataTypeField, String dataTypeValue) {
        Annotation[] annotations = dataTypeField.getDeclaredAnnotationsByType(DataType.class);
        log.info("annotations:{}", Arrays.toString(annotations));
        Assertions.assertNotNull(annotations);
        Assertions.assertEquals(1, annotations.length);
        Annotation annotation = annotations[0];
        log.info("annotation:{}", annotation);
        Assertions.assertNotNull(annotation);
        Assertions.assertInstanceOf(DataType.class, annotation);
        DataType dataType = (DataType) annotation;
        Assertions.assertEquals(dataTypeValue, dataType.value());
    }

    @ParameterizedTest
    @MethodSource("org.caotc.unit4j.api.annotation.provider.DataTypeProvider#dataTypeGetMethodAndValues")
    void getDeclaredAnnotationsByType(Method dataTypeGetMethod, String dataTypeValue) {
        Annotation[] annotations = dataTypeGetMethod.getDeclaredAnnotationsByType(DataType.class);
        log.info("annotations:{}", Arrays.toString(annotations));
        Assertions.assertNotNull(annotations);
        Assertions.assertEquals(1, annotations.length);
        Annotation annotation = annotations[0];
        log.info("annotation:{}", annotation);
        Assertions.assertNotNull(annotation);
        Assertions.assertInstanceOf(DataType.class, annotation);
        DataType dataType = (DataType) annotation;
        Assertions.assertEquals(dataTypeValue, dataType.value());
    }
}

