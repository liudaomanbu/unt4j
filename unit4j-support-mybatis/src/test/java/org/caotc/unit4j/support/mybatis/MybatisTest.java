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

package org.caotc.unit4j.support.mybatis;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.delete.Delete;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.caotc.unit4j.core.Amount;
import org.caotc.unit4j.core.constant.UnitConstant;
import org.caotc.unit4j.support.mybatis.mapper.TestAmountMapper;
import org.caotc.unit4j.support.mybatis.model.TestAmount;
import org.caotc.unit4j.support.mybatis.sql.visitor.AbstractExpressionVisitor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;

@Slf4j
public class MybatisTest {

  private static final Amount AMOUNT = Amount.create(123L, UnitConstant.SECOND);
  private static final TestAmount TEST_AMOUNT = new TestAmount()
          .withUnitValue(BigDecimal.TEN).withUnitProperty(BigDecimal.ONE)
          .unit(UnitConstant.HOUR.id());
  private static SqlSessionFactory sqlSessionFactory;

  static {
    InputStream inputStream = null;
    try {
      inputStream = Resources.getResourceAsStream("mybatis-config.xml");
    } catch (IOException e) {
      e.printStackTrace();
    }
    sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
  }

  @Test
  void findByPrimaryKeyOnResultType() {
    try (SqlSession session = sqlSessionFactory.openSession()) {
      TestAmountMapper dao = session.getMapper(TestAmountMapper.class);
      TestAmount result = dao.findByPrimaryKeyOnResultType(1L);
      log.debug("result:{}", result);
      Assertions.assertEquals(BigDecimal.TEN, result.withUnitValue());
      Assertions.assertEquals(BigDecimal.ONE, result.withUnitProperty());
      Assertions.assertEquals(UnitConstant.MINUTE.id(), result.unit());
    }
  }

  @Test
  void findByPrimaryKeyOnResultMap() {
    try (SqlSession session = sqlSessionFactory.openSession()) {
      TestAmountMapper dao = session.getMapper(TestAmountMapper.class);
      TestAmount result = dao.findByPrimaryKeyOnResultMap(1L);
      log.debug("result:{}", result);
      Assertions.assertEquals(BigDecimal.TEN, result.withUnitValue());
      Assertions.assertEquals(BigDecimal.ONE, result.withUnitProperty());
      Assertions.assertEquals(UnitConstant.MINUTE.id(), result.unit());
    }
  }

  @Test
  void insert() {
    try (SqlSession session = sqlSessionFactory.openSession()) {
      TestAmountMapper dao = session.getMapper(TestAmountMapper.class);
      Amount amount = Amount.create(BigDecimal.ONE, UnitConstant.HOUR);
      TestAmount param = new TestAmount()
              .withUnitValue(BigDecimal.TEN).withUnitProperty(amount.bigDecimalValue())
              .unit(amount.unit().id());
      log.debug("param:{}", param);
      int effectRows = dao.insertSelective(param);
      log.debug("effectRows:{}", effectRows);
      Assertions.assertEquals(1, effectRows);

      TestAmount result = dao.findByPrimaryKeyOnResultType(param.id());
      log.debug("result:{}", result);
      Assertions.assertNotNull(result);

      Assertions.assertEquals(param.withUnitValue(), result.withUnitValue());
      Assertions.assertEquals(amount.convertTo(UnitConstant.MINUTE).unit().id(), result.unit());
      Assertions.assertEquals(amount.convertTo(UnitConstant.MINUTE).bigDecimalValue(), result.withUnitProperty());
      session.commit();
    }
  }

  @Test
  void update() {
    try (SqlSession session = sqlSessionFactory.openSession()) {
      TestAmountMapper dao = session.getMapper(TestAmountMapper.class);
      int effectRows = dao.updateByPrimaryKeySelective(TEST_AMOUNT);
      session.commit();
      log.info("effectRows:{}", effectRows);
    }
  }

  @Test
  @SneakyThrows
  void sqlParse() {
    Delete delete = (Delete) CCJSqlParserUtil.parse("delete from table1 where a=?");
    delete.getWhere().accept(new AbstractExpressionVisitor() {
    });
  }
}
