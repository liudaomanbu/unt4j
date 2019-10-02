package org.caotc.unit4j.support.mybatis;

import java.io.IOException;
import java.io.InputStream;
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
import org.junit.jupiter.api.Test;

@Slf4j
public class MybatisTest {

  private static final Amount AMOUNT = Amount.create(123L, UnitConstant.SECOND);
  private static final TestAmount TEST_AMOUNT = new TestAmount(9L, AMOUNT, AMOUNT);
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
  void queryTable() {
    try (SqlSession session = sqlSessionFactory.openSession()) {
      TestAmountMapper dao = session.getMapper(TestAmountMapper.class);
      TestAmount testAmount = dao.findByPrimaryKey(1L);
      log.info("testAmount:{}", testAmount);
    }
  }

  @Test
  void insert() {
    try (SqlSession session = sqlSessionFactory.openSession()) {
      TestAmountMapper dao = session.getMapper(TestAmountMapper.class);
      int effectRows = dao.insert(TEST_AMOUNT);
      session.commit();
      log.info("effectRows:{}", effectRows);
    }
  }

  @Test
  void update() {
    try (SqlSession session = sqlSessionFactory.openSession()) {
      TestAmountMapper dao = session.getMapper(TestAmountMapper.class);
      int effectRows = dao.updateByPrimaryKey(TEST_AMOUNT);
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
