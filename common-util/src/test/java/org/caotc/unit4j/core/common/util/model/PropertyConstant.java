package org.caotc.unit4j.core.common.util.model;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Streams;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.XSlf4j;
import org.caotc.unit4j.core.common.reflect.property.accessor.PropertyReader;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Stream;

/**
 * @author caotc
 * @date 2022-08-11
 * @since 1.0.0
 */
@UtilityClass
@XSlf4j
public class PropertyConstant {
    public static final ImmutableSet<Method> OBJECT_METHODS;
    public static final Method STRING_FIELD_SETTER_STRING_FIELD_SET_METHOD;
    public static final Method STRING_FIELD_GETTER_STRING_FIELD_GET_METHOD;
    public static final PropertyReader<StringFieldGetter, String> STRING_FIELD_GETTER_STRING_FIELD_GET_METHOD_READER;
    public static final Field SUPER_STRING_FIELD;
    public static final Field SUPER_INT_FIELD;
    public static final ImmutableSet<Field> SUPER_DECLARED_FIELDS;
    public static final ImmutableSet<Field> SUPER_FIELDS;
    public static final Method SUPER_STRING_FIELD_GET_METHOD;
    public static final Method SUPER_INT_FIELD_GET_METHOD;
    public static final ImmutableSet<Method> SUPER_GET_METHODS;
    public static final Method SUPER_STRING_FIELD_SET_METHOD;
    public static final Method SUPER_INT_FIELD_SET_METHOD;
    public static final ImmutableSet<Method> SUPER_STRING_FIELD_SET_METHODS;
    public static final ImmutableSet<Method> SUPER_SET_METHODS;
    public static final ImmutableSet<Method> SUPER_METHODS;
    public static final ImmutableSet<Constructor<Super>> SUPER_CONSTRUCTORS;
    public static final PropertyReader<Super, String> SUPER_STRING_FIELD_READER = null;
    public static final PropertyReader<Super, Integer> SUPER_INT_FIELD_READER = null;
    public static final PropertyReader<Super, String> SUPER_STRING_FIELD_GET_METHOD_READER = null;
    public static final PropertyReader<Super, Integer> SUPER_INT_FIELD_GET_METHOD_READER = null;
    public static final ImmutableSet<PropertyReader<? super Super, ?>> SUPER_READERS = null;
    public static final Field SUB_LOG;
    public static final Field SUB_STRING_FIELD;
    public static final Field SUB_NUMBER_FIELD;
    public static final ImmutableSet<Field> SUB_DECLARED_FIELDS;
    public static final ImmutableSet<Field> SUB_STRING_FIELDS;
    public static final ImmutableSet<Field> SUB_FIELDS;
    public static final Method SUB_STRING_FIELD_GET_METHOD;
    public static final Method SUB_NUMBER_FIELD_GET_METHOD;
    public static final Method SUB_READ_NUMBER_FIELD_GET_METHOD;
    public static final Method SUB_INT_FIELD_GET_METHOD;
    public static final ImmutableSet<Method> SUB_GET_METHODS;
    public static final Method SUB_NUMBER_FIELD_NUMBER_SET_METHOD;
    public static final Method SUB_NUMBER_FIELD_INT_SET_METHOD;
    public static final Method SUB_NUMBER_FIELD_LONG_SET_METHOD;
    public static final Method SUB_STRING_FIELD_SET_METHOD;
    public static final Method SUB_INT_FIELD_SET_METHOD;
    public static final ImmutableSet<Method> SUB_INT_FIELD_SET_METHODS;
    public static final ImmutableSet<Method> SUB_STRING_FIELD_SET_METHODS;
    public static final ImmutableSet<Method> SUB_NUMBER_FIELD_SET_METHODS;
    public static final ImmutableSet<Method> SUB_SET_METHODS;
    public static final ImmutableSet<Method> SUB_METHODS;
    public static final ImmutableSet<Constructor<Sub>> SUB_CONSTRUCTORS;
    public static final PropertyReader<Sub, String> SUB_STRING_FIELD_READER = null;
    public static final PropertyReader<Sub, Number> SUB_NUMBER_FIELD_READER = null;
    public static final PropertyReader<Sub, String> SUB_STRING_FIELD_GET_METHOD_READER = null;
    public static final PropertyReader<Sub, Number> SUB_NUMBER_FIELD_GET_METHOD_READER = null;
    public static final PropertyReader<Sub, Number> SUB_READ_NUMBER_FIELD_GET_METHOD_READER = null;
    public static final PropertyReader<Sub, Integer> SUB_INT_FIELD_GET_METHOD_READER = null;
    public static final ImmutableSet<PropertyReader<? super Sub, ?>> SUB_READERS = null;

