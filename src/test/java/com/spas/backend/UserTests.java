package com.spas.backend;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.spas.backend.entity.User;
import com.spas.backend.mapper.UserMapper;
import com.spas.backend.util.PasswordHelper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Map;

@SpringBootTest
@Slf4j
public class UserTests {
  @Resource
  UserMapper userMapper;

  @Autowired
  ModelMapper modelMapper;

  @Test
  void selectUser(){
    User user = userMapper.selectUserByEmail("handy1998@qq.com");
    log.info(user.toString());
  }

  User selectUserByEmail(){
    User user = userMapper.selectUserByEmail("handy1998@qq.com");
    log.info(user.toString());
    return user;
  }

  @Test
  void updateUser() {
    // 使用 条件构造器
    User user = selectUserByEmail();
    Map<String, String> map = new PasswordHelper().hashPassword("123456");
    String salt = (String) map.get("salt");
    String password = (String) map.get("password");
    int update = userMapper.update(user, new UpdateWrapper<User>()
        .set("salt",salt)
        .set("password",password)
    );
    log.info(String.valueOf(update));
  }
}
