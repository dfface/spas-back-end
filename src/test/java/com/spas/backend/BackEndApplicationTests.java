package com.spas.backend;

import com.spas.backend.dto.UserDto;
import com.spas.backend.entity.*;
import com.spas.backend.mapper.*;
import com.spas.backend.service.UserService;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

//@SpringBootTest
class BackEndApplicationTests {

  @Autowired
  ModelMapper modelMapper;

  @Autowired
  OfficeMapper officeMapper;

  @Autowired
  DepartmentMapper departmentMapper;

  @Resource
  OperationMapper operationMapper;

  @Resource
  RoleOperationMapper roleOperationMapper;

  @Autowired
  UserService userService;

  @Autowired
  RoleMapper roleMapper;

  @Autowired
  UserRoleMapper userRoleMapper;

//  @Test
  void contextLoads() {
  }

//  @Test
  void addUser() {
    User user = new User();
    user.setName("李四");
    user.setPosition("国土资源局局长");
    user.setEmail("handy1998@qq.com");
    user.setPhone("18072837645");
    user.setAvatar("https://mybatis.plus/img/logo.png");
    user.setOfficeId("a887c5eb331721807d88f4e9c40e2dcd");
    user.setDepartmentId("de20808201c66109f3a3202d05e1f795");
    user.setPassword("123456");
    UserDto userDto = new UserDto();
    modelMapper.map(user,userDto);
    userService.insertUser(userDto);
  }

//  @Test
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

//  @Test
  void addDepartment() {
    Department department = new Department();
    department.setName("被监督机关");
    department.setOfficeId("a887c5eb331721807d88f4e9c40e2dcd");
    departmentMapper.insert(department);
    System.out.println("id:" + department.getId());
  }

//  @Test
  void addRole() {
    Role role = new Role();
    role.setCode("administrative_personnel");
    role.setDescription("行政机关人员");
    role.setOfficeId("a887c5eb331721807d88f4e9c40e2dcd");
    roleMapper.insert(role);
    System.out.println("id:" + role.getId());
  }

//  @Test
  void addUserRole() {
    UserRole userRole = new UserRole();
    userRole.setUseId("b7a894040681bc2c56c48cea3a5a7ab4");
    userRole.setRolId("6aae396f6af4d9bdf5d7683c82101d82");
    userRole.setOfficeId("a887c5eb331721807d88f4e9c40e2dcd");
    userRoleMapper.insert(userRole);
  }

//  @Test
  void addOperation() {
    Operation operation = new Operation();
    operation.setCode("user:addByDto");
    operation.setDescription("可通过添加用户");
    operation.setOfficeId("a887c5eb331721807d88f4e9c40e2dcd");
    operationMapper.insert(operation);
  }

//  @Test
  void addRoleOperation() {
    RoleOperation roleOperation = new RoleOperation();
    roleOperation.setRolId("da05fed3b0626305edc8368527e071ca");
    roleOperation.setOpeId("06c0f43cd4eeaae285c06712596427bd");
    roleOperation.setOfficeId("a887c5eb331721807d88f4e9c40e2dcd");
    roleOperationMapper.insert(roleOperation);
  }
}