    static {
        try {
            OBJECT_METHODS = Arrays.stream(Object.class.getDeclaredMethods()).collect(ImmutableSet.toImmutableSet());
            STRING_FIELD_SETTER_STRING_FIELD_SET_METHOD = StringFieldSetter.class.getDeclaredMethod("setStringField", String.class);
            STRING_FIELD_GETTER_STRING_FIELD_GET_METHOD = StringFieldGetter.class.getDeclaredMethod("getStringField");
            STRING_FIELD_GETTER_STRING_FIELD_GET_METHOD_READER = PropertyReader.from(STRING_FIELD_GETTER_STRING_FIELD_GET_METHOD, "stringField");
            SUPER_STRING_FIELD = Super.class.getDeclaredField(Super.Fields.STRING_FIELD);
            SUPER_INT_FIELD = Super.class.getDeclaredField(Super.Fields.INT_FIELD);
            SUPER_DECLARED_FIELDS = Arrays.stream(Super.class.getDeclaredFields()).collect(ImmutableSet.toImmutableSet());
            SUPER_FIELDS = SUPER_DECLARED_FIELDS;
            SUPER_STRING_FIELD_GET_METHOD = Super.class.getDeclaredMethod("getStringField");
            SUPER_INT_FIELD_GET_METHOD = Super.class.getDeclaredMethod("getIntField");
            SUPER_GET_METHODS = ImmutableSet.of(SUPER_STRING_FIELD_GET_METHOD, SUPER_INT_FIELD_GET_METHOD);
            SUPER_STRING_FIELD_SET_METHOD = Super.class.getDeclaredMethod("setStringField", String.class);
            SUPER_INT_FIELD_SET_METHOD = Super.class.getDeclaredMethod("setIntField", int.class);
            SUPER_STRING_FIELD_SET_METHODS = ImmutableSet.of(STRING_FIELD_SETTER_STRING_FIELD_SET_METHOD, SUPER_STRING_FIELD_SET_METHOD);
            SUPER_SET_METHODS = Stream.concat(SUPER_STRING_FIELD_SET_METHODS.stream(), Stream.of(SUPER_INT_FIELD_SET_METHOD)).collect(ImmutableSet.toImmutableSet());
            SUPER_METHODS = Streams.concat(OBJECT_METHODS.stream(), Arrays.stream(Super.class.getDeclaredMethods()), Stream.of(STRING_FIELD_SETTER_STRING_FIELD_SET_METHOD)).collect(ImmutableSet.toImmutableSet());
            //noinspection unchecked
            SUPER_CONSTRUCTORS = Arrays.stream(Super.class.getDeclaredConstructors()).map(c -> (Constructor<Super>) c).collect(ImmutableSet.toImmutableSet());
//            SUPER_STRING_FIELD_READER=PropertyReader.from(SUPER_STRING_FIELD);
//            SUPER_INT_FIELD_READER=PropertyReader.from(SUPER_INT_FIELD);
//            SUPER_STRING_FIELD_GET_METHOD_READER =PropertyReader.from(SUPER_STRING_FIELD_GET_METHOD,Super.Fields.STRING_FIELD);
//            SUPER_INT_FIELD_GET_METHOD_READER =PropertyReader.from(SUPER_INT_FIELD_GET_METHOD,Super.Fields.INT_FIELD);
//            SUPER_READERS=ImmutableSet.of(SUPER_STRING_FIELD_READER,SUPER_INT_FIELD_READER,SUPER_STRING_FIELD_GET_METHOD_READER, SUPER_INT_FIELD_GET_METHOD_READER);
            SUB_LOG = Sub.class.getDeclaredField("log");
            SUB_STRING_FIELD = Sub.class.getDeclaredField(Sub.Fields.STRING_FIELD);
            SUB_NUMBER_FIELD = Sub.class.getDeclaredField(Sub.Fields.NUMBER_FIELD);
            SUB_DECLARED_FIELDS = Arrays.stream(Sub.class.getDeclaredFields()).collect(ImmutableSet.toImmutableSet());
            SUB_STRING_FIELDS = ImmutableSet.of(SUPER_STRING_FIELD, SUB_STRING_FIELD);
            SUB_FIELDS = Stream.concat(SUPER_DECLARED_FIELDS.stream(), SUB_DECLARED_FIELDS.stream()).collect(ImmutableSet.toImmutableSet());
            SUB_STRING_FIELD_GET_METHOD = Sub.class.getDeclaredMethod("getStringField");
            SUB_NUMBER_FIELD_GET_METHOD = Sub.class.getDeclaredMethod("getNumberField");
            SUB_READ_NUMBER_FIELD_GET_METHOD = Sub.class.getDeclaredMethod("getReadNumberField");
            SUB_INT_FIELD_GET_METHOD = Sub.class.getDeclaredMethod("getIntField");
            SUB_GET_METHODS = Stream.concat(SUPER_GET_METHODS.stream(), Stream.of(SUB_STRING_FIELD_GET_METHOD
                            , SUB_NUMBER_FIELD_GET_METHOD, SUB_READ_NUMBER_FIELD_GET_METHOD
                            , SUB_INT_FIELD_GET_METHOD, STRING_FIELD_GETTER_STRING_FIELD_GET_METHOD))
                    .collect(ImmutableSet.toImmutableSet());
            SUB_NUMBER_FIELD_NUMBER_SET_METHOD = Sub.class.getDeclaredMethod("setNumberField", Number.class);
            SUB_NUMBER_FIELD_INT_SET_METHOD = Sub.class.getDeclaredMethod("setNumberField", int.class);
            SUB_NUMBER_FIELD_LONG_SET_METHOD = Sub.class.getDeclaredMethod("setNumberField", long.class);
            SUB_STRING_FIELD_SET_METHOD = Sub.class.getDeclaredMethod("setStringField", String.class);
            SUB_INT_FIELD_SET_METHOD = Sub.class.getDeclaredMethod("setIntField", int.class);
            SUB_INT_FIELD_SET_METHODS = ImmutableSet.of(SUB_INT_FIELD_SET_METHOD, SUPER_INT_FIELD_SET_METHOD);
            SUB_STRING_FIELD_SET_METHODS = Stream.concat(SUPER_STRING_FIELD_SET_METHODS.stream(), Stream.of(SUB_STRING_FIELD_SET_METHOD)).collect(ImmutableSet.toImmutableSet());
            SUB_NUMBER_FIELD_SET_METHODS = ImmutableSet.of(SUB_NUMBER_FIELD_NUMBER_SET_METHOD, SUB_NUMBER_FIELD_INT_SET_METHOD, SUB_NUMBER_FIELD_LONG_SET_METHOD);
            SUB_SET_METHODS = Streams.concat(SUPER_SET_METHODS.stream(), SUB_NUMBER_FIELD_SET_METHODS.stream(),
                    Stream.of(SUB_STRING_FIELD_SET_METHOD, SUB_INT_FIELD_SET_METHOD)).collect(ImmutableSet.toImmutableSet());
            SUB_METHODS = Streams.concat(SUPER_METHODS.stream(), Arrays.stream(Sub.class.getDeclaredMethods()), Stream.of(STRING_FIELD_GETTER_STRING_FIELD_GET_METHOD)).collect(ImmutableSet.toImmutableSet());
            //noinspection unchecked
            SUB_CONSTRUCTORS = Arrays.stream(Sub.class.getDeclaredConstructors()).map(c -> (Constructor<Sub>) c).collect(ImmutableSet.toImmutableSet());
//            SUB_STRING_FIELD_READER=PropertyReader.from(SUB_STRING_FIELD);
//            SUB_NUMBER_FIELD_READER=PropertyReader.from(SUB_NUMBER_FIELD);
//            SUB_STRING_FIELD_GET_METHOD_READER=PropertyReader.from(SUB_STRING_FIELD_GET_METHOD,Sub.Fields.STRING_FIELD);
//            SUB_NUMBER_FIELD_GET_METHOD_READER=PropertyReader.from(SUB_NUMBER_FIELD_GET_METHOD,Sub.Fields.NUMBER_FIELD);
//            SUB_READ_NUMBER_FIELD_GET_METHOD_READER=PropertyReader.from(SUB_READ_NUMBER_FIELD_GET_METHOD,"readNumberField");
//            SUB_INT_FIELD_GET_METHOD_READER=PropertyReader.from(SUB_INT_FIELD_GET_METHOD,Super.Fields.INT_FIELD);
//            Stream<PropertyReader<? super Sub, ?>> subReadersStream = Stream.concat(SUPER_READERS.stream()
////                    ,Stream.of(SUB_STRING_FIELD_READER,SUB_NUMBER_FIELD_READER,SUB_STRING_FIELD_GET_METHOD_READER,SUB_NUMBER_FIELD_GET_METHOD_READER,SUB_READ_NUMBER_FIELD_GET_METHOD_READER,SUB_INT_FIELD_GET_METHOD_READER));
//                    ,Stream.of());
//            SUB_READERS=subReadersStream.collect(ImmutableSet.toImmutableSet());
        } catch (NoSuchFieldException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

    }
}
