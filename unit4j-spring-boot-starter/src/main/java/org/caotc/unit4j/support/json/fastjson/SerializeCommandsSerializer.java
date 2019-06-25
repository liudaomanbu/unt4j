package org.caotc.unit4j.support.json.fastjson;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import lombok.Value;
import org.caotc.unit4j.core.Amount;
import org.caotc.unit4j.core.exception.NeverHappenException;
import org.caotc.unit4j.support.SerializeCommand;
import org.caotc.unit4j.support.SerializeCommands;
import org.caotc.unit4j.support.json.JsonConstant;

/**
 * {@link SerializeCommands}在fastjson中的序列化器. {@link Amount}通过转换为序列化指令对象,来实现同时存在多套序列化格式的需求
 *
 * @author caotc
 * @date 2019-05-10
 * @since 1.0.0
 */
@Value
public class SerializeCommandsSerializer implements ObjectSerializer {

  @Override
  public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType,
      int features) throws IOException {
    SerializeWriter out = serializer.out;
    SerializeCommands serializeCommands = (SerializeCommands) object;
    for (SerializeCommand command : serializeCommands.commands()) {
      switch (command.type()) {
        case START_OBJECT:
          out.write(JsonConstant.OBJECT_BEGIN);
          break;
        case END_OBJECT:
          out.write(JsonConstant.OBJECT_END);
          break;
        case WRITE_FIELD_SEPARATOR:
          out.write(JsonConstant.FIELD_SEPARATOR);
          break;
        case WRITE_FIELD:
          out.writeFieldName(command.fieldName());
          serializer.write(command.fieldValue());
          break;
        case WRITE_VALUE:
          serializer.write(command.fieldValue());
          break;
        case REMOVE_ORIGINAL_FIELD:
          break;
        default:
          throw NeverHappenException.instance();
      }
    }
  }
}
