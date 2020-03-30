package com.spas.backend.controller;


import com.spas.backend.common.ApiCode;
import com.spas.backend.common.ApiResponse;
import com.spas.backend.common.exception.CustomException;
import com.spas.backend.dto.UserDto;
import com.spas.backend.service.UserService;
import com.spas.backend.util.JWTHelper;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Yuhan Liu
 * @since 2020-03-23
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

  private UserService userService;

  @GetMapping("/test")
  @ApiOperation("测试能否访问")
  public ApiResponse test(@RequestParam String id) {
    return new ApiResponse(ApiCode.OK,userService.selectUserByIdToVo(id));
  }

  @Autowired
  private void setUserService(UserService userService){
    this.userService = userService;
  }

  @PostMapping("/add")
  public ApiResponse add(UserDto userDto){
    return userService.insertUser(userDto);
  }

  @GetMapping("/{email}/")
  @RequiresRoles("chief_procurator")
  @ApiOperation("通过邮箱获取用户信息")
  public ApiResponse get(@PathVariable String email) {
    try {
      return new ApiResponse(userService.selectUser(email));
    } catch (Exception e) {
      return new ApiResponse(ApiCode.ILLEGAL_ARGUMENT, "邮箱未注册！");
    }
  }
}

