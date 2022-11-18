package org.caotc.unit4j.core.common.util.provider;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Streams;
import com.google.common.reflect.TypeToken;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.caotc.unit4j.core.common.reflect.property.AccessibleProperty;
import org.caotc.unit4j.core.common.reflect.property.Property;
import org.caotc.unit4j.core.common.reflect.property.ReadableProperty;
import org.caotc.unit4j.core.common.reflect.property.WritableProperty;
import org.caotc.unit4j.core.common.reflect.property.accessor.*;
import org.caotc.unit4j.core.common.util.model.*;
import org.junit.jupiter.params.provider.Arguments;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
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
        return Stream.of(byte.class, short.class, int.class, long.class, char.class, boolean.class, float.class, double.class
                , Object.class, BooleanFieldGetMethodObject.class, BooleanFieldIsMethodObject.class, ChildrenLongFieldObject.class
                , ChildrenSameNameFieldObject.class, DoubleGenericFieldObject.class, DuplicateNumberFieldSetMethodObject.class
                , FinalFieldObject.class, GenericFieldGetter.class, GenericFieldObject.class, GenericFieldSetter.class
                , IntegerGenericFieldGetter.class, LongGenericFieldSetter.class, MultipleConstructObject.class, MultipleFieldObject.class
                , NoFieldObject.class, PrivateConstructObject.class, ProtectedConstructChildrenObject.class, ProtectedConstructObject.class
                , StaticFieldObject.class, StringFieldAndStringFieldGetMethodObject.class, StringFieldAndStringFieldSetMethodObject.class
                , StringFieldChainSetMethodObject.class, StringFieldFluentGetMethodObject.class, StringFieldFluentSetMethodObject.class
                , StringFieldGetMethodObject.class, StringFieldGetter.class, StringFieldGetterObject.class, StringFieldObject.class
                , StringFieldSetMethodObject.class, StringFieldSetter.class, StringFieldSetterObject.class);
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
                , Arguments.of(DoubleGenericFieldObject.class, Constant.DOUBLE_GENERIC_FIELD_OBJECT_FIELDS)
                , Arguments.of(DuplicateNumberFieldSetMethodObject.class, ImmutableSet.of())
                , Arguments.of(FinalFieldObject.class, Constant.FINAL_FIELD_OBJECT_FIELDS)
                , Arguments.of(GenericFieldGetter.class, ImmutableSet.of())
                , Arguments.of(GenericFieldObject.class, Constant.DOUBLE_GENERIC_FIELD_OBJECT_FIELDS)
                , Arguments.of(GenericFieldSetter.class, ImmutableSet.of())
                , Arguments.of(IntegerGenericFieldGetter.class, ImmutableSet.of())
                , Arguments.of(LongGenericFieldSetter.class, ImmutableSet.of())
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
                , Arguments.of(StringFieldSetterObject.class, ImmutableSet.of()));
    }

    static Stream<Arguments> typeTokenAndFieldSets() {
        return classAndFieldSets().map(arguments -> Arguments.of(TypeToken.of((Class<?>) arguments.get()[0]), arguments.get()[1]));
    }

    static Stream<Arguments> classAndFieldNameAndFields() {
        return Stream.of(Arguments.of(StringFieldObject.class, StringFieldObject.Fields.STRING_FIELD, ImmutableSet.of(Constant.STRING_FIELD_OBJECT_STRING_FIELD))
                , Arguments.of(DoubleGenericFieldObject.class, DoubleGenericFieldObject.Fields.GENERIC_FIELD, Constant.DOUBLE_GENERIC_FIELD_OBJECT_FIELDS)
                , Arguments.of(FinalFieldObject.class, FinalFieldObject.Fields.STRING_FIELD, ImmutableSet.of(Constant.FINAL_FIELD_OBJECT_STRING_FIELD))
                , Arguments.of(GenericFieldObject.class, GenericFieldObject.Fields.GENERIC_FIELD, ImmutableSet.of(Constant.GENERIC_FIELD_OBJECT_GENERIC_FIELD))
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
                , Arguments.of(DoubleGenericFieldObject.class, Constant.OBJECT_METHODS)
                , Arguments.of(DuplicateNumberFieldSetMethodObject.class, Constant.DUPLICATE_NUMBER_FIELD_SET_METHOD_OBJECT_METHODS)
                , Arguments.of(FinalFieldObject.class, Constant.OBJECT_METHODS)
                , Arguments.of(GenericFieldGetter.class, ImmutableSet.of(Constant.GENERIC_FIELD_GETTER_GET_GENERIC_FIELD_METHOD))
                , Arguments.of(GenericFieldObject.class, Constant.OBJECT_METHODS)
                , Arguments.of(GenericFieldSetter.class, ImmutableSet.of(Constant.GENERIC_FIELD_SETTER_SET_GENERIC_FIELD_METHOD))
                , Arguments.of(IntegerGenericFieldGetter.class, Constant.INTEGER_GENERIC_FIELD_GETTER_METHODS)
                , Arguments.of(LongGenericFieldSetter.class, Constant.LONG_GENERIC_FIELD_SETTER_METHODS)
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
                , Arguments.of(StringFieldSetterObject.class, Constant.STRING_FIELD_SETTER_OBJECT_METHODS));
    }

    static Stream<Arguments> typeTokenAndMethodSets() {
        return classAndMethodSets().map(arguments -> Arguments.of(TypeToken.of((Class<?>) arguments.get()[0]), arguments.get()[1]));
    }

    static Stream<Arguments> classAndConstructorSets() {
        return Stream.of(Arguments.of(NoFieldObject.class, ImmutableSet.of(Constant.NO_FIELD_OBJECT_CONSTRUCTOR))
                , Arguments.of(StringFieldObject.class, ImmutableSet.of(Constant.STRING_FIELD_OBJECT_CONSTRUCTOR))
                , Arguments.of(StaticFieldObject.class, ImmutableSet.of(Constant.STATIC_FIELD_OBJECT_CONSTRUCTOR))
                , Arguments.of(DoubleGenericFieldObject.class, ImmutableSet.of(Constant.DOUBLE_GENERIC_FIELD_OBJECT_CONSTRUCTOR))
                , Arguments.of(DuplicateNumberFieldSetMethodObject.class, ImmutableSet.of(Constant.DUPLICATE_NUMBER_FIELD_SET_METHOD_OBJECT_CONSTRUCTOR))
                , Arguments.of(FinalFieldObject.class, ImmutableSet.of(Constant.FINAL_FIELD_OBJECT_CONSTRUCTOR))
                , Arguments.of(GenericFieldGetter.class, ImmutableSet.of())
                , Arguments.of(GenericFieldObject.class, ImmutableSet.of(Constant.GENERIC_FIELD_OBJECT_CONSTRUCTOR))
                , Arguments.of(GenericFieldSetter.class, ImmutableSet.of())
                , Arguments.of(IntegerGenericFieldGetter.class, ImmutableSet.of(Constant.INTEGER_GENERIC_FIELD_GETTER_CONSTRUCTOR))
                , Arguments.of(LongGenericFieldSetter.class, ImmutableSet.of(Constant.LONG_GENERIC_FIELD_SETTER_CONSTRUCTOR))
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
                , Arguments.of(StringFieldSetterObject.class, ImmutableSet.of(Constant.STRING_FIELD_SETTER_OBJECT_CONSTRUCTOR)));
    }

    static Stream<Arguments> typeTokenAndConstructorSets() {
        return classAndConstructorSets().map(arguments -> Arguments.of(TypeToken.of((Class<?>) arguments.get()[0]), arguments.get()[1]));
    }

    static Stream<Arguments> classAndFieldNameAndGetMethods() {
        return Stream.of(Arguments.of(StringFieldGetMethodObject.class, "stringField", Constant.STRING_FIELD_GET_METHOD_OBJECT_GET_STRING_FIELD_METHOD)
                , Arguments.of(StringFieldAndStringFieldGetMethodObject.class, StringFieldAndStringFieldGetMethodObject.Fields.STRING_FIELD, Constant.STRING_FIELD_AND_STRING_FIELD_GET_METHOD_OBJECT_GET_STRING_FIELD_METHOD)
                , Arguments.of(GenericFieldGetter.class, "genericField", Constant.GENERIC_FIELD_GETTER_GET_GENERIC_FIELD_METHOD)
                , Arguments.of(IntegerGenericFieldGetter.class, "genericField", Constant.INTEGER_GENERIC_FIELD_GETTER_GET_GENERIC_FIELD_METHOD)
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
                , Arguments.of(DoubleGenericFieldObject.class, ImmutableSet.of())
                , Arguments.of(DuplicateNumberFieldSetMethodObject.class, ImmutableSet.of())
                , Arguments.of(FinalFieldObject.class, ImmutableSet.of())
                , Arguments.of(GenericFieldGetter.class, ImmutableSet.of(Constant.GENERIC_FIELD_GETTER_GET_GENERIC_FIELD_METHOD))
                , Arguments.of(GenericFieldObject.class, ImmutableSet.of())
                , Arguments.of(GenericFieldSetter.class, ImmutableSet.of())
                , Arguments.of(IntegerGenericFieldGetter.class, Constant.INTEGER_GENERIC_FIELD_GETTER_GET_METHODS)
                , Arguments.of(LongGenericFieldSetter.class, ImmutableSet.of())
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
                , Arguments.of(GenericFieldSetter.class, "genericField", Constant.GENERIC_FIELD_SETTER_SET_GENERIC_FIELD_METHOD)
                , Arguments.of(LongGenericFieldSetter.class, "genericField", Constant.LONG_GENERIC_FIELD_SETTER_SET_GENERIC_FIELD_METHOD)
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
                        , Arguments.of(GenericFieldSetter.class, "genericField", ImmutableSet.of(Constant.GENERIC_FIELD_SETTER_SET_GENERIC_FIELD_METHOD))
                        , Arguments.of(LongGenericFieldSetter.class, "genericField", Constant.LONG_GENERIC_FIELD_SETTER_SET_METHODS)
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
                , Arguments.of(DoubleGenericFieldObject.class, ImmutableSet.of())
                , Arguments.of(DuplicateNumberFieldSetMethodObject.class, Constant.DUPLICATE_NUMBER_FIELD_SET_METHOD_OBJECT_SET_METHODS)
                , Arguments.of(FinalFieldObject.class, ImmutableSet.of())
                , Arguments.of(GenericFieldGetter.class, ImmutableSet.of())
                , Arguments.of(GenericFieldObject.class, ImmutableSet.of())
                , Arguments.of(GenericFieldSetter.class, ImmutableSet.of(Constant.GENERIC_FIELD_SETTER_SET_GENERIC_FIELD_METHOD))
                , Arguments.of(IntegerGenericFieldGetter.class, ImmutableSet.of())
                , Arguments.of(LongGenericFieldSetter.class, Constant.LONG_GENERIC_FIELD_SETTER_SET_METHODS)
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
        return typeTokenAndPropertyAccessorMethodFormatAndPropertyElementSets()
                .filter(arguments -> ((TypeToken<?>) arguments.get()[0]).equals(TypeToken.of(((TypeToken<?>) arguments.get()[0]).getRawType())))
                .map(arguments -> Arguments.of(((TypeToken<?>) arguments.get()[0]).getRawType(), arguments.get()[1], arguments.get()[2]));
    }

    static Stream<Arguments> typeTokenAndPropertyAccessorMethodFormatAndPropertyElementSets() {
        return Stream.of(Arguments.of(TypeToken.of(BooleanFieldGetMethodObject.class), PropertyAccessorMethodFormat.JAVA_BEAN, ImmutableSet.of(Constant.BOOLEAN_FIELD_GET_METHOD_OBJECT_BOOLEAN_FIELD_PROPERTY_READER))
                , Arguments.of(TypeToken.of(BooleanFieldGetMethodObject.class), PropertyAccessorMethodFormat.FLUENT, ImmutableSet.of())
                , Arguments.of(TypeToken.of(BooleanFieldIsMethodObject.class), PropertyAccessorMethodFormat.JAVA_BEAN, ImmutableSet.of(Constant.BOOLEAN_FIELD_IS_METHOD_OBJECT_BOOLEAN_FIELD_PROPERTY_READER))
                , Arguments.of(TypeToken.of(BooleanFieldIsMethodObject.class), PropertyAccessorMethodFormat.FLUENT, ImmutableSet.of())
                , Arguments.of(TypeToken.of(ChildrenLongFieldObject.class), PropertyAccessorMethodFormat.JAVA_BEAN, Constant.CHILDREN_LONG_FIELD_OBJECT_PROPERTY_ACCESSORS)
                , Arguments.of(TypeToken.of(ChildrenLongFieldObject.class), PropertyAccessorMethodFormat.FLUENT, Constant.CHILDREN_LONG_FIELD_OBJECT_PROPERTY_ACCESSORS)
                , Arguments.of(TypeToken.of(ChildrenSameNameFieldObject.class), PropertyAccessorMethodFormat.JAVA_BEAN, Constant.CHILDREN_SAME_NAME_FIELD_OBJECT_PROPERTY_ACCESSORS)
                , Arguments.of(TypeToken.of(ChildrenSameNameFieldObject.class), PropertyAccessorMethodFormat.FLUENT, Constant.CHILDREN_SAME_NAME_FIELD_OBJECT_PROPERTY_ACCESSORS)
                , Arguments.of(TypeToken.of(DoubleGenericFieldObject.class), PropertyAccessorMethodFormat.JAVA_BEAN, ImmutableSet.of(Constant.DOUBLE_GENERIC_FIELD_OBJECT_GENERIC_FIELD_PROPERTY_ACCESSOR))
                , Arguments.of(TypeToken.of(DoubleGenericFieldObject.class), PropertyAccessorMethodFormat.FLUENT, ImmutableSet.of(Constant.DOUBLE_GENERIC_FIELD_OBJECT_GENERIC_FIELD_PROPERTY_ACCESSOR))
                , Arguments.of(TypeToken.of(DuplicateNumberFieldSetMethodObject.class), PropertyAccessorMethodFormat.JAVA_BEAN, Constant.DUPLICATE_NUMBER_FIELD_SET_METHOD_OBJECT_PROPERTY_WRITERS)
                , Arguments.of(TypeToken.of(DuplicateNumberFieldSetMethodObject.class), PropertyAccessorMethodFormat.FLUENT, ImmutableSet.of())
                , Arguments.of(TypeToken.of(FinalFieldObject.class), PropertyAccessorMethodFormat.JAVA_BEAN, ImmutableSet.of(Constant.FINAL_FIELD_OBJECT_STRING_FIELD_PROPERTY_ACCESSOR))
                , Arguments.of(TypeToken.of(FinalFieldObject.class), PropertyAccessorMethodFormat.FLUENT, ImmutableSet.of(Constant.FINAL_FIELD_OBJECT_STRING_FIELD_PROPERTY_ACCESSOR))
                , Arguments.of(new TypeToken<GenericFieldGetter<Object>>() {
                }, PropertyAccessorMethodFormat.JAVA_BEAN, ImmutableSet.of(Constant.GENERIC_FIELD_GETTER_GENERIC_FIELD_PROPERTY_READER))
                , Arguments.of(new TypeToken<GenericFieldGetter<Object>>() {
                }, PropertyAccessorMethodFormat.FLUENT, ImmutableSet.of())
                , Arguments.of(new TypeToken<GenericFieldObject<Object>>() {
                }, PropertyAccessorMethodFormat.JAVA_BEAN, ImmutableSet.of(Constant.GENERIC_FIELD_OBJECT_GENERIC_FIELD_PROPERTY_ACCESSOR))
                , Arguments.of(new TypeToken<GenericFieldObject<Object>>() {
                }, PropertyAccessorMethodFormat.FLUENT, ImmutableSet.of(Constant.GENERIC_FIELD_OBJECT_GENERIC_FIELD_PROPERTY_ACCESSOR))
                , Arguments.of(new TypeToken<GenericFieldSetter<Object>>() {
                }, PropertyAccessorMethodFormat.JAVA_BEAN, ImmutableSet.of(Constant.GENERIC_FIELD_SETTER_GENERIC_FIELD_PROPERTY_WRITER))
                , Arguments.of(new TypeToken<GenericFieldSetter<Object>>() {
                }, PropertyAccessorMethodFormat.FLUENT, ImmutableSet.of())
                , Arguments.of(TypeToken.of(GenericFieldGetter.class), PropertyAccessorMethodFormat.JAVA_BEAN, ImmutableSet.of(PropertyReader.from(GenericFieldGetter.class, Constant.GENERIC_FIELD_GETTER_GET_GENERIC_FIELD_METHOD, "genericField")))
                , Arguments.of(TypeToken.of(GenericFieldGetter.class), PropertyAccessorMethodFormat.FLUENT, ImmutableSet.of())
                , Arguments.of(TypeToken.of(GenericFieldObject.class), PropertyAccessorMethodFormat.JAVA_BEAN, ImmutableSet.of(PropertyAccessor.from(GenericFieldObject.class, Constant.GENERIC_FIELD_OBJECT_GENERIC_FIELD)))
                , Arguments.of(TypeToken.of(GenericFieldObject.class), PropertyAccessorMethodFormat.FLUENT, ImmutableSet.of(PropertyAccessor.from(GenericFieldObject.class, Constant.GENERIC_FIELD_OBJECT_GENERIC_FIELD)))
                , Arguments.of(TypeToken.of(GenericFieldSetter.class), PropertyAccessorMethodFormat.JAVA_BEAN, ImmutableSet.of(PropertyWriter.from(GenericFieldSetter.class, Constant.GENERIC_FIELD_SETTER_SET_GENERIC_FIELD_METHOD, "genericField")))
                , Arguments.of(TypeToken.of(GenericFieldSetter.class), PropertyAccessorMethodFormat.FLUENT, ImmutableSet.of())
                , Arguments.of(TypeToken.of(IntegerGenericFieldGetter.class), PropertyAccessorMethodFormat.JAVA_BEAN, Constant.INTEGER_GENERIC_FIELD_GETTER_PROPERTY_READERS)
                , Arguments.of(TypeToken.of(IntegerGenericFieldGetter.class), PropertyAccessorMethodFormat.FLUENT, ImmutableSet.of())
                , Arguments.of(TypeToken.of(LongGenericFieldSetter.class), PropertyAccessorMethodFormat.JAVA_BEAN, Constant.LONG_GENERIC_FIELD_SETTER_PROPERTY_WRITERS)
                , Arguments.of(TypeToken.of(LongGenericFieldSetter.class), PropertyAccessorMethodFormat.FLUENT, ImmutableSet.of())
                , Arguments.of(TypeToken.of(MultipleConstructObject.class), PropertyAccessorMethodFormat.JAVA_BEAN, ImmutableSet.of())
                , Arguments.of(TypeToken.of(MultipleConstructObject.class), PropertyAccessorMethodFormat.FLUENT, ImmutableSet.of())
                , Arguments.of(TypeToken.of(MultipleFieldObject.class), PropertyAccessorMethodFormat.JAVA_BEAN, Constant.MULTIPLE_FIELD_OBJECT_PROPERTY_ACCESSORS)
                , Arguments.of(TypeToken.of(MultipleFieldObject.class), PropertyAccessorMethodFormat.FLUENT, Constant.MULTIPLE_FIELD_OBJECT_PROPERTY_ACCESSORS)
                , Arguments.of(TypeToken.of(NoFieldObject.class), PropertyAccessorMethodFormat.JAVA_BEAN, ImmutableSet.of())
                , Arguments.of(TypeToken.of(NoFieldObject.class), PropertyAccessorMethodFormat.FLUENT, ImmutableSet.of())
                , Arguments.of(TypeToken.of(PrivateConstructObject.class), PropertyAccessorMethodFormat.JAVA_BEAN, ImmutableSet.of())
                , Arguments.of(TypeToken.of(PrivateConstructObject.class), PropertyAccessorMethodFormat.FLUENT, ImmutableSet.of())
                , Arguments.of(TypeToken.of(ProtectedConstructChildrenObject.class), PropertyAccessorMethodFormat.JAVA_BEAN, ImmutableSet.of())
                , Arguments.of(TypeToken.of(ProtectedConstructChildrenObject.class), PropertyAccessorMethodFormat.FLUENT, ImmutableSet.of())
                , Arguments.of(TypeToken.of(ProtectedConstructObject.class), PropertyAccessorMethodFormat.JAVA_BEAN, ImmutableSet.of())
                , Arguments.of(TypeToken.of(ProtectedConstructObject.class), PropertyAccessorMethodFormat.FLUENT, ImmutableSet.of())
                , Arguments.of(TypeToken.of(StaticFieldObject.class), PropertyAccessorMethodFormat.JAVA_BEAN, ImmutableSet.of())
                , Arguments.of(TypeToken.of(StaticFieldObject.class), PropertyAccessorMethodFormat.FLUENT, ImmutableSet.of())
                , Arguments.of(TypeToken.of(StringFieldAndStringFieldGetMethodObject.class), PropertyAccessorMethodFormat.JAVA_BEAN, Constant.STRING_FIELD_AND_STRING_FIELD_GET_METHOD_OBJECT_PROPERTY_READERS)
                , Arguments.of(TypeToken.of(StringFieldAndStringFieldGetMethodObject.class), PropertyAccessorMethodFormat.FLUENT, ImmutableSet.of(Constant.STRING_FIELD_AND_STRING_FIELD_GET_METHOD_OBJECT_STRING_FIELD_PROPERTY_ACCESSOR))
                , Arguments.of(TypeToken.of(StringFieldAndStringFieldSetMethodObject.class), PropertyAccessorMethodFormat.JAVA_BEAN, ImmutableSet.of(Constant.STRING_FIELD_AND_STRING_FIELD_SET_METHOD_OBJECT_STRING_FIELD_PROPERTY_ACCESSOR, Constant.STRING_FIELD_AND_STRING_FIELD_SET_METHOD_OBJECT_SET_STRING_FIELD_METHOD_PROPERTY_WRITER))
                , Arguments.of(TypeToken.of(StringFieldAndStringFieldSetMethodObject.class), PropertyAccessorMethodFormat.FLUENT, ImmutableSet.of(Constant.STRING_FIELD_AND_STRING_FIELD_SET_METHOD_OBJECT_STRING_FIELD_PROPERTY_ACCESSOR))
                , Arguments.of(TypeToken.of(StringFieldChainSetMethodObject.class), PropertyAccessorMethodFormat.JAVA_BEAN, ImmutableSet.of(Constant.STRING_FIELD_CHAIN_SET_METHOD_OBJECT_SET_STRING_FIELD_METHOD_PROPERTY_WRITER))
                , Arguments.of(TypeToken.of(StringFieldChainSetMethodObject.class), PropertyAccessorMethodFormat.FLUENT, ImmutableSet.of())
                , Arguments.of(TypeToken.of(StringFieldFluentGetMethodObject.class), PropertyAccessorMethodFormat.JAVA_BEAN, ImmutableSet.of())
                , Arguments.of(TypeToken.of(StringFieldFluentGetMethodObject.class), PropertyAccessorMethodFormat.FLUENT, ImmutableSet.of(Constant.STRING_FIELD_FLUENT_GET_METHOD_OBJECT_STRING_FIELD_PROPERTY_READER))
                , Arguments.of(TypeToken.of(StringFieldFluentSetMethodObject.class), PropertyAccessorMethodFormat.JAVA_BEAN, ImmutableSet.of())
                , Arguments.of(TypeToken.of(StringFieldFluentSetMethodObject.class), PropertyAccessorMethodFormat.FLUENT, ImmutableSet.of(Constant.STRING_FIELD_FLUENT_SET_METHOD_OBJECT_STRING_FIELD_METHOD_PROPERTY_WRITER))
                , Arguments.of(TypeToken.of(StringFieldGetMethodObject.class), PropertyAccessorMethodFormat.JAVA_BEAN, ImmutableSet.of(Constant.STRING_FIELD_GET_METHOD_OBJECT_STRING_FIELD_PROPERTY_READER))
                , Arguments.of(TypeToken.of(StringFieldGetMethodObject.class), PropertyAccessorMethodFormat.FLUENT, ImmutableSet.of())
                , Arguments.of(TypeToken.of(StringFieldGetter.class), PropertyAccessorMethodFormat.JAVA_BEAN, ImmutableSet.of(Constant.STRING_FIELD_GETTER_STRING_FIELD_PROPERTY_READER))
                , Arguments.of(TypeToken.of(StringFieldGetter.class), PropertyAccessorMethodFormat.FLUENT, ImmutableSet.of())
                , Arguments.of(TypeToken.of(StringFieldGetterObject.class), PropertyAccessorMethodFormat.JAVA_BEAN, Constant.STRING_FIELD_GETTER_OBJECT_PROPERTY_READERS)
                , Arguments.of(TypeToken.of(StringFieldGetterObject.class), PropertyAccessorMethodFormat.FLUENT, ImmutableSet.of())
                , Arguments.of(TypeToken.of(StringFieldObject.class), PropertyAccessorMethodFormat.JAVA_BEAN, ImmutableSet.of(Constant.STRING_FIELD_OBJECT_STRING_FIELD_PROPERTY_ACCESSOR))
                , Arguments.of(TypeToken.of(StringFieldObject.class), PropertyAccessorMethodFormat.FLUENT, ImmutableSet.of(Constant.STRING_FIELD_OBJECT_STRING_FIELD_PROPERTY_ACCESSOR))
                , Arguments.of(TypeToken.of(StringFieldSetMethodObject.class), PropertyAccessorMethodFormat.JAVA_BEAN, ImmutableSet.of(Constant.STRING_FIELD_SET_METHOD_OBJECT_SET_STRING_FIELD_METHOD_PROPERTY_WRITER))
                , Arguments.of(TypeToken.of(StringFieldSetMethodObject.class), PropertyAccessorMethodFormat.FLUENT, ImmutableSet.of())
                , Arguments.of(TypeToken.of(StringFieldSetter.class), PropertyAccessorMethodFormat.JAVA_BEAN, ImmutableSet.of(Constant.STRING_FIELD_SETTER_SET_STRING_FIELD_METHOD_PROPERTY_WRITER))
                , Arguments.of(TypeToken.of(StringFieldSetter.class), PropertyAccessorMethodFormat.FLUENT, ImmutableSet.of())
                , Arguments.of(TypeToken.of(StringFieldSetterObject.class), PropertyAccessorMethodFormat.JAVA_BEAN, Constant.STRING_FIELD_SETTER_OBJECT_PROPERTY_WRITERS)
                , Arguments.of(TypeToken.of(StringFieldSetterObject.class), PropertyAccessorMethodFormat.FLUENT, ImmutableSet.of()));
    }

    static Stream<Arguments> classAndPropertyElementSets() {
        return typeTokenAndPropertyElementSets()
                .filter(arguments -> ((TypeToken<?>) arguments.get()[0]).equals(TypeToken.of(((TypeToken<?>) arguments.get()[0]).getRawType())))
                .map(arguments -> Arguments.of(((TypeToken<?>) arguments.get()[0]).getRawType(), arguments.get()[1]));
    }

    static Stream<Arguments> typeTokenAndPropertyElementSets() {
        return typeTokenAndPropertyAccessorMethodFormatAndPropertyElementSets()
                .collect(ImmutableMap.toImmutableMap(arguments -> arguments.get()[0], Function.identity()
                        , (arguments1, arguments2) ->
                                Arguments.of(arguments1.get()[0], Stream.of(arguments1.get()[2], arguments2.get()[2]).map(a -> (Set<?>) a).flatMap(Collection::stream).collect(ImmutableSet.toImmutableSet()))
                ))
                .values().stream();
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
        return typeTokenAndPropertyAccessorMethodFormatArrayAndPropertySets()
                .filter(arguments -> arguments.get()[0].equals(TypeToken.of(((TypeToken<?>) arguments.get()[0]).getRawType())))
                .map(arguments -> Arguments.of(((TypeToken<?>) arguments.get()[0]).getRawType(), arguments.get()[1], arguments.get()[2]));
    }

    static Stream<Arguments> typeTokenAndPropertyAccessorMethodFormatArrayAndPropertySets() {
        return Stream.of(Arguments.of(TypeToken.of(BooleanFieldGetMethodObject.class), JAVA_BEAN, ImmutableSet.of(Constant.BOOLEAN_FIELD_GET_METHOD_OBJECT_BOOLEAN_PROPERTY))
                , Arguments.of(TypeToken.of(BooleanFieldGetMethodObject.class), FLUENT, ImmutableSet.of())
                , Arguments.of(TypeToken.of(BooleanFieldGetMethodObject.class), PropertyAccessorMethodFormat.values(), ImmutableSet.of(Constant.BOOLEAN_FIELD_GET_METHOD_OBJECT_BOOLEAN_PROPERTY))
                , Arguments.of(TypeToken.of(BooleanFieldIsMethodObject.class), JAVA_BEAN, ImmutableSet.of(Constant.BOOLEAN_FIELD_IS_METHOD_OBJECT_BOOLEAN_PROPERTY))
                , Arguments.of(TypeToken.of(BooleanFieldIsMethodObject.class), FLUENT, ImmutableSet.of())
                , Arguments.of(TypeToken.of(BooleanFieldIsMethodObject.class), PropertyAccessorMethodFormat.values(), ImmutableSet.of(Constant.BOOLEAN_FIELD_IS_METHOD_OBJECT_BOOLEAN_PROPERTY))
                , Arguments.of(TypeToken.of(ChildrenLongFieldObject.class), JAVA_BEAN, Constant.CHILDREN_LONG_FIELD_OBJECT_PROPERTIES)
                , Arguments.of(TypeToken.of(ChildrenLongFieldObject.class), FLUENT, Constant.CHILDREN_LONG_FIELD_OBJECT_PROPERTIES)
                , Arguments.of(TypeToken.of(ChildrenLongFieldObject.class), PropertyAccessorMethodFormat.values(), Constant.CHILDREN_LONG_FIELD_OBJECT_PROPERTIES)
                , Arguments.of(TypeToken.of(ChildrenSameNameFieldObject.class), JAVA_BEAN, ImmutableSet.of(Constant.CHILDREN_SAME_NAME_FIELD_OBJECT_STRING_PROPERTY))
                , Arguments.of(TypeToken.of(ChildrenSameNameFieldObject.class), FLUENT, ImmutableSet.of(Constant.CHILDREN_SAME_NAME_FIELD_OBJECT_STRING_PROPERTY))
                , Arguments.of(TypeToken.of(ChildrenSameNameFieldObject.class), PropertyAccessorMethodFormat.values(), ImmutableSet.of(Constant.CHILDREN_SAME_NAME_FIELD_OBJECT_STRING_PROPERTY))
                , Arguments.of(TypeToken.of(DoubleGenericFieldObject.class), JAVA_BEAN, ImmutableSet.of(Constant.DOUBLE_GENERIC_FIELD_OBJECT_GENERIC_PROPERTY))
                , Arguments.of(TypeToken.of(DoubleGenericFieldObject.class), FLUENT, ImmutableSet.of(Constant.DOUBLE_GENERIC_FIELD_OBJECT_GENERIC_PROPERTY))
                , Arguments.of(TypeToken.of(DoubleGenericFieldObject.class), PropertyAccessorMethodFormat.values(), ImmutableSet.of(Constant.DOUBLE_GENERIC_FIELD_OBJECT_GENERIC_PROPERTY))
                , Arguments.of(TypeToken.of(DuplicateNumberFieldSetMethodObject.class), JAVA_BEAN, ImmutableSet.of(Constant.DUPLICATE_NUMBER_FIELD_SET_METHOD_OBJECT_NUMBER_PROPERTY))
                , Arguments.of(TypeToken.of(DuplicateNumberFieldSetMethodObject.class), FLUENT, ImmutableSet.of())
                , Arguments.of(TypeToken.of(DuplicateNumberFieldSetMethodObject.class), PropertyAccessorMethodFormat.values(), ImmutableSet.of(Constant.DUPLICATE_NUMBER_FIELD_SET_METHOD_OBJECT_NUMBER_PROPERTY))
                , Arguments.of(TypeToken.of(FinalFieldObject.class), JAVA_BEAN, ImmutableSet.of(Constant.FINAL_FIELD_OBJECT_STRING_PROPERTY))
                , Arguments.of(TypeToken.of(FinalFieldObject.class), FLUENT, ImmutableSet.of(Constant.FINAL_FIELD_OBJECT_STRING_PROPERTY))
                , Arguments.of(TypeToken.of(FinalFieldObject.class), PropertyAccessorMethodFormat.values(), ImmutableSet.of(Constant.FINAL_FIELD_OBJECT_STRING_PROPERTY))
                , Arguments.of(TypeToken.of(GenericFieldGetter.class), JAVA_BEAN, ImmutableSet.of(ReadableProperty.create(ImmutableSet.of(PropertyReader.from(GenericFieldGetter.class, Constant.GENERIC_FIELD_GETTER_GET_GENERIC_FIELD_METHOD, "genericField")))))
                , Arguments.of(TypeToken.of(GenericFieldGetter.class), FLUENT, ImmutableSet.of())
                , Arguments.of(TypeToken.of(GenericFieldGetter.class), PropertyAccessorMethodFormat.values(), ImmutableSet.of(ReadableProperty.create(ImmutableSet.of(PropertyReader.from(GenericFieldGetter.class, Constant.GENERIC_FIELD_GETTER_GET_GENERIC_FIELD_METHOD, "genericField")))))
                , Arguments.of(new TypeToken<GenericFieldGetter<Object>>() {
                }, JAVA_BEAN, ImmutableSet.of(Constant.GENERIC_FIELD_GETTER_GENERIC_PROPERTY))
                , Arguments.of(new TypeToken<GenericFieldGetter<Object>>() {
                }, FLUENT, ImmutableSet.of())
                , Arguments.of(new TypeToken<GenericFieldGetter<Object>>() {
                }, PropertyAccessorMethodFormat.values(), ImmutableSet.of(Constant.GENERIC_FIELD_GETTER_GENERIC_PROPERTY))
                , Arguments.of(TypeToken.of(GenericFieldObject.class), JAVA_BEAN, ImmutableSet.of(AccessibleProperty.create(ImmutableSet.of(PropertyAccessor.from(GenericFieldObject.class, Constant.GENERIC_FIELD_OBJECT_GENERIC_FIELD)))))
                , Arguments.of(TypeToken.of(GenericFieldObject.class), FLUENT, ImmutableSet.of(AccessibleProperty.create(ImmutableSet.of(PropertyAccessor.from(GenericFieldObject.class, Constant.GENERIC_FIELD_OBJECT_GENERIC_FIELD)))))
                , Arguments.of(TypeToken.of(GenericFieldObject.class), PropertyAccessorMethodFormat.values(), ImmutableSet.of(AccessibleProperty.create(ImmutableSet.of(PropertyAccessor.from(GenericFieldObject.class, Constant.GENERIC_FIELD_OBJECT_GENERIC_FIELD)))))
                , Arguments.of(new TypeToken<GenericFieldObject<Object>>() {
                }, JAVA_BEAN, ImmutableSet.of(Constant.GENERIC_FIELD_OBJECT_GENERIC_PROPERTY))
                , Arguments.of(new TypeToken<GenericFieldObject<Object>>() {
                }, FLUENT, ImmutableSet.of(Constant.GENERIC_FIELD_OBJECT_GENERIC_PROPERTY))
                , Arguments.of(new TypeToken<GenericFieldObject<Object>>() {
                }, PropertyAccessorMethodFormat.values(), ImmutableSet.of(Constant.GENERIC_FIELD_OBJECT_GENERIC_PROPERTY))
                , Arguments.of(TypeToken.of(GenericFieldSetter.class), JAVA_BEAN, ImmutableSet.of(WritableProperty.create(ImmutableSet.of(PropertyWriter.from(GenericFieldSetter.class, Constant.GENERIC_FIELD_SETTER_SET_GENERIC_FIELD_METHOD, "genericField")))))
                , Arguments.of(TypeToken.of(GenericFieldSetter.class), FLUENT, ImmutableSet.of())
                , Arguments.of(TypeToken.of(GenericFieldSetter.class), PropertyAccessorMethodFormat.values(), ImmutableSet.of(WritableProperty.create(ImmutableSet.of(PropertyWriter.from(GenericFieldSetter.class, Constant.GENERIC_FIELD_SETTER_SET_GENERIC_FIELD_METHOD, "genericField")))))
                , Arguments.of(new TypeToken<GenericFieldSetter<Object>>() {
                }, JAVA_BEAN, ImmutableSet.of(Constant.GENERIC_FIELD_SETTER_GENERIC_PROPERTY))
                , Arguments.of(new TypeToken<GenericFieldSetter<Object>>() {
                }, FLUENT, ImmutableSet.of())
                , Arguments.of(new TypeToken<GenericFieldSetter<Object>>() {
                }, PropertyAccessorMethodFormat.values(), ImmutableSet.of(Constant.GENERIC_FIELD_SETTER_GENERIC_PROPERTY))
                , Arguments.of(TypeToken.of(IntegerGenericFieldGetter.class), JAVA_BEAN, ImmutableSet.of(Constant.INTEGER_GENERIC_FIELD_GETTER_GENERIC_PROPERTY))
                , Arguments.of(TypeToken.of(IntegerGenericFieldGetter.class), FLUENT, ImmutableSet.of())
                , Arguments.of(TypeToken.of(IntegerGenericFieldGetter.class), PropertyAccessorMethodFormat.values(), ImmutableSet.of(Constant.INTEGER_GENERIC_FIELD_GETTER_GENERIC_PROPERTY))
                , Arguments.of(TypeToken.of(LongGenericFieldSetter.class), JAVA_BEAN, ImmutableSet.of(Constant.LONG_GENERIC_FIELD_SETTER_GENERIC_PROPERTY))
                , Arguments.of(TypeToken.of(LongGenericFieldSetter.class), FLUENT, ImmutableSet.of())
                , Arguments.of(TypeToken.of(LongGenericFieldSetter.class), PropertyAccessorMethodFormat.values(), ImmutableSet.of(Constant.LONG_GENERIC_FIELD_SETTER_GENERIC_PROPERTY))
                , Arguments.of(TypeToken.of(MultipleConstructObject.class), JAVA_BEAN, ImmutableSet.of())
                , Arguments.of(TypeToken.of(MultipleConstructObject.class), FLUENT, ImmutableSet.of())
                , Arguments.of(TypeToken.of(MultipleConstructObject.class), PropertyAccessorMethodFormat.values(), ImmutableSet.of())
                , Arguments.of(TypeToken.of(MultipleFieldObject.class), JAVA_BEAN, Constant.MULTIPLE_FIELD_OBJECT_PROPERTIES)
                , Arguments.of(TypeToken.of(MultipleFieldObject.class), FLUENT, Constant.MULTIPLE_FIELD_OBJECT_PROPERTIES)
                , Arguments.of(TypeToken.of(MultipleFieldObject.class), PropertyAccessorMethodFormat.values(), Constant.MULTIPLE_FIELD_OBJECT_PROPERTIES)
                , Arguments.of(TypeToken.of(NoFieldObject.class), JAVA_BEAN, ImmutableSet.of())
                , Arguments.of(TypeToken.of(NoFieldObject.class), FLUENT, ImmutableSet.of())
                , Arguments.of(TypeToken.of(NoFieldObject.class), PropertyAccessorMethodFormat.values(), ImmutableSet.of())
                , Arguments.of(TypeToken.of(PrivateConstructObject.class), JAVA_BEAN, ImmutableSet.of())
                , Arguments.of(TypeToken.of(PrivateConstructObject.class), FLUENT, ImmutableSet.of())
                , Arguments.of(TypeToken.of(PrivateConstructObject.class), PropertyAccessorMethodFormat.values(), ImmutableSet.of())
                , Arguments.of(TypeToken.of(ProtectedConstructChildrenObject.class), JAVA_BEAN, ImmutableSet.of())
                , Arguments.of(TypeToken.of(ProtectedConstructChildrenObject.class), FLUENT, ImmutableSet.of())
                , Arguments.of(TypeToken.of(ProtectedConstructChildrenObject.class), PropertyAccessorMethodFormat.values(), ImmutableSet.of())
                , Arguments.of(TypeToken.of(ProtectedConstructObject.class), JAVA_BEAN, ImmutableSet.of())
                , Arguments.of(TypeToken.of(ProtectedConstructObject.class), FLUENT, ImmutableSet.of())
                , Arguments.of(TypeToken.of(ProtectedConstructObject.class), PropertyAccessorMethodFormat.values(), ImmutableSet.of())
                , Arguments.of(TypeToken.of(StaticFieldObject.class), JAVA_BEAN, ImmutableSet.of())
                , Arguments.of(TypeToken.of(StaticFieldObject.class), FLUENT, ImmutableSet.of())
                , Arguments.of(TypeToken.of(StaticFieldObject.class), PropertyAccessorMethodFormat.values(), ImmutableSet.of())
                , Arguments.of(TypeToken.of(StringFieldAndStringFieldGetMethodObject.class), JAVA_BEAN, ImmutableSet.of(Constant.STRING_FIELD_AND_STRING_FIELD_GET_METHOD_OBJECT_STRING_PROPERTY))
                , Arguments.of(TypeToken.of(StringFieldAndStringFieldGetMethodObject.class), FLUENT, ImmutableSet.of(AccessibleProperty.create(ImmutableSet.of(Constant.STRING_FIELD_AND_STRING_FIELD_GET_METHOD_OBJECT_STRING_FIELD_PROPERTY_ACCESSOR))))
                , Arguments.of(TypeToken.of(StringFieldAndStringFieldGetMethodObject.class), PropertyAccessorMethodFormat.values(), ImmutableSet.of(Constant.STRING_FIELD_AND_STRING_FIELD_GET_METHOD_OBJECT_STRING_PROPERTY))
                , Arguments.of(TypeToken.of(StringFieldAndStringFieldSetMethodObject.class), JAVA_BEAN, ImmutableSet.of(Constant.STRING_FIELD_AND_STRING_FIELD_SET_METHOD_OBJECT_STRING_PROPERTY))
                , Arguments.of(TypeToken.of(StringFieldAndStringFieldSetMethodObject.class), FLUENT, ImmutableSet.of(AccessibleProperty.create(ImmutableSet.of(Constant.STRING_FIELD_AND_STRING_FIELD_SET_METHOD_OBJECT_STRING_FIELD_PROPERTY_ACCESSOR))))
                , Arguments.of(TypeToken.of(StringFieldAndStringFieldSetMethodObject.class), PropertyAccessorMethodFormat.values(), ImmutableSet.of(Constant.STRING_FIELD_AND_STRING_FIELD_SET_METHOD_OBJECT_STRING_PROPERTY))
                , Arguments.of(TypeToken.of(StringFieldChainSetMethodObject.class), JAVA_BEAN, ImmutableSet.of(Constant.STRING_FIELD_CHAIN_SET_METHOD_OBJECT_STRING_PROPERTY))
                , Arguments.of(TypeToken.of(StringFieldChainSetMethodObject.class), FLUENT, ImmutableSet.of())
                , Arguments.of(TypeToken.of(StringFieldChainSetMethodObject.class), PropertyAccessorMethodFormat.values(), ImmutableSet.of(Constant.STRING_FIELD_CHAIN_SET_METHOD_OBJECT_STRING_PROPERTY))
                , Arguments.of(TypeToken.of(StringFieldFluentGetMethodObject.class), JAVA_BEAN, ImmutableSet.of())
                , Arguments.of(TypeToken.of(StringFieldFluentGetMethodObject.class), FLUENT, ImmutableSet.of(Constant.STRING_FIELD_FLUENT_GET_METHOD_OBJECT_STRING_PROPERTY))
                , Arguments.of(TypeToken.of(StringFieldFluentGetMethodObject.class), PropertyAccessorMethodFormat.values(), ImmutableSet.of(Constant.STRING_FIELD_FLUENT_GET_METHOD_OBJECT_STRING_PROPERTY))
                , Arguments.of(TypeToken.of(StringFieldFluentSetMethodObject.class), JAVA_BEAN, ImmutableSet.of())
                , Arguments.of(TypeToken.of(StringFieldFluentSetMethodObject.class), FLUENT, ImmutableSet.of(Constant.STRING_FIELD_FLUENT_SET_METHOD_OBJECT_STRING_PROPERTY))
                , Arguments.of(TypeToken.of(StringFieldFluentSetMethodObject.class), PropertyAccessorMethodFormat.values(), ImmutableSet.of(Constant.STRING_FIELD_FLUENT_SET_METHOD_OBJECT_STRING_PROPERTY))
                , Arguments.of(TypeToken.of(StringFieldGetMethodObject.class), JAVA_BEAN, ImmutableSet.of(Constant.STRING_FIELD_GET_METHOD_OBJECT_STRING_PROPERTY))
                , Arguments.of(TypeToken.of(StringFieldGetMethodObject.class), FLUENT, ImmutableSet.of())
                , Arguments.of(TypeToken.of(StringFieldGetMethodObject.class), PropertyAccessorMethodFormat.values(), ImmutableSet.of(Constant.STRING_FIELD_GET_METHOD_OBJECT_STRING_PROPERTY))
                , Arguments.of(TypeToken.of(StringFieldGetter.class), JAVA_BEAN, ImmutableSet.of(Constant.STRING_FIELD_GETTER_STRING_PROPERTY))
                , Arguments.of(TypeToken.of(StringFieldGetter.class), FLUENT, ImmutableSet.of())
                , Arguments.of(TypeToken.of(StringFieldGetter.class), PropertyAccessorMethodFormat.values(), ImmutableSet.of(Constant.STRING_FIELD_GETTER_STRING_PROPERTY))
                , Arguments.of(TypeToken.of(StringFieldGetterObject.class), JAVA_BEAN, ImmutableSet.of(Constant.STRING_FIELD_GETTER_OBJECT_STRING_PROPERTY))
                , Arguments.of(TypeToken.of(StringFieldGetterObject.class), FLUENT, ImmutableSet.of())
                , Arguments.of(TypeToken.of(StringFieldGetterObject.class), PropertyAccessorMethodFormat.values(), ImmutableSet.of(Constant.STRING_FIELD_GETTER_OBJECT_STRING_PROPERTY))
                , Arguments.of(TypeToken.of(StringFieldObject.class), JAVA_BEAN, ImmutableSet.of(Constant.STRING_FIELD_OBJECT_STRING_PROPERTY))
                , Arguments.of(TypeToken.of(StringFieldObject.class), FLUENT, ImmutableSet.of(Constant.STRING_FIELD_OBJECT_STRING_PROPERTY))
                , Arguments.of(TypeToken.of(StringFieldObject.class), PropertyAccessorMethodFormat.values(), ImmutableSet.of(Constant.STRING_FIELD_OBJECT_STRING_PROPERTY))
                , Arguments.of(TypeToken.of(StringFieldSetMethodObject.class), JAVA_BEAN, ImmutableSet.of(Constant.STRING_FIELD_SET_METHOD_OBJECT_STRING_PROPERTY))
                , Arguments.of(TypeToken.of(StringFieldSetMethodObject.class), FLUENT, ImmutableSet.of())
                , Arguments.of(TypeToken.of(StringFieldSetMethodObject.class), PropertyAccessorMethodFormat.values(), ImmutableSet.of(Constant.STRING_FIELD_SET_METHOD_OBJECT_STRING_PROPERTY))
                , Arguments.of(TypeToken.of(StringFieldSetter.class), JAVA_BEAN, ImmutableSet.of(Constant.STRING_FIELD_SETTER_STRING_PROPERTY))
                , Arguments.of(TypeToken.of(StringFieldSetter.class), FLUENT, ImmutableSet.of())
                , Arguments.of(TypeToken.of(StringFieldSetter.class), PropertyAccessorMethodFormat.values(), ImmutableSet.of(Constant.STRING_FIELD_SETTER_STRING_PROPERTY))
                , Arguments.of(TypeToken.of(StringFieldSetterObject.class), JAVA_BEAN, ImmutableSet.of(Constant.STRING_FIELD_SETTER_OBJECT_STRING_PROPERTY))
                , Arguments.of(TypeToken.of(StringFieldSetterObject.class), FLUENT, ImmutableSet.of())
                , Arguments.of(TypeToken.of(StringFieldSetterObject.class), PropertyAccessorMethodFormat.values(), ImmutableSet.of(Constant.STRING_FIELD_SETTER_OBJECT_STRING_PROPERTY)));
    }

    static Stream<Arguments> classAndPropertySets() {
        return typeTokenAndPropertySets()
                .filter(arguments -> arguments.get()[0].equals(TypeToken.of(((TypeToken<?>) arguments.get()[0]).getRawType())))
                .map(arguments -> Arguments.of(((TypeToken<?>) arguments.get()[0]).getRawType(), arguments.get()[1]));
    }

    static Stream<Arguments> typeTokenAndPropertySets() {
        return typeTokenAndPropertyAccessorMethodFormatArrayAndPropertySets()
                .filter(arguments -> Arrays.equals((PropertyAccessorMethodFormat[]) arguments.get()[1], PropertyAccessorMethodFormat.values()))
                .map(arguments -> Arguments.of(arguments.get()[0], arguments.get()[2]));
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

    static Stream<Field> fields() {
        return Stream.of(Constant.STRING_FIELD_OBJECT_STRING_FIELD, Constant.FINAL_FIELD_OBJECT_STRING_FIELD
                , Constant.GENERIC_FIELD_OBJECT_GENERIC_FIELD
                , Constant.STATIC_FIELD_OBJECT_STRING_FIELD, Constant.CHILDREN_LONG_FIELD_OBJECT_LONG_FIELD
                , Constant.CHILDREN_SAME_NAME_FIELD_OBJECT_STRING_FIELD, Constant.MULTIPLE_FIELD_OBJECT_STRING_FIELD
                , Constant.MULTIPLE_FIELD_OBJECT_INTEGER_FIELD, Constant.MULTIPLE_FIELD_OBJECT_INT_FIELD
                , Constant.MULTIPLE_FIELD_OBJECT_BOOLEAN_FIELD, Constant.STRING_FIELD_AND_STRING_FIELD_GET_METHOD_OBJECT_STRING_FIELD
                , Constant.STRING_FIELD_AND_STRING_FIELD_SET_METHOD_OBJECT_STRING_FIELD);
    }

    static Stream<Field> staticFields() {
        return fields().filter(field -> Modifier.isStatic(field.getModifiers()));
    }

    static Stream<Field> unstaticFields() {
        return fields().filter(field -> !Modifier.isStatic(field.getModifiers()));
    }

    static Stream<Method> propertyElementMethods() {
        return propertyElementMethodAndPropertyAccessorMethodFormats()
                .map(arguments -> (Method) arguments.get()[0]);
    }

    static Stream<Arguments> propertyElementMethodAndPropertyAccessorMethodFormats() {
        return Stream.concat(propertyReaderMethodAndPropertyAccessorMethodFormats(), propertyWriterMethodAndPropertyAccessorMethodFormats());
    }

    static Stream<Method> propertyReaderMethods() {
        return propertyReaderMethodAndPropertyAccessorMethodFormats()
                .map(arguments -> (Method) arguments.get()[0]);
    }

    static Stream<Arguments> propertyReaderMethodAndPropertyAccessorMethodFormats() {
        return Stream.of(Arguments.of(Constant.GENERIC_FIELD_GETTER_GET_GENERIC_FIELD_METHOD, PropertyAccessorMethodFormat.JAVA_BEAN)
                , Arguments.of(Constant.INTEGER_GENERIC_FIELD_GETTER_GET_GENERIC_FIELD_METHOD, PropertyAccessorMethodFormat.JAVA_BEAN)
                , Arguments.of(Constant.INTEGER_GENERIC_FIELD_GETTER_BRIDGE_GET_GENERIC_FIELD_METHOD, PropertyAccessorMethodFormat.JAVA_BEAN)
                , Arguments.of(Constant.STRING_FIELD_GET_METHOD_OBJECT_GET_STRING_FIELD_METHOD, PropertyAccessorMethodFormat.JAVA_BEAN)
                , Arguments.of(Constant.STRING_FIELD_FLUENT_GET_METHOD_OBJECT_STRING_FIELD_METHOD, PropertyAccessorMethodFormat.FLUENT)
                , Arguments.of(Constant.STRING_FIELD_AND_STRING_FIELD_GET_METHOD_OBJECT_GET_STRING_FIELD_METHOD, PropertyAccessorMethodFormat.JAVA_BEAN)
                , Arguments.of(Constant.STRING_FIELD_GETTER_GET_STRING_FIELD_METHOD, PropertyAccessorMethodFormat.JAVA_BEAN)
                , Arguments.of(Constant.STRING_FIELD_GETTER_OBJECT_GET_STRING_FIELD_METHOD, PropertyAccessorMethodFormat.JAVA_BEAN)
                , Arguments.of(Constant.BOOLEAN_FIELD_IS_METHOD_OBJECT_IS_BOOLEAN_FIELD_METHOD, PropertyAccessorMethodFormat.JAVA_BEAN)
                , Arguments.of(Constant.BOOLEAN_FIELD_GET_METHOD_OBJECT_GET_BOOLEAN_FIELD_METHOD, PropertyAccessorMethodFormat.JAVA_BEAN));
    }

    static Stream<Method> propertyWriterMethods() {
        return propertyWriterMethodAndPropertyAccessorMethodFormats()
                .map(arguments -> (Method) arguments.get()[0]);
    }

    static Stream<Arguments> propertyWriterMethodAndPropertyAccessorMethodFormats() {
        return Stream.of(Arguments.of(Constant.STRING_FIELD_SET_METHOD_OBJECT_SET_STRING_FIELD_METHOD, PropertyAccessorMethodFormat.JAVA_BEAN)
                , Arguments.of(Constant.STRING_FIELD_FLUENT_SET_METHOD_OBJECT_STRING_FIELD_METHOD, PropertyAccessorMethodFormat.FLUENT)
                , Arguments.of(Constant.STRING_FIELD_AND_STRING_FIELD_SET_METHOD_OBJECT_SET_STRING_FIELD_METHOD, PropertyAccessorMethodFormat.JAVA_BEAN)
                , Arguments.of(Constant.GENERIC_FIELD_SETTER_SET_GENERIC_FIELD_METHOD, PropertyAccessorMethodFormat.JAVA_BEAN)
                , Arguments.of(Constant.LONG_GENERIC_FIELD_SETTER_SET_GENERIC_FIELD_METHOD, PropertyAccessorMethodFormat.JAVA_BEAN)
                , Arguments.of(Constant.LONG_GENERIC_FIELD_SETTER_BRIDGE_SET_GENERIC_FIELD_METHOD, PropertyAccessorMethodFormat.JAVA_BEAN)
                , Arguments.of(Constant.STRING_FIELD_SETTER_SET_STRING_FIELD_METHOD, PropertyAccessorMethodFormat.JAVA_BEAN)
                , Arguments.of(Constant.STRING_FIELD_SETTER_OBJECT_SET_STRING_FIELD_METHOD, PropertyAccessorMethodFormat.JAVA_BEAN)
                , Arguments.of(Constant.STRING_FIELD_CHAIN_SET_METHOD_OBJECT_SET_STRING_FIELD_METHOD, PropertyAccessorMethodFormat.JAVA_BEAN)
                , Arguments.of(Constant.DUPLICATE_NUMBER_FIELD_SET_METHOD_OBJECT_NUMBER_SET_NUMBER_FIELD_METHOD, PropertyAccessorMethodFormat.JAVA_BEAN)
                , Arguments.of(Constant.DUPLICATE_NUMBER_FIELD_SET_METHOD_OBJECT_INT_SET_NUMBER_FIELD_METHOD, PropertyAccessorMethodFormat.JAVA_BEAN));
    }

    static Stream<Method> methods() {
        return classes()
                .map(Class::getDeclaredMethods)
                .flatMap(Arrays::stream);
    }

    static Stream<Method> getMethods() {
        return propertyReaderMethodAndPropertyAccessorMethodFormats()
                .filter(arguments -> PropertyAccessorMethodFormat.JAVA_BEAN == arguments.get()[1])
                .map(arguments -> (Method) arguments.get()[0]);
    }

    static Stream<Method> setMethods() {
        return propertyWriterMethodAndPropertyAccessorMethodFormats()
                .filter(arguments -> PropertyAccessorMethodFormat.JAVA_BEAN == arguments.get()[1])
                .map(arguments -> (Method) arguments.get()[0]);
    }

    static Stream<Method> notGetMethods() {
        return methods().filter(method -> getMethods().noneMatch(method::equals));
    }

    static Stream<Method> notSetMethods() {
        return methods().filter(method -> setMethods().noneMatch(method::equals));

    }

    static Stream<Arguments> methodAndSuperMethods() {
        return Stream.of(Arguments.of(Constant.STRING_FIELD_GETTER_OBJECT_GET_STRING_FIELD_METHOD, Constant.STRING_FIELD_GETTER_GET_STRING_FIELD_METHOD)
                , Arguments.of(Constant.STRING_FIELD_SETTER_OBJECT_SET_STRING_FIELD_METHOD, Constant.STRING_FIELD_SETTER_SET_STRING_FIELD_METHOD)
                , Arguments.of(Constant.INTEGER_GENERIC_FIELD_GETTER_GET_GENERIC_FIELD_METHOD, Constant.GENERIC_FIELD_GETTER_GET_GENERIC_FIELD_METHOD)
                , Arguments.of(Constant.INTEGER_GENERIC_FIELD_GETTER_BRIDGE_GET_GENERIC_FIELD_METHOD, Constant.GENERIC_FIELD_GETTER_GET_GENERIC_FIELD_METHOD)
                , Arguments.of(Constant.LONG_GENERIC_FIELD_SETTER_SET_GENERIC_FIELD_METHOD, Constant.GENERIC_FIELD_SETTER_SET_GENERIC_FIELD_METHOD)
                , Arguments.of(Constant.LONG_GENERIC_FIELD_SETTER_BRIDGE_SET_GENERIC_FIELD_METHOD, Constant.GENERIC_FIELD_SETTER_SET_GENERIC_FIELD_METHOD));
    }

    static Stream<Arguments> methodAndNotSuperMethods() {
        return methods().flatMap(method -> methods()
                        .map(m -> Arguments.of(method, m)))
                .filter(arguments -> methodAndSuperMethods().noneMatch(a -> Arrays.equals(arguments.get(), a.get())));
    }

    static Stream<Arguments> methodAndSuperClasss() {
        return Stream.of(Arguments.of(Constant.INTEGER_GENERIC_FIELD_GETTER_GET_GENERIC_FIELD_METHOD, GenericFieldGetter.class)
                , Arguments.of(Constant.INTEGER_GENERIC_FIELD_GETTER_BRIDGE_GET_GENERIC_FIELD_METHOD, GenericFieldGetter.class)
                , Arguments.of(Constant.LONG_GENERIC_FIELD_SETTER_SET_GENERIC_FIELD_METHOD, GenericFieldSetter.class)
                , Arguments.of(Constant.LONG_GENERIC_FIELD_SETTER_BRIDGE_SET_GENERIC_FIELD_METHOD, GenericFieldSetter.class)
                , Arguments.of(Constant.STRING_FIELD_GETTER_OBJECT_GET_STRING_FIELD_METHOD, StringFieldGetter.class)
                , Arguments.of(Constant.STRING_FIELD_SETTER_OBJECT_SET_STRING_FIELD_METHOD, StringFieldSetter.class));
    }

    static Stream<Arguments> methodAndNotSuperClasss() {
        return methods().flatMap(method -> classes()
                        .map(clazz -> Arguments.of(method, clazz)))
                .filter(arguments -> methodAndSuperClasss().noneMatch(a -> Arrays.equals(arguments.get(), a.get())));
    }

    static Stream<Arguments> methodAndSuperTypeTokens() {
        return methodAndSuperClasss().map(arguments -> Arguments.of(arguments.get()[0], TypeToken.of((Class<?>) arguments.get()[1])));
    }

    static Stream<Arguments> methodAndNotSuperTypeTokens() {
        return methods().flatMap(method -> typeTokens()
                        .map(type -> Arguments.of(method, type)))
                .filter(arguments -> methodAndSuperTypeTokens().noneMatch(a -> Arrays.equals(arguments.get(), a.get())));
    }

    static Stream<Method> overrideMethods() {
        return methodAndSuperMethods().map(arguments -> (Method) arguments.get()[0]);
    }

    static Stream<Method> notOverrideMethods() {
        return methods().filter(method -> overrideMethods().noneMatch(method::equals));
    }

    static Stream<Class<?>> primitiveClasses() {
        return classes().filter(Class::isPrimitive);
    }

    static Stream<Class<?>> notPrimitiveClasses() {
        return classes().filter(clazz -> !clazz.isPrimitive());
    }

    static Stream<Class<?>> objectSubClasses() {
        return classes().filter(clazz -> clazz != Object.class && clazz.getSuperclass() == Object.class);
    }

    static Stream<Arguments> objectSubClassArrayAndLowestCommonSuperClassSets() {
        return objectSubClasses().flatMap(clazz -> objectSubClasses().filter(c -> clazz != c).map(c -> new Class[]{clazz, c})).map(classSet -> Arguments.of(classSet, ImmutableSet.of(Object.class)));
    }

    static Stream<Arguments> ContainPrimitiveClassArrayAndLowestCommonSuperClassSets() {
        return classes().flatMap(clazz -> primitiveClasses().filter(c -> clazz != c).map(c -> new Class[]{clazz, c})).map(classSet -> Arguments.of(classSet, ImmutableSet.of()));
    }

    static Stream<Arguments> classArrayAndLowestCommonSuperClassSets() {
        return Streams.concat(objectSubClassArrayAndLowestCommonSuperClassSets(), ContainPrimitiveClassArrayAndLowestCommonSuperClassSets(),
                Stream.of(Arguments.of(new Class[]{String.class, Integer.class}, ImmutableSet.of(Serializable.class, Comparable.class))
                        , Arguments.of(new Class[]{Integer.class, Long.class}, ImmutableSet.of(Number.class, Comparable.class))
                        , Arguments.of(new Class[]{Byte.class, Short.class, Integer.class, Long.class}, ImmutableSet.of(Number.class, Comparable.class))
                        , Arguments.of(new Class[]{Integer.class, Number.class}, ImmutableSet.of(Number.class))
                        , Arguments.of(new Class[]{DoubleGenericFieldObject.class, GenericFieldObject.class}, ImmutableSet.of(GenericFieldObject.class))
                        , Arguments.of(new Class[]{IntegerGenericFieldGetter.class, GenericFieldGetter.class}, ImmutableSet.of(GenericFieldGetter.class))
                        , Arguments.of(new Class[]{LongGenericFieldSetter.class, GenericFieldSetter.class}, ImmutableSet.of(GenericFieldSetter.class))
                        , Arguments.of(new Class[]{StringFieldGetterObject.class, StringFieldGetter.class}, ImmutableSet.of(StringFieldGetter.class))
                        , Arguments.of(new Class[]{StringFieldSetterObject.class, StringFieldSetter.class}, ImmutableSet.of(StringFieldSetter.class))));
    }

    static Stream<Arguments> classSetAndLowestCommonSuperClassSets() {
        return classArrayAndLowestCommonSuperClassSets().map(arguments -> Arguments.of(Arrays.stream((Class[]) arguments.get()[0]).collect(ImmutableSet.toImmutableSet()), arguments.get()[1]));
    }

    static Stream<Arguments> objectSubClassArrayAndLowestCommonAncestorSets() {
        return objectSubClassArrayAndLowestCommonSuperClassSets().map(arguments -> Arguments.of(arguments.get()[0], ((Collection<Class<?>>) arguments.get()[1]).stream().map(TypeToken::of).collect(ImmutableSet.toImmutableSet())));
    }

    static Stream<Arguments> objectSubTypeTokenArrayAndLowestCommonAncestorSets() {
        return objectSubClassArrayAndLowestCommonAncestorSets().map(arguments -> Arguments.of(Arrays.stream((Class[]) arguments.get()[0]).map(TypeToken::of).toArray(TypeToken[]::new), arguments.get()[1]));
    }

    static Stream<Arguments> ContainPrimitiveClassArrayAndLowestCommonAncestorSets() {
        return ContainPrimitiveClassArrayAndLowestCommonSuperClassSets().map(arguments -> Arguments.of(arguments.get()[0], ((Collection<Class<?>>) arguments.get()[1]).stream().map(TypeToken::of).collect(ImmutableSet.toImmutableSet())));
    }

    static Stream<Arguments> ContainPrimitiveTypeTokenArrayAndLowestCommonAncestorSets() {
        return ContainPrimitiveClassArrayAndLowestCommonAncestorSets().map(arguments -> Arguments.of(Arrays.stream((Class[]) arguments.get()[0]).map(TypeToken::of).toArray(TypeToken[]::new), arguments.get()[1]));
    }

    static Stream<Arguments> classArrayAndLowestCommonAncestorSets() {
        return typeTokenArrayAndLowestCommonAncestorSets()
                .filter(arguments -> Arrays.stream((TypeToken<?>[]) arguments.get()[0]).allMatch(type -> type.equals(TypeToken.of(type.getRawType()))))
                .map(arguments -> Arguments.of(Arrays.stream((TypeToken<?>[]) arguments.get()[0]).map(TypeToken::getRawType).toArray(Class[]::new), arguments.get()[1]));
    }

    static Stream<Arguments> typeTokenArrayAndLowestCommonAncestorSets() {
        return Streams.concat(objectSubTypeTokenArrayAndLowestCommonAncestorSets(), ContainPrimitiveTypeTokenArrayAndLowestCommonAncestorSets(),
                Stream.of(Arguments.of(new TypeToken[]{TypeToken.of(String.class), TypeToken.of(Integer.class)}, ImmutableSet.of(TypeToken.of(Serializable.class)))
                        , Arguments.of(new TypeToken[]{TypeToken.of(Integer.class), TypeToken.of(Long.class)}, ImmutableSet.of(TypeToken.of(Number.class)))
                        , Arguments.of(new TypeToken[]{TypeToken.of(Byte.class), TypeToken.of(Short.class), TypeToken.of(Integer.class), TypeToken.of(Long.class)}, ImmutableSet.of(TypeToken.of(Number.class)))
                        , Arguments.of(new TypeToken[]{TypeToken.of(Integer.class), TypeToken.of(Number.class)}, ImmutableSet.of(TypeToken.of(Number.class)))
                        , Arguments.of(new TypeToken[]{TypeToken.of(DoubleGenericFieldObject.class), TypeToken.of(GenericFieldObject.class)}, ImmutableSet.of(TypeToken.of(Object.class)))
                        , Arguments.of(new TypeToken[]{TypeToken.of(IntegerGenericFieldGetter.class), TypeToken.of(GenericFieldGetter.class)}, ImmutableSet.of(TypeToken.of(Object.class)))
                        , Arguments.of(new TypeToken[]{TypeToken.of(LongGenericFieldSetter.class), TypeToken.of(GenericFieldSetter.class)}, ImmutableSet.of(TypeToken.of(Object.class)))
                        , Arguments.of(new TypeToken[]{TypeToken.of(DoubleGenericFieldObject.class), new TypeToken<GenericFieldObject<Double>>() {
                        }}, ImmutableSet.of(new TypeToken<GenericFieldObject<Double>>() {
                        }))
                        , Arguments.of(new TypeToken[]{TypeToken.of(IntegerGenericFieldGetter.class), new TypeToken<GenericFieldGetter<Integer>>() {
                        }}, ImmutableSet.of(new TypeToken<GenericFieldGetter<Integer>>() {
                        }))
                        , Arguments.of(new TypeToken[]{TypeToken.of(LongGenericFieldSetter.class), new TypeToken<GenericFieldSetter<Long>>() {
                        }}, ImmutableSet.of(new TypeToken<GenericFieldSetter<Long>>() {
                        }))
                        , Arguments.of(new TypeToken[]{TypeToken.of(StringFieldGetterObject.class), TypeToken.of(StringFieldGetter.class)}, ImmutableSet.of(TypeToken.of(StringFieldGetter.class)))
                        , Arguments.of(new TypeToken[]{TypeToken.of(StringFieldSetterObject.class), TypeToken.of(StringFieldSetter.class)}, ImmutableSet.of(TypeToken.of(StringFieldSetter.class)))));
    }

    static Stream<Arguments> typeTokenSetAndLowestCommonAncestorSets() {
        return classArrayAndLowestCommonAncestorSets().map(arguments -> Arguments.of(Arrays.stream((Class<?>[]) arguments.get()[0]).map(TypeToken::of).collect(ImmutableSet.toImmutableSet()), arguments.get()[1]));
    }
}