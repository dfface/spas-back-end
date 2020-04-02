package com.spas.backend;

import com.spas.backend.entity.Cases;
import com.spas.backend.service.CaseService;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Random;

@SpringBootTest
public class CaseTests {

  private final static Logger log = LoggerFactory.getLogger(CaseTests.class);

  @Resource
  CaseService caseService;

  @Test
  public void addCaseTest() {
    for(int i = 0; i < 10; i++){
      Cases cases = RandomString.getCase();
      cases.setCreatorId("6bf8719eec5bec13aa61d6eacdd74818");
      cases.setOfficeId("a887c5eb331721807d88f4e9c40e2dcd");
      log.info("Case New ID: " + cases.getId());
      caseService.insertCase(cases);
    }
  }
}
