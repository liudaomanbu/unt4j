package org.caotc.unit4j.core.common.util.model;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Streams;
import lombok.experimental.UtilityClass;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Stream;

/**
 * @author caotc
 * @date 2022-09-08
 * @since 1.0.0
 */
@UtilityClass
public class Constant {
    public static final Field STRING_FIELD_OBJECT_STRING_FIELD;
    public static final Field FINAL_FIELD_OBJECT_STRING_FIELD;
    public static final Field STATIC_FIELD_OBJECT_STRING_FIELD;
    public static final Field CHILDREN_LONG_FIELD_OBJECT_LONG_FIELD;
    public static final Field CHILDREN_SAME_NAME_FIELD_OBJECT_STRING_FIELD;
    public static final Field MULTIPLE_FIELD_OBJECT_STRING_FIELD;
    public static final Field MULTIPLE_FIELD_OBJECT_INTEGER_FIELD;
    public static final Field MULTIPLE_FIELD_OBJECT_INT_FIELD;
    public static final Field MULTIPLE_FIELD_OBJECT_BOOLEAN_FIELD;
    public static final Field STRING_FIELD_AND_STRING_FIELD_GET_METHOD_OBJECT_STRING_FIELD;
    public static final Method STRING_FIELD_GET_METHOD_OBJECT_GET_STRING_FIELD_METHOD;
    public static final Method STRING_FIELD_FLUENT_GET_METHOD_OBJECT_STRING_FIELD_METHOD;
    public static final Method STRING_FIELD_AND_STRING_FIELD_GET_METHOD_OBJECT_GET_STRING_FIELD_METHOD;
    public static final Method STRING_FIELD_GETTER_GET_STRING_FIELD_METHOD;
    public static final Method STRING_FIELD_GETTER_OBJECT_GET_STRING_FIELD_METHOD;
    public static final Method BOOLEAN_FIELD_IS_METHOD_OBJECT_IS_BOOLEAN_FIELD_METHOD;
    public static final Method BOOLEAN_FIELD_GET_METHOD_OBJECT_GET_BOOLEAN_FIELD_METHOD;
    public static final ImmutableSet<Method> STRING_FIELD_GETTER_OBJECT_GET_METHODS;
    public static final ImmutableSet<Method> OBJECT_METHODS;
    public static final ImmutableSet<Method> STRING_FIELD_GET_METHOD_OBJECT_METHODS;
    public static final ImmutableSet<Method> STRING_FIELD_FLUENT_GET_METHOD_OBJECT_METHODS;
    public static final ImmutableSet<Method> STRING_FIELD_AND_STRING_FIELD_GET_METHOD_OBJECT_METHODS;
    public static final ImmutableSet<Method> STRING_FIELD_GETTER_METHODS;
    public static final ImmutableSet<Method> STRING_FIELD_GETTER_OBJECT_METHODS;
    public static final ImmutableSet<Method> BOOLEAN_FIELD_IS_METHOD_OBJECT_METHODS;
    public static final ImmutableSet<Method> BOOLEAN_FIELD_GET_METHOD_OBJECT_METHODS;
    public static final Constructor<NoFieldObject> NO_FIELD_OBJECT_CONSTRUCTOR;
    public static final Constructor<FinalFieldObject> FINAL_FIELD_OBJECT_CONSTRUCTOR;
    public static final Constructor<MultipleFieldObject> MULTIPLE_FIELD_OBJECT_CONSTRUCTOR;
    public static final Constructor<StaticFieldObject> STATIC_FIELD_OBJECT_CONSTRUCTOR;
    public static final Constructor<StringFieldObject> STRING_FIELD_OBJECT_CONSTRUCTOR;
    public static final Constructor<ChildrenLongFieldObject> CHILDREN_LONG_FIELD_OBJECT_CONSTRUCTOR;
    public static final Constructor<ChildrenSameNameFieldObject> CHILDREN_SAME_NAME_FIELD_OBJECT_CONSTRUCTOR;
    public static final Constructor<PrivateConstructObject> PRIVATE_CONSTRUCT_OBJECT_CONSTRUCTOR;
    public static final Constructor<ProtectedConstructObject> PROTECTED_CONSTRUCT_OBJECT_CONSTRUCTOR;
    public static final Constructor<ProtectedConstructChildrenObject> PROTECTED_CONSTRUCT_CHILDREN_OBJECT_CONSTRUCTOR;
    public static final ImmutableSet<Constructor<MultipleConstructObject>> MULTIPLE_CONSTRUCT_OBJECT_CONSTRUCTORS;
    public static final Constructor<StringFieldGetMethodObject> STRING_FIELD_GET_METHOD_OBJECT_CONSTRUCTOR;
    public static final Constructor<StringFieldFluentGetMethodObject> STRING_FIELD_FLUENT_GET_METHOD_OBJECT_CONSTRUCTOR;
    public static final Constructor<StringFieldAndStringFieldGetMethodObject> STRING_FIELD_AND_STRING_FIELD_GET_METHOD_OBJECT_CONSTRUCTOR;
    public static final Constructor<StringFieldGetterObject> STRING_FIELD_GETTER_OBJECT_CONSTRUCTOR;
    public static final Constructor<BooleanFieldIsMethodObject> BOOLEAN_FIELD_IS_METHOD_OBJECT_CONSTRUCTOR;
    public static final Constructor<BooleanFieldGetMethodObject> BOOLEAN_FIELD_GET_METHOD_OBJECT_CONSTRUCTOR;
    public static final ImmutableSet<Field> STRING_FIELD_OBJECT_FIELDS;
    public static final ImmutableSet<Field> CHILDREN_LONG_FIELD_OBJECT_FIELDS;
    public static final ImmutableSet<Field> CHILDREN_SAME_NAME_FIELD_OBJECT_FIELDS;
    public static final ImmutableSet<Field> FINAL_FIELD_OBJECT_FIELDS;
    public static final ImmutableSet<Field> STATIC_FIELD_OBJECT_FIELDS;
    public static final ImmutableSet<Field> MULTIPLE_FIELD_OBJECT_FIELDS;

