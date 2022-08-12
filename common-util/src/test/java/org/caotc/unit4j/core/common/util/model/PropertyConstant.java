package org.caotc.unit4j.core.common.util.model;

import com.google.common.collect.ImmutableSet;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.XSlf4j;

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
    public static final Field SUPER_STRING_FIELD;
    public static final Field SUPER_INT_FIELD;
    public static final ImmutableSet<Field> SUPER_DECLARED_FIELDS;
    public static final ImmutableSet<Field> SUPER_FIELDS;
    public static final ImmutableSet<Method> SUPER_METHODS;
    public static final ImmutableSet<Constructor<Super>> SUPER_CONSTRUCTORS;
    public static final Field SUB_LOG;
    public static final Field SUB_STRING_FIELD;
    public static final Field SUB_NUMBER_FIELD;
    public static final ImmutableSet<Field> SUB_DECLARED_FIELDS;
    public static final ImmutableSet<Field> SUB_STRING_FIELDS;
    public static final ImmutableSet<Field> SUB_FIELDS;
    public static final ImmutableSet<Method> SUB_METHODS;
    public static final ImmutableSet<Constructor<Sub>> SUB_CONSTRUCTORS;

    static {
        try {
            SUPER_STRING_FIELD = Super.class.getDeclaredField(Super.Fields.STRING_FIELD);
            SUPER_INT_FIELD = Super.class.getDeclaredField(Super.Fields.INT_FIELD);
            SUPER_DECLARED_FIELDS = Arrays.stream(Super.class.getDeclaredFields()).collect(ImmutableSet.toImmutableSet());
            SUPER_FIELDS = SUPER_DECLARED_FIELDS;
            SUPER_METHODS = Stream.concat(Arrays.stream(Object.class.getDeclaredMethods()), Arrays.stream(Super.class.getDeclaredMethods())).collect(ImmutableSet.toImmutableSet());
            //noinspection unchecked
            SUPER_CONSTRUCTORS = Arrays.stream(Super.class.getDeclaredConstructors()).map(c -> (Constructor<Super>) c).collect(ImmutableSet.toImmutableSet());
            SUB_LOG = Sub.class.getDeclaredField("log");
            SUB_STRING_FIELD = Sub.class.getDeclaredField(Sub.Fields.STRING_FIELD);
            SUB_NUMBER_FIELD = Sub.class.getDeclaredField(Sub.Fields.NUMBER_FIELD);
            SUB_DECLARED_FIELDS = Arrays.stream(Sub.class.getDeclaredFields()).collect(ImmutableSet.toImmutableSet());
            SUB_STRING_FIELDS = ImmutableSet.of(SUPER_STRING_FIELD, SUB_STRING_FIELD);
            SUB_FIELDS = Stream.concat(SUPER_DECLARED_FIELDS.stream(), SUB_DECLARED_FIELDS.stream()).collect(ImmutableSet.toImmutableSet());
            SUB_METHODS = Stream.concat(SUPER_METHODS.stream(), Arrays.stream(Sub.class.getDeclaredMethods())).collect(ImmutableSet.toImmutableSet());
            //noinspection unchecked
            SUB_CONSTRUCTORS = Arrays.stream(Sub.class.getDeclaredConstructors()).map(c -> (Constructor<Sub>) c).collect(ImmutableSet.toImmutableSet());
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }

    }
}
