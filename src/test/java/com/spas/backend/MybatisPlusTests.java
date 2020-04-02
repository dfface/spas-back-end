package com.spas.backend;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.spas.backend.entity.User;
import com.spas.backend.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class MybatisPlusTests {

  @Resource
  UserMapper userMapper;

//  @Test
  public void paginationTest() {
    Page<User> page = new Page<>(1,2);
    QueryWrapper<User> wrapper = new QueryWrapper<>();
    wrapper.like("name","李");
    Page<User> ipage = userMapper.selectPage(page,wrapper);
    System.out.println("总页数：" + ipage.getPages());
    ipage.getRecords().forEach(System.out::println);
  }
}
