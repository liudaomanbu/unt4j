package org.caotc.unit4j.support;

import lombok.NonNull;
import lombok.Value;

/**
 * 序列化指令，每一个对象代表序列化过程中的一次操作
 *
 * @author caotc
 * @date 2019-05-02
 * @since 1.0.0
 */
@Value(staticConstructor = "create")
public class SerializeCommand {

  /**
   * 序列化指令类型
   *
   * @author caotc
   * @date 2019-05-29
   * @since 1.0.0
   */
  public enum Type {
    /**
     * 开始序列化一个对象
     */
    START_OBJECT,
    /**
     * 结束序列化一个对象
     */
    END_OBJECT,
    /**
     * 序列化属性
     */
    WRITE_FIELD,
    /**
     * 序列化属性之间分隔符
     */
    WRITE_FIELD_SEPARATOR,
    /**
     * 序列化属性
     */
    WRITE_VALUE,
    /**
     * 取消原始属性名称的输出(只有{@see AmountCodecStrategy.FLAT}使用)
     */
    REMOVE_ORIGINAL_FIELD;
  }

  public static final SerializeCommand START_OBJECT = create(Type.START_OBJECT, null, null);
  public static final SerializeCommand END_OBJECT = create(Type.END_OBJECT, null, null);
  public static final SerializeCommand WRITE_FIELD_SEPARATOR = create(Type.WRITE_FIELD_SEPARATOR,
      null, null);
  public static final SerializeCommand REMOVE_ORIGINAL_FIELD = create(
      Type.REMOVE_ORIGINAL_FIELD, null, null);

  /**
   * 指令类型
   */
  @NonNull
  Type type;
  /**
   * 属性名称,{@code type}为{@link Type#WRITE_FIELD}时才有值
   */
  String fieldName;
  /**
   * 属性值,{@code type}为{@link Type#WRITE_FIELD}或{@link Type#WRITE_VALUE}时才有值
   */
  Object fieldValue;
}
