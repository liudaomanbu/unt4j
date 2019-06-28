package org.caotc.unit4j.core.util;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.Invokable;
import com.google.common.reflect.TypeToken;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.caotc.unit4j.core.constant.StringConstant;
//TODO 将所有方法优化到只有一次流操作

/**
 * 反射工具类
 *
 * @author caotc
 * @date 2019-05-10
 * @since 1.0.0
 */
@UtilityClass
public class ReflectionUtil {

  /**
   * get/set方法的命名风格枚举
   *
   * @author caotc
   * @date 2019-05-29
   * @since 1.0.0
   */
  public enum MethodNameStyle {
    /**
     * 默认的javaBean规范风格
     */
    JAVA_BEAN {
      private static final String GET_METHOD_PREFIX = "get";
      private static final String IS_METHOD_PREFIX = "is";
      private static final String SET_METHOD_PREFIX = "set";

      @Override
      public boolean getInvokableNameMatches(@NonNull Invokable<?, ?> invokable) {
        return invokable.getName().startsWith(GET_METHOD_PREFIX)
            || (boolean.class.equals(invokable.getReturnType().getRawType()) && invokable.getName()
            .startsWith(IS_METHOD_PREFIX));
      }

      @Override
      protected boolean setInvokableNameMatches(@NonNull Invokable<?, ?> invokable) {
        return invokable.getName().startsWith(SET_METHOD_PREFIX);
      }

      @NonNull
      @Override
      public String getMethodNameFromField(@NonNull Field field) {
        String methodFieldName = CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, field.getName());
        String prefix =
            boolean.class.equals(field.getType()) ? IS_METHOD_PREFIX : GET_METHOD_PREFIX;
        return StringConstant.EMPTY_JOINER.join(prefix, methodFieldName);
      }

      @Override
      public @NonNull String fieldNameFromGetInvokable(@NonNull Invokable<?, ?> getInvokable) {
        String prefix = getInvokable.getName().startsWith(GET_METHOD_PREFIX) ? GET_METHOD_PREFIX
            : IS_METHOD_PREFIX;
        String methodFieldName = getInvokable.getName().replaceFirst(prefix, StringConstant.EMPTY);
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, methodFieldName);
      }

