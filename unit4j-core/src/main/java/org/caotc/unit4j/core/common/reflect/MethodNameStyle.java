package org.caotc.unit4j.core.common.reflect;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.Invokable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.function.Function;
import lombok.NonNull;
import org.caotc.unit4j.core.common.base.CaseFormat;
import org.caotc.unit4j.core.constant.StringConstant;

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
  private static final Function<Invokable<?, ?>, ImmutableList<?>> INVOKABLE_TO_SIGN = invokable -> ImmutableList
      .of(invokable.getName(), invokable.getParameters());

  private static final ImmutableSet<ImmutableList<?>> OBJECT_METHOD_SIGNS = Arrays
      .stream(Object.class.getDeclaredMethods())
      .map(Invokable::from)
      .map(INVOKABLE_TO_SIGN::apply).collect(ImmutableSet.toImmutableSet());

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
    return !invokable.isStatic()
        && !OBJECT_METHOD_SIGNS.contains(INVOKABLE_TO_SIGN.apply(invokable))
        && invokable.getParameters().isEmpty()
        && !invokable.getReturnType().getRawType().equals(void.class)
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
    return !invokable.isStatic()
        && !OBJECT_METHOD_SIGNS.contains(INVOKABLE_TO_SIGN.apply(invokable))
        && (invokable.getReturnType().getRawType().equals(void.class) || invokable.getReturnType()
        .equals(invokable.getOwnerType()))
        && invokable.getParameters().size() == 1
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
    return fieldNameFromSetInvokable(Invokable.from(setMethod));
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
