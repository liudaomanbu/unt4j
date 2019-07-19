/*
 * Copyright (c) 2001-2018 GuaHao.com Corporation Limited.
 * All rights reserved.
 * This software is the confidential and proprietary information of GuaHao Company.
 *  ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with GuaHao.com.
 */

package org.caotc.unit4j.support.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication(scanBasePackages = {"org.caotc.unit4j.support.spring"})
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
    log.error("end run");
  }

//    @FieldDefaults(makeFinal = false, level = AccessLevel.PRIVATE)
//    @Service
//    public static class TestService{
//        @Resource
//        TestCompoent testCompoent;
//    }
}