      @Override
      public @NonNull String setMethodNameFromField(@NonNull Field field) {
        String methodFieldName = CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, field.getName());
        return StringConstant.EMPTY_JOINER.join(SET_METHOD_PREFIX, methodFieldName);
      }

      @Override
      public @NonNull String fieldNameFromSetInvokable(@NonNull Invokable<?, ?> setInvokable) {
        String methodFieldName = setInvokable.getName()
            .replaceFirst(SET_METHOD_PREFIX, StringConstant.EMPTY);
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, methodFieldName);
      }

    },
    /**
     * 流式风格,get/set方法名和属性名称一样
     */
    FLUENT {
      @Override
      public boolean getInvokableNameMatches(@NonNull Invokable<?, ?> invokable) {
        return true;
      }

      @Override
      protected boolean setInvokableNameMatches(@NonNull Invokable<?, ?> invokable) {
        return true;
      }

      @NonNull
      @Override
      public String getMethodNameFromField(@NonNull Field field) {
        return field.getName();
      }

      @Override
      public @NonNull String fieldNameFromGetInvokable(@NonNull Invokable<?, ?> getInvokable) {
        return getInvokable.getName();
      }

      @Override
      public @NonNull String setMethodNameFromField(@NonNull Field field) {
        return field.getName();
      }

      @Override
      public @NonNull String fieldNameFromSetInvokable(@NonNull Invokable<?, ?> setInvokable) {
        return setInvokable.getName();
      }
    };

    /**
     * 检查传入方法是否是属于该命名风格的get方法
     *
     * @param method 需要检查的方法
     * @return 是否是属于该命名风格的get方法
     * @author caotc
     * @date 2019-05-23
     * @since 1.0.0
     */
    public boolean isGetMethod(@NonNull Method method) {
      return isGetInvokable(Invokable.from(method));
    }

    /**
     * 检查传入方法是否是属于该命名风格的get方法
     *
     * @param invokable 需要检查的方法
     * @return 是否是属于该命名风格的get方法
     * @author caotc
     * @date 2019-05-23
     * @since 1.0.0
     */
    public boolean isGetInvokable(@NonNull Invokable<?, ?> invokable) {
      return !invokable.isStatic() && invokable.getParameters().isEmpty()
          && getInvokableNameMatches(invokable);
    }

    /**
     * 检查传入方法是否是属于该命名风格的set方法
     *
     * @param method 需要检查的方法
     * @return 是否是属于该命名风格的set方法
     * @author caotc
     * @date 2019-05-23
     * @since 1.0.0
     */
    public boolean isSetMethod(@NonNull Method method) {
      return isSetInvokable(Invokable.from(method));
    }

    /**
     * 检查传入方法是否是属于该命名风格的set方法
     *
     * @param invokable 需要检查的方法
     * @return 是否是属于该命名风格的set方法
     * @author caotc
     * @date 2019-05-23
     * @since 1.0.0
     */
    public boolean isSetInvokable(@NonNull Invokable<?, ?> invokable) {
      return !invokable.isStatic() && invokable.getParameters().size() == 1
          && setInvokableNameMatches(invokable);
    }

    /**
     * 获取传入属性的该命名风格的get方法名
     *
     * @param field 属性
     * @return 该命名风格的get方法名
     * @author caotc
     * @date 2019-05-23
     * @since 1.0.0
     */
    @NonNull
    public abstract String getMethodNameFromField(@NonNull Field field);

    /**
     * 获取传入get方法的对应的属性名称
     *
     * @param getMethod get方法
     * @return 对应的属性名称
     * @author caotc
     * @date 2019-05-23
     * @apiNote 传入的参数必须是该命名风格的get方法, 不可传入普通方法
     * @since 1.0.0
     */
    @NonNull
    public String fieldNameFromGetMethod(@NonNull Method getMethod) {
      return fieldNameFromGetInvokable(Invokable.from(getMethod));
    }

    /**
     * 获取传入get方法的对应的属性名称
     *
     * @param getInvokable get方法
     * @return 对应的属性名称
     * @author caotc
     * @date 2019-05-23
     * @apiNote 传入的参数必须是该命名风格的get方法, 不可传入普通方法
     * @since 1.0.0
     */
    @NonNull
    public abstract String fieldNameFromGetInvokable(@NonNull Invokable<?, ?> getInvokable);

    /**
     * 获取传入属性的该命名风格的set方法名
     *
     * @param field 属性
     * @return 该命名风格的set方法名
     * @author caotc
     * @date 2019-05-23
     * @since 1.0.0
     */
    @NonNull
    public abstract String setMethodNameFromField(@NonNull Field field);

    /**
     * 获取传入set方法的对应的属性名称
     *
     * @param setMethod set方法
     * @return 对应的属性名称
     * @author caotc
     * @date 2019-05-23
     * @apiNote 传入的参数必须是该命名风格的set方法, 不可传入普通方法
     * @since 1.0.0
     */
    @NonNull
    public String fieldNameFromSetMethod(@NonNull Method setMethod) {
      return fieldNameFromGetInvokable(Invokable.from(setMethod));
    }

    /**
     * 获取传入set方法的对应的属性名称
     *
     * @param setInvokable get方法
     * @return 对应的属性名称
     * @author caotc
     * @date 2019-05-23
     * @apiNote 传入的参数必须是该命名风格的set方法, 不可传入普通方法
     * @since 1.0.0
     */
    @NonNull
    public abstract String fieldNameFromSetInvokable(@NonNull Invokable<?, ?> setInvokable);

    /**
     * 检查传入方法是否在方法名上属于该命名风格的get方法
     *
     * @param invokable 需要检查的方法
     * @return 是否在方法名上属于该命名风格的get方法
     * @author caotc
     * @date 2019-05-23
     * @since 1.0.0
     */
    protected abstract boolean getInvokableNameMatches(@NonNull Invokable<?, ?> invokable);

    /**
     * 检查传入方法是否在方法名上属于该命名风格的set方法
     *
     * @param invokable 需要检查的方法
     * @return 是否在方法名上属于该命名风格的set方法
     * @author caotc
     * @date 2019-05-23
     * @since 1.0.0
     */
    protected abstract boolean setInvokableNameMatches(@NonNull Invokable<?, ?> invokable);
  }

  /**
   * 从传入的类中获取包括所有超类和接口的所有属性
   *
   * @param clazz 需要获取属性的类
   * @return 包括所有超类和接口的所有属性
   * @author caotc
   * @date 2019-05-10
   * @since 1.0.0
   */
  @NonNull
  public static ImmutableSet<Field> fieldsFromClass(@NonNull Class<?> clazz) {
    return TypeToken.of(clazz).getTypes().rawTypes().stream().map(Class::getDeclaredFields).flatMap(
        Arrays::stream).collect(ImmutableSet.toImmutableSet());
  }

  /**
   * 从传入的类中获取包括所有超类和接口的所有属性中属性名称为指定名称的属性
   *
   * @param clazz 需要获取属性的类
   * @param fieldName 指定属性名称
   * @return 包括所有超类和接口的所有属性中属性名称为指定名称的属性
   * @author caotc
   * @date 2019-05-22
   * @apiNote 在java中超类和字类可以定义相同名称的属性, 并且不会冲突, 因此返回的属性可能为复数
   * @since 1.0.0
   */
  @NonNull
  public static ImmutableSet<Field> fieldsFromClass(@NonNull Class<?> clazz,
      @NonNull String fieldName) {
    return fieldsFromClass(clazz).stream().filter(field -> fieldName.equals(field.getName()))
        .collect(ImmutableSet.toImmutableSet());
  }

  /**
   * 从传入的类中获取包括所有超类和接口的所有方法
   *
   * @param clazz 需要获取方法的类
   * @return 所有方法(包括所有超类和接口的方法)
   * @author caotc
   * @date 2019-05-10
   * @since 1.0.0
   */
  @NonNull
  public static ImmutableSet<Method> methodsFromClass(@NonNull Class<?> clazz) {
    return TypeToken.of(clazz).getTypes().rawTypes().stream().map(Class::getDeclaredMethods)
        .flatMap(Arrays::stream).collect(ImmutableSet.toImmutableSet());
  }

  /**
   * 根据指定的get方法名称格式从传入的类中获取包括所有超类和接口的所有get方法
   *
   * @param clazz 需要获取get方法的类
   * @return 所有超类和接口的所有get方法
   * @author caotc
   * @date 2019-05-22
   */
  @NonNull
  public ImmutableSet<Method> getMethodsFromClass(@NonNull Class<?> clazz) {
    return getMethodsFromClass(clazz, true);
  }

  /**
   * 根据指定的get方法名称格式从传入的类中获取包括所有超类和接口的所有get方法
   *
   * @param clazz 需要获取get方法的类
   * @param fieldExistCheck 是否检查有对应{@link Field}存在
   * @return 所有超类和接口的所有get方法
   * @author caotc
   * @date 2019-05-22
   */
  @NonNull
  public ImmutableSet<Method> getMethodsFromClass(@NonNull Class<?> clazz,
      boolean fieldExistCheck) {
    return getMethodsFromClass(clazz, fieldExistCheck, MethodNameStyle.values());
  }

  /**
   * 根据指定的get方法名称格式从传入的类中获取包括所有超类和接口的所有get方法
   *
   * @param clazz 需要获取get方法的类
   * @param fieldExistCheck 是否检查有对应{@link Field}存在
   * @param methodNameStyles 指定的get方法名称格式集合 {@link MethodNameStyle}
   * @return 所有超类和接口的所有get方法
   * @author caotc
   * @date 2019-05-22
   * @apiNote 如果只想获得JavaBean规范的get方法, {@code methodNameStyles}请传入{@link MethodNameStyle#JAVA_BEAN}
   */
  @NonNull
  public ImmutableSet<Method> getMethodsFromClass(@NonNull Class<?> clazz, boolean fieldExistCheck,
      @NonNull MethodNameStyle... methodNameStyles) {
    Stream<Method> methodStream = methodsFromClass(clazz).stream()
        .filter(method -> isGetMethod(method, methodNameStyles));
    if (fieldExistCheck) {
      ImmutableSet<ImmutableList<?>> getMethodKeys = fieldsFromClass(clazz).stream()
          .flatMap(field -> Arrays.stream(methodNameStyles)
              .map(methodNameStyle -> ImmutableList
                  .of(methodNameStyle.getMethodNameFromField(field), field.getType())))
          .collect(ImmutableSet.toImmutableSet());
      methodStream = methodStream.filter(getMethod -> getMethodKeys
          .contains(ImmutableList.of(getMethod.getName(), getMethod.getReturnType())));
    }
    return methodStream.collect(ImmutableSet.toImmutableSet());
  }

  /**
   * 根据指定的set方法名称格式从传入的类中获取包括所有超类和接口的所有set方法
   *
   * @param clazz 需要获取set方法的类
   * @return 所有超类和接口的所有set方法
   * @author caotc
   * @date 2019-05-22
   */
  @NonNull
  public ImmutableSet<Method> setMethodsFromClass(@NonNull Class<?> clazz) {
    return setMethodsFromClass(clazz, true);
  }

  /**
   * 根据指定的set方法名称格式从传入的类中获取包括所有超类和接口的所有set方法
   *
   * @param clazz 需要获取set方法的类
   * @param fieldExistCheck 是否检查有对应{@link Field}存在
   * @return 所有超类和接口的所有set方法
   * @author caotc
   * @date 2019-05-22
   */
  @NonNull
  public ImmutableSet<Method> setMethodsFromClass(@NonNull Class<?> clazz,
      boolean fieldExistCheck) {
    return setMethodsFromClass(clazz, fieldExistCheck, MethodNameStyle.values());
  }

  /**
   * 根据指定的set方法名称格式从传入的类中获取包括所有超类和接口的所有set方法
   *
   * @param clazz 需要获取set方法的类
   * @param fieldExistCheck 是否检查有对应{@link Field}存在
   * @param methodNameStyles 指定的set方法名称格式集合 {@link MethodNameStyle}
   * @return 所有超类和接口的所有set方法
   * @author caotc
   * @date 2019-05-22
   * @apiNote 如果只想获得JavaBean规范的set方法, {@code methodNameStyles}请传入{@link MethodNameStyle#JAVA_BEAN}
   */
  @NonNull
  public ImmutableSet<Method> setMethodsFromClass(@NonNull Class<?> clazz, boolean fieldExistCheck,
      @NonNull MethodNameStyle... methodNameStyles) {
    Stream<Method> methodStream = methodsFromClass(clazz).stream()
        .filter(method -> isSetMethod(method, methodNameStyles));
    if (fieldExistCheck) {
      ImmutableSet<ImmutableList<?>> setMethodKeys = fieldsFromClass(clazz).stream()
          .flatMap(field -> Arrays.stream(methodNameStyles)
              .map(methodNameStyle -> ImmutableList
                  .of(methodNameStyle.setMethodNameFromField(field), field.getType())))
          .collect(ImmutableSet.toImmutableSet());
      methodStream = methodStream.filter(setMethod -> setMethodKeys
          .contains(ImmutableList.of(setMethod.getName(), setMethod.getParameterTypes()[0])));
    }
    return methodStream.collect(ImmutableSet.toImmutableSet());
  }

  /**
   * 根据指定的get方法名称格式从传入的类中获取包括所有超类和接口的所有get方法中指定属性名称的get方法
   *
   * @param clazz 需要获取get方法的类
   * @param fieldName 指定属性名称
   * @return 包括所有超类和接口的所有get方法中指定属性名称的get方法
   * @author caotc
   * @date 2019-05-22
   */
  @NonNull
  public Optional<Method> getMethodFromClass(@NonNull Class<?> clazz,
      @NonNull String fieldName) {
    return getMethodFromClass(clazz, fieldName, true);
  }

  /**
   * 根据指定的get方法名称格式从传入的类中获取包括所有超类和接口的所有get方法中指定属性名称的get方法
   *
   * @param clazz 需要获取get方法的类
   * @param fieldName 指定属性名称
   * @param fieldExistCheck 是否检查有对应{@link Field}存在
   * @return 包括所有超类和接口的所有get方法中指定属性名称的get方法
   * @author caotc
   * @date 2019-05-22
   */
  @NonNull
  public Optional<Method> getMethodFromClass(@NonNull Class<?> clazz,
      @NonNull String fieldName, boolean fieldExistCheck) {
    return getMethodFromClass(clazz, fieldName, fieldExistCheck, MethodNameStyle.JAVA_BEAN);
  }

  /**
   * 根据指定的get方法名称格式从传入的类中获取包括所有超类和接口的所有get方法中指定属性名称的get方法
   *
   * @param clazz 需要获取get方法的类
   * @param methodNameStyle 指定的get方法名称格式 {@link MethodNameStyle}
   * @param fieldName 指定属性名称
   * @param fieldExistCheck 是否检查有对应{@link Field}存在
   * @return 包括所有超类和接口的所有get方法中指定属性名称的get方法
   * @author caotc
   * @date 2019-05-22
   */
  @NonNull
  public Optional<Method> getMethodFromClass(@NonNull Class<?> clazz,
      @NonNull String fieldName, boolean fieldExistCheck,
      @NonNull ReflectionUtil.MethodNameStyle methodNameStyle) {
    return getMethodsFromClass(clazz, fieldExistCheck, methodNameStyle).stream()
        .filter(getMethod -> fieldName.equals(methodNameStyle.fieldNameFromGetMethod(getMethod)))
        .findAny();
  }

  /**
   * 根据指定的set方法名称格式从传入的类中获取包括所有超类和接口的所有set方法中指定属性名称的set方法
   *
   * @param clazz 需要获取set方法的类
   * @param fieldName 指定属性名称
   * @return 包括所有超类和接口的所有set方法中指定属性名称的set方法
   * @author caotc
   * @date 2019-05-22
   */
  @NonNull
  public Optional<Method> setMethodFromClass(@NonNull Class<?> clazz,
      @NonNull String fieldName) {
    return setMethodFromClass(clazz, fieldName, true);
  }

  /**
   * 根据指定的set方法名称格式从传入的类中获取包括所有超类和接口的所有set方法中指定属性名称的set方法
   *
   * @param clazz 需要获取set方法的类
   * @param fieldName 指定属性名称
   * @param fieldExistCheck 是否检查有对应{@link Field}存在
   * @return 包括所有超类和接口的所有set方法中指定属性名称的set方法
   * @author caotc
   * @date 2019-05-22
   */
  @NonNull
  public Optional<Method> setMethodFromClass(@NonNull Class<?> clazz,
      @NonNull String fieldName, boolean fieldExistCheck) {
    return setMethodFromClass(clazz, fieldName, fieldExistCheck, MethodNameStyle.JAVA_BEAN);
  }

  /**
   * 根据指定的set方法名称格式从传入的类中获取包括所有超类和接口的所有set方法中指定属性名称的set方法
   *
   * @param clazz 需要获取set方法的类
   * @param methodNameStyle 指定的set方法名称格式 {@link MethodNameStyle}
   * @param fieldName 指定属性名称
   * @param fieldExistCheck 是否检查有对应{@link Field}存在
   * @return 包括所有超类和接口的所有set方法中指定属性名称的set方法
   * @author caotc
   * @date 2019-05-22
   */
  @NonNull
  public Optional<Method> setMethodFromClass(@NonNull Class<?> clazz,
      @NonNull String fieldName, boolean fieldExistCheck,
      @NonNull ReflectionUtil.MethodNameStyle methodNameStyle) {
    return setMethodsFromClass(clazz, fieldExistCheck, methodNameStyle).stream()
        .filter(setMethod -> fieldName.equals(methodNameStyle.fieldNameFromSetMethod(setMethod)))
        .findAny();
  }

  /**
   * 从传入的类中获取包括所有超类和接口的所有get方法与属性的包装{@link PropertyGetter}
   *
   * @param clazz 需要获取get方法的类
   * @return 包括所有超类和接口的所有get方法与属性的包装 {@link PropertyGetter}
   * @author caotc
   * @date 2019-05-10
   * @since 1.0.0
   */
  @NonNull
  public static <T> ImmutableSet<PropertyGetter<T, ?>> propertyGettersFromClass(
      @NonNull Class<T> clazz) {
    return propertyGettersFromClass(clazz, true);
  }

  /**
   * 从传入的类中获取包括所有超类和接口的所有get方法与属性的包装{@link PropertyGetter}
   *
   * @param clazz 需要获取get方法的类
   * @param fieldExistCheck 是否检查是否有对应{@link Field}存在
   * @return 包括所有超类和接口的所有get方法与属性的包装 {@link PropertyGetter}
   * @author caotc
   * @date 2019-05-10
   * @since 1.0.0
   */
  @NonNull
  public static <T> ImmutableSet<PropertyGetter<T, ?>> propertyGettersFromClass(
      @NonNull Class<T> clazz, boolean fieldExistCheck) {
    return propertyGettersFromClass(clazz, fieldExistCheck, MethodNameStyle.values());
  }

  /**
   * 从传入的类中获取包括所有超类和接口的所有get方法与属性的包装{@link PropertyGetter}
   *
   * @param clazz 需要获取get方法的类
   * @param fieldExistCheck 是否检查是否有对应{@link Field}存在
   * @param methodNameStyles get方法格式集合
   * @return 包括所有超类和接口的所有get方法与属性的包装 {@link PropertyGetter}
   * @author caotc
   * @date 2019-05-10
   * @apiNote 如果只想要获取JavaBean规范的get方法, {@code methodNameStyles}参数使用{@link
   * MethodNameStyle#JAVA_BEAN}
   * @since 1.0.0
   */
  @SuppressWarnings("unchecked")
  @NonNull
  public static <T, R> ImmutableSet<PropertyGetter<T, ?>> propertyGettersFromClass(
      @NonNull Class<T> clazz, boolean fieldExistCheck,
      @NonNull MethodNameStyle... methodNameStyles) {

    Function<PropertyGetter<T, ?>, ImmutableList<?>> propertyGetterToKeyFunction = propertyGetter -> ImmutableList
        .of(propertyGetter.propertyName(), propertyGetter.propertyType());

    Stream<PropertyGetter<T, ?>> getInvokablePropertyGetters = getMethodsFromClass(clazz,
        fieldExistCheck, methodNameStyles).stream()
        .flatMap(getMethod -> Arrays.stream(methodNameStyles)
            .filter(methodNameStyle -> methodNameStyle.isGetMethod(getMethod))
            .map(methodNameStyle -> PropertyGetter.create(getMethod, methodNameStyle)));

    Stream<PropertyGetter<T, ?>> fieldPropertyGetters = fieldsFromClass(
        clazz).stream().map(PropertyGetter::create);

    ImmutableListMultimap<@NonNull ImmutableList<?>, PropertyGetter<T, ?>> propertyGetterMultimap =
        Stream.concat(fieldPropertyGetters, getInvokablePropertyGetters)
            .collect(ImmutableListMultimap
                .toImmutableListMultimap(propertyGetterToKeyFunction, Function.identity()));

    return propertyGetterMultimap.asMap().values().stream()
        .map(propertyGetters -> propertyGetters.stream().map(o -> (PropertyGetter<T, ?>) o))
        .map(PropertyGetter::create).collect(ImmutableSet.toImmutableSet());
  }

  /**
   * 从传入的类中获取包括所有超类和接口的所有get方法与属性的包装{@link PropertyGetter}
   *
   * @param clazz 需要获取get方法的类
   * @param fieldName 指定属性名称
   * @return 包括所有超类和接口的所有get方法与属性的包装 {@link PropertyGetter}
   * @author caotc
   * @date 2019-05-10
   * @since 1.0.0
   */
  @NonNull
  public static <T, R> Optional<PropertyGetter<T, R>> propertyGetterFromClass(
      @NonNull Class<T> clazz, @NonNull String fieldName) {
    return propertyGetterFromClass(clazz, fieldName, true);
  }

  /**
   * 从传入的类中获取包括所有超类和接口的所有get方法与属性的包装{@link PropertyGetter}
   *
   * @param clazz 需要获取get方法的类
   * @param fieldName 指定属性名称
   * @param fieldExistCheck 是否检查是否有对应{@link Field}存在
   * @return 包括所有超类和接口的所有get方法与属性的包装 {@link PropertyGetter}
   * @author caotc
   * @date 2019-05-10
   * @since 1.0.0
   */
  @NonNull
  public static <T, R> Optional<PropertyGetter<T, R>> propertyGetterFromClass(
      @NonNull Class<T> clazz, @NonNull String fieldName, boolean fieldExistCheck) {
    return propertyGetterFromClass(clazz, fieldName, fieldExistCheck, MethodNameStyle.values());
  }

  /**
   * 从传入的类中获取包括所有超类和接口的所有get方法与属性的包装{@link PropertyGetter}
   *
   * @param clazz 需要获取get方法的类
   * @param fieldName 指定属性名称
   * @param fieldExistCheck 是否检查是否有对应{@link Field}存在
   * @param methodNameStyles get方法格式集合
   * @return 包括所有超类和接口的所有get方法与属性的包装 {@link PropertyGetter}
   * @author caotc
   * @date 2019-05-10
   * @apiNote 如果只想要获取JavaBean规范的get方法, {@code methodNameStyles}参数使用{@link
   * MethodNameStyle#JAVA_BEAN}
   * @since 1.0.0
   */
  @SuppressWarnings("unchecked")
  @NonNull
  public static <T, R> Optional<PropertyGetter<T, R>> propertyGetterFromClass(
      @NonNull Class<T> clazz, @NonNull String fieldName, boolean fieldExistCheck,
      @NonNull MethodNameStyle... methodNameStyles) {
    return propertyGettersFromClass(clazz, fieldExistCheck, methodNameStyles).stream()
        .filter(propertyGetter -> propertyGetter.propertyName().equals(fieldName))
        .map(propertyGetter -> (PropertyGetter<T, R>) propertyGetter)
        .findAny();
  }

  /**
   * 从传入的类中获取包括所有超类和接口的所有set方法与属性的包装{@link PropertySetter}
   *
   * @param clazz 需要获取set方法的类
   * @return 包括所有超类和接口的所有set方法与属性的包装 {@link PropertySetter}
   * @author caotc
   * @date 2019-05-10
   * @since 1.0.0
   */
  @NonNull
  public static <T> ImmutableSet<PropertySetter<T, ?>> propertySettersFromClass(
      @NonNull Class<T> clazz) {
    return propertySettersFromClass(clazz, true);
  }

  /**
   * 从传入的类中获取包括所有超类和接口的所有set方法与属性的包装{@link PropertySetter}
   *
   * @param clazz 需要获取set方法的类
   * @param fieldExistCheck 是否检查是否有对应{@link Field}存在
   * @return 包括所有超类和接口的所有set方法与属性的包装 {@link PropertySetter}
   * @author caotc
   * @date 2019-05-10
   * @since 1.0.0
   */
  @NonNull
  public static <T> ImmutableSet<PropertySetter<T, ?>> propertySettersFromClass(
      @NonNull Class<T> clazz, boolean fieldExistCheck) {
    return propertySettersFromClass(clazz, fieldExistCheck, MethodNameStyle.values());
  }

  /**
   * 从传入的类中获取包括所有超类和接口的所有set方法与属性的包装{@link PropertySetter}
   *
   * @param clazz 需要获取set方法的类
   * @param fieldExistCheck 是否检查是否有对应{@link Field}存在
   * @param methodNameStyles set方法格式集合
   * @return 包括所有超类和接口的所有set方法与属性的包装 {@link PropertySetter}
   * @author caotc
   * @date 2019-05-10
   * @apiNote 如果只想要获取JavaBean规范的set方法, {@code methodNameStyles}参数使用{@link
   * MethodNameStyle#JAVA_BEAN}
   * @since 1.0.0
   */
  @SuppressWarnings("unchecked")
  @NonNull
  public static <T> ImmutableSet<PropertySetter<T, ?>> propertySettersFromClass(
      @NonNull Class<T> clazz, boolean fieldExistCheck,
      @NonNull MethodNameStyle... methodNameStyles) {
    Function<PropertySetter<T, ?>, ImmutableList<?>> propertySetterToKeyFunction = propertySetter -> ImmutableList
        .of(propertySetter.propertyName(), propertySetter.propertyType());

    Stream<PropertySetter<T, ?>> setInvokablePropertySetters = getMethodsFromClass(clazz,
        fieldExistCheck, methodNameStyles).stream()
        .flatMap(getMethod -> Arrays.stream(methodNameStyles)
            .filter(methodNameStyle -> methodNameStyle.isSetMethod(getMethod))
            .map(methodNameStyle -> PropertySetter.create(getMethod, methodNameStyle)));

    Stream<PropertySetter<T, ?>> fieldPropertySetters = fieldsFromClass(
        clazz).stream().map(PropertySetter::create);

    ImmutableListMultimap<@NonNull ImmutableList<?>, PropertySetter<T, ?>> propertySetterMultimap =
        Stream.concat(fieldPropertySetters, setInvokablePropertySetters)
            .collect(ImmutableListMultimap
                .toImmutableListMultimap(propertySetterToKeyFunction, Function.identity()));

    return propertySetterMultimap.asMap().values().stream()
        .map(propertySetters -> propertySetters.stream().map(o -> (PropertySetter<T, ?>) o))
        .map(PropertySetter::create).collect(ImmutableSet.toImmutableSet());
  }

  /**
   * 从传入的类中获取包括所有超类和接口的所有set方法与属性的包装{@link PropertySetter}
   *
   * @param clazz 需要获取set方法的类
   * @param fieldName 指定属性名称
   * @return 包括所有超类和接口的所有set方法与属性的包装 {@link PropertySetter}
   * @author caotc
   * @date 2019-05-10
   * @since 1.0.0
   */
  @NonNull
  public static <T, R> Optional<PropertySetter<T, R>> propertySetterFromClass(
      @NonNull Class<T> clazz, @NonNull String fieldName) {
    return propertySetterFromClass(clazz, fieldName, true);
  }

  /**
   * 从传入的类中获取包括所有超类和接口的所有set方法与属性的包装{@link PropertySetter}
   *
   * @param clazz 需要获取set方法的类
   * @param fieldName 指定属性名称
   * @param fieldExistCheck 是否检查是否有对应{@link Field}存在
   * @return 包括所有超类和接口的所有set方法与属性的包装 {@link PropertySetter}
   * @author caotc
   * @date 2019-05-10
   * @since 1.0.0
   */
  @NonNull
  public static <T, R> Optional<PropertySetter<T, R>> propertySetterFromClass(
      @NonNull Class<T> clazz, @NonNull String fieldName, boolean fieldExistCheck) {
    return propertySetterFromClass(clazz, fieldName, fieldExistCheck, MethodNameStyle.values());
  }

  /**
   * 从传入的类中获取包括所有超类和接口的所有set方法与属性的包装{@link PropertySetter}
   *
   * @param clazz 需要获取set方法的类
   * @param fieldName 指定属性名称
   * @param fieldExistCheck 是否检查是否有对应{@link Field}存在
   * @param methodNameStyles set方法格式集合
   * @return 包括所有超类和接口的所有set方法与属性的包装 {@link PropertySetter}
   * @author caotc
   * @date 2019-05-10
   * @apiNote 如果只想要获取JavaBean规范的set方法, {@code methodNameStyles}参数使用{@link
   * MethodNameStyle#JAVA_BEAN}
   * @since 1.0.0
   */
  @SuppressWarnings("unchecked")
  @NonNull
  public static <T, R> Optional<PropertySetter<T, R>> propertySetterFromClass(
      @NonNull Class<T> clazz, @NonNull String fieldName, boolean fieldExistCheck,
      @NonNull MethodNameStyle... methodNameStyles) {
    return propertySettersFromClass(clazz, fieldExistCheck, methodNameStyles).stream()
        .filter(propertySetter -> propertySetter.propertyName().equals(fieldName))
        .map(propertySetter -> (PropertySetter<T, R>) propertySetter)
        .findAny();
  }

  /**
   * 从传入的类中获取包括所有超类和接口的所有属性存取器
   *
   * @param clazz 需要获取属性存取器的类
   * @param fieldExistCheck 是否检查是否有对应{@link Field}存在
   * @param methodNameStyles set/get方法格式集合
   * @return 包括所有超类和接口的所有属性存取器
   * @author caotc
   * @date 2019-05-10
   * @apiNote 如果只想要获取JavaBean规范的get/set方法, {@code methodNameStyles}参数使用{@link
   * MethodNameStyle#JAVA_BEAN}
   * @since 1.0.0
   */
