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

package org.caotc.unit4j.support.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import lombok.Value;
import org.caotc.unit4j.api.annotation.SerializeCommand;
import org.caotc.unit4j.api.annotation.SerializeCommands;
import org.caotc.unit4j.core.Amount;
import org.caotc.unit4j.core.exception.NeverHappenException;

import java.io.IOException;

/**
 * {@link SerializeCommands}在jackson中的序列化器. {@link Amount}通过转换为序列化指令对象,来实现同时存在多套序列化格式的需求
 *
 * @author caotc
 * @date 2019-05-11
 * @since 1.0.0
 */
@Value
public class SerializeCommandsSerializer extends StdSerializer<SerializeCommands> {

  public SerializeCommandsSerializer() {
    super(SerializeCommands.class);
  }

  @Override
  public void serialize(SerializeCommands value, JsonGenerator gen, SerializerProvider provider)
      throws IOException {
    for (SerializeCommand command : value.commands()) {
      switch (command.type()) {
        case START_OBJECT:
          gen.writeStartObject();
          break;
        case END_OBJECT:
          gen.writeEndObject();
          break;
          case WRITE_FIELD_DELIMITER:
          break;
        case WRITE_FIELD:
          gen.writeObjectField(command.fieldName(), command.fieldValue());
          break;
        case WRITE_VALUE:
          gen.writeObject(command.fieldValue());
          break;
        case REMOVE_ORIGINAL_FIELD:
          //TODO
          gen.writeNull();
          break;
        default:
          throw NeverHappenException.instance();
      }
    }
  }

}
