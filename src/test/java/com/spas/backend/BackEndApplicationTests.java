package com.spas.backend;

import com.spas.backend.entity.*;
import com.spas.backend.mapper.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BackEndApplicationTests {

  @Autowired
  OfficeMapper officeMapper;

  @Autowired
  DepartmentMapper departmentMapper;

  @Autowired
  UserMapper userMapper;

  @Autowired
  RoleMapper roleMapper;

  @Autowired
  UserRoleMapper userRoleMapper;

  @Test
  void contextLoads() {
  }

  @Test
  void addUser() {
    User user = new User();
    user.setName("张三");
    user.setPosition("检察官");
    user.setEmail("handy1998@qq.com");
    user.setPhone("18072837645");
    user.setAvatar("https://mybatis.plus/img/logo.png");
    user.setOfficeId("a887c5eb331721807d88f4e9c40e2dcd");
    user.setDepartmentId("494a49c4e895a791b61e53df257f9f60");
    userMapper.insert(user);
    System.out.println("id:" + user.getId());
  }

  @Test
  void addOffice() {
    Office office = new Office();
    office.setName("内江市人民检察院");
    office.setAddress("四川省内江市太白路269号");
    office.setPostcode("234123");
    office.setEmail("email@njsjcy.gov.cn");
    office.setPhone("0832-6123199");
    officeMapper.insert(office);
    System.out.println("id:" + office.getId());
  }

  @Test
  void addDepartment() {
    Department department = new Department();
    department.setName("检察一组");
    department.setOfficeId("a887c5eb331721807d88f4e9c40e2dcd");
    departmentMapper.insert(department);
    System.out.println("id:" + department.getId());
  }

  @Test
  void addRole() {
    Role role = new Role();
    role.setName("检察长");
    role.setOfficeId("a887c5eb331721807d88f4e9c40e2dcd");
    roleMapper.insert(role);
    System.out.println("id:" + role.getId());
  }

  @Test
  void addUserRole() {
    UserRole userRole = new UserRole();
    userRole.setUseId("6bf8719eec5bec13aa61d6eacdd74818");
    userRole.setRolId("d07d74d6b64c3af27899e5963efb9921");
    userRole.setOfficeId("a887c5eb331721807d88f4e9c40e2dcd");
    userRoleMapper.insert(userRole);
  }
}
