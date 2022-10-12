package org.caotc.unit4j.core.common.util.provider;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.TypeToken;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.caotc.unit4j.core.common.reflect.property.AccessibleProperty;
import org.caotc.unit4j.core.common.reflect.property.Property;
import org.caotc.unit4j.core.common.reflect.property.accessor.PropertyAccessorMethodFormat;
import org.caotc.unit4j.core.common.reflect.property.accessor.PropertyElement;
import org.caotc.unit4j.core.common.util.model.*;
import org.junit.jupiter.params.provider.Arguments;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * @author caotc
 * @date 2022-08-18
 * @since 1.0.0
 */
@SuppressWarnings("ALL")
@UtilityClass
@Slf4j
public class Provider {
    private static final PropertyAccessorMethodFormat[] JAVA_BEAN = new PropertyAccessorMethodFormat[]{PropertyAccessorMethodFormat.JAVA_BEAN};
    private static final PropertyAccessorMethodFormat[] FLUENT = new PropertyAccessorMethodFormat[]{PropertyAccessorMethodFormat.FLUENT};

    static Stream<Class<?>> classes() {
        return Stream.of(NoFieldObject.class, StringFieldObject.class, FinalFieldObject.class, StaticFieldObject.class
                , ChildrenLongFieldObject.class, ChildrenSameNameFieldObject.class, MultipleFieldObject.class
                , PrivateConstructObject.class, ProtectedConstructObject.class, ProtectedConstructChildrenObject.class
                , MultipleConstructObject.class, StringFieldGetMethodObject.class, StringFieldFluentGetMethodObject.class
                , StringFieldAndStringFieldGetMethodObject.class, StringFieldGetter.class, StringFieldGetterObject.class
                , BooleanFieldIsMethodObject.class, BooleanFieldGetMethodObject.class, StringFieldSetMethodObject.class
                , StringFieldFluentSetMethodObject.class, StringFieldAndStringFieldSetMethodObject.class
                , StringFieldChainSetMethodObject.class, StringFieldSetter.class, StringFieldSetterObject.class
                , DuplicateNumberFieldSetMethodObject.class);
    }

    static Stream<TypeToken<?>> typeTokens() {
        return classes().map(TypeToken::of);
    }

    static Stream<Arguments> classAndFieldSets() {
        return Stream.of(Arguments.of(NoFieldObject.class, ImmutableSet.of())
                , Arguments.of(PrivateConstructObject.class, ImmutableSet.of())
                , Arguments.of(ProtectedConstructObject.class, ImmutableSet.of())
                , Arguments.of(ProtectedConstructChildrenObject.class, ImmutableSet.of())
                , Arguments.of(MultipleConstructObject.class, ImmutableSet.of())
                , Arguments.of(StringFieldObject.class, Constant.STRING_FIELD_OBJECT_FIELDS)
                , Arguments.of(FinalFieldObject.class, Constant.FINAL_FIELD_OBJECT_FIELDS)
                , Arguments.of(StaticFieldObject.class, Constant.STATIC_FIELD_OBJECT_FIELDS)
                , Arguments.of(ChildrenLongFieldObject.class, Constant.CHILDREN_LONG_FIELD_OBJECT_FIELDS)
                , Arguments.of(ChildrenSameNameFieldObject.class, Constant.CHILDREN_SAME_NAME_FIELD_OBJECT_FIELDS)
                , Arguments.of(MultipleFieldObject.class, Constant.MULTIPLE_FIELD_OBJECT_FIELDS)
                , Arguments.of(StringFieldGetMethodObject.class, ImmutableSet.of())
                , Arguments.of(StringFieldFluentGetMethodObject.class, ImmutableSet.of())
                , Arguments.of(StringFieldAndStringFieldGetMethodObject.class, ImmutableSet.of(Constant.STRING_FIELD_AND_STRING_FIELD_GET_METHOD_OBJECT_STRING_FIELD))
                , Arguments.of(StringFieldGetterObject.class, ImmutableSet.of())
                , Arguments.of(BooleanFieldIsMethodObject.class, ImmutableSet.of())
                , Arguments.of(BooleanFieldGetMethodObject.class, ImmutableSet.of())
                , Arguments.of(StringFieldGetter.class, ImmutableSet.of())
                , Arguments.of(StringFieldSetMethodObject.class, ImmutableSet.of())
                , Arguments.of(StringFieldFluentSetMethodObject.class, ImmutableSet.of())
                , Arguments.of(StringFieldAndStringFieldSetMethodObject.class, ImmutableSet.of(Constant.STRING_FIELD_AND_STRING_FIELD_SET_METHOD_OBJECT_STRING_FIELD))
                , Arguments.of(StringFieldChainSetMethodObject.class, ImmutableSet.of())
                , Arguments.of(StringFieldSetter.class, ImmutableSet.of())
                , Arguments.of(StringFieldSetterObject.class, ImmutableSet.of())
                , Arguments.of(DuplicateNumberFieldSetMethodObject.class, ImmutableSet.of()));
    }

    static Stream<Arguments> typeTokenAndFieldSets() {
        return classAndFieldSets().map(arguments -> Arguments.of(TypeToken.of((Class<?>) arguments.get()[0]), arguments.get()[1]));
    }

    static Stream<Arguments> classAndFieldNameAndFields() {
        return Stream.of(Arguments.of(StringFieldObject.class, StringFieldObject.Fields.STRING_FIELD, ImmutableSet.of(Constant.STRING_FIELD_OBJECT_STRING_FIELD))
                , Arguments.of(FinalFieldObject.class, FinalFieldObject.Fields.STRING_FIELD, ImmutableSet.of(Constant.FINAL_FIELD_OBJECT_STRING_FIELD))
                , Arguments.of(StaticFieldObject.class, "stringField", ImmutableSet.of(Constant.STATIC_FIELD_OBJECT_STRING_FIELD))
                , Arguments.of(ChildrenLongFieldObject.class, StringFieldObject.Fields.STRING_FIELD, ImmutableSet.of(Constant.STRING_FIELD_OBJECT_STRING_FIELD))
                , Arguments.of(ChildrenLongFieldObject.class, ChildrenLongFieldObject.Fields.LONG_FIELD, ImmutableSet.of(Constant.CHILDREN_LONG_FIELD_OBJECT_LONG_FIELD))
                , Arguments.of(ChildrenSameNameFieldObject.class, ChildrenSameNameFieldObject.Fields.STRING_FIELD, ImmutableSet.of(Constant.STRING_FIELD_OBJECT_STRING_FIELD, Constant.CHILDREN_SAME_NAME_FIELD_OBJECT_STRING_FIELD))
                , Arguments.of(MultipleFieldObject.class, MultipleFieldObject.Fields.STRING_FIELD, ImmutableSet.of(Constant.MULTIPLE_FIELD_OBJECT_STRING_FIELD))
                , Arguments.of(MultipleFieldObject.class, MultipleFieldObject.Fields.INT_FIELD, ImmutableSet.of(Constant.MULTIPLE_FIELD_OBJECT_INT_FIELD))
                , Arguments.of(MultipleFieldObject.class, MultipleFieldObject.Fields.INTEGER_FIELD, ImmutableSet.of(Constant.MULTIPLE_FIELD_OBJECT_INTEGER_FIELD))
                , Arguments.of(MultipleFieldObject.class, MultipleFieldObject.Fields.BOOLEAN_FIELD, ImmutableSet.of(Constant.MULTIPLE_FIELD_OBJECT_BOOLEAN_FIELD))
                , Arguments.of(StringFieldAndStringFieldGetMethodObject.class, StringFieldAndStringFieldGetMethodObject.Fields.STRING_FIELD, ImmutableSet.of(Constant.STRING_FIELD_AND_STRING_FIELD_GET_METHOD_OBJECT_STRING_FIELD)));
    }

    static Stream<Arguments> typeTokenAndFieldNameAndFields() {
        return classAndFieldNameAndFields().map(arguments -> Arguments.of(TypeToken.of((Class<?>) arguments.get()[0]), arguments.get()[1], arguments.get()[2]));
    }