    static {
        try {
            STRING_FIELD_OBJECT_STRING_FIELD = StringFieldObject.class.getDeclaredField(StringFieldObject.Fields.STRING_FIELD);
            FINAL_FIELD_OBJECT_STRING_FIELD = FinalFieldObject.class.getDeclaredField(FinalFieldObject.Fields.STRING_FIELD);
            STATIC_FIELD_OBJECT_STRING_FIELD = StaticFieldObject.class.getDeclaredField("stringField");
            CHILDREN_LONG_FIELD_OBJECT_LONG_FIELD = ChildrenLongFieldObject.class.getDeclaredField(ChildrenLongFieldObject.Fields.LONG_FIELD);
            CHILDREN_SAME_NAME_FIELD_OBJECT_STRING_FIELD = ChildrenSameNameFieldObject.class.getDeclaredField(ChildrenSameNameFieldObject.Fields.STRING_FIELD);
            MULTIPLE_FIELD_OBJECT_STRING_FIELD = MultipleFieldObject.class.getDeclaredField(MultipleFieldObject.Fields.STRING_FIELD);
            MULTIPLE_FIELD_OBJECT_INTEGER_FIELD = MultipleFieldObject.class.getDeclaredField(MultipleFieldObject.Fields.INTEGER_FIELD);
            MULTIPLE_FIELD_OBJECT_INT_FIELD = MultipleFieldObject.class.getDeclaredField(MultipleFieldObject.Fields.INT_FIELD);
            MULTIPLE_FIELD_OBJECT_BOOLEAN_FIELD = MultipleFieldObject.class.getDeclaredField(MultipleFieldObject.Fields.BOOLEAN_FIELD);
            STRING_FIELD_AND_STRING_FIELD_GET_METHOD_OBJECT_STRING_FIELD = StringFieldAndStringFieldGetMethodObject.class.getDeclaredField(StringFieldAndStringFieldGetMethodObject.Fields.STRING_FIELD);
            STRING_FIELD_GET_METHOD_OBJECT_GET_STRING_FIELD_METHOD = StringFieldGetMethodObject.class.getDeclaredMethod("getStringField");
            STRING_FIELD_FLUENT_GET_METHOD_OBJECT_STRING_FIELD_METHOD = StringFieldFluentGetMethodObject.class.getDeclaredMethod("stringField");
            STRING_FIELD_AND_STRING_FIELD_GET_METHOD_OBJECT_GET_STRING_FIELD_METHOD = StringFieldAndStringFieldGetMethodObject.class.getDeclaredMethod("getStringField");
            STRING_FIELD_GETTER_GET_STRING_FIELD_METHOD = StringFieldGetter.class.getDeclaredMethod("getStringField");
            STRING_FIELD_GETTER_OBJECT_GET_STRING_FIELD_METHOD = StringFieldGetterObject.class.getDeclaredMethod("getStringField");
            BOOLEAN_FIELD_IS_METHOD_OBJECT_IS_BOOLEAN_FIELD_METHOD = BooleanFieldIsMethodObject.class.getDeclaredMethod("isBooleanField");
            BOOLEAN_FIELD_GET_METHOD_OBJECT_GET_BOOLEAN_FIELD_METHOD = BooleanFieldGetMethodObject.class.getDeclaredMethod("getBooleanField");
            STRING_FIELD_GETTER_OBJECT_GET_METHODS = ImmutableSet.of(STRING_FIELD_GETTER_GET_STRING_FIELD_METHOD, STRING_FIELD_GETTER_OBJECT_GET_STRING_FIELD_METHOD);
            OBJECT_METHODS = Arrays.stream(Object.class.getDeclaredMethods()).collect(ImmutableSet.toImmutableSet());
            STRING_FIELD_GET_METHOD_OBJECT_METHODS = Stream.concat(OBJECT_METHODS.stream(), Stream.of(STRING_FIELD_GET_METHOD_OBJECT_GET_STRING_FIELD_METHOD)).collect(ImmutableSet.toImmutableSet());
            STRING_FIELD_FLUENT_GET_METHOD_OBJECT_METHODS = Stream.concat(OBJECT_METHODS.stream(), Stream.of(STRING_FIELD_FLUENT_GET_METHOD_OBJECT_STRING_FIELD_METHOD)).collect(ImmutableSet.toImmutableSet());
            STRING_FIELD_AND_STRING_FIELD_GET_METHOD_OBJECT_METHODS = Stream.concat(OBJECT_METHODS.stream(), Stream.of(STRING_FIELD_AND_STRING_FIELD_GET_METHOD_OBJECT_GET_STRING_FIELD_METHOD)).collect(ImmutableSet.toImmutableSet());
            STRING_FIELD_GETTER_METHODS = ImmutableSet.of(STRING_FIELD_GETTER_GET_STRING_FIELD_METHOD);
            STRING_FIELD_GETTER_OBJECT_METHODS = Streams.concat(STRING_FIELD_GETTER_METHODS.stream(), OBJECT_METHODS.stream(), Stream.of(STRING_FIELD_GETTER_OBJECT_GET_STRING_FIELD_METHOD)).collect(ImmutableSet.toImmutableSet());
            BOOLEAN_FIELD_IS_METHOD_OBJECT_METHODS = Stream.concat(OBJECT_METHODS.stream(), Stream.of(BOOLEAN_FIELD_IS_METHOD_OBJECT_IS_BOOLEAN_FIELD_METHOD)).collect(ImmutableSet.toImmutableSet());
            BOOLEAN_FIELD_GET_METHOD_OBJECT_METHODS = Stream.concat(OBJECT_METHODS.stream(), Stream.of(BOOLEAN_FIELD_GET_METHOD_OBJECT_GET_BOOLEAN_FIELD_METHOD)).collect(ImmutableSet.toImmutableSet());
            NO_FIELD_OBJECT_CONSTRUCTOR = NoFieldObject.class.getDeclaredConstructor();
            FINAL_FIELD_OBJECT_CONSTRUCTOR = FinalFieldObject.class.getDeclaredConstructor();
            MULTIPLE_FIELD_OBJECT_CONSTRUCTOR = MultipleFieldObject.class.getDeclaredConstructor();
            STATIC_FIELD_OBJECT_CONSTRUCTOR = StaticFieldObject.class.getDeclaredConstructor();
            STRING_FIELD_OBJECT_CONSTRUCTOR = StringFieldObject.class.getDeclaredConstructor();
            CHILDREN_LONG_FIELD_OBJECT_CONSTRUCTOR = ChildrenLongFieldObject.class.getDeclaredConstructor();
            CHILDREN_SAME_NAME_FIELD_OBJECT_CONSTRUCTOR = ChildrenSameNameFieldObject.class.getDeclaredConstructor();
            PRIVATE_CONSTRUCT_OBJECT_CONSTRUCTOR = PrivateConstructObject.class.getDeclaredConstructor(String.class, Integer.class, int.class, boolean.class);
            PROTECTED_CONSTRUCT_OBJECT_CONSTRUCTOR = ProtectedConstructObject.class.getDeclaredConstructor(String.class, Integer.class, int.class, boolean.class);
            PROTECTED_CONSTRUCT_CHILDREN_OBJECT_CONSTRUCTOR = ProtectedConstructChildrenObject.class.getConstructor(String.class, Integer.class, int.class, boolean.class);
            //noinspection unchecked
            MULTIPLE_CONSTRUCT_OBJECT_CONSTRUCTORS = Arrays.stream(MultipleConstructObject.class.getDeclaredConstructors()).map(c -> (Constructor<MultipleConstructObject>) c).collect(ImmutableSet.toImmutableSet());
            STRING_FIELD_GET_METHOD_OBJECT_CONSTRUCTOR = StringFieldGetMethodObject.class.getDeclaredConstructor();
            STRING_FIELD_FLUENT_GET_METHOD_OBJECT_CONSTRUCTOR = StringFieldFluentGetMethodObject.class.getDeclaredConstructor();
            STRING_FIELD_AND_STRING_FIELD_GET_METHOD_OBJECT_CONSTRUCTOR = StringFieldAndStringFieldGetMethodObject.class.getDeclaredConstructor();
            STRING_FIELD_GETTER_OBJECT_CONSTRUCTOR = StringFieldGetterObject.class.getDeclaredConstructor();
            BOOLEAN_FIELD_IS_METHOD_OBJECT_CONSTRUCTOR = BooleanFieldIsMethodObject.class.getDeclaredConstructor();
            BOOLEAN_FIELD_GET_METHOD_OBJECT_CONSTRUCTOR = BooleanFieldGetMethodObject.class.getDeclaredConstructor();

            STRING_FIELD_OBJECT_FIELDS = ImmutableSet.of(STRING_FIELD_OBJECT_STRING_FIELD);
            CHILDREN_LONG_FIELD_OBJECT_FIELDS = ImmutableSet.<Field>builder().addAll(STRING_FIELD_OBJECT_FIELDS).add(CHILDREN_LONG_FIELD_OBJECT_LONG_FIELD).build();
            CHILDREN_SAME_NAME_FIELD_OBJECT_FIELDS = ImmutableSet.<Field>builder().addAll(STRING_FIELD_OBJECT_FIELDS).add(CHILDREN_SAME_NAME_FIELD_OBJECT_STRING_FIELD).build();
            FINAL_FIELD_OBJECT_FIELDS = ImmutableSet.of(FINAL_FIELD_OBJECT_STRING_FIELD);
            STATIC_FIELD_OBJECT_FIELDS = ImmutableSet.of(STATIC_FIELD_OBJECT_STRING_FIELD);
            MULTIPLE_FIELD_OBJECT_FIELDS = ImmutableSet.of(MULTIPLE_FIELD_OBJECT_STRING_FIELD, MULTIPLE_FIELD_OBJECT_INTEGER_FIELD, MULTIPLE_FIELD_OBJECT_INT_FIELD, MULTIPLE_FIELD_OBJECT_BOOLEAN_FIELD);
        } catch (NoSuchFieldException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
