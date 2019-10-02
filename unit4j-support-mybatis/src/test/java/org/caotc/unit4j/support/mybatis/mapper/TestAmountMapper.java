
package org.caotc.unit4j.support.mybatis.mapper;

import org.caotc.unit4j.support.mybatis.model.TestAmount;


public interface TestAmountMapper {

  /**
   * 保存一个实体，null的属性也会保存，不会使用数据库默认值
   *
   * @param entity 入库对象
   * @return 入库数目
   */
  int insert(TestAmount entity);

  /**
   * 对象入库
   *
   * @param entity 入库对象
   * @return 入库数目
   */
  int insertSelective(TestAmount entity);

  /**
   * 按主键更新，null的属性也会更新
   *
   * @param entity 更新对象
   * @return 影响行数
   */
  int updateByPrimaryKey(TestAmount entity);

  /**
   * 按主键更新
   *
   * @param entity 更新对象
   * @return 影响行数
   */
  int updateByPrimaryKeySelective(TestAmount entity);

  /**
   * 按主键查询
   *
   * @param primaryKey 业务主键
   * @return 查询结果
   */
  TestAmount findByPrimaryKey(Long primaryKey);

  /**
   * 按主键删除
   *
   * @param primaryKey 业务主键
   * @return 删除数目
   */
  int deleteByPrimaryKey(Long primaryKey);


}