//  @NonNull
//  public static <T> ImmutableSet<PropertyAccessor<T, ?>> propertyAccessorsFromClass(
//      @NonNull Class<T> clazz, boolean fieldExistCheck,
//      @NonNull MethodNameStyle... methodNameStyles) {
//    ImmutableMap<ImmutableList<?>, PropertyGetter<T, ?>> keyToPropertyGetters = propertyGettersFromClass(
//        clazz,
//        fieldExistCheck, methodNameStyles).stream()
//        .collect(ImmutableMap.toImmutableMap(
//            propertyGetter -> ImmutableList.of(propertyGetter.name(), propertyGetter.type()),
//            Function.identity()));
//    return propertySettersFromClass(clazz,
//        fieldExistCheck, methodNameStyles).stream()
//        .filter(propertySetter -> keyToPropertyGetters.containsKey(ImmutableList.of(propertySetter.name(),propertySetter.type())))
//    .map(propertySetter -> PropertyAccessor.create(propertySetter,(PropertyGetter<T, ?>)keyToPropertyGetters.get(ImmutableList.of(propertySetter.name(),propertySetter.type()))))
//    .collect(ImmutableSet.toImmutableSet());
//  }

  /**
   * 检查传入方法是否是get方法
   *
   * @param method 需要检查的方法
   * @param methodNameStyles 方法风格
   * @return 是否是get方法
   * @author caotc
   * @date 2019-05-23
   * @apiNote 这里所指的get方法并不是专指JavaBean规范的get方法, 而是所有获取属性的方法, 符合任意{@link MethodNameStyle}检查即视为get方法.
   * 所以如果只想要判断是否是JavaBean规范的get方法请使用 {@link MethodNameStyle#JAVA_BEAN#isGetMethod(Method)}
   * @since 1.0.0
   */
  public static boolean isGetMethod(@NonNull Method method,
      @NonNull MethodNameStyle... methodNameStyles) {
    return isGetInvokable(Invokable.from(method), methodNameStyles);
  }

  /**
   * 检查传入方法是否是get方法
   *
   * @param invokable 需要检查的方法
   * @param methodNameStyles 方法风格
   * @return 是否是get方法
   * @author caotc
   * @date 2019-05-23
   * @apiNote 这里所指的get方法并不是专指JavaBean规范的get方法, 而是所有获取属性的方法, 符合任意{@link MethodNameStyle}检查即视为get方法.
   * 所以如果只想要判断是否是JavaBean规范的get方法请使用 {@link MethodNameStyle#JAVA_BEAN#isGetInvokable(Invokable)}
   * @since 1.0.0
   */
  public static boolean isGetInvokable(@NonNull Invokable<?, ?> invokable,
      @NonNull MethodNameStyle... methodNameStyles) {
    return Arrays.stream(methodNameStyles)
        .anyMatch(methodNameStyle -> methodNameStyle.isGetInvokable(invokable));
  }

  /**
   * 检查传入方法是否是set方法
   *
   * @param method 需要检查的方法
   * @param methodNameStyles 方法风格
   * @return 是否是set方法
   * @author caotc
   * @date 2019-05-23
   * @apiNote 这里所指的set方法并不是专指JavaBean规范的set方法, 而是所有获取属性的方法, 符合任意{@link MethodNameStyle}检查即视为set方法.
   * 所以如果只想要判断是否是JavaBean规范的set方法请使用 {@link MethodNameStyle#JAVA_BEAN#isSetMethod(Method)}
   * @since 1.0.0
   */
  public static boolean isSetMethod(@NonNull Method method,
      @NonNull MethodNameStyle... methodNameStyles) {
    return isSetInvokable(Invokable.from(method), methodNameStyles);
  }

  /**
   * 检查传入方法是否是set方法
   *
   * @param invokable 需要检查的方法
   * @param methodNameStyles 方法风格
   * @return 是否是set方法
   * @author caotc
   * @date 2019-05-23
   * @apiNote 这里所指的set方法并不是专指JavaBean规范的set方法, 而是所有获取属性的方法, 符合任意{@link MethodNameStyle}检查即视为set方法.
   * 所以如果只想要判断是否是JavaBean规范的set方法请使用 {@link MethodNameStyle#JAVA_BEAN#isSetInvokable(Invokable)}
   * @since 1.0.0
   */
  public static boolean isSetInvokable(@NonNull Invokable<?, ?> invokable,
      @NonNull MethodNameStyle... methodNameStyles) {
    return Arrays.stream(methodNameStyles)
        .anyMatch(methodNameStyle -> methodNameStyle.isSetInvokable(invokable));
  }
}
