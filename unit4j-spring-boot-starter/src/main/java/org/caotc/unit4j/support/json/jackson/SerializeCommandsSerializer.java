package org.caotc.unit4j.support.json.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import lombok.Value;
import org.caotc.unit4j.core.Amount;
import org.caotc.unit4j.core.exception.NeverHappenException;
import org.caotc.unit4j.support.SerializeCommand;
import org.caotc.unit4j.support.SerializeCommands;

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
        case WRITE_FIELD_SEPARATOR:
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
