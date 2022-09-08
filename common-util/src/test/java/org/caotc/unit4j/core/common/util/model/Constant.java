package org.caotc.unit4j.core.common.util.model;

import com.google.common.collect.ImmutableSet;
import lombok.experimental.UtilityClass;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

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

    public static final ImmutableSet<Method> OBJECT_METHODS;
    public static final ImmutableSet<Field> STRING_FIELD_OBJECT_FIELDS = ImmutableSet.of(STRING_FIELD_OBJECT_STRING_FIELD);
    public static final ImmutableSet<Field> CHILDREN_LONG_FIELD_OBJECT_FIELDS = ImmutableSet.<Field>builder().addAll(STRING_FIELD_OBJECT_FIELDS).add(CHILDREN_LONG_FIELD_OBJECT_LONG_FIELD).build();
    public static final ImmutableSet<Field> CHILDREN_SAME_NAME_FIELD_OBJECT_FIELDS = ImmutableSet.<Field>builder().addAll(STRING_FIELD_OBJECT_FIELDS).add(CHILDREN_SAME_NAME_FIELD_OBJECT_STRING_FIELD).build();
    public static final ImmutableSet<Field> FINAL_FIELD_OBJECT_FIELDS = ImmutableSet.of(FINAL_FIELD_OBJECT_STRING_FIELD);
    public static final ImmutableSet<Field> STATIC_FIELD_OBJECT_FIELDS = ImmutableSet.of(STATIC_FIELD_OBJECT_STRING_FIELD);
    public static final ImmutableSet<Field> MULTIPLE_FIELD_OBJECT_FIELDS = ImmutableSet.of(MULTIPLE_FIELD_OBJECT_STRING_FIELD, MULTIPLE_FIELD_OBJECT_INTEGER_FIELD, MULTIPLE_FIELD_OBJECT_INT_FIELD, MULTIPLE_FIELD_OBJECT_BOOLEAN_FIELD);

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
            OBJECT_METHODS = Arrays.stream(Object.class.getDeclaredMethods()).collect(ImmutableSet.toImmutableSet());
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

}
