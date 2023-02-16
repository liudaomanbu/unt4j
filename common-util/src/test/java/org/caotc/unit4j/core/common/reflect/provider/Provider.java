package org.caotc.unit4j.core.common.reflect.provider;

import com.google.common.reflect.TypeToken;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.caotc.unit4j.core.common.reflect.Invokable;
import org.caotc.unit4j.core.common.reflect.model.BridgeSampleOne;
import org.caotc.unit4j.core.common.reflect.model.BridgeSampleThree;
import org.caotc.unit4j.core.common.reflect.model.BridgeSampleTwo;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

/**
 * @author caotc
 * @date 2022-08-18
 * @since 1.0.0
 */
@UtilityClass
@Slf4j
public class Provider {
    static Stream<Arguments> overriddenInvokableAndOverridingInvokable() {
        return Stream.of(Arguments.of(Invokable.from(Constant.BRIDGE_SAMPLE_ONE_A_METHOD, TypeToken.of(BridgeSampleOne.B.class)), Invokable.from(Constant.BRIDGE_SAMPLE_ONE_B_METHOD)),
                Arguments.of(Invokable.from(Constant.BRIDGE_SAMPLE_ONE_A_METHOD, new TypeToken<BridgeSampleOne.A<String>>() {
                }), Invokable.from(Constant.BRIDGE_SAMPLE_ONE_B_METHOD)),
                Arguments.of(Invokable.from(Constant.BRIDGE_SAMPLE_ONE_A_METHOD, TypeToken.of(BridgeSampleOne.B.class)), Invokable.from(Constant.BRIDGE_SAMPLE_ONE_B_BRIDGE_METHOD)),
                Arguments.of(Invokable.from(Constant.BRIDGE_SAMPLE_ONE_A_METHOD, new TypeToken<BridgeSampleOne.A<String>>() {
                }), Invokable.from(Constant.BRIDGE_SAMPLE_ONE_B_BRIDGE_METHOD)),
                Arguments.of(Invokable.from(Constant.BRIDGE_SAMPLE_ONE_B_METHOD), Invokable.from(Constant.BRIDGE_SAMPLE_ONE_C_METHOD)),
                Arguments.of(Invokable.from(Constant.BRIDGE_SAMPLE_ONE_B_BRIDGE_METHOD), Invokable.from(Constant.BRIDGE_SAMPLE_ONE_C_METHOD)),
                Arguments.of(Invokable.from(Constant.BRIDGE_SAMPLE_ONE_A_METHOD, TypeToken.of(BridgeSampleOne.C.class)), Invokable.from(Constant.BRIDGE_SAMPLE_ONE_C_METHOD)),
                Arguments.of(Invokable.from(Constant.BRIDGE_SAMPLE_ONE_A_METHOD, new TypeToken<BridgeSampleOne.A<String>>() {
                }), Invokable.from(Constant.BRIDGE_SAMPLE_ONE_C_METHOD)),
                Arguments.of(Invokable.from(Constant.BRIDGE_SAMPLE_TWO_A_METHOD, TypeToken.of(BridgeSampleTwo.B.class)), Invokable.from(Constant.BRIDGE_SAMPLE_TWO_B_METHOD)),
                Arguments.of(Invokable.from(Constant.BRIDGE_SAMPLE_TWO_A_METHOD, new TypeToken<BridgeSampleTwo.A<String>>() {
                }), Invokable.from(Constant.BRIDGE_SAMPLE_TWO_B_METHOD)),
                Arguments.of(Invokable.from(Constant.BRIDGE_SAMPLE_TWO_A_METHOD, TypeToken.of(BridgeSampleTwo.B.class)), Invokable.from(Constant.BRIDGE_SAMPLE_TWO_B_BRIDGE_METHOD)),
                Arguments.of(Invokable.from(Constant.BRIDGE_SAMPLE_TWO_A_METHOD, new TypeToken<BridgeSampleTwo.A<String>>() {
                }), Invokable.from(Constant.BRIDGE_SAMPLE_TWO_B_BRIDGE_METHOD)),
                Arguments.of(Invokable.from(Constant.BRIDGE_SAMPLE_TWO_B_METHOD), Invokable.from(Constant.BRIDGE_SAMPLE_TWO_C_METHOD)),
                Arguments.of(Invokable.from(Constant.BRIDGE_SAMPLE_TWO_B_BRIDGE_METHOD), Invokable.from(Constant.BRIDGE_SAMPLE_TWO_C_METHOD)),
                Arguments.of(Invokable.from(Constant.BRIDGE_SAMPLE_TWO_A_METHOD, TypeToken.of(BridgeSampleTwo.C.class)), Invokable.from(Constant.BRIDGE_SAMPLE_TWO_C_METHOD)),
                Arguments.of(Invokable.from(Constant.BRIDGE_SAMPLE_TWO_A_METHOD, new TypeToken<BridgeSampleTwo.A<String>>() {
                }), Invokable.from(Constant.BRIDGE_SAMPLE_TWO_C_METHOD)),
                Arguments.of(Invokable.from(Constant.BRIDGE_SAMPLE_THREE_A_METHOD), Invokable.from(Constant.BRIDGE_SAMPLE_THREE_B_BRIDGE_METHOD)),
                Arguments.of(Invokable.from(Constant.BRIDGE_SAMPLE_THREE_B_BRIDGE_METHOD), Invokable.from(Constant.BRIDGE_SAMPLE_THREE_C_METHOD)),
                Arguments.of(Invokable.from(Constant.BRIDGE_SAMPLE_THREE_A_METHOD), Invokable.from(Constant.BRIDGE_SAMPLE_THREE_C_METHOD)));
    }

