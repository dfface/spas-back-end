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

//  @Autowired
//  ModelMapper modelMapper;
//
//  @Autowired
//  OfficeMapper officeMapper;
//
//  @Resource
//  OperationMapper operationMapper;
//
//  @Resource
//  RoleOperationMapper roleOperationMapper;
//
//  @Autowired
//  UserService userService;
//
//  @Autowired
//  RoleMapper roleMapper;
//
//  @Autowired
//  UserRoleMapper userRoleMapper;
//
//  @Test
//  void contextLoads() {
//  }

//  @Test
//  void addUser() {
//    User user = new User();
//    user.setName("刘六");
//    user.setPosition("平台管理员");
//    user.setEmail("1106741606@qq.com");
//    user.setOfficeId("664bfa13ea5ee06c27e0e5c74ced6ec2");
//    user.setPassword("123456");
//    UserDto userDto = new UserDto();
//    modelMapper.map(user,userDto);
//    userService.insertUser(userDto);
//  }
//
//  @Test
//  void addOffice() {
//    Office office = new Office();
//    office.setName("检察建议流程辅助办案系统");
//    office.setUrl("#");
//    officeMapper.insert(office);
//    System.out.println("id:" + office.getId());
//  }
//
////  @Test
//  void addDepartment() {
//    Department department = new Department();
//    department.setName("被监督机关");
//    department.setOfficeId("a887c5eb331721807d88f4e9c40e2dcd");
//    departmentMapper.insert(department);
//    System.out.println("id:" + department.getId());
//  }

//  @Test
//  void addRole() {
//    Role role = new Role();
//    role.setCode("super_admin");
//    role.setDescription("管理员");
//    role.setOfficeId("664bfa13ea5ee06c27e0e5c74ced6ec2");
//    roleMapper.insert(role);
//    System.out.println("id:" + role.getId());
//  }

//  @Test
//  void addUserRole() {
//    UserRole userRole = new UserRole();
//    userRole.setUseId("a6c5c7f38ed76639c50de354aa1eef83");
//    userRole.setRolId("5783e0fa99adcb4d27b8f66aedf7c40f");
//    userRole.setOfficeId("664bfa13ea5ee06c27e0e5c74ced6ec2");
//    userRoleMapper.insert(userRole);
//  }
//
////  @Test
//  void addOperation() {
//    Operation operation = new Operation();
//    operation.setCode("user:addByDto");
//    operation.setDescription("可通过添加用户");
//    operation.setOfficeId("a887c5eb331721807d88f4e9c40e2dcd");
//    operationMapper.insert(operation);
//  }

////  @Test
//  void addRoleOperation() {
//    RoleOperation roleOperation = new RoleOperation();
//    roleOperation.setRolId("da05fed3b0626305edc8368527e071ca");
//    roleOperation.setOpeId("06c0f43cd4eeaae285c06712596427bd");
//    roleOperation.setOfficeId("a887c5eb331721807d88f4e9c40e2dcd");
//    roleOperationMapper.insert(roleOperation);
//  }
}