    static Stream<Arguments> classAndMethodSets() {
        return Stream.of(Arguments.of(NoFieldObject.class, Constant.OBJECT_METHODS)
                , Arguments.of(StringFieldObject.class, Constant.OBJECT_METHODS)
                , Arguments.of(FinalFieldObject.class, Constant.OBJECT_METHODS)
                , Arguments.of(StaticFieldObject.class, Constant.OBJECT_METHODS)
                , Arguments.of(ChildrenLongFieldObject.class, Constant.OBJECT_METHODS)
                , Arguments.of(ChildrenSameNameFieldObject.class, Constant.OBJECT_METHODS)
                , Arguments.of(MultipleFieldObject.class, Constant.OBJECT_METHODS)
                , Arguments.of(PrivateConstructObject.class, Constant.OBJECT_METHODS)
                , Arguments.of(ProtectedConstructObject.class, Constant.OBJECT_METHODS)
                , Arguments.of(ProtectedConstructChildrenObject.class, Constant.OBJECT_METHODS)
                , Arguments.of(MultipleConstructObject.class, Constant.OBJECT_METHODS)
                , Arguments.of(StringFieldGetMethodObject.class, Constant.STRING_FIELD_GET_METHOD_OBJECT_METHODS)
                , Arguments.of(StringFieldFluentGetMethodObject.class, Constant.STRING_FIELD_FLUENT_GET_METHOD_OBJECT_METHODS)
                , Arguments.of(StringFieldAndStringFieldGetMethodObject.class, Constant.STRING_FIELD_AND_STRING_FIELD_GET_METHOD_OBJECT_METHODS)
                , Arguments.of(StringFieldGetter.class, ImmutableSet.of(Constant.STRING_FIELD_GETTER_GET_STRING_FIELD_METHOD))
                , Arguments.of(StringFieldGetterObject.class, Constant.STRING_FIELD_GETTER_OBJECT_METHODS)
                , Arguments.of(BooleanFieldIsMethodObject.class, Constant.BOOLEAN_FIELD_IS_METHOD_OBJECT_METHODS)
                , Arguments.of(BooleanFieldGetMethodObject.class, Constant.BOOLEAN_FIELD_GET_METHOD_OBJECT_METHODS)
                , Arguments.of(StringFieldSetMethodObject.class, Constant.STRING_FIELD_SET_METHOD_OBJECT_METHODS)
                , Arguments.of(StringFieldFluentSetMethodObject.class, Constant.STRING_FIELD_FLUENT_SET_METHOD_OBJECT_METHODS)
                , Arguments.of(StringFieldAndStringFieldSetMethodObject.class, Constant.STRING_FIELD_AND_STRING_FIELD_SET_METHOD_OBJECT_METHODS)
                , Arguments.of(StringFieldChainSetMethodObject.class, Constant.STRING_FIELD_CHAIN_SET_METHOD_OBJECT_METHODS)
                , Arguments.of(StringFieldSetter.class, ImmutableSet.of(Constant.STRING_FIELD_SETTER_SET_STRING_FIELD_METHOD))
                , Arguments.of(StringFieldSetterObject.class, Constant.STRING_FIELD_SETTER_OBJECT_METHODS)
                , Arguments.of(DuplicateNumberFieldSetMethodObject.class, Constant.DUPLICATE_NUMBER_FIELD_SET_METHOD_OBJECT_METHODS));
    }

    static Stream<Arguments> typeTokenAndMethodSets() {
        return classAndMethodSets().map(arguments -> Arguments.of(TypeToken.of((Class<?>) arguments.get()[0]), arguments.get()[1]));
    }

    static Stream<Arguments> classAndConstructorSets() {
        return Stream.of(Arguments.of(NoFieldObject.class, ImmutableSet.of(Constant.NO_FIELD_OBJECT_CONSTRUCTOR))
                , Arguments.of(StringFieldObject.class, ImmutableSet.of(Constant.STRING_FIELD_OBJECT_CONSTRUCTOR))
                , Arguments.of(StaticFieldObject.class, ImmutableSet.of(Constant.STATIC_FIELD_OBJECT_CONSTRUCTOR))
                , Arguments.of(FinalFieldObject.class, ImmutableSet.of(Constant.FINAL_FIELD_OBJECT_CONSTRUCTOR))
                , Arguments.of(ChildrenLongFieldObject.class, ImmutableSet.of(Constant.CHILDREN_LONG_FIELD_OBJECT_CONSTRUCTOR))
                , Arguments.of(ChildrenSameNameFieldObject.class, ImmutableSet.of(Constant.CHILDREN_SAME_NAME_FIELD_OBJECT_CONSTRUCTOR))
                , Arguments.of(MultipleFieldObject.class, ImmutableSet.of(Constant.MULTIPLE_FIELD_OBJECT_CONSTRUCTOR))
                , Arguments.of(PrivateConstructObject.class, ImmutableSet.of(Constant.PRIVATE_CONSTRUCT_OBJECT_CONSTRUCTOR))
                , Arguments.of(ProtectedConstructObject.class, ImmutableSet.of(Constant.PROTECTED_CONSTRUCT_OBJECT_CONSTRUCTOR))
                , Arguments.of(ProtectedConstructChildrenObject.class, ImmutableSet.of(Constant.PROTECTED_CONSTRUCT_CHILDREN_OBJECT_CONSTRUCTOR))
                , Arguments.of(MultipleConstructObject.class, Constant.MULTIPLE_CONSTRUCT_OBJECT_CONSTRUCTORS)
                , Arguments.of(StringFieldGetMethodObject.class, ImmutableSet.of(Constant.STRING_FIELD_GET_METHOD_OBJECT_CONSTRUCTOR))
                , Arguments.of(StringFieldFluentGetMethodObject.class, ImmutableSet.of(Constant.STRING_FIELD_FLUENT_GET_METHOD_OBJECT_CONSTRUCTOR))
                , Arguments.of(StringFieldAndStringFieldGetMethodObject.class, ImmutableSet.of(Constant.STRING_FIELD_AND_STRING_FIELD_GET_METHOD_OBJECT_CONSTRUCTOR))
                , Arguments.of(StringFieldGetter.class, ImmutableSet.of())
                , Arguments.of(StringFieldGetterObject.class, ImmutableSet.of(Constant.STRING_FIELD_GETTER_OBJECT_CONSTRUCTOR))
                , Arguments.of(BooleanFieldIsMethodObject.class, ImmutableSet.of(Constant.BOOLEAN_FIELD_IS_METHOD_OBJECT_CONSTRUCTOR))
                , Arguments.of(BooleanFieldGetMethodObject.class, ImmutableSet.of(Constant.BOOLEAN_FIELD_GET_METHOD_OBJECT_CONSTRUCTOR))
                , Arguments.of(StringFieldSetMethodObject.class, ImmutableSet.of(Constant.STRING_FIELD_SET_METHOD_OBJECT_CONSTRUCTOR))
                , Arguments.of(StringFieldFluentSetMethodObject.class, ImmutableSet.of(Constant.STRING_FIELD_FLUENT_SET_METHOD_OBJECT_CONSTRUCTOR))
                , Arguments.of(StringFieldAndStringFieldSetMethodObject.class, ImmutableSet.of(Constant.STRING_FIELD_AND_STRING_FIELD_SET_METHOD_OBJECT_CONSTRUCTOR))
                , Arguments.of(StringFieldChainSetMethodObject.class, ImmutableSet.of(Constant.STRING_FIELD_CHAIN_SET_METHOD_OBJECT_CONSTRUCTOR))
                , Arguments.of(StringFieldSetter.class, ImmutableSet.of())
                , Arguments.of(StringFieldSetterObject.class, ImmutableSet.of(Constant.STRING_FIELD_SETTER_OBJECT_CONSTRUCTOR))
                , Arguments.of(DuplicateNumberFieldSetMethodObject.class, ImmutableSet.of(Constant.DUPLICATE_NUMBER_FIELD_SET_METHOD_OBJECT_CONSTRUCTOR)));
    }

    static Stream<Arguments> typeTokenAndConstructorSets() {
        return classAndConstructorSets().map(arguments -> Arguments.of(TypeToken.of((Class<?>) arguments.get()[0]), arguments.get()[1]));
    }

    static Stream<Arguments> classAndFieldNameAndGetMethods() {
        return Stream.of(Arguments.of(StringFieldGetMethodObject.class, "stringField", Constant.STRING_FIELD_GET_METHOD_OBJECT_GET_STRING_FIELD_METHOD)
                , Arguments.of(StringFieldAndStringFieldGetMethodObject.class, StringFieldAndStringFieldGetMethodObject.Fields.STRING_FIELD, Constant.STRING_FIELD_AND_STRING_FIELD_GET_METHOD_OBJECT_GET_STRING_FIELD_METHOD)
                , Arguments.of(StringFieldGetter.class, "stringField", Constant.STRING_FIELD_GETTER_GET_STRING_FIELD_METHOD)
                , Arguments.of(StringFieldGetterObject.class, "stringField", Constant.STRING_FIELD_GETTER_OBJECT_GET_STRING_FIELD_METHOD)
                , Arguments.of(BooleanFieldIsMethodObject.class, "booleanField", Constant.BOOLEAN_FIELD_IS_METHOD_OBJECT_IS_BOOLEAN_FIELD_METHOD)
                , Arguments.of(BooleanFieldGetMethodObject.class, "booleanField", Constant.BOOLEAN_FIELD_GET_METHOD_OBJECT_GET_BOOLEAN_FIELD_METHOD));
    }

    static Stream<Arguments> typeTokenAndFieldNameAndGetMethods() {
        return classAndFieldNameAndGetMethods().map(arguments -> Arguments.of(TypeToken.of((Class<?>) arguments.get()[0]), arguments.get()[1], arguments.get()[2]));
    }

