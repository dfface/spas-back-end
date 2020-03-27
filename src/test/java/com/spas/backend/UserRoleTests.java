package com.spas.backend;

import com.spas.backend.entity.UserRole;
import com.spas.backend.service.UserRoleService;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
public class UserRoleTests {

  private static final Logger log = LoggerFactory.getLogger(UserRoleTests.class);
  @Resource
  UserRoleService userRoleService;

//  @Test
  void findRoles() {
    log.info(userRoleService.selectRoles("6bf8719eec5bec13aa61d6eacdd74818").toString());
  }
}
