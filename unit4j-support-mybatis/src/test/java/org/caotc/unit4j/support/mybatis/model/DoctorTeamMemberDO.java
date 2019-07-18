/*
 * Copyright (c) 2001-2018 GuaHao.com Corporation Limited. All rights reserved.
 * This software is the confidential and proprietary information of GuaHao Company.
 * ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with GuaHao.com.
 */
package org.caotc.unit4j.support.mybatis.model;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.ToString;
import lombok.Value;
import org.caotc.unit4j.core.Amount;
import org.caotc.unit4j.core.constant.UnitConstant;
import org.caotc.unit4j.support.CodecStrategy;
import org.caotc.unit4j.support.annotation.AmountSerialize;

/**
 * 医生团队成员
 *
 * <p>
 * DO对象已使用lombok，定义时无需get/set/hashCode/equals/toString方法， lombok使用请参见：<a
 * href="https://www.projectlombok.org/">
 * </p>
 *
 * @author initializr
 * @version 1.0
 * @since 2019-05-25
 */
@Data
@ToString(callSuper = true)
public class DoctorTeamMemberDO {

  Long id;
  Boolean isDeleted;
  /**
   * 创建时间
   */
  LocalDateTime gmtCreated;

  /**
   * 修改时间
   */
  LocalDateTime gmtModified;
  /**
   * 医生团队主键
   */
  String doctorTeamId;

  /**
   * 医生主键
   */
  TestDO doctorId = new TestDO();
}

@Value
class TestDO {

  @AmountSerialize(strategy = CodecStrategy.FLAT)
  Amount value = Amount.create(123L, UnitConstant.SECOND);
}