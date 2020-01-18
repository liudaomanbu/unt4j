/*
 * Copyright (C) 2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.caotc.unit4j.api.annotation;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import java.util.Set;
import java.util.function.Function;
import lombok.NonNull;
import org.caotc.unit4j.api.annotation.SerializeCommand.Type;
import org.caotc.unit4j.core.common.base.CaseFormat;
import org.caotc.unit4j.core.common.reflect.property.accessor.PropertyReader;

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
        @NonNull Function<Class<? extends T>, ? extends Set<PropertyReader<T, ?>>> fieldWrapperConverter,
        @NonNull CaseFormat fieldNameFormat) {
//      SerializeCommandsBuilder builder = SerializeCommands.builder()
//          .command(SerializeCommand.START_OBJECT);
//      ImmutableSet<SerializeCommand> writeFieldCommands = fieldWrapperConverter
//          .apply((Class<? extends T>) value.getClass()).stream()
//          .map(fieldWrapper -> SerializeCommand
//              .create(Type.WRITE_FIELD,
//                  fieldNameConverter.apply(fieldNameFormat.split(fieldWrapper.propertyName())),
//                  fieldWrapper.read(value).orElse(null))).collect(ImmutableSet.toImmutableSet());
//      writeFieldCommands.stream().limit(writeFieldCommands.size() - 1)
//          .forEach(writeFieldCommand -> builder.command(writeFieldCommand)
//              .command(SerializeCommand.WRITE_FIELD_SEPARATOR));
//      writeFieldCommands.stream().skip(writeFieldCommands.size() - 1).forEach(builder::command);
//
//      return builder.command(SerializeCommand.END_OBJECT).build();
      return null;
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
        @NonNull Function<Class<? extends T>, ? extends Set<PropertyReader<T, ?>>> fieldWrapperConverter,
        @NonNull CaseFormat fieldNameFormat) {
      Set<PropertyReader<T, ?>> propertyReaders = fieldWrapperConverter
          .apply((Class<? extends T>) value.getClass());
      Preconditions.checkArgument(propertyReaders.size() == 1,
          "value strategy serialize must have one fieldWrapper,but now fieldWrappers is %s",
          propertyReaders);
      return SerializeCommands.builder()
          .command(SerializeCommand.create(Type.WRITE_VALUE, null,
              Iterables.getOnlyElement(propertyReaders).read(value).orElse(null)))
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
        @NonNull Function<Class<? extends T>, ? extends Set<PropertyReader<T, ?>>> fieldWrapperConverter,
        @NonNull CaseFormat fieldNameFormat) {
//      SerializeCommandsBuilder builder = SerializeCommands.builder();
//      ImmutableSet<SerializeCommand> writeFieldCommands = fieldWrapperConverter
//          .apply((Class<? extends T>) value.getClass()).stream()
//          .map(fieldWrapper -> SerializeCommand
//              .create(Type.WRITE_FIELD,
//                  fieldNameConverter.apply(fieldNameFormat.split(fieldWrapper.propertyName())),
//                  fieldWrapper.read(value).orElse(null))).collect(ImmutableSet.toImmutableSet());
//      writeFieldCommands.stream().limit(writeFieldCommands.size() - 1)
//          .forEach(writeFieldCommand -> builder.command(writeFieldCommand)
//              .command(SerializeCommand.WRITE_FIELD_SEPARATOR));
//      writeFieldCommands.stream().skip(writeFieldCommands.size() - 1).forEach(builder::command);
//      return builder.command(SerializeCommand.REMOVE_ORIGINAL_FIELD).build();
      return null;
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
      @NonNull Function<Class<? extends T>, ? extends Set<PropertyReader<T, ?>>> fieldWrapperConverter
      , @NonNull CaseFormat fieldNameFormat);

}