    static Stream<Arguments> classAndGetMethodSets() {
        return Stream.of(Arguments.of(BooleanFieldGetMethodObject.class, ImmutableSet.of(Constant.BOOLEAN_FIELD_GET_METHOD_OBJECT_GET_BOOLEAN_FIELD_METHOD))
                , Arguments.of(BooleanFieldIsMethodObject.class, ImmutableSet.of(Constant.BOOLEAN_FIELD_IS_METHOD_OBJECT_IS_BOOLEAN_FIELD_METHOD))
                , Arguments.of(ChildrenLongFieldObject.class, ImmutableSet.of())
                , Arguments.of(ChildrenSameNameFieldObject.class, ImmutableSet.of())
                , Arguments.of(DuplicateNumberFieldSetMethodObject.class, ImmutableSet.of())
                , Arguments.of(FinalFieldObject.class, ImmutableSet.of())
                , Arguments.of(MultipleConstructObject.class, ImmutableSet.of())
                , Arguments.of(MultipleFieldObject.class, ImmutableSet.of())
                , Arguments.of(NoFieldObject.class, ImmutableSet.of())
                , Arguments.of(PrivateConstructObject.class, ImmutableSet.of())
                , Arguments.of(ProtectedConstructChildrenObject.class, ImmutableSet.of())
                , Arguments.of(ProtectedConstructObject.class, ImmutableSet.of())
                , Arguments.of(StaticFieldObject.class, ImmutableSet.of())
                , Arguments.of(StringFieldAndStringFieldGetMethodObject.class, ImmutableSet.of(Constant.STRING_FIELD_AND_STRING_FIELD_GET_METHOD_OBJECT_GET_STRING_FIELD_METHOD))
                , Arguments.of(StringFieldAndStringFieldSetMethodObject.class, ImmutableSet.of())
                , Arguments.of(StringFieldChainSetMethodObject.class, ImmutableSet.of())
                , Arguments.of(StringFieldFluentGetMethodObject.class, ImmutableSet.of())
                , Arguments.of(StringFieldFluentSetMethodObject.class, ImmutableSet.of())
                , Arguments.of(StringFieldGetMethodObject.class, ImmutableSet.of(Constant.STRING_FIELD_GET_METHOD_OBJECT_GET_STRING_FIELD_METHOD))
                , Arguments.of(StringFieldGetter.class, ImmutableSet.of(Constant.STRING_FIELD_GETTER_GET_STRING_FIELD_METHOD))
                , Arguments.of(StringFieldGetterObject.class, Constant.STRING_FIELD_GETTER_OBJECT_GET_METHODS)
                , Arguments.of(StringFieldObject.class, ImmutableSet.of())
                , Arguments.of(StringFieldSetMethodObject.class, ImmutableSet.of())
                , Arguments.of(StringFieldSetter.class, ImmutableSet.of())
                , Arguments.of(StringFieldSetterObject.class, ImmutableSet.of()));
    }

    static Stream<Arguments> typeTokenAndGetMethodSets() {
        return classAndGetMethodSets().map(arguments -> Arguments.of(TypeToken.of((Class<?>) arguments.get()[0]), arguments.get()[1]));
    }

    static Stream<Arguments> classAndDuplicateSetMethodFieldNames() {
        return Stream.of(Arguments.of(DuplicateNumberFieldSetMethodObject.class, "numberField"));
    }

    static Stream<Arguments> typeTokenAndDuplicateSetMethodFieldNames() {
        return classAndDuplicateSetMethodFieldNames().map(arguments -> Arguments.of(TypeToken.of((Class<?>) arguments.get()[0]), arguments.get()[1]));
    }

    static Stream<Arguments> classAndFieldNameAndSetMethods() {
        return Stream.of(Arguments.of(StringFieldSetMethodObject.class, "stringField", Constant.STRING_FIELD_SET_METHOD_OBJECT_SET_STRING_FIELD_METHOD)
                , Arguments.of(StringFieldAndStringFieldSetMethodObject.class, StringFieldAndStringFieldSetMethodObject.Fields.STRING_FIELD, Constant.STRING_FIELD_AND_STRING_FIELD_SET_METHOD_OBJECT_SET_STRING_FIELD_METHOD)
                , Arguments.of(StringFieldChainSetMethodObject.class, "stringField", Constant.STRING_FIELD_CHAIN_SET_METHOD_OBJECT_SET_STRING_FIELD_METHOD)
                , Arguments.of(StringFieldSetter.class, "stringField", Constant.STRING_FIELD_SETTER_SET_STRING_FIELD_METHOD)
                , Arguments.of(StringFieldSetterObject.class, "stringField", Constant.STRING_FIELD_SETTER_OBJECT_SET_STRING_FIELD_METHOD));
    }

    static Stream<Arguments> typeTokenAndFieldNameAndSetMethods() {
        return classAndFieldNameAndSetMethods().map(arguments -> Arguments.of(TypeToken.of((Class<?>) arguments.get()[0]), arguments.get()[1], arguments.get()[2]));
    }

    static Stream<Arguments> classAndFieldNameAndSetMethodSets() {
        return Stream.of(Arguments.of(StringFieldSetMethodObject.class, "stringField", ImmutableSet.of(Constant.STRING_FIELD_SET_METHOD_OBJECT_SET_STRING_FIELD_METHOD))
                , Arguments.of(StringFieldFluentSetMethodObject.class, "stringField", ImmutableSet.of()
                        , Arguments.of(StringFieldAndStringFieldSetMethodObject.class, StringFieldAndStringFieldSetMethodObject.Fields.STRING_FIELD, ImmutableSet.of(Constant.STRING_FIELD_AND_STRING_FIELD_SET_METHOD_OBJECT_SET_STRING_FIELD_METHOD))
                        , Arguments.of(StringFieldChainSetMethodObject.class, "stringField", ImmutableSet.of(Constant.STRING_FIELD_CHAIN_SET_METHOD_OBJECT_SET_STRING_FIELD_METHOD))
                        , Arguments.of(StringFieldSetter.class, "stringField", ImmutableSet.of(Constant.STRING_FIELD_SETTER_SET_STRING_FIELD_METHOD))
                        , Arguments.of(StringFieldSetterObject.class, "stringField", Constant.STRING_FIELD_SETTER_OBJECT_SET_METHODS)
                        , Arguments.of(DuplicateNumberFieldSetMethodObject.class, "numberField", Constant.DUPLICATE_NUMBER_FIELD_SET_METHOD_OBJECT_SET_METHODS)));
    }

    static Stream<Arguments> typeTokenAndFieldNameAndSetMethodSets() {
        return classAndFieldNameAndSetMethodSets().map(arguments -> Arguments.of(TypeToken.of((Class<?>) arguments.get()[0]), arguments.get()[1], arguments.get()[2]));
    }

    static Stream<Arguments> classAndSetMethodSets() {
        return Stream.of(Arguments.of(BooleanFieldGetMethodObject.class, ImmutableSet.of())
                , Arguments.of(BooleanFieldIsMethodObject.class, ImmutableSet.of())
                , Arguments.of(ChildrenLongFieldObject.class, ImmutableSet.of())
                , Arguments.of(ChildrenSameNameFieldObject.class, ImmutableSet.of())
                , Arguments.of(DuplicateNumberFieldSetMethodObject.class, Constant.DUPLICATE_NUMBER_FIELD_SET_METHOD_OBJECT_SET_METHODS)
                , Arguments.of(FinalFieldObject.class, ImmutableSet.of())
                , Arguments.of(MultipleConstructObject.class, ImmutableSet.of())
                , Arguments.of(MultipleFieldObject.class, ImmutableSet.of())
                , Arguments.of(NoFieldObject.class, ImmutableSet.of())
                , Arguments.of(PrivateConstructObject.class, ImmutableSet.of())
                , Arguments.of(ProtectedConstructChildrenObject.class, ImmutableSet.of())
                , Arguments.of(ProtectedConstructObject.class, ImmutableSet.of())
                , Arguments.of(StaticFieldObject.class, ImmutableSet.of())
                , Arguments.of(StringFieldAndStringFieldGetMethodObject.class, ImmutableSet.of())
                , Arguments.of(StringFieldAndStringFieldSetMethodObject.class, ImmutableSet.of(Constant.STRING_FIELD_AND_STRING_FIELD_SET_METHOD_OBJECT_SET_STRING_FIELD_METHOD))
                , Arguments.of(StringFieldChainSetMethodObject.class, ImmutableSet.of(Constant.STRING_FIELD_CHAIN_SET_METHOD_OBJECT_SET_STRING_FIELD_METHOD))
                , Arguments.of(StringFieldFluentGetMethodObject.class, ImmutableSet.of())
                , Arguments.of(StringFieldFluentSetMethodObject.class, ImmutableSet.of())
                , Arguments.of(StringFieldGetMethodObject.class, ImmutableSet.of())
                , Arguments.of(StringFieldGetter.class, ImmutableSet.of())
                , Arguments.of(StringFieldGetterObject.class, ImmutableSet.of())
                , Arguments.of(StringFieldObject.class, ImmutableSet.of())
                , Arguments.of(StringFieldSetMethodObject.class, ImmutableSet.of(Constant.STRING_FIELD_SET_METHOD_OBJECT_SET_STRING_FIELD_METHOD))
                , Arguments.of(StringFieldSetter.class, ImmutableSet.of(Constant.STRING_FIELD_SETTER_SET_STRING_FIELD_METHOD))
                , Arguments.of(StringFieldSetterObject.class, Constant.STRING_FIELD_SETTER_OBJECT_SET_METHODS));
    }

