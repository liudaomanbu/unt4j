package org.caotc.unit4j.core.common.util;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import com.google.common.collect.Streams;
import com.google.common.reflect.TypeToken;
import lombok.experimental.UtilityClass;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author caotc
 * @date 2023-02-21
 * @since 1.0.0
 */
@UtilityClass
public class ClassUtil {
    public static Set<Class<?>> lowestCommonSuperclasses(Class<?>... classes) {
        return lowestCommonSuperclasses(Arrays.stream(classes).collect(ImmutableSet.toImmutableSet()));
    }

    public static Set<Class<?>> lowestCommonSuperclasses(Iterable<Class<?>> classes) {
        if (Iterables.isEmpty(classes)) {
            return Sets.newHashSet();
        }
        final Set<Class<?>> result = TypeToken.of(classes.iterator().next()).getTypes().stream()
                .map(TypeToken::getRawType)
                .filter(sup -> Streams.stream(classes).allMatch(sup::isAssignableFrom))
                .collect(ImmutableSet.toImmutableSet());
        return result.stream()
                .filter(type1 -> result.stream()
                        .filter(type2 -> !Objects.equals(type1, type2))
                        .noneMatch(type2 -> type1.isAssignableFrom(type2)))
                .collect(Collectors.toSet());
    }

    public static Set<TypeToken<?>> lowestCommonAncestors(Class<?>... classes) {
        return lowestCommonAncestors(Arrays.stream(classes).map(TypeToken::of).collect(ImmutableSet.toImmutableSet()));
    }

    public static Set<TypeToken<?>> lowestCommonAncestors(TypeToken<?>... types) {
        return lowestCommonAncestors(Arrays.stream(types).collect(ImmutableSet.toImmutableSet()));
    }


    public static Set<TypeToken<?>> lowestCommonAncestors(Iterable<? extends TypeToken<?>> types) {
        if (Iterables.isEmpty(types)) {
            return Sets.newHashSet();
        }

        final Set<? extends TypeToken<?>> result = types.iterator().next().getTypes().stream()
                .filter(sup -> Streams.stream(types).allMatch(sup::isSupertypeOf))
                .collect(ImmutableSet.toImmutableSet());
        return result.stream()
                .filter(type1 -> result.stream()
                        .filter(type2 -> !Objects.equals(type1, type2))
                        .noneMatch(type2 -> type1.isSupertypeOf(type2)))
                .collect(Collectors.toSet());
    }

    public static TypeToken<?> unwrapContainer(TypeToken<?> type) {
        if (type.isArray()) {
            return type.getComponentType();
        }
        if (List.class.equals(type.getRawType())) {
            return type.resolveType(List.class.getTypeParameters()[0]);
        }
        if (Set.class.equals(type.getRawType())) {
            return type.resolveType(Set.class.getTypeParameters()[0]);
        }
        if (Collection.class.equals(type.getRawType())) {
            return type.resolveType(Collection.class.getTypeParameters()[0]);
        }
        return type;
    }
}
