package org.caotc.unit4j.support.mybatis;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.delete.Delete;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.caotc.unit4j.support.mybatis.mapper.DoctorTeamMemberMapper;
import org.caotc.unit4j.support.mybatis.model.DoctorTeamMemberDO;
import org.caotc.unit4j.support.mybatis.sql.visitor.AbstractExpressionVisitor;
import org.junit.jupiter.api.Test;

@Slf4j
public class TestMybatis {

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
      DoctorTeamMemberMapper dao = session.getMapper(DoctorTeamMemberMapper.class);
      DoctorTeamMemberDO doctorTeamMemberDO = dao.findByPrimaryKey(1L);
      log.info("doctorTeamMemberDO:{}", doctorTeamMemberDO);
    }
  }

  @Test
  void insert() {
    try (SqlSession session = sqlSessionFactory.openSession()) {
      DoctorTeamMemberMapper dao = session.getMapper(DoctorTeamMemberMapper.class);
      int effectRows = dao.insert(
          new DoctorTeamMemberDO(0L, true, LocalDateTime.now(), LocalDateTime.now(), ""));
      session.commit();
      log.info("effectRows:{}", effectRows);
    }
  }

  @Test
  void update() {
    try (SqlSession session = sqlSessionFactory.openSession()) {
      DoctorTeamMemberMapper dao = session.getMapper(DoctorTeamMemberMapper.class);
      int effectRows = dao.updateByPrimaryKey(
          new DoctorTeamMemberDO(0L, true, LocalDateTime.now(), LocalDateTime.now(), ""));
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