    static Stream<Arguments> invokableAndNotOverridingInvokable() {
        return Stream.of(Arguments.of(Invokable.from(Constant.BRIDGE_SAMPLE_ONE_A_METHOD), Invokable.from(Constant.BRIDGE_SAMPLE_ONE_B_METHOD)),
                Arguments.of(Invokable.from(Constant.BRIDGE_SAMPLE_ONE_A_METHOD, new TypeToken<BridgeSampleOne.A<Object>>() {
                }), Invokable.from(Constant.BRIDGE_SAMPLE_ONE_B_METHOD)),
                Arguments.of(Invokable.from(Constant.BRIDGE_SAMPLE_ONE_A_METHOD), Invokable.from(Constant.BRIDGE_SAMPLE_ONE_B_BRIDGE_METHOD)),
                Arguments.of(Invokable.from(Constant.BRIDGE_SAMPLE_ONE_A_METHOD, new TypeToken<BridgeSampleOne.A<Object>>() {
                }), Invokable.from(Constant.BRIDGE_SAMPLE_ONE_B_BRIDGE_METHOD)),
                Arguments.of(Invokable.from(Constant.BRIDGE_SAMPLE_ONE_A_METHOD), Invokable.from(Constant.BRIDGE_SAMPLE_ONE_C_METHOD)),
                Arguments.of(Invokable.from(Constant.BRIDGE_SAMPLE_ONE_A_METHOD, new TypeToken<BridgeSampleOne.A<Object>>() {
                }), Invokable.from(Constant.BRIDGE_SAMPLE_ONE_C_METHOD)),
                Arguments.of(Invokable.from(Constant.BRIDGE_SAMPLE_TWO_A_METHOD), Invokable.from(Constant.BRIDGE_SAMPLE_TWO_B_METHOD)),
                Arguments.of(Invokable.from(Constant.BRIDGE_SAMPLE_TWO_A_METHOD, new TypeToken<BridgeSampleTwo.A<Object>>() {
                }), Invokable.from(Constant.BRIDGE_SAMPLE_TWO_B_METHOD)),
                Arguments.of(Invokable.from(Constant.BRIDGE_SAMPLE_TWO_A_METHOD), Invokable.from(Constant.BRIDGE_SAMPLE_TWO_B_BRIDGE_METHOD)),
                Arguments.of(Invokable.from(Constant.BRIDGE_SAMPLE_TWO_A_METHOD, new TypeToken<BridgeSampleTwo.A<Object>>() {
                }), Invokable.from(Constant.BRIDGE_SAMPLE_TWO_B_BRIDGE_METHOD)),
                Arguments.of(Invokable.from(Constant.BRIDGE_SAMPLE_TWO_A_METHOD), Invokable.from(Constant.BRIDGE_SAMPLE_TWO_C_METHOD)),
                Arguments.of(Invokable.from(Constant.BRIDGE_SAMPLE_TWO_A_METHOD, new TypeToken<BridgeSampleTwo.A<Object>>() {
                }), Invokable.from(Constant.BRIDGE_SAMPLE_TWO_C_METHOD)));
    }

