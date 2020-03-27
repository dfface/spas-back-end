package com.spas.backend;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.spas.backend.dto.UserDto;
import com.spas.backend.entity.User;
import com.spas.backend.mapper.UserMapper;
import com.spas.backend.util.PasswordHelper;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Map;

@SpringBootTest
public class UserTests {

  private static final Logger log = LoggerFactory.getLogger(UserRoleTests.class);

  @Resource
  UserMapper userMapper;

  @Autowired
  ModelMapper modelMapper;

  void selectUserByEmail(){

  }

//  @Test
  void updateUser() {
    // 使用 条件构造器
//    User user = selectUserByEmail();
//    Map<String, String> map = new PasswordHelper().hashPassword("123456");
//    String salt = (String) map.get("salt");
//    String password = (String) map.get("password");
//    int update = userMapper.update(user, new UpdateWrapper<User>()
//        .set("salt",salt)
//        .set("password",password)
//    );
//    log.info(String.valueOf(update));
  }

//  @Test
  void selectUser() {
    UserDto userDto = userMapper.selectUserByEmail("handy1998@qq.com");
    System.out.println(userDto);
  }
}
