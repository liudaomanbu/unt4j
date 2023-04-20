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
import org.caotc.unit4j.core.Quantity;
import org.caotc.unit4j.core.unit.UnitConstant;
import org.caotc.unit4j.support.mybatis.mapper.TestQuantityMapper;
import org.caotc.unit4j.support.mybatis.model.TestAmountField;
import org.caotc.unit4j.support.mybatis.model.TestQuantity;
import org.caotc.unit4j.support.mybatis.sql.visitor.AbstractExpressionVisitor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Random;

@Slf4j
public class MybatisTest {
    private static final SqlSessionFactory SQL_SESSION_FACTORY;
    private static final SqlSession SESSION;
    private static final TestQuantityMapper MAPPER;
    private static final Random RANDOM = new Random();

    static {
        InputStream inputStream = null;
        try {
            inputStream = Resources.getResourceAsStream("mybatis-config.xml");
        } catch (IOException e) {
            e.printStackTrace();
        }
        SQL_SESSION_FACTORY = new SqlSessionFactoryBuilder().build(inputStream);
        SESSION = SQL_SESSION_FACTORY.openSession();
        MAPPER = SESSION.getMapper(TestQuantityMapper.class);
    }

  @Test
  void findByPrimaryKeyOnResultType() {
      TestQuantity result = MAPPER.findByPrimaryKeyOnResultType(1L);
      log.debug("result:{}", result);
      Assertions.assertEquals(BigDecimal.ONE.negate(), result.data().bigDecimalValue());
      Assertions.assertEquals(UnitConstant.SECOND, result.data().unit());
  }

  @Test
  void findByPrimaryKeyOnResultMap() {
      TestQuantity result = MAPPER.findByPrimaryKeyOnResultMap(1L);
      log.debug("result:{}", result);
      Assertions.assertEquals(BigDecimal.ONE.negate(), result.data().bigDecimalValue());
      Assertions.assertEquals(UnitConstant.SECOND, result.data().unit());
  }

  @Test
  void insert() {
      Quantity quantity = Quantity.create(BigDecimal.valueOf(RANDOM.nextDouble()), UnitConstant.HOUR);
      TestQuantity param = new TestQuantity().data(quantity);
      log.debug("param:{}", param);
      int effectRows = MAPPER.insertSelective(param);
      log.debug("effectRows:{}", effectRows);
      Assertions.assertEquals(1, effectRows);

      TestAmountField result = MAPPER.findByPrimaryKeyOnTestAmountField(param.id());
      log.debug("result:{}", result);
      Assertions.assertNotNull(result);

      Assertions.assertEquals(param.data().value().bigDecimalValue().toString(), result.dataValue());
      Assertions.assertEquals(param.data().unit().id(), result.dataUnit());
  }

  @Test
  void update() {
      Quantity quantity = Quantity.create(BigDecimal.valueOf(RANDOM.nextDouble()), UnitConstant.HOUR);
      TestQuantity param = new TestQuantity()
              .data(quantity).id(1L);
      int effectRows = MAPPER.updateByPrimaryKeySelective(param);
      log.debug("effectRows:{}", effectRows);
      Assertions.assertEquals(1, effectRows);

      TestAmountField result = MAPPER.findByPrimaryKeyOnTestAmountField(param.id());
      log.debug("result:{}", result);
      Assertions.assertNotNull(result);

      Assertions.assertEquals(param.data().value().bigDecimalValue().toString(), result.dataValue());
      Assertions.assertEquals(param.data().unit().id(), result.dataUnit());
  }

  @Test
  @SneakyThrows
  void sqlParse() {
    Delete delete = (Delete) CCJSqlParserUtil.parse("delete from table1 where a=?");
    delete.getWhere().accept(new AbstractExpressionVisitor() {
    });
  }
}
