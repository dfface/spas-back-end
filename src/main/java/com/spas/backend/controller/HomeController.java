package com.spas.backend.controller;

import com.spas.backend.dto.UserDto;
import com.spas.backend.entity.User;
import com.spas.backend.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class HomeController {

  @Resource
  private UserService userService;

  @GetMapping("login")
  public Object login() {
    return "Here is Login page";
  }

  @GetMapping("unauthc")
  public Object unauthc() {
    return "Here is Unauthc page";
  }

  @PostMapping("doLogin")
  public Object doLogin(@RequestParam String email, @RequestParam String password) {
    UsernamePasswordToken token = new UsernamePasswordToken(email, password);
    Subject subject = SecurityUtils.getSubject();
    try {
      subject.login(token);
    } catch (UnknownAccountException u) {
      return "username error!";
    }
    UserDto user = (UserDto) userService.selectUser(email).getData();
    subject.getSession().setAttribute("user",user);
    return "SUCCESS";
  }
}
