package com.spas.backend.common;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.shiro.authc.AuthenticationToken;

@AllArgsConstructor
@NoArgsConstructor
public class JWTToken implements AuthenticationToken {

  private String token;

  @Override
  public Object getPrincipal() {
    return token;
  }

  @Override
  public Object getCredentials() {
    return token;
  }
}