    static Stream<Arguments> typeTokenAndSetMethodSets() {
        return classAndSetMethodSets().map(arguments -> Arguments.of(TypeToken.of((Class<?>) arguments.get()[0]), arguments.get()[1]));
    }

    static Stream<Arguments> classAndPropertyAccessorMethodFormatAndPropertyElementSets() {
        return Stream.of(Arguments.of(BooleanFieldGetMethodObject.class, PropertyAccessorMethodFormat.JAVA_BEAN, ImmutableSet.of(Constant.BOOLEAN_FIELD_GET_METHOD_OBJECT_BOOLEAN_FIELD_PROPERTY_READER))
                , Arguments.of(BooleanFieldGetMethodObject.class, PropertyAccessorMethodFormat.FLUENT, ImmutableSet.of())
                , Arguments.of(BooleanFieldIsMethodObject.class, PropertyAccessorMethodFormat.JAVA_BEAN, ImmutableSet.of(Constant.BOOLEAN_FIELD_IS_METHOD_OBJECT_BOOLEAN_FIELD_PROPERTY_READER))
                , Arguments.of(BooleanFieldIsMethodObject.class, PropertyAccessorMethodFormat.FLUENT, ImmutableSet.of())
                , Arguments.of(ChildrenLongFieldObject.class, PropertyAccessorMethodFormat.JAVA_BEAN, Constant.CHILDREN_LONG_FIELD_OBJECT_PROPERTY_ACCESSORS)
                , Arguments.of(ChildrenLongFieldObject.class, PropertyAccessorMethodFormat.FLUENT, Constant.CHILDREN_LONG_FIELD_OBJECT_PROPERTY_ACCESSORS)
                , Arguments.of(ChildrenSameNameFieldObject.class, PropertyAccessorMethodFormat.JAVA_BEAN, Constant.CHILDREN_SAME_NAME_FIELD_OBJECT_PROPERTY_ACCESSORS)
                , Arguments.of(ChildrenSameNameFieldObject.class, PropertyAccessorMethodFormat.FLUENT, Constant.CHILDREN_SAME_NAME_FIELD_OBJECT_PROPERTY_ACCESSORS)
                , Arguments.of(DuplicateNumberFieldSetMethodObject.class, PropertyAccessorMethodFormat.JAVA_BEAN, Constant.DUPLICATE_NUMBER_FIELD_SET_METHOD_OBJECT_PROPERTY_WRITERS)
                , Arguments.of(DuplicateNumberFieldSetMethodObject.class, PropertyAccessorMethodFormat.FLUENT, ImmutableSet.of())
                , Arguments.of(FinalFieldObject.class, PropertyAccessorMethodFormat.JAVA_BEAN, ImmutableSet.of(Constant.FINAL_FIELD_OBJECT_STRING_FIELD_PROPERTY_ACCESSOR))
                , Arguments.of(FinalFieldObject.class, PropertyAccessorMethodFormat.FLUENT, ImmutableSet.of(Constant.FINAL_FIELD_OBJECT_STRING_FIELD_PROPERTY_ACCESSOR))
                , Arguments.of(MultipleConstructObject.class, PropertyAccessorMethodFormat.JAVA_BEAN, ImmutableSet.of())
                , Arguments.of(MultipleConstructObject.class, PropertyAccessorMethodFormat.FLUENT, ImmutableSet.of())
                , Arguments.of(MultipleFieldObject.class, PropertyAccessorMethodFormat.JAVA_BEAN, Constant.MULTIPLE_FIELD_OBJECT_PROPERTY_ACCESSORS)
                , Arguments.of(MultipleFieldObject.class, PropertyAccessorMethodFormat.FLUENT, Constant.MULTIPLE_FIELD_OBJECT_PROPERTY_ACCESSORS)
                , Arguments.of(NoFieldObject.class, PropertyAccessorMethodFormat.JAVA_BEAN, ImmutableSet.of())
                , Arguments.of(NoFieldObject.class, PropertyAccessorMethodFormat.FLUENT, ImmutableSet.of())
                , Arguments.of(PrivateConstructObject.class, PropertyAccessorMethodFormat.JAVA_BEAN, ImmutableSet.of())
                , Arguments.of(PrivateConstructObject.class, PropertyAccessorMethodFormat.FLUENT, ImmutableSet.of())
                , Arguments.of(ProtectedConstructChildrenObject.class, PropertyAccessorMethodFormat.JAVA_BEAN, ImmutableSet.of())
                , Arguments.of(ProtectedConstructChildrenObject.class, PropertyAccessorMethodFormat.FLUENT, ImmutableSet.of())
                , Arguments.of(ProtectedConstructObject.class, PropertyAccessorMethodFormat.JAVA_BEAN, ImmutableSet.of())
                , Arguments.of(ProtectedConstructObject.class, PropertyAccessorMethodFormat.FLUENT, ImmutableSet.of())
                , Arguments.of(StaticFieldObject.class, PropertyAccessorMethodFormat.JAVA_BEAN, ImmutableSet.of())
                , Arguments.of(StaticFieldObject.class, PropertyAccessorMethodFormat.FLUENT, ImmutableSet.of())
                , Arguments.of(StringFieldAndStringFieldGetMethodObject.class, PropertyAccessorMethodFormat.JAVA_BEAN, Constant.STRING_FIELD_AND_STRING_FIELD_GET_METHOD_OBJECT_PROPERTY_READERS)
                , Arguments.of(StringFieldAndStringFieldGetMethodObject.class, PropertyAccessorMethodFormat.FLUENT, ImmutableSet.of(Constant.STRING_FIELD_AND_STRING_FIELD_GET_METHOD_OBJECT_STRING_FIELD_PROPERTY_ACCESSOR))
                , Arguments.of(StringFieldAndStringFieldSetMethodObject.class, PropertyAccessorMethodFormat.JAVA_BEAN, ImmutableSet.of(Constant.STRING_FIELD_AND_STRING_FIELD_SET_METHOD_OBJECT_STRING_FIELD_PROPERTY_ACCESSOR, Constant.STRING_FIELD_AND_STRING_FIELD_SET_METHOD_OBJECT_SET_STRING_FIELD_METHOD_PROPERTY_WRITER))
                , Arguments.of(StringFieldAndStringFieldSetMethodObject.class, PropertyAccessorMethodFormat.FLUENT, ImmutableSet.of(Constant.STRING_FIELD_AND_STRING_FIELD_SET_METHOD_OBJECT_STRING_FIELD_PROPERTY_ACCESSOR))
                , Arguments.of(StringFieldChainSetMethodObject.class, PropertyAccessorMethodFormat.JAVA_BEAN, ImmutableSet.of(Constant.STRING_FIELD_CHAIN_SET_METHOD_OBJECT_SET_STRING_FIELD_METHOD_PROPERTY_WRITER))
                , Arguments.of(StringFieldChainSetMethodObject.class, PropertyAccessorMethodFormat.FLUENT, ImmutableSet.of())
                , Arguments.of(StringFieldFluentGetMethodObject.class, PropertyAccessorMethodFormat.JAVA_BEAN, ImmutableSet.of())
                , Arguments.of(StringFieldFluentGetMethodObject.class, PropertyAccessorMethodFormat.FLUENT, ImmutableSet.of(Constant.STRING_FIELD_FLUENT_GET_METHOD_OBJECT_STRING_FIELD_PROPERTY_READER))
                , Arguments.of(StringFieldFluentSetMethodObject.class, PropertyAccessorMethodFormat.JAVA_BEAN, ImmutableSet.of())
                , Arguments.of(StringFieldFluentSetMethodObject.class, PropertyAccessorMethodFormat.FLUENT, ImmutableSet.of(Constant.STRING_FIELD_FLUENT_SET_METHOD_OBJECT_STRING_FIELD_METHOD_PROPERTY_WRITER))
                , Arguments.of(StringFieldGetMethodObject.class, PropertyAccessorMethodFormat.JAVA_BEAN, ImmutableSet.of(Constant.STRING_FIELD_GET_METHOD_OBJECT_STRING_FIELD_PROPERTY_READER))
                , Arguments.of(StringFieldGetMethodObject.class, PropertyAccessorMethodFormat.FLUENT, ImmutableSet.of())
                , Arguments.of(StringFieldGetter.class, PropertyAccessorMethodFormat.JAVA_BEAN, ImmutableSet.of(Constant.STRING_FIELD_GETTER_STRING_FIELD_PROPERTY_READER))
                , Arguments.of(StringFieldGetter.class, PropertyAccessorMethodFormat.FLUENT, ImmutableSet.of())
                , Arguments.of(StringFieldGetterObject.class, PropertyAccessorMethodFormat.JAVA_BEAN, Constant.STRING_FIELD_GETTER_OBJECT_PROPERTY_READERS)
                , Arguments.of(StringFieldGetterObject.class, PropertyAccessorMethodFormat.FLUENT, ImmutableSet.of())
                , Arguments.of(StringFieldObject.class, PropertyAccessorMethodFormat.JAVA_BEAN, ImmutableSet.of(Constant.STRING_FIELD_OBJECT_STRING_FIELD_PROPERTY_ACCESSOR))
                , Arguments.of(StringFieldObject.class, PropertyAccessorMethodFormat.FLUENT, ImmutableSet.of(Constant.STRING_FIELD_OBJECT_STRING_FIELD_PROPERTY_ACCESSOR))
                , Arguments.of(StringFieldSetMethodObject.class, PropertyAccessorMethodFormat.JAVA_BEAN, ImmutableSet.of(Constant.STRING_FIELD_SET_METHOD_OBJECT_SET_STRING_FIELD_METHOD_PROPERTY_WRITER))
                , Arguments.of(StringFieldSetMethodObject.class, PropertyAccessorMethodFormat.FLUENT, ImmutableSet.of())
                , Arguments.of(StringFieldSetter.class, PropertyAccessorMethodFormat.JAVA_BEAN, ImmutableSet.of(Constant.STRING_FIELD_SETTER_SET_STRING_FIELD_METHOD_PROPERTY_WRITER))
                , Arguments.of(StringFieldSetter.class, PropertyAccessorMethodFormat.FLUENT, ImmutableSet.of())
                , Arguments.of(StringFieldSetterObject.class, PropertyAccessorMethodFormat.JAVA_BEAN, Constant.STRING_FIELD_SETTER_OBJECT_PROPERTY_WRITERS)
                , Arguments.of(StringFieldSetterObject.class, PropertyAccessorMethodFormat.FLUENT, ImmutableSet.of()));
    }

