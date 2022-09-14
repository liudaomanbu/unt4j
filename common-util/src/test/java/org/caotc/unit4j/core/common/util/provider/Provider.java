package org.caotc.unit4j.core.common.util.provider;

import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.TypeToken;
import lombok.experimental.UtilityClass;
import org.caotc.unit4j.core.common.util.model.*;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

/**
 * @author caotc
 * @date 2022-08-18
 * @since 1.0.0
 */
@UtilityClass
public class Provider {
    static Stream<Class<?>> classes() {
        return Stream.of(NoFieldObject.class, StringFieldObject.class, FinalFieldObject.class, StaticFieldObject.class
                , ChildrenLongFieldObject.class, ChildrenSameNameFieldObject.class, MultipleFieldObject.class
                , PrivateConstructObject.class, ProtectedConstructObject.class, ProtectedConstructChildrenObject.class
                , MultipleConstructObject.class, StringFieldGetMethodObject.class, StringFieldFluentGetMethodObject.class
                , StringFieldAndStringFieldGetMethodObject.class, StringFieldGetterObject.class, BooleanFieldIsMethodObject.class
                , BooleanFieldGetMethodObject.class, StringFieldGetter.class);
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
                , Arguments.of(StringFieldGetter.class, ImmutableSet.of()));
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
                , Arguments.of(StringFieldGetter.class, Constant.STRING_FIELD_GETTER_METHODS)
                , Arguments.of(StringFieldGetterObject.class, Constant.STRING_FIELD_GETTER_OBJECT_METHODS)
                , Arguments.of(BooleanFieldIsMethodObject.class, Constant.BOOLEAN_FIELD_IS_METHOD_OBJECT_METHODS)
                , Arguments.of(BooleanFieldGetMethodObject.class, Constant.BOOLEAN_FIELD_GET_METHOD_OBJECT_METHODS));
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
                , Arguments.of(BooleanFieldGetMethodObject.class, ImmutableSet.of(Constant.BOOLEAN_FIELD_GET_METHOD_OBJECT_CONSTRUCTOR)));
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
                , Arguments.of(FinalFieldObject.class, ImmutableSet.of())
                , Arguments.of(MultipleConstructObject.class, ImmutableSet.of())
                , Arguments.of(MultipleFieldObject.class, ImmutableSet.of())
                , Arguments.of(NoFieldObject.class, ImmutableSet.of())
                , Arguments.of(PrivateConstructObject.class, ImmutableSet.of())
                , Arguments.of(ProtectedConstructChildrenObject.class, ImmutableSet.of())
                , Arguments.of(ProtectedConstructObject.class, ImmutableSet.of())
                , Arguments.of(StaticFieldObject.class, ImmutableSet.of())
                , Arguments.of(StringFieldAndStringFieldGetMethodObject.class, ImmutableSet.of(Constant.STRING_FIELD_AND_STRING_FIELD_GET_METHOD_OBJECT_GET_STRING_FIELD_METHOD))
                , Arguments.of(StringFieldFluentGetMethodObject.class, ImmutableSet.of())
                , Arguments.of(StringFieldGetMethodObject.class, ImmutableSet.of(Constant.STRING_FIELD_GET_METHOD_OBJECT_GET_STRING_FIELD_METHOD))
                , Arguments.of(StringFieldGetter.class, ImmutableSet.of(Constant.STRING_FIELD_GETTER_GET_STRING_FIELD_METHOD))
                , Arguments.of(StringFieldGetterObject.class, Constant.STRING_FIELD_GETTER_OBJECT_GET_METHODS)
                , Arguments.of(StringFieldObject.class, ImmutableSet.of()));
    }

    static Stream<Arguments> typeTokenAndGetMethodSets() {
        return classAndGetMethodSets().map(arguments -> Arguments.of(TypeToken.of((Class<?>) arguments.get()[0]), arguments.get()[1]));
    }

    static Stream<Arguments> classAndDuplicateSetMethodFieldNames() {
        return Stream.of(Arguments.of(Sub.class, Sub.Fields.NUMBER_FIELD));
    }

    static Stream<Arguments> typeTokenAndDuplicateSetMethodFieldNames() {
        return classAndDuplicateSetMethodFieldNames().map(arguments -> Arguments.of(TypeToken.of((Class<?>) arguments.get()[0]), arguments.get()[1]));
    }

    static Stream<Arguments> classAndFieldNameAndSetMethods() {
        return Stream.of(Arguments.of(Super.class, Super.Fields.STRING_FIELD, PropertyConstant.SUPER_STRING_FIELD_SET_METHOD)
                , Arguments.of(Super.class, Super.Fields.INT_FIELD, PropertyConstant.SUPER_INT_FIELD_SET_METHOD)
                , Arguments.of(Sub.class, Sub.Fields.STRING_FIELD, PropertyConstant.SUB_STRING_FIELD_SET_METHOD)
                , Arguments.of(Sub.class, Super.Fields.INT_FIELD, PropertyConstant.SUB_INT_FIELD_SET_METHOD)
                , Arguments.of(StringFieldSetter.class, "stringField", PropertyConstant.STRING_FIELD_SETTER_STRING_FIELD_SET_METHOD));
    }

    static Stream<Arguments> typeTokenAndFieldNameAndSetMethods() {
        return classAndFieldNameAndSetMethods().map(arguments -> Arguments.of(TypeToken.of((Class<?>) arguments.get()[0]), arguments.get()[1], arguments.get()[2]));
    }

    static Stream<Arguments> classAndFieldNameAndSetMethodSets() {
        return Stream.of(Arguments.of(Super.class, Super.Fields.STRING_FIELD, PropertyConstant.SUPER_STRING_FIELD_SET_METHODS)
                , Arguments.of(Super.class, Super.Fields.INT_FIELD, ImmutableSet.of(PropertyConstant.SUPER_INT_FIELD_SET_METHOD))
                , Arguments.of(Sub.class, Sub.Fields.STRING_FIELD, PropertyConstant.SUB_STRING_FIELD_SET_METHODS)
                , Arguments.of(Sub.class, Super.Fields.INT_FIELD, PropertyConstant.SUB_INT_FIELD_SET_METHODS)
                , Arguments.of(StringFieldSetter.class, "stringField", ImmutableSet.of(PropertyConstant.STRING_FIELD_SETTER_STRING_FIELD_SET_METHOD))
                , Arguments.of(Sub.class, Sub.Fields.NUMBER_FIELD, PropertyConstant.SUB_NUMBER_FIELD_SET_METHODS));
    }

    static Stream<Arguments> typeTokenAndFieldNameAndSetMethodSets() {
        return classAndFieldNameAndSetMethodSets().map(arguments -> Arguments.of(TypeToken.of((Class<?>) arguments.get()[0]), arguments.get()[1], arguments.get()[2]));
    }

    static Stream<Arguments> classAndSetMethodSets() {
        return Stream.of(Arguments.of(Super.class, PropertyConstant.SUPER_SET_METHODS)
                , Arguments.of(Sub.class, PropertyConstant.SUB_SET_METHODS)
                , Arguments.of(StringFieldSetter.class, ImmutableSet.of(PropertyConstant.STRING_FIELD_SETTER_STRING_FIELD_SET_METHOD))
                , Arguments.of(StringFieldGetter.class, ImmutableSet.of()));
    }

    static Stream<Arguments> typeTokenAndSetMethodSets() {
        return classAndSetMethodSets().map(arguments -> Arguments.of(TypeToken.of((Class<?>) arguments.get()[0]), arguments.get()[1]));
    }


    static Stream<Arguments> classAndPropertyReaderSets() {
        return Stream.of(Arguments.of(StringFieldGetter.class, ImmutableSet.of(PropertyConstant.STRING_FIELD_GETTER_STRING_FIELD_GET_METHOD_READER))
                , Arguments.of(StringFieldSetter.class, ImmutableSet.of())
                , Arguments.of(Super.class, PropertyConstant.SUPER_READERS)
                , Arguments.of(Sub.class, PropertyConstant.SUB_READERS));
    }


}
