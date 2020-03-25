package com.spas.backend;

import com.spas.backend.entity.UserRole;
import com.spas.backend.service.UserRoleService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
@Slf4j
public class UserRoleTests {

  @Resource
  UserRoleService userRoleService;

  @Test
  void findRoles() {
    log.info(userRoleService.selectRoles("6bf8719eec5bec13aa61d6eacdd74818").toString());
  }
}
