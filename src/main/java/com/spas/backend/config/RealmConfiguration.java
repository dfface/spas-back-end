package com.spas.backend.config;

import com.spas.backend.dto.UserDto;
import com.spas.backend.entity.User;
import com.spas.backend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

@Slf4j
public class RealmConfiguration extends AuthorizingRealm {

  private UserService userService;

  @Autowired
  private void setUserService(UserService userService){
    this.userService = userService;
  }

  @Override
  protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
    SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
    String email = (String) principals.getPrimaryPrincipal();
    User user = (User) userService.selectUser(email).getData();

    return authorizationInfo;
  }

  @Override
  protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
    String email = (String) token.getPrincipal();
    Object ps = token.getCredentials();
    log.info("Realm: " + ps);
    UserDto user = (UserDto) userService.selectUser(email).getData();
    if(user == null){
      throw new UnknownAccountException("账户不存在！");
    }
    return new SimpleAuthenticationInfo(user.getEmail(), user.getPassword(), ByteSource.Util.bytes(user.getSalt()), getName());
  }
}