    static Stream<Arguments> typeTokenAndPropertyAccessorMethodFormatAndPropertyElementSets() {
        return classAndPropertyAccessorMethodFormatAndPropertyElementSets().map(arguments -> Arguments.of(TypeToken.of((Class<?>) arguments.get()[0]), arguments.get()[1], arguments.get()[2]));
    }

    static Stream<Arguments> classAndPropertyElementSets() {
        return classAndPropertyAccessorMethodFormatAndPropertyElementSets()
                .collect(ImmutableMap.toImmutableMap(arguments -> arguments.get()[0], Function.identity()
                        , (arguments1, arguments2) ->
                                Arguments.of(arguments1.get()[0], Stream.of(arguments1.get()[2], arguments2.get()[2]).map(a -> (Set<?>) a).flatMap(Collection::stream).collect(ImmutableSet.toImmutableSet()))
                ))
                .values().stream();
    }

    static Stream<Arguments> typeTokenAndPropertyElementSets() {
        return classAndPropertyElementSets().map(arguments -> Arguments.of(TypeToken.of((Class<?>) arguments.get()[0]), arguments.get()[1]));
    }

    static Stream<Arguments> classAndPropertyAccessorMethodFormatAndPropertyReaderSets() {
        return classAndPropertyAccessorMethodFormatAndPropertyElementSets()
                .map(arguments -> Arguments.of(arguments.get()[0], arguments.get()[1], ((Set<PropertyElement<?, ?>>) arguments.get()[2]).stream()
                        .filter(PropertyElement::isReader).collect(ImmutableSet.toImmutableSet())));
    }

    static Stream<Arguments> typeTokenAndPropertyAccessorMethodFormatAndPropertyReaderSets() {
        return classAndPropertyAccessorMethodFormatAndPropertyReaderSets().map(arguments -> Arguments.of(TypeToken.of((Class<?>) arguments.get()[0]), arguments.get()[1], arguments.get()[2]));
    }

    static Stream<Arguments> classAndPropertyReaderSets() {
        return classAndPropertyAccessorMethodFormatAndPropertyReaderSets()
                .collect(ImmutableMap.toImmutableMap(arguments -> arguments.get()[0], Function.identity()
                        , (arguments1, arguments2) ->
                                Arguments.of(arguments1.get()[0], Stream.of(arguments1.get()[2], arguments2.get()[2]).map(a -> (Set<?>) a).flatMap(Collection::stream).collect(ImmutableSet.toImmutableSet()))
                ))
                .values().stream();
    }

    static Stream<Arguments> typeTokenAndPropertyReaderSets() {
        return classAndPropertyReaderSets().map(arguments -> Arguments.of(TypeToken.of((Class<?>) arguments.get()[0]), arguments.get()[1]));
    }

    static Stream<Arguments> classAndPropertyAccessorMethodFormatAndPropertyWriterSets() {
        return classAndPropertyAccessorMethodFormatAndPropertyElementSets()
                .map(arguments -> Arguments.of(arguments.get()[0], arguments.get()[1], ((Set<PropertyElement<?, ?>>) arguments.get()[2]).stream()
                        .filter(PropertyElement::isWriter).collect(ImmutableSet.toImmutableSet())));
    }

    static Stream<Arguments> typeTokenAndPropertyAccessorMethodFormatAndPropertyWriterSets() {
        return classAndPropertyAccessorMethodFormatAndPropertyWriterSets().map(arguments -> Arguments.of(TypeToken.of((Class<?>) arguments.get()[0]), arguments.get()[1], arguments.get()[2]));
    }

    static Stream<Arguments> classAndPropertyWriterSets() {
        return classAndPropertyAccessorMethodFormatAndPropertyWriterSets()
                .collect(ImmutableMap.toImmutableMap(arguments -> arguments.get()[0], Function.identity()
                        , (arguments1, arguments2) ->
                                Arguments.of(arguments1.get()[0], Stream.of(arguments1.get()[2], arguments2.get()[2]).map(a -> (Set<?>) a).flatMap(Collection::stream).collect(ImmutableSet.toImmutableSet()))
                ))
                .values().stream();
    }

    static Stream<Arguments> typeTokenAndPropertyWriterSets() {
        return classAndPropertyWriterSets().map(arguments -> Arguments.of(TypeToken.of((Class<?>) arguments.get()[0]), arguments.get()[1]));
    }

    static Stream<Arguments> classAndPropertyAccessorMethodFormatAndPropertyAccessorSets() {
        return classAndPropertyAccessorMethodFormatAndPropertyElementSets()
                .map(arguments -> Arguments.of(arguments.get()[0], arguments.get()[1], ((Set<PropertyElement<?, ?>>) arguments.get()[2]).stream()
                        .filter(PropertyElement::isAccessor).collect(ImmutableSet.toImmutableSet())));
    }

    static Stream<Arguments> typeTokenAndPropertyAccessorMethodFormatAndPropertyAccessorSets() {
        return classAndPropertyAccessorMethodFormatAndPropertyAccessorSets().map(arguments -> Arguments.of(TypeToken.of((Class<?>) arguments.get()[0]), arguments.get()[1], arguments.get()[2]));
    }

    static Stream<Arguments> classAndPropertyAccessorSets() {
        return classAndPropertyAccessorMethodFormatAndPropertyAccessorSets()
                .collect(ImmutableMap.toImmutableMap(arguments -> arguments.get()[0], Function.identity()
                        , (arguments1, arguments2) ->
                                Arguments.of(arguments1.get()[0], Stream.of(arguments1.get()[2], arguments2.get()[2]).map(a -> (Set<?>) a).flatMap(Collection::stream).collect(ImmutableSet.toImmutableSet()))
                ))
                .values().stream();
    }

    static Stream<Arguments> typeTokenAndPropertyAccessorSets() {
        return classAndPropertyAccessorSets().map(arguments -> Arguments.of(TypeToken.of((Class<?>) arguments.get()[0]), arguments.get()[1]));
    }

