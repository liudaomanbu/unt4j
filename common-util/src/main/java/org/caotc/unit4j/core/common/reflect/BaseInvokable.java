package org.caotc.unit4j.core.common.reflect;

import com.google.common.collect.ImmutableList;
import com.google.common.reflect.TypeToken;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.core.BridgeMethodResolver;

import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Objects;

/**
 * @param <S> source type
 * @param <O> owner type
 * @param <R> return type
 * @author caotc
 * @date 2022-11-22
 * @since 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@Getter
public abstract class BaseInvokable<S extends Executable, O, R> extends BaseElement implements Invokable<O, R> {
    S source;//todo 范型

    BaseInvokable(@NonNull S source) {
        super(source);
        this.source = source;
    }

    @Override
    @NonNull
    public abstract Class<? super O> getDeclaringClass();

    @Override
    public @NonNull ImmutableList<Type> genericParameterTypes() {
        return Arrays.stream(source().getGenericParameterTypes()).collect(ImmutableList.toImmutableList());
    }

    @Override
    public boolean isOverrideBy(@NonNull Invokable<?, ?> other) {
        if (source().equals(other.source())
                || declaringType().equals(other.declaringType())
                || !(ownerType().isSupertypeOf(other.ownerType())//todo
                && Objects.equals(getName(), other.getName())
                && isOverridableIn(other.getDeclaringClass())
                && accessLevel().compareTo(other.accessLevel()) <= 0)) {//todo accessLevel defined
            return false;
        }

        if (isBridge()) {
            //bridge invokable source must be method
            Invokable<O, ?> bridgedInvokable = Invokable.from(BridgeMethodResolver.findBridgedMethod((Method) source()), ownerType());
            return bridgedInvokable.isOverrideBy(other);//todo 确认BridgeMethodResolver.isVisibilityBridgeMethodPair的case
        }
        if (other.isBridge()) {
            //bridge invokable source must be method
            other = Invokable.from(BridgeMethodResolver.findBridgedMethod((Method) other.source()), other.ownerType());
            return isOverrideBy(other);
        }

        return isReturnTypeTheSameAs(other) &&
                areParametersTheSameAs(other);
    }

    boolean isOverridableIn(Class<?> cls) {
        if (!isOverridable()) return false;
        if (!isSubclassVisible()) return false;
        if (!getDeclaringClass().isAssignableFrom(cls)) return false;//todo

        if (isPublic()) return true;
        if (isPackageVisible() && cls.getPackage() == getDeclaringClass().getPackage()) return true;

        return false;
    }

    boolean areParametersTheSameAs(Invokable<?, ?> other) {
        ImmutableList<TypeToken<?>> myPrmTypes = parameters().stream().map(Parameter::type).collect(ImmutableList.toImmutableList());
        ImmutableList<TypeToken<?>> otherPrmTypes = other.parameters().stream().map(Parameter::type).collect(ImmutableList.toImmutableList());
        return myPrmTypes.equals(otherPrmTypes);
    }

    boolean isReturnTypeTheSameAs(Invokable<?, ?> other) {
        return other.returnType().equals(returnType());
    }

    boolean isAccessMoreRestrictiveThan(Member other) {
        return accessLevel().compareTo(other.accessLevel()) < 0;
    }

    boolean isAccessLessRestrictiveThan(Member other) {
        return accessLevel().compareTo(other.accessLevel()) > 0;
    }
}
