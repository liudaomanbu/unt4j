package org.caotc.unit4j.core.common.reflect.provider;

import lombok.experimental.UtilityClass;
import org.caotc.unit4j.core.common.reflect.model.BridgeSampleOne;
import org.caotc.unit4j.core.common.reflect.model.BridgeSampleThree;
import org.caotc.unit4j.core.common.reflect.model.BridgeSampleTwo;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author caotc
 * @date 2023-02-16
 * @since 1.0.0
 */
@SuppressWarnings({"JavaReflectionMemberAccess", "OptionalGetWithoutIsPresent"})
@UtilityClass
public class Constant {
    public static final Method BRIDGE_SAMPLE_ONE_A_METHOD;
    public static final Method BRIDGE_SAMPLE_ONE_B_METHOD;
    public static final Method BRIDGE_SAMPLE_ONE_B_BRIDGE_METHOD;
    public static final Method BRIDGE_SAMPLE_ONE_C_METHOD;
    public static final Method BRIDGE_SAMPLE_TWO_A_METHOD;
    public static final Method BRIDGE_SAMPLE_TWO_B_METHOD;
    public static final Method BRIDGE_SAMPLE_TWO_B_BRIDGE_METHOD;
    public static final Method BRIDGE_SAMPLE_TWO_C_METHOD;
    public static final Method BRIDGE_SAMPLE_THREE_A_METHOD;
    public static final Method BRIDGE_SAMPLE_THREE_B_BRIDGE_METHOD;
    public static final Method BRIDGE_SAMPLE_THREE_C_METHOD;

    static {
        try {
            BRIDGE_SAMPLE_ONE_A_METHOD = BridgeSampleOne.A.class.getDeclaredMethod("getT");
            BRIDGE_SAMPLE_ONE_B_METHOD = BridgeSampleOne.B.class.getDeclaredMethod("getT");
            BRIDGE_SAMPLE_ONE_B_BRIDGE_METHOD = Arrays.stream(BridgeSampleOne.B.class.getDeclaredMethods())
                    .filter(method -> method.getReturnType().equals(Object.class))
                    .findAny()
                    .get();
            BRIDGE_SAMPLE_ONE_C_METHOD = BridgeSampleOne.C.class.getDeclaredMethod("getT");
            BRIDGE_SAMPLE_TWO_A_METHOD = BridgeSampleTwo.A.class.getDeclaredMethod("getT", Object.class);
            BRIDGE_SAMPLE_TWO_B_METHOD = BridgeSampleTwo.B.class.getDeclaredMethod("getT", String.class);
            BRIDGE_SAMPLE_TWO_B_BRIDGE_METHOD = BridgeSampleTwo.B.class.getDeclaredMethod("getT", Object.class);
            BRIDGE_SAMPLE_TWO_C_METHOD = BridgeSampleTwo.C.class.getDeclaredMethod("getT", String.class);
            BRIDGE_SAMPLE_THREE_B_BRIDGE_METHOD = BridgeSampleThree.B.class.getDeclaredMethod("foo");
            BRIDGE_SAMPLE_THREE_A_METHOD = BRIDGE_SAMPLE_THREE_B_BRIDGE_METHOD.getDeclaringClass().getSuperclass().getDeclaredMethod("foo");
            BRIDGE_SAMPLE_THREE_C_METHOD = BridgeSampleThree.C.class.getDeclaredMethod("foo");
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