    static Stream<Arguments> classAndPropertyAccessorMethodFormatArrayAndPropertySets() {
        return Stream.of(Arguments.of(BooleanFieldGetMethodObject.class, JAVA_BEAN, ImmutableSet.of(Constant.BOOLEAN_FIELD_GET_METHOD_OBJECT_BOOLEAN_PROPERTY))
                , Arguments.of(BooleanFieldGetMethodObject.class, FLUENT, ImmutableSet.of())
                , Arguments.of(BooleanFieldGetMethodObject.class, PropertyAccessorMethodFormat.values(), ImmutableSet.of(Constant.BOOLEAN_FIELD_GET_METHOD_OBJECT_BOOLEAN_PROPERTY))
                , Arguments.of(BooleanFieldIsMethodObject.class, JAVA_BEAN, ImmutableSet.of(Constant.BOOLEAN_FIELD_IS_METHOD_OBJECT_BOOLEAN_PROPERTY))
                , Arguments.of(BooleanFieldIsMethodObject.class, FLUENT, ImmutableSet.of())
                , Arguments.of(BooleanFieldIsMethodObject.class, PropertyAccessorMethodFormat.values(), ImmutableSet.of(Constant.BOOLEAN_FIELD_IS_METHOD_OBJECT_BOOLEAN_PROPERTY))
                , Arguments.of(ChildrenLongFieldObject.class, JAVA_BEAN, Constant.CHILDREN_LONG_FIELD_OBJECT_PROPERTIES)
                , Arguments.of(ChildrenLongFieldObject.class, FLUENT, Constant.CHILDREN_LONG_FIELD_OBJECT_PROPERTIES)
                , Arguments.of(ChildrenLongFieldObject.class, PropertyAccessorMethodFormat.values(), Constant.CHILDREN_LONG_FIELD_OBJECT_PROPERTIES)
                , Arguments.of(ChildrenSameNameFieldObject.class, JAVA_BEAN, ImmutableSet.of(Constant.CHILDREN_SAME_NAME_FIELD_OBJECT_STRING_PROPERTY))
                , Arguments.of(ChildrenSameNameFieldObject.class, FLUENT, ImmutableSet.of(Constant.CHILDREN_SAME_NAME_FIELD_OBJECT_STRING_PROPERTY))
                , Arguments.of(ChildrenSameNameFieldObject.class, PropertyAccessorMethodFormat.values(), ImmutableSet.of(Constant.CHILDREN_SAME_NAME_FIELD_OBJECT_STRING_PROPERTY))
                , Arguments.of(DuplicateNumberFieldSetMethodObject.class, JAVA_BEAN, ImmutableSet.of(Constant.DUPLICATE_NUMBER_FIELD_SET_METHOD_OBJECT_NUMBER_PROPERTY))
                , Arguments.of(DuplicateNumberFieldSetMethodObject.class, FLUENT, ImmutableSet.of())
                , Arguments.of(DuplicateNumberFieldSetMethodObject.class, PropertyAccessorMethodFormat.values(), ImmutableSet.of(Constant.DUPLICATE_NUMBER_FIELD_SET_METHOD_OBJECT_NUMBER_PROPERTY))
                , Arguments.of(FinalFieldObject.class, JAVA_BEAN, ImmutableSet.of(Constant.FINAL_FIELD_OBJECT_STRING_PROPERTY))
                , Arguments.of(FinalFieldObject.class, FLUENT, ImmutableSet.of(Constant.FINAL_FIELD_OBJECT_STRING_PROPERTY))
                , Arguments.of(FinalFieldObject.class, PropertyAccessorMethodFormat.values(), ImmutableSet.of(Constant.FINAL_FIELD_OBJECT_STRING_PROPERTY))
                , Arguments.of(MultipleConstructObject.class, JAVA_BEAN, ImmutableSet.of())
                , Arguments.of(MultipleConstructObject.class, FLUENT, ImmutableSet.of())
                , Arguments.of(MultipleConstructObject.class, PropertyAccessorMethodFormat.values(), ImmutableSet.of())
                , Arguments.of(MultipleFieldObject.class, JAVA_BEAN, Constant.MULTIPLE_FIELD_OBJECT_PROPERTIES)
                , Arguments.of(MultipleFieldObject.class, FLUENT, Constant.MULTIPLE_FIELD_OBJECT_PROPERTIES)
                , Arguments.of(MultipleFieldObject.class, PropertyAccessorMethodFormat.values(), Constant.MULTIPLE_FIELD_OBJECT_PROPERTIES)
                , Arguments.of(NoFieldObject.class, JAVA_BEAN, ImmutableSet.of())
                , Arguments.of(NoFieldObject.class, FLUENT, ImmutableSet.of())
                , Arguments.of(NoFieldObject.class, PropertyAccessorMethodFormat.values(), ImmutableSet.of())
                , Arguments.of(PrivateConstructObject.class, JAVA_BEAN, ImmutableSet.of())
                , Arguments.of(PrivateConstructObject.class, FLUENT, ImmutableSet.of())
                , Arguments.of(PrivateConstructObject.class, PropertyAccessorMethodFormat.values(), ImmutableSet.of())
                , Arguments.of(ProtectedConstructChildrenObject.class, JAVA_BEAN, ImmutableSet.of())
                , Arguments.of(ProtectedConstructChildrenObject.class, FLUENT, ImmutableSet.of())
                , Arguments.of(ProtectedConstructChildrenObject.class, PropertyAccessorMethodFormat.values(), ImmutableSet.of())
                , Arguments.of(ProtectedConstructObject.class, JAVA_BEAN, ImmutableSet.of())
                , Arguments.of(ProtectedConstructObject.class, FLUENT, ImmutableSet.of())
                , Arguments.of(ProtectedConstructObject.class, PropertyAccessorMethodFormat.values(), ImmutableSet.of())
                , Arguments.of(StaticFieldObject.class, JAVA_BEAN, ImmutableSet.of())
                , Arguments.of(StaticFieldObject.class, FLUENT, ImmutableSet.of())
                , Arguments.of(StaticFieldObject.class, PropertyAccessorMethodFormat.values(), ImmutableSet.of())
                , Arguments.of(StringFieldAndStringFieldGetMethodObject.class, JAVA_BEAN, ImmutableSet.of(Constant.STRING_FIELD_AND_STRING_FIELD_GET_METHOD_OBJECT_STRING_PROPERTY))
                , Arguments.of(StringFieldAndStringFieldGetMethodObject.class, FLUENT, ImmutableSet.of(AccessibleProperty.create(ImmutableSet.of(Constant.STRING_FIELD_AND_STRING_FIELD_GET_METHOD_OBJECT_STRING_FIELD_PROPERTY_ACCESSOR))))
                , Arguments.of(StringFieldAndStringFieldGetMethodObject.class, PropertyAccessorMethodFormat.values(), ImmutableSet.of(Constant.STRING_FIELD_AND_STRING_FIELD_GET_METHOD_OBJECT_STRING_PROPERTY))
                , Arguments.of(StringFieldAndStringFieldSetMethodObject.class, JAVA_BEAN, ImmutableSet.of(Constant.STRING_FIELD_AND_STRING_FIELD_SET_METHOD_OBJECT_STRING_PROPERTY))
                , Arguments.of(StringFieldAndStringFieldSetMethodObject.class, FLUENT, ImmutableSet.of(AccessibleProperty.create(ImmutableSet.of(Constant.STRING_FIELD_AND_STRING_FIELD_SET_METHOD_OBJECT_STRING_FIELD_PROPERTY_ACCESSOR))))
                , Arguments.of(StringFieldAndStringFieldSetMethodObject.class, PropertyAccessorMethodFormat.values(), ImmutableSet.of(Constant.STRING_FIELD_AND_STRING_FIELD_SET_METHOD_OBJECT_STRING_PROPERTY))
                , Arguments.of(StringFieldChainSetMethodObject.class, JAVA_BEAN, ImmutableSet.of(Constant.STRING_FIELD_CHAIN_SET_METHOD_OBJECT_STRING_PROPERTY))
                , Arguments.of(StringFieldChainSetMethodObject.class, FLUENT, ImmutableSet.of())
                , Arguments.of(StringFieldChainSetMethodObject.class, PropertyAccessorMethodFormat.values(), ImmutableSet.of(Constant.STRING_FIELD_CHAIN_SET_METHOD_OBJECT_STRING_PROPERTY))
                , Arguments.of(StringFieldFluentGetMethodObject.class, JAVA_BEAN, ImmutableSet.of())
                , Arguments.of(StringFieldFluentGetMethodObject.class, FLUENT, ImmutableSet.of(Constant.STRING_FIELD_FLUENT_GET_METHOD_OBJECT_STRING_PROPERTY))
                , Arguments.of(StringFieldFluentGetMethodObject.class, PropertyAccessorMethodFormat.values(), ImmutableSet.of(Constant.STRING_FIELD_FLUENT_GET_METHOD_OBJECT_STRING_PROPERTY))
                , Arguments.of(StringFieldFluentSetMethodObject.class, JAVA_BEAN, ImmutableSet.of())
                , Arguments.of(StringFieldFluentSetMethodObject.class, FLUENT, ImmutableSet.of(Constant.STRING_FIELD_FLUENT_SET_METHOD_OBJECT_STRING_PROPERTY))
                , Arguments.of(StringFieldFluentSetMethodObject.class, PropertyAccessorMethodFormat.values(), ImmutableSet.of(Constant.STRING_FIELD_FLUENT_SET_METHOD_OBJECT_STRING_PROPERTY))
                , Arguments.of(StringFieldGetMethodObject.class, JAVA_BEAN, ImmutableSet.of(Constant.STRING_FIELD_GET_METHOD_OBJECT_STRING_PROPERTY))
                , Arguments.of(StringFieldGetMethodObject.class, FLUENT, ImmutableSet.of())
                , Arguments.of(StringFieldGetMethodObject.class, PropertyAccessorMethodFormat.values(), ImmutableSet.of(Constant.STRING_FIELD_GET_METHOD_OBJECT_STRING_PROPERTY))
                , Arguments.of(StringFieldGetter.class, JAVA_BEAN, ImmutableSet.of(Constant.STRING_FIELD_GETTER_STRING_PROPERTY))
                , Arguments.of(StringFieldGetter.class, FLUENT, ImmutableSet.of())
                , Arguments.of(StringFieldGetter.class, PropertyAccessorMethodFormat.values(), ImmutableSet.of(Constant.STRING_FIELD_GETTER_STRING_PROPERTY))
                , Arguments.of(StringFieldGetterObject.class, JAVA_BEAN, ImmutableSet.of(Constant.STRING_FIELD_GETTER_OBJECT_STRING_PROPERTY))
                , Arguments.of(StringFieldGetterObject.class, FLUENT, ImmutableSet.of())
                , Arguments.of(StringFieldGetterObject.class, PropertyAccessorMethodFormat.values(), ImmutableSet.of(Constant.STRING_FIELD_GETTER_OBJECT_STRING_PROPERTY))
                , Arguments.of(StringFieldObject.class, JAVA_BEAN, ImmutableSet.of(Constant.STRING_FIELD_OBJECT_STRING_PROPERTY))
                , Arguments.of(StringFieldObject.class, FLUENT, ImmutableSet.of(Constant.STRING_FIELD_OBJECT_STRING_PROPERTY))
                , Arguments.of(StringFieldObject.class, PropertyAccessorMethodFormat.values(), ImmutableSet.of(Constant.STRING_FIELD_OBJECT_STRING_PROPERTY))
                , Arguments.of(StringFieldSetMethodObject.class, JAVA_BEAN, ImmutableSet.of(Constant.STRING_FIELD_SET_METHOD_OBJECT_STRING_PROPERTY))
                , Arguments.of(StringFieldSetMethodObject.class, FLUENT, ImmutableSet.of())
                , Arguments.of(StringFieldSetMethodObject.class, PropertyAccessorMethodFormat.values(), ImmutableSet.of(Constant.STRING_FIELD_SET_METHOD_OBJECT_STRING_PROPERTY))
                , Arguments.of(StringFieldSetter.class, JAVA_BEAN, ImmutableSet.of(Constant.STRING_FIELD_SETTER_STRING_PROPERTY))
                , Arguments.of(StringFieldSetter.class, FLUENT, ImmutableSet.of())
                , Arguments.of(StringFieldSetter.class, PropertyAccessorMethodFormat.values(), ImmutableSet.of(Constant.STRING_FIELD_SETTER_STRING_PROPERTY))
                , Arguments.of(StringFieldSetterObject.class, JAVA_BEAN, ImmutableSet.of(Constant.STRING_FIELD_SETTER_OBJECT_STRING_PROPERTY))
                , Arguments.of(StringFieldSetterObject.class, FLUENT, ImmutableSet.of())
                , Arguments.of(StringFieldSetterObject.class, PropertyAccessorMethodFormat.values(), ImmutableSet.of(Constant.STRING_FIELD_SETTER_OBJECT_STRING_PROPERTY)));
    }

