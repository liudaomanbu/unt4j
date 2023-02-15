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
    S source;

    BaseInvokable(@NonNull S source) {
        super(source);
        this.source = source;
    }

    @Override
    @NonNull
    public abstract Class<? super O> declaringClass();

    @Override
    public @NonNull ImmutableList<Type> genericParameterTypes() {
        return Arrays.stream(source().getGenericParameterTypes()).collect(ImmutableList.toImmutableList());
    }

    @Override
    public boolean isOverridden(@NonNull Invokable<?, ?> other) {
        //same method can't override
        if (Objects.equals(source(), other.source())
                //same declaring type can't override
                || Objects.equals(declaringType(), other.declaringType())
                || !Objects.equals(getName(), other.getName())
                //overriding method must give more or equal access than the overridden method
                || accessLevel().isMore(other.accessLevel())
                || !ownerType().isSupertypeOf(other.ownerType())
                || !isOverridableIn(other.declaringType())) {
            return false;
        }

        if (isBridge()) {
            //bridge invokable source must be method
            Invokable<O, ?> bridgedInvokable = Invokable.from(BridgeMethodResolver.findBridgedMethod((Method) source()), ownerType());
            return bridgedInvokable.isOverridden(other);//todo 确认BridgeMethodResolver.isVisibilityBridgeMethodPair的case
        }
        if (other.isBridge()) {
            //bridge invokable source must be method
            other = Invokable.from(BridgeMethodResolver.findBridgedMethod((Method) other.source()), other.ownerType());
            return isOverridden(other);
        }

        return isReturnTypeTheSameAs(other) &&
                areParametersTheSameAs(other);
    }

    boolean isOverridableIn(TypeToken<?> type) {
        if (!isOverridable()) return false;
        if (!isSubclassVisible()) return false;
        if (!declaringType().isSupertypeOf(type)) return false;

        if (isPublic()) return true;
        if (isPackageVisible() && type.getRawType().getPackage() == declaringClass().getPackage()) return true;

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
