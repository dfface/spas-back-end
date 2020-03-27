package com.spas.backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spas.backend.common.ApiCode;
import com.spas.backend.common.ApiResponse;
import com.spas.backend.dto.UserDto;
import com.spas.backend.service.UserService;
import com.spas.backend.util.JWTHelper;
import com.spas.backend.util.PasswordHelper;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@RestController
@Setter
@Getter
public class HomeController {

  @Resource
  private UserService userService;

  private PasswordHelper passwordHelper;

  private JWTHelper jwtHelper;

  // 访问令牌过期时间
  private int accessExpire = 5;

  // 更新令牌过期时间
  private int refreshExpire = 30;

  // id令牌过期时间
  private int idExpire = 30;

  private StringRedisTemplate redisTemplate;

  @Autowired
  public void setRedisTemplate(StringRedisTemplate redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  @Autowired
  public void setPasswordHelper(PasswordHelper passwordHelper) {
    this.passwordHelper = passwordHelper;
  }

  @Autowired
  public void setJwtHelper(JWTHelper jwtHelper) {
    this.jwtHelper = jwtHelper;
  }

  //  @GetMapping("login")
//  public Object login() {
//    return new ModelAndView("index");
//  }

  /**
   * 登录，发放令牌.  数据库中 password 作为秘密
   * @param UserLoginInfo 接受JSON字符串，包含多个字段，必有 email 和 password
   * @return 三个令牌
   */
  @PostMapping("login")
  public ApiResponse login(HttpServletResponse response, @RequestBody HashMap<String,String> UserLoginInfo) throws JsonProcessingException {
    String email = UserLoginInfo.get("email");
    String password = UserLoginInfo.get("password");
    Assert.notNull(email,"邮箱不能为空");
    Assert.notNull(password,"密码不能为空");
    UserDto userDto = userService.selectUser(email);
    if(userDto == null) {
      return new ApiResponse(ApiCode.UNKNOWN_ACCOUNT);
    }
    String password_encode = passwordHelper.hashPassword(password,userDto.getSalt());
    if(password_encode.equals(userDto.getPassword())) {
      // 登录成功，发放令牌，并保存 RefreshToken 以及 secret 于 Redis
      // 封装过期时间，进行传递
      HashMap<String,Integer> expire = new HashMap<>();
      expire.put("accessExpire",accessExpire);
      expire.put("refreshExpire",refreshExpire);
      expire.put("idExpire",idExpire);
      HashMap<String,String> token = jwtHelper.sign(userDto,expire);
      String refreshToken = token.get("refreshToken");
      // 将用户秘密、更新令牌放进去
      HashMap<String,String> userInfo = new HashMap<>();
      userInfo.put("refreshToken",refreshToken);
      userInfo.put("secret",userDto.getPassword());
      // 将 RefreshToken 设置为 Cookie
      Cookie cookie = new Cookie("SPAS_REFRESH",refreshToken);
      cookie.setHttpOnly(true);
      cookie.setMaxAge(refreshExpire*60); // 以秒为单位
//      cookie.setSecure(true);
      response.addCookie(cookie);
      // 将 userInfo 存储到 Redis 中
      ValueOperations<String,String> valueOperations = redisTemplate.opsForValue();
      Assert.notNull(refreshToken,"令牌不能为空");
      valueOperations.set(userDto.getId(), new ObjectMapper().writeValueAsString(userInfo),refreshExpire, TimeUnit.MINUTES);
      return new ApiResponse(ApiCode.LOGIN_SUCCESS,token);
    }
    else {
      // 登录失败，密码错误
      return new ApiResponse(ApiCode.PASSWORD_INVALID);
    }
  }

  @GetMapping("error")
  public Object unauthc() {
    return "Here is Unauthc page";
  }

//  @PostMapping("doLogin")
//  public Object doLogin(@RequestParam String email, @RequestParam String password) {
//    UsernamePasswordToken token = new UsernamePasswordToken(email, password);
//    Subject subject = SecurityUtils.getSubject();
//    try {
//      subject.login(token);
//    } catch (UnknownAccountException u) {
//      return "username error!";
//    }
//    UserDto user = (UserDto) userService.selectUser(email).getData();
//    subject.getSession().setAttribute("user",user);
//    return "SUCCESS";
//  }
}
