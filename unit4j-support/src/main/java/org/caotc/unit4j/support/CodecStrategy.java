package org.caotc.unit4j.support;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import java.util.Set;
import java.util.function.Function;
import lombok.NonNull;
import org.caotc.unit4j.core.util.CaseFormat;
import org.caotc.unit4j.core.util.PropertyGetter;
import org.caotc.unit4j.support.SerializeCommand.Type;
import org.caotc.unit4j.support.SerializeCommands.SerializeCommandsBuilder;

/**
 * 序列化、反序列化时的策略
 *
 * @author caotc
 * @date 2019-04-24
 * @since 1.0.0
 */
public enum CodecStrategy {
  /**
   * 像普通对象一样序列化，即输出为一个json对象，里面包含所有属性
   */
  OBJECT {
    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T> SerializeCommands createSerializeCommands(@NonNull T value,
        @NonNull Function<ImmutableList<String>, String> fieldNameConverter,
        @NonNull Function<Class<? extends T>, ? extends Set<PropertyGetter<T, ?>>> fieldWrapperConverter,
        @NonNull CaseFormat fieldNameFormat) {
      SerializeCommandsBuilder builder = SerializeCommands.builder()
          .command(SerializeCommand.START_OBJECT);
      ImmutableSet<SerializeCommand> writeFieldCommands = fieldWrapperConverter
          .apply((Class<? extends T>) value.getClass()).stream()
          .map(fieldWrapper -> SerializeCommand
              .create(Type.WRITE_FIELD,
                  fieldNameConverter.apply(fieldNameFormat.split(fieldWrapper.name())),
                  fieldWrapper.get(value).orElse(null))).collect(ImmutableSet.toImmutableSet());
      writeFieldCommands.stream().limit(writeFieldCommands.size() - 1)
          .forEach(writeFieldCommand -> builder.command(writeFieldCommand)
              .command(SerializeCommand.WRITE_FIELD_SEPARATOR));
      writeFieldCommands.stream().skip(writeFieldCommands.size() - 1).forEach(builder::command);

      return builder.command(SerializeCommand.END_OBJECT).build();
    }
  },
  /**
   * 只输出一个属性的值
   */
  VALUE {
    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T> SerializeCommands createSerializeCommands(@NonNull T value,
        @NonNull Function<ImmutableList<String>, String> fieldNameConverter,
        @NonNull Function<Class<? extends T>, ? extends Set<PropertyGetter<T, ?>>> fieldWrapperConverter,
        @NonNull CaseFormat fieldNameFormat) {
      Set<PropertyGetter<T, ?>> propertyGetters = fieldWrapperConverter
          .apply((Class<? extends T>) value.getClass());
      Preconditions.checkArgument(propertyGetters.size() == 1,
          "value strategy serialize must have one fieldWrapper,but now fieldWrappers is %s",
          propertyGetters);
      return SerializeCommands.builder()
          .command(SerializeCommand.create(Type.WRITE_VALUE, null,
              Iterables.getOnlyElement(propertyGetters).get(value).orElse(null)))
          .build();
    }
  },
  /**
   * 与{@see OBJECT}一样输出所有属性, 但是改为扁平化输出，即自己不是一个对象，而是多个字段
   */
  FLAT {
    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T> SerializeCommands createSerializeCommands(@NonNull T value,
        @NonNull Function<ImmutableList<String>, String> fieldNameConverter,
        @NonNull Function<Class<? extends T>, ? extends Set<PropertyGetter<T, ?>>> fieldWrapperConverter,
        @NonNull CaseFormat fieldNameFormat) {
      SerializeCommandsBuilder builder = SerializeCommands.builder();
      ImmutableSet<SerializeCommand> writeFieldCommands = fieldWrapperConverter
          .apply((Class<? extends T>) value.getClass()).stream()
          .map(fieldWrapper -> SerializeCommand
              .create(Type.WRITE_FIELD,
                  fieldNameConverter.apply(fieldNameFormat.split(fieldWrapper.name())),
                  fieldWrapper.get(value).orElse(null))).collect(ImmutableSet.toImmutableSet());
      writeFieldCommands.stream().limit(writeFieldCommands.size() - 1)
          .forEach(writeFieldCommand -> builder.command(writeFieldCommand)
              .command(SerializeCommand.WRITE_FIELD_SEPARATOR));
      writeFieldCommands.stream().skip(writeFieldCommands.size() - 1).forEach(builder::command);

      return builder.command(SerializeCommand.REMOVE_ORIGINAL_FIELD).build();
    }

  };

  /**
   * 生成序列化指令
   *
   * @param value 序列化对象
   * @param fieldNameConverter 属性名称转换器
   * @param fieldWrapperConverter 序列化属性转换器
   * @return 序列化指令
   * @author caotc
   * @date 2019-05-29
   * @since 1.0.0
   */
  @NonNull
  public abstract <T> SerializeCommands createSerializeCommands(@NonNull T value,
      @NonNull Function<ImmutableList<String>, String> fieldNameConverter,
      @NonNull Function<Class<? extends T>, ? extends Set<PropertyGetter<T, ?>>> fieldWrapperConverter
      , @NonNull CaseFormat fieldNameFormat);

}
