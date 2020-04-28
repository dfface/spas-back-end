package com.spas.backend.controller;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.spas.backend.common.ApiCode;
import com.spas.backend.common.ApiResponse;
import com.spas.backend.dto.UserDto;
import com.spas.backend.entity.Login;
import com.spas.backend.entity.Logout;
import com.spas.backend.entity.User;
import com.spas.backend.entity.UserRole;
import com.spas.backend.service.UserRoleService;
import com.spas.backend.service.UserService;
import com.spas.backend.util.JWTHelper;
import com.spas.backend.util.PasswordHelper;
import com.spas.backend.util.JSONData;
import com.spas.backend.util.UserAgentHelper;
import com.spas.backend.vo.LoginVo;
import com.spas.backend.vo.UserRegisterVo;
import com.spas.backend.vo.UserVo;
import io.swagger.annotations.ApiOperation;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@Setter
@Getter
@Slf4j
public class HomeController {

  @Resource
  private UserService userService;

  private PasswordHelper passwordHelper;

  @Resource
  private ModelMapper modelMapper;

  // 访问令牌过期时间
  private int accessExpire = 5;

  // 更新令牌过期时间
  private int refreshExpire = 30;

  // id令牌过期时间
  private int idExpire = 30;

  @Value("${user.cookie.refresh}")
  private String cookieName;

  @Value("${user.cookie.domain}")
  private String domainName;

  private StringRedisTemplate redisTemplate;

  @Resource
  private UserRoleService userRoleService;

  @Resource
  private MongoTemplate mongoTemplate;