    static Stream<Arguments> invokableAndOverridableType() {
        return Stream.of(Arguments.of(Invokable.from(Constant.BRIDGE_SAMPLE_ONE_A_METHOD), TypeToken.of(BridgeSampleOne.B.class)),
                Arguments.of(Invokable.from(Constant.BRIDGE_SAMPLE_ONE_A_METHOD), TypeToken.of(BridgeSampleOne.C.class)),
                Arguments.of(Invokable.from(Constant.BRIDGE_SAMPLE_ONE_A_METHOD, TypeToken.of(BridgeSampleOne.B.class)), TypeToken.of(BridgeSampleOne.B.class)),
                Arguments.of(Invokable.from(Constant.BRIDGE_SAMPLE_ONE_A_METHOD, TypeToken.of(BridgeSampleOne.B.class)), TypeToken.of(BridgeSampleOne.C.class)),
                Arguments.of(Invokable.from(Constant.BRIDGE_SAMPLE_ONE_A_METHOD, new TypeToken<BridgeSampleOne.A<String>>() {
                }), TypeToken.of(BridgeSampleOne.B.class)),
                Arguments.of(Invokable.from(Constant.BRIDGE_SAMPLE_ONE_A_METHOD, new TypeToken<BridgeSampleOne.A<String>>() {
                }), TypeToken.of(BridgeSampleOne.C.class)),
                Arguments.of(Invokable.from(Constant.BRIDGE_SAMPLE_ONE_A_METHOD, new TypeToken<BridgeSampleOne.A<Object>>() {
                }), TypeToken.of(BridgeSampleOne.B.class)),
                Arguments.of(Invokable.from(Constant.BRIDGE_SAMPLE_ONE_A_METHOD, new TypeToken<BridgeSampleOne.A<Object>>() {
                }), TypeToken.of(BridgeSampleOne.C.class)),
                Arguments.of(Invokable.from(Constant.BRIDGE_SAMPLE_ONE_B_METHOD), TypeToken.of(BridgeSampleOne.C.class)),
                Arguments.of(Invokable.from(Constant.BRIDGE_SAMPLE_ONE_B_BRIDGE_METHOD), TypeToken.of(BridgeSampleOne.C.class)),
                Arguments.of(Invokable.from(Constant.BRIDGE_SAMPLE_ONE_A_METHOD, TypeToken.of(BridgeSampleOne.C.class)), TypeToken.of(BridgeSampleOne.B.class)),
                Arguments.of(Invokable.from(Constant.BRIDGE_SAMPLE_ONE_A_METHOD, TypeToken.of(BridgeSampleOne.C.class)), TypeToken.of(BridgeSampleOne.C.class)),
                Arguments.of(Invokable.from(Constant.BRIDGE_SAMPLE_TWO_A_METHOD), TypeToken.of(BridgeSampleTwo.B.class)),
                Arguments.of(Invokable.from(Constant.BRIDGE_SAMPLE_TWO_A_METHOD), TypeToken.of(BridgeSampleTwo.C.class)),
                Arguments.of(Invokable.from(Constant.BRIDGE_SAMPLE_TWO_A_METHOD, TypeToken.of(BridgeSampleTwo.B.class)), TypeToken.of(BridgeSampleTwo.B.class)),
                Arguments.of(Invokable.from(Constant.BRIDGE_SAMPLE_TWO_A_METHOD, TypeToken.of(BridgeSampleTwo.B.class)), TypeToken.of(BridgeSampleTwo.C.class)),
                Arguments.of(Invokable.from(Constant.BRIDGE_SAMPLE_TWO_A_METHOD, new TypeToken<BridgeSampleTwo.A<String>>() {
                }), TypeToken.of(BridgeSampleTwo.B.class)),
                Arguments.of(Invokable.from(Constant.BRIDGE_SAMPLE_TWO_A_METHOD, new TypeToken<BridgeSampleTwo.A<String>>() {
                }), TypeToken.of(BridgeSampleTwo.C.class)),
                Arguments.of(Invokable.from(Constant.BRIDGE_SAMPLE_TWO_A_METHOD, new TypeToken<BridgeSampleTwo.A<Object>>() {
                }), TypeToken.of(BridgeSampleTwo.B.class)),
                Arguments.of(Invokable.from(Constant.BRIDGE_SAMPLE_TWO_A_METHOD, new TypeToken<BridgeSampleTwo.A<Object>>() {
                }), TypeToken.of(BridgeSampleTwo.C.class)),
                Arguments.of(Invokable.from(Constant.BRIDGE_SAMPLE_TWO_B_METHOD), TypeToken.of(BridgeSampleTwo.C.class)),
                Arguments.of(Invokable.from(Constant.BRIDGE_SAMPLE_TWO_B_BRIDGE_METHOD), TypeToken.of(BridgeSampleTwo.C.class)),
                Arguments.of(Invokable.from(Constant.BRIDGE_SAMPLE_TWO_A_METHOD), TypeToken.of(BridgeSampleTwo.B.class)),
                Arguments.of(Invokable.from(Constant.BRIDGE_SAMPLE_TWO_A_METHOD), TypeToken.of(BridgeSampleTwo.C.class)),
                Arguments.of(Invokable.from(Constant.BRIDGE_SAMPLE_TWO_A_METHOD, TypeToken.of(BridgeSampleTwo.C.class)), TypeToken.of(BridgeSampleTwo.B.class)),
                Arguments.of(Invokable.from(Constant.BRIDGE_SAMPLE_TWO_A_METHOD, TypeToken.of(BridgeSampleTwo.C.class)), TypeToken.of(BridgeSampleTwo.C.class)),
                Arguments.of(Invokable.from(Constant.BRIDGE_SAMPLE_THREE_A_METHOD), TypeToken.of(BridgeSampleThree.B.class)),
                Arguments.of(Invokable.from(Constant.BRIDGE_SAMPLE_THREE_A_METHOD), TypeToken.of(BridgeSampleThree.C.class)),
                Arguments.of(Invokable.from(Constant.BRIDGE_SAMPLE_THREE_B_BRIDGE_METHOD), TypeToken.of(BridgeSampleThree.C.class)));
    }
}