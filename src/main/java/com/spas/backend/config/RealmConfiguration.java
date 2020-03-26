package com.spas.backend.config;

import com.auth0.jwt.JWT;
import com.spas.backend.common.JWTToken;
import com.spas.backend.dto.UserDto;
import com.spas.backend.mapper.UserMapper;
import com.spas.backend.util.JWTHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashSet;

@Slf4j
public class RealmConfiguration extends AuthorizingRealm {

  @Resource
  private UserMapper userMapper;

  /**
   * 大坑！，必须重写此方法，不然Shiro会报错
   */
  @Override
  public boolean supports(AuthenticationToken token) {
    return token instanceof JWTToken;
  }

  /**
   * 授权：检验用户权限时使用.
   * @param principals 令牌
   * @return 信息
   */
  @Override
  protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
    log.info("授权");
    // 由于重写了 JWTToken 因此这里principals就是token
    //    // 用户 id
    String id = JWTHelper.getUserId(principals.toString());
    // 通过 id 得到用户信息
//    UserDto userDto = userMapper.selectUserById(id);
    // 权限信息，直接从 Token 拿
    String[] roles = JWT.decode(principals.toString()).getClaim("roles").asArray(String.class);
    String[] permissions = JWT.decode(principals.toString()).getClaim("permissions").asArray(String.class);
    // 构造认证信息
    SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
    authorizationInfo.setRoles(new HashSet<>(Arrays.asList(roles)));
    authorizationInfo.setStringPermissions(new HashSet<>(Arrays.asList(permissions)));
    return authorizationInfo;
  }

  /**
   * 认证，而不用于登录发送令牌，或者直接省略也行.
   * @param token 令牌
   * @return 认证信息
   * @throws AuthenticationException 认证异常
   */
  @Override
  protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
    log.info("认证");
    // 根据之前的 JWTToken 可知，这里仍然尝试获取 token
    String JWTtoken = (String)token.getCredentials();
    String id = JWTHelper.getUserId(JWTtoken);
    if(id == null) {
      throw new UnknownAccountException("令牌不合法！");
    }
    UserDto userDto = userMapper.selectUserById(id);  // 使用 Redis 取代, Redis 中没有信息，则你要重新登录了（Redis在登录时设定）
    String secret = userDto.getPassword();  // 将密码Hash值作为秘密
    // 验证
    if(! JWTHelper.verify(secret, JWTtoken)) {
      throw new IncorrectCredentialsException("令牌失效！");
    }
    return new SimpleAuthenticationInfo(JWTtoken,JWTtoken,getName());
  }
}