    static Stream<Arguments> typeTokenAndPropertyAccessorMethodFormatArrayAndPropertySets() {
        return classAndPropertyAccessorMethodFormatArrayAndPropertySets().map(arguments -> Arguments.of(TypeToken.of((Class<?>) arguments.get()[0]), arguments.get()[1], arguments.get()[2]));
    }

    static Stream<Arguments> classAndPropertySets() {
        return classAndPropertyAccessorMethodFormatArrayAndPropertySets()
                .filter(arguments -> Arrays.equals((PropertyAccessorMethodFormat[]) arguments.get()[1], PropertyAccessorMethodFormat.values()))
                .map(arguments -> Arguments.of(arguments.get()[0], arguments.get()[2]));
    }

    static Stream<Arguments> typeTokenAndPropertySets() {
        return classAndPropertySets().map(arguments -> Arguments.of(TypeToken.of((Class<?>) arguments.get()[0]), arguments.get()[1]));
    }

    static Stream<Arguments> classAndPropertyAccessorMethodFormatArrayAndReadablePropertySets() {
        return classAndPropertyAccessorMethodFormatArrayAndPropertySets()
                .map(arguments -> Arguments.of(arguments.get()[0], arguments.get()[1], ((Collection<Property<?, ?>>) arguments.get()[2]).stream()
                        .filter(Property::readable).map(Property::toReadable).collect(ImmutableSet.toImmutableSet())));
    }

    static Stream<Arguments> typeTokenAndPropertyAccessorMethodFormatArrayAndReadablePropertySets() {
        return classAndPropertyAccessorMethodFormatArrayAndReadablePropertySets().map(arguments -> Arguments.of(TypeToken.of((Class<?>) arguments.get()[0]), arguments.get()[1], arguments.get()[2]));
    }

    static Stream<Arguments> classAndReadablePropertySets() {
        return classAndPropertyAccessorMethodFormatArrayAndReadablePropertySets()
                .filter(arguments -> Arrays.equals((PropertyAccessorMethodFormat[]) arguments.get()[1], PropertyAccessorMethodFormat.values()))
                .map(arguments -> Arguments.of(arguments.get()[0], arguments.get()[2]));
    }

    static Stream<Arguments> typeTokenAndReadablePropertySets() {
        return classAndReadablePropertySets().map(arguments -> Arguments.of(TypeToken.of((Class<?>) arguments.get()[0]), arguments.get()[1]));
    }

    static Stream<Arguments> classAndPropertyAccessorMethodFormatArrayAndWritablePropertySets() {
        return classAndPropertyAccessorMethodFormatArrayAndPropertySets()
                .map(arguments -> Arguments.of(arguments.get()[0], arguments.get()[1], ((Collection<Property<?, ?>>) arguments.get()[2]).stream()
                        .filter(Property::writable).map(Property::toWritable).collect(ImmutableSet.toImmutableSet())));
    }

    static Stream<Arguments> typeTokenAndPropertyAccessorMethodFormatArrayAndWritablePropertySets() {
        return classAndPropertyAccessorMethodFormatArrayAndWritablePropertySets().map(arguments -> Arguments.of(TypeToken.of((Class<?>) arguments.get()[0]), arguments.get()[1], arguments.get()[2]));
    }

    static Stream<Arguments> classAndWritablePropertySets() {
        return classAndPropertyAccessorMethodFormatArrayAndWritablePropertySets()
                .filter(arguments -> Arrays.equals((PropertyAccessorMethodFormat[]) arguments.get()[1], PropertyAccessorMethodFormat.values()))
                .map(arguments -> Arguments.of(arguments.get()[0], arguments.get()[2]));
    }

    static Stream<Arguments> typeTokenAndWritablePropertySets() {
        return classAndWritablePropertySets().map(arguments -> Arguments.of(TypeToken.of((Class<?>) arguments.get()[0]), arguments.get()[1]));
    }

    static Stream<Arguments> classAndPropertyAccessorMethodFormatArrayAndAccessiblePropertySets() {
        return classAndPropertyAccessorMethodFormatArrayAndPropertySets()
                .map(arguments -> Arguments.of(arguments.get()[0], arguments.get()[1], ((Collection<Property<?, ?>>) arguments.get()[2]).stream()
                        .filter(Property::accessible).map(Property::toAccessible).collect(ImmutableSet.toImmutableSet())));
    }

    static Stream<Arguments> typeTokenAndPropertyAccessorMethodFormatArrayAndAccessiblePropertySets() {
        return classAndPropertyAccessorMethodFormatArrayAndAccessiblePropertySets().map(arguments -> Arguments.of(TypeToken.of((Class<?>) arguments.get()[0]), arguments.get()[1], arguments.get()[2]));
    }

    static Stream<Arguments> classAndAccessiblePropertySets() {
        return classAndPropertyAccessorMethodFormatArrayAndAccessiblePropertySets()
                .filter(arguments -> Arrays.equals((PropertyAccessorMethodFormat[]) arguments.get()[1], PropertyAccessorMethodFormat.values()))
                .map(arguments -> Arguments.of(arguments.get()[0], arguments.get()[2]));
    }

    static Stream<Arguments> typeTokenAndAccessiblePropertySets() {
        return classAndAccessiblePropertySets().map(arguments -> Arguments.of(TypeToken.of((Class<?>) arguments.get()[0]), arguments.get()[1]));
    }

    static Stream<Arguments> classAndPropertyNameAndPropertyAccessorMethodFormatArrayAndPropertys() {
        return classAndPropertyAccessorMethodFormatArrayAndPropertySets()
                .flatMap(arguments -> ((Collection<Property<?, ?>>) arguments.get()[2]).stream()
                        .map(property -> Arguments.of(arguments.get()[0], property.name(), arguments.get()[1], property)));
    }

    static Stream<Arguments> typeTokenAndPropertyNameAndPropertyAccessorMethodFormatArrayAndPropertys() {
        return classAndPropertyNameAndPropertyAccessorMethodFormatArrayAndPropertys().map(arguments -> Arguments.of(TypeToken.of((Class<?>) arguments.get()[0]), arguments.get()[1], arguments.get()[2], arguments.get()[3]));
    }

