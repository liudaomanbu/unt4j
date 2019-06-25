/*
 * Copyright (c) 2001-2018 GuaHao.com Corporation Limited. All rights reserved.
 * This software is the confidential and proprietary information of GuaHao Company.
 * ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with GuaHao.com.
 */
package org.caotc.unit4j.support.mybatis.mapper;

import org.caotc.unit4j.support.mybatis.model.DoctorTeamMemberDO;

/**
 * 医生团队成员 mapper
 *
 * <p>
 * mapper接口应当准守符合微医开发规约：
 * <li>分页查询尽量使用游标分页，提升性能<li/>
 * 具体游标查询、普通查询区别详情请见<a href="https://gi.guahao.cn/docbook/index.html">
 * </p>
 *
 * @author initializr
 * @version 1.0
 * @since 2019-05-25
 */
public interface DoctorTeamMemberMapper {

  /**
   * 保存一个实体，null的属性也会保存，不会使用数据库默认值
   *
   * @param entity 入库对象
   * @return 入库数目
   */
  int insert(DoctorTeamMemberDO entity);

  /**
   * 对象入库
   *
   * @param entity 入库对象
   * @return 入库数目
   */
  int insertSelective(DoctorTeamMemberDO entity);

  /**
   * 按主键更新，null的属性也会更新
   *
   * @param entity 更新对象
   * @return 影响行数
   */
  int updateByPrimaryKey(DoctorTeamMemberDO entity);

  /**
   * 按主键更新
   *
   * @param entity 更新对象
   * @return 影响行数
   */
  int updateByPrimaryKeySelective(DoctorTeamMemberDO entity);

  /**
   * 按主键查询
   *
   * @param primaryKey 业务主键
   * @return 查询结果
   */
  DoctorTeamMemberDO findByPrimaryKey(Long primaryKey);

  /**
   * 按主键删除
   *
   * @param primaryKey 业务主键
   * @return 删除数目
   */
  int deleteByPrimaryKey(Long primaryKey);


}