  @Autowired
  public void setRedisTemplate(StringRedisTemplate redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  @Autowired
  public void setPasswordHelper(PasswordHelper passwordHelper) {
    this.passwordHelper = passwordHelper;
  }


  //  @GetMapping("login")
//  public Object login() {
//    return new ModelAndView("index");
//  }

  /**
   * 登录，发放令牌.  数据库中 password 作为秘密
   * @param loginVo 接受JSON字符串，包含 email 和 password
   * @return 三个令牌
   */
  @PostMapping("login")
  public ApiResponse login(HttpServletRequest request, HttpServletResponse response, @RequestBody @Validated LoginVo loginVo){
    String email = loginVo.getEmail();
    String password = loginVo.getPassword();
    Assert.notNull(email,"邮箱不能为空");
    Assert.notNull(password,"密码不能为空");
    UserDto userDto = userService.selectUser(email,loginVo.getOfficeId());
    if(userDto == null) {
      return new ApiResponse(ApiCode.UNKNOWN_ACCOUNT);
    }
    // 判断用户状态：1刚注册未审核 2审核不通过 3审核通过
    if(userDto.getState() != 3){
      return new ApiResponse(ApiCode.USER_AUDIT_NOT_PASSED);
    }
    // 判断hash后的密码正确与否
    String password_encode = passwordHelper.hashPassword(password,userDto.getSalt());
    if(password_encode.equals(userDto.getPassword())) {
      // 登录成功，发放令牌，并保存 RefreshToken 以及 secret 于 Redis
      // JWTHelper: 封装过期时间，进行传递
      HashMap<String,Integer> expire = new HashMap<>();
      expire.put("accessExpire",accessExpire);
      expire.put("refreshExpire",refreshExpire);
      expire.put("idExpire",idExpire);
      HashMap<String,String> token = JWTHelper.sign(userService.selectUserByIdToVo(userDto.getId()),expire);
      String refreshToken = token.get("refreshToken");
      // Redis: 将用户秘密、更新令牌放进去
      HashMap<String,String> userInfo = new HashMap<>();
      userInfo.put("refreshToken",refreshToken);
      userInfo.put("secret",userDto.getPassword());
      // 将 RefreshToken 设置为 Cookie
      Cookie cookie = new Cookie(cookieName,refreshToken);
      cookie.setDomain(domainName);
      cookie.setHttpOnly(true);
      cookie.setMaxAge(refreshExpire*60); // 以秒为单位
//      cookie.setSecure(true);
      response.addCookie(cookie);
      // 将 isLogged 设置为 Cookie
      Cookie cookieLogged = new Cookie("isLogged","true");
      cookieLogged.setDomain(domainName);
      cookieLogged.setMaxAge(refreshExpire*60);  // 一不小心写成了 cookie ！
      response.addCookie(cookieLogged);
      // 将 userInfo 存储到 Redis 中
      ValueOperations<String,String> valueOperations = redisTemplate.opsForValue();
      Assert.notNull(refreshToken,"令牌不能为空");
      // fastJSON 序列化后存储到 Redis
      valueOperations.set(userDto.getId(), JSON.toJSONString(userInfo),refreshExpire, TimeUnit.MINUTES);
      // 去除 RefreshToken 之后返回
      token.remove("refreshToken");
      // 添加 MongoDB 日志
      Login login = new Login();
      login.setIp(UserAgentHelper.getIp(request));
      login.setUserAgent(UserAgentHelper.getBrowserName(request));
      login.setOsName(UserAgentHelper.getOsName(request));
      login.setUid(userDto.getId());
      login.setTime(LocalDateTime.now());
      mongoTemplate.save(login);
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

  @GetMapping("refresh")
  @ApiOperation("通过刷新令牌获取访问令牌")
  public ApiResponse refresh(HttpServletRequest request) {
    return refreshOrInit(request,true);
  }

  @GetMapping("init")
  @ApiOperation("初始化阶段，获取访问和ID令牌")
  public ApiResponse init(HttpServletRequest request){
    return refreshOrInit(request,false);
  }

  /**
   * 服务端判断用户是否登录：redis 中是否有值，且值是否正确
   * @param request 请求中通过 Cookie 获取 id
   * @return 有且正确则已登录，否则未登录
   */
  @GetMapping("isLogged")
  @ApiOperation("服务端判断用户是否登录：redis 中是否有值")
  public ApiResponse isLogged(HttpServletRequest request) {
    Cookie[] cookies = request.getCookies();
    if(cookies == null){
      return new ApiResponse(ApiCode.IS_LOGGED_FALSE);
    }
    for(Cookie cookie : cookies){
      if(cookie.getName().equals(cookieName)){
        String refreshToken = cookie.getValue();
        String userId = JWTHelper.getUserId(cookie.getValue());
        // Redis 检查
        ValueOperations<String, String> valueOperations =  redisTemplate.opsForValue();
        Assert.notNull(userId,"用户ID不能为空");
        // 没有用户id的 redis 记录
        if(valueOperations.get(userId) == null){
          return new ApiResponse(ApiCode.IS_LOGGED_FALSE);
        }
        HashMap<String, String> map = JSON.parseObject(valueOperations.get(userId),JSONData.class);
        // 验证 refresh Token 是否一致
        Assert.notNull(map,"用户信息不能为空");
        if(map.get("refreshToken").equals(refreshToken)){
          return new ApiResponse(ApiCode.IS_LOGGED_TRUE);
        }
        else{
          return new ApiResponse(ApiCode.IS_LOGGED_FALSE);
        }
      }
    }
    return new ApiResponse(ApiCode.IS_LOGGED_FALSE);
  }

  /**
   * 抽出公共部分：是刷新还是初始阶段（是只要 access 还是要 accessAndId）
   * @param request
   * @param b 1代表刷新，0代表初始化
   * @return
   */
  private ApiResponse refreshOrInit(HttpServletRequest request, Boolean b){
    Cookie[] cookies = request.getCookies();
    log.info("Refresh Token Cookies: " + Arrays.toString(cookies));
    if(cookies == null) {
      return new ApiResponse(ApiCode.IS_LOGGED_FALSE);
    }
    // 谨防空指针异常
    for(Cookie c : cookies){
      if(c.getName().equals(cookieName)){
        String refreshToken = c.getValue();
        log.debug("收到客户端传来refreshToken: " + refreshToken);
        String userId = JWTHelper.getUserId(refreshToken);
        // 验证令牌有效性: Redis + Token 本身
        // Redis 检查
        ValueOperations<String, String> valueOperations =  redisTemplate.opsForValue();
        Assert.notNull(userId,"用户ID不能为空");
        String userInfoSerial = valueOperations.get(userId);
        if(userInfoSerial == null) {
          return new ApiResponse(ApiCode.IS_LOGGED_FALSE);
        }
        else {
          // 从 Redis 中获取秘密
          HashMap<String,String> userInfo = JSON.parseObject(userInfoSerial, JSONData.class);
          String refreshTokenRedis = userInfo.get("refreshToken");
          String secret = userInfo.get("secret");
          log.debug("Redis中的refreshToken: " + refreshTokenRedis);
          if(refreshToken.equals(refreshTokenRedis)){
            if(JWTHelper.verify(secret,refreshToken) == ApiCode.OK){
              // 有效则给予Access令牌
              JSONData jsonData = new JSONData();
              if(b){
                UserDto userDto = userService.selectUserById(userId);
                String accessTokenNew = JWTHelper.signAccess(userDto,accessExpire);
                jsonData.put("accessToken",accessTokenNew);
              }
              else{
                UserVo userVo = userService.selectUserByIdToVo(userId);
                String accessTokenNew = JWTHelper.signAccess(userVo,accessExpire);
                String idTokenNew = JWTHelper.signId(userVo,idExpire);
                jsonData.put("idToken",idTokenNew);
                jsonData.put("accessToken",accessTokenNew);
              }
              return new ApiResponse(ApiCode.IS_LOGGED_TRUE,JSON.toJSON(jsonData));
            }
          }
          else{
            return new ApiResponse(ApiCode.IS_LOGGED_FALSE);
          }
        }
      }
    }
    // 不含 cookie 则未登录
    return new ApiResponse(ApiCode.IS_LOGGED_FALSE);
  }

  /**
   * 用户注册.
   * @param userRegisterVo 用户信息
   * @return 注册成功的用户id
   */
  @PostMapping("/register")
  public ApiResponse register(@RequestBody @Validated UserRegisterVo userRegisterVo){
    // 参数校验 先判断邮箱是否已经注册过了
    UserDto userDto = userService.selectUser(userRegisterVo.getEmail(),userRegisterVo.getOfficeId());
    if(userDto != null){
      // 已经注册过了
      return new ApiResponse(ApiCode.ALREADY_REGISTERED);
    }
    // 密码设置
    PasswordHelper passwordHelper = new PasswordHelper();
    Map<String, String> map;
    map = passwordHelper.createPassword(userRegisterVo.getPassword());
    String salt = map.get("salt");
    String password = map.get("password");
    // 构造用户
    User user = new User();
    modelMapper.map(userRegisterVo,user);
    user.setPassword(password);
    user.setSalt(salt);
    userService.save(user);
    // 构造用户的角色
    UserRole userRole = new UserRole();
    userRole.setRolId(userRegisterVo.getRoleId());
    userRole.setOfficeId(userRegisterVo.getOfficeId());
    userRole.setUseId(user.getId());
    userRoleService.save(userRole);
    return new ApiResponse(ApiCode.OK,user.getId());
  }

  /**
   * 通过传入 userId 与 refreshToken 来退出用户
   * @param request
   * @param response
   * @param userId
   * @return
   */
  @GetMapping("/logout/{userId}")
  public ApiResponse logout(HttpServletRequest request, HttpServletResponse response, @PathVariable String userId){
    // 判断 userId 的合法性 由于 UUID，事实上是单数据库唯一
    UserDto userDto = userService.selectUserById(userId);
    if(userDto == null){
      return new ApiResponse(ApiCode.UNKNOWN_ACCOUNT);
    }
    // 判断 refreshToken 是否正确
    String userInfo = redisTemplate.opsForValue().get(userId);
    if(userInfo == null){
      return new ApiResponse(ApiCode.IS_LOGGED_FALSE);
    }
    HashMap<String,String> userInfoParsed = JSON.parseObject(userInfo, JSONData.class);
    String userRefreshToken = userInfoParsed.get("refreshToken");
    Cookie[] cookies = request.getCookies();
    for(Cookie cookie : cookies){
      if(cookie.getValue().equals(userRefreshToken)){
        // 执行登出操作
        redisTemplate.opsForValue().set(userId,"",1, TimeUnit.MICROSECONDS);
        Cookie refresh = new Cookie(cookieName,null);
        refresh.setDomain(domainName);
        refresh.setHttpOnly(true);
        refresh.setPath("/");  // 重要
        refresh.setMaxAge(0);  // 以秒为单位，0则表示立即删除
        response.addCookie(refresh);
        Cookie isLogged = new Cookie("isLogged",null);
        isLogged.setDomain(domainName);
        isLogged.setHttpOnly(false);
        isLogged.setMaxAge(0);
        isLogged.setPath("/");
        response.addCookie(isLogged);
        // MongoDB 写入日志
        Logout logout = new Logout();
        logout.setUid(userId);
        logout.setIp(UserAgentHelper.getIp(request));
        logout.setOsName(UserAgentHelper.getOsName(request));
        logout.setUserAgent(UserAgentHelper.getBrowserName(request));
        logout.setTime(LocalDateTime.now());
        mongoTemplate.save(logout);
        return new ApiResponse(ApiCode.LOGOUT_SUCCESS);
      }
    }
    return new ApiResponse(ApiCode.LOGOUT_FAILED);
  }
}