    static Stream<Arguments> classAndPropertyNameAndPropertys() {
        return classAndPropertyNameAndPropertyAccessorMethodFormatArrayAndPropertys()
                .filter(arguments -> Arrays.equals((PropertyAccessorMethodFormat[]) arguments.get()[2], PropertyAccessorMethodFormat.values()))
                .map(arguments -> Arguments.of(arguments.get()[0], arguments.get()[1], arguments.get()[3]));
    }

    static Stream<Arguments> typeTokenAndPropertyNameAndPropertys() {
        return classAndPropertyNameAndPropertys().map(arguments -> Arguments.of(TypeToken.of((Class<?>) arguments.get()[0]), arguments.get()[1], arguments.get()[2]));
    }

    static Stream<Arguments> classAndPropertyNameAndPropertyAccessorMethodFormatArrayAndReadablePropertys() {
        return classAndPropertyNameAndPropertyAccessorMethodFormatArrayAndPropertys()
                .filter(arguments -> ((Property<?, ?>) arguments.get()[3]).readable());
    }

    static Stream<Arguments> typeTokenAndPropertyNameAndPropertyAccessorMethodFormatArrayAndReadablePropertys() {
        return classAndPropertyNameAndPropertyAccessorMethodFormatArrayAndReadablePropertys().map(arguments -> Arguments.of(TypeToken.of((Class<?>) arguments.get()[0]), arguments.get()[1], arguments.get()[2], arguments.get()[3]));
    }

    static Stream<Arguments> classAndPropertyNameAndReadablePropertys() {
        return classAndPropertyNameAndPropertys()
                .filter(arguments -> ((Property<?, ?>) arguments.get()[2]).readable());
    }

    static Stream<Arguments> typeTokenAndPropertyNameAndReadablePropertys() {
        return classAndPropertyNameAndReadablePropertys().map(arguments -> Arguments.of(TypeToken.of((Class<?>) arguments.get()[0]), arguments.get()[1], arguments.get()[2]));
    }

    static Stream<Arguments> classAndErrorReadablePropertyNameAndPropertyAccessorMethodFormatArrays() {
        return classAndPropertyNameAndPropertyAccessorMethodFormatArrayAndPropertys()
                .map(arguments -> {
                    Property<?, ?> property = (Property<?, ?>) arguments.get()[3];
                    String errorReadablePropertyName = property.readable() ? Math.random() + "" : property.name();
                    return Arguments.of(arguments.get()[0], errorReadablePropertyName, arguments.get()[2]);
                });
    }

    static Stream<Arguments> typeTokenAndErrorReadablePropertyNameAndPropertyAccessorMethodFormatArrays() {
        return classAndErrorReadablePropertyNameAndPropertyAccessorMethodFormatArrays()
                .map(arguments -> Arguments.of(TypeToken.of((Class<?>) arguments.get()[0]), arguments.get()[1], arguments.get()[2]));
    }

    static Stream<Arguments> classAndErrorReadablePropertyName() {
        return classAndErrorReadablePropertyNameAndPropertyAccessorMethodFormatArrays()
                .filter(arguments -> Arrays.equals((PropertyAccessorMethodFormat[]) arguments.get()[2], PropertyAccessorMethodFormat.values()));
    }

    static Stream<Arguments> typeTokenAndErrorReadablePropertyName() {
        return classAndErrorReadablePropertyName()
                .map(arguments -> Arguments.of(TypeToken.of((Class<?>) arguments.get()[0]), arguments.get()[1]));
    }

    static Stream<Arguments> classAndPropertyNameAndPropertyAccessorMethodFormatArrayAndWritablePropertys() {
        return classAndPropertyNameAndPropertyAccessorMethodFormatArrayAndPropertys()
                .filter(arguments -> ((Property<?, ?>) arguments.get()[3]).writable());
    }

    static Stream<Arguments> typeTokenAndPropertyNameAndPropertyAccessorMethodFormatArrayAndWritablePropertys() {
        return classAndPropertyNameAndPropertyAccessorMethodFormatArrayAndWritablePropertys().map(arguments -> Arguments.of(TypeToken.of((Class<?>) arguments.get()[0]), arguments.get()[1], arguments.get()[2], arguments.get()[3]));
    }

    static Stream<Arguments> classAndPropertyNameAndWritablePropertys() {
        return classAndPropertyNameAndPropertys()
                .filter(arguments -> ((Property<?, ?>) arguments.get()[2]).writable());
    }

    static Stream<Arguments> typeTokenAndPropertyNameAndWritablePropertys() {
        return classAndPropertyNameAndWritablePropertys().map(arguments -> Arguments.of(TypeToken.of((Class<?>) arguments.get()[0]), arguments.get()[1], arguments.get()[2]));
    }

    static Stream<Arguments> classAndErrorWritablePropertyNameAndPropertyAccessorMethodFormatArrays() {
        return classAndPropertyNameAndPropertyAccessorMethodFormatArrayAndPropertys()
                .map(arguments -> {
                    Property<?, ?> property = (Property<?, ?>) arguments.get()[3];
                    String errorWritablePropertyName = property.writable() ? Math.random() + "" : property.name();
                    return Arguments.of(arguments.get()[0], errorWritablePropertyName, arguments.get()[2]);
                });
    }

    static Stream<Arguments> typeTokenAndErrorWritablePropertyNameAndPropertyAccessorMethodFormatArrays() {
        return classAndErrorWritablePropertyNameAndPropertyAccessorMethodFormatArrays()
                .map(arguments -> Arguments.of(TypeToken.of((Class<?>) arguments.get()[0]), arguments.get()[1], arguments.get()[2]));
    }

    static Stream<Arguments> classAndErrorWritablePropertyName() {
        return classAndErrorWritablePropertyNameAndPropertyAccessorMethodFormatArrays()
                .filter(arguments -> Arrays.equals((PropertyAccessorMethodFormat[]) arguments.get()[2], PropertyAccessorMethodFormat.values()));
    }

    static Stream<Arguments> typeTokenAndErrorWritablePropertyName() {
        return classAndErrorWritablePropertyName()
                .map(arguments -> Arguments.of(TypeToken.of((Class<?>) arguments.get()[0]), arguments.get()[1]));
    }

    static Stream<Arguments> classAndPropertyNameAndPropertyAccessorMethodFormatArrayAndAccessiblePropertys() {
        return classAndPropertyNameAndPropertyAccessorMethodFormatArrayAndPropertys()
                .filter(arguments -> ((Property<?, ?>) arguments.get()[3]).accessible());
    }

    static Stream<Arguments> typeTokenAndPropertyNameAndPropertyAccessorMethodFormatArrayAndAccessiblePropertys() {
        return classAndPropertyNameAndPropertyAccessorMethodFormatArrayAndAccessiblePropertys().map(arguments -> Arguments.of(TypeToken.of((Class<?>) arguments.get()[0]), arguments.get()[1], arguments.get()[2], arguments.get()[3]));
    }

    static Stream<Arguments> classAndPropertyNameAndAccessiblePropertys() {
        return classAndPropertyNameAndPropertys()
                .filter(arguments -> ((Property<?, ?>) arguments.get()[2]).accessible());
    }

    static Stream<Arguments> typeTokenAndPropertyNameAndAccessiblePropertys() {
        return classAndPropertyNameAndAccessiblePropertys().map(arguments -> Arguments.of(TypeToken.of((Class<?>) arguments.get()[0]), arguments.get()[1], arguments.get()[2]));
    }

    static Stream<Arguments> classAndErrorAccessiblePropertyNameAndPropertyAccessorMethodFormatArrays() {
        return classAndPropertyNameAndPropertyAccessorMethodFormatArrayAndPropertys()
                .map(arguments -> {
                    Property<?, ?> property = (Property<?, ?>) arguments.get()[3];
                    String errorAccessiblePropertyName = property.accessible() ? Math.random() + "" : property.name();
                    return Arguments.of(arguments.get()[0], errorAccessiblePropertyName, arguments.get()[2]);
                });
    }

    static Stream<Arguments> typeTokenAndErrorAccessiblePropertyNameAndPropertyAccessorMethodFormatArrays() {
        return classAndErrorAccessiblePropertyNameAndPropertyAccessorMethodFormatArrays()
                .map(arguments -> Arguments.of(TypeToken.of((Class<?>) arguments.get()[0]), arguments.get()[1], arguments.get()[2]));
    }

    static Stream<Arguments> classAndErrorAccessiblePropertyName() {
        return classAndErrorAccessiblePropertyNameAndPropertyAccessorMethodFormatArrays()
                .filter(arguments -> Arrays.equals((PropertyAccessorMethodFormat[]) arguments.get()[2], PropertyAccessorMethodFormat.values()));
    }

    static Stream<Arguments> typeTokenAndErrorAccessiblePropertyName() {
        return classAndErrorAccessiblePropertyName()
                .map(arguments -> Arguments.of(TypeToken.of((Class<?>) arguments.get()[0]), arguments.get()[1]));
    }
}
