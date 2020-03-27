package com.spas.backend.controller;


import com.spas.backend.common.ApiCode;
import com.spas.backend.common.ApiResponse;
import com.spas.backend.common.exception.CustomException;
import com.spas.backend.dto.UserDto;
import com.spas.backend.service.UserService;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Yuhan Liu
 * @since 2020-03-23
 */
@RestController
@RequestMapping("/user")
public class UserController {

  private UserService userService;

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
  public ApiResponse get(@PathVariable String email) {
    try {
      return new ApiResponse(userService.selectUser(email));
    } catch (Exception e) {
      return new ApiResponse(ApiCode.ILLEGAL_ARGUMENT, "邮箱未注册！");
    }
  }

  @GetMapping("/test")
  public ApiResponse test() {
    return new ApiResponse();
  }
}

