package com.spas.backend.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.spas.backend.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * JWT 工具类.
 * 借助 auth0 的 JWT 实现
 * @author Yuhan Liu
 * @since 2020-03-26
 */
@Component
public class JWTHelper {
  // 过期时间 2分钟
//  private static final long EXPIRE_TIME = 2*60*1000;

  private StringRedisTemplate redisTemplate;

  @Autowired
  public void setRedisTemplate(StringRedisTemplate redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  /**
   * 校验 token 是否有效.
   * @param token 令牌
   * @param secret 秘密
   * @return 正确与否
   */
  public static boolean verify(String secret, String token) {
    try {
      Algorithm algorithm = Algorithm.HMAC256(secret);
      JWTVerifier verifier = JWT.require(algorithm)
          .build(); //Reusable verifier instance
      DecodedJWT jwt = verifier.verify(token);  // 当验证令牌时，时间验证自动发生，当值无效时抛出JWTVerificationException。如果前面的字段中有任何一个丢失，则不会在此验证中考虑它们。
      return true;
    } catch (JWTVerificationException exception){
      //Invalid signature/claims
      return false;
    }
  }

  /**
   * 获取 token 中的id
   * @param token 令牌，包含 sub 字段（用户ID）
   * @return token 中的用户ID
   */
  public static String getUserId(String token) {
    try {
      DecodedJWT jwt = JWT.decode(token);
      return jwt.getKeyId();
    } catch (JWTDecodeException exception){
      //Invalid token
      return null;
    }
  }

  /**
   * 生成访问令牌.
   * @param secret 用户秘密
   * @param id 用户id
   * @param roles 用户角色集合
   * @param permissions 用户权限集合
   * @param expire 过期时间，以分钟计算
   * @return 签发失败返回 null
   */
  public String signAccess(String secret, String id, Set<String> roles, Set<String> permissions, int expire) {
    try {
      Date date = new Date(System.currentTimeMillis() + expire * 60 * 1000);
      Algorithm algorithm = Algorithm.HMAC256(secret);
      return JWT.create()
          .withKeyId(id)
          .withIssuer("http://www.spas.com/")
          .withNotBefore(new Date(System.currentTimeMillis()))
          .withIssuedAt(new Date(System.currentTimeMillis()))
          .withExpiresAt(date)
          .withArrayClaim("roles", roles.toArray(new String[0]))
          .withArrayClaim("permissions", permissions.toArray(new String[0]))
          .sign(algorithm);
    } catch (JWTCreationException e) {
      //Invalid Signing configuration / Couldn't convert Claims.
      return null;
    }
  }

  /**
   * 生成刷新令牌.
   * @param secret 用户秘密
   * @param id 用户id
   * @param expire 过期时间，以分钟为单位
   * @return 签发失败返回 null
   */
  public String signRefresh(String secret, String id, int expire) {
    try {
      Date date = new Date(System.currentTimeMillis() + expire * 60 * 1000);
      Algorithm algorithm = Algorithm.HMAC256(secret);
      String refreshToken =  JWT.create()
          .withKeyId(id)
          .withIssuer("http://www.spas.com/")
          .withNotBefore(new Date(System.currentTimeMillis()))
          .withIssuedAt(new Date(System.currentTimeMillis()))
          .withExpiresAt(date)
          .sign(algorithm);
      // 将 RefreshToken 存储到 Redis 中
      ValueOperations<String,String> valueOperations = redisTemplate.opsForValue();
      Assert.notNull(refreshToken,"令牌不能为空");
      valueOperations.set(id,refreshToken,expire, TimeUnit.MINUTES);
      return refreshToken;
    } catch (JWTCreationException e) {
      return null;
    }
  }

  /**
   * 生成 ID 令牌.
   * @param secret 用户秘密
   * @param id 用户id
   * @param expire 以分钟为单位
   * @return 签发失败返回 null
   */
  public String signId(String secret, String id, UserDto userDto, int expire) {
    try {
      Date date = new Date(System.currentTimeMillis() + expire * 60 * 1000);
      Algorithm algorithm = Algorithm.HMAC256(secret);
      return JWT.create()
          // header
          .withKeyId(id)
          // payload
          .withIssuer("http://www.spas.com/")
          .withNotBefore(new Date(System.currentTimeMillis()))
          .withIssuedAt(new Date(System.currentTimeMillis()))
          .withExpiresAt(date)
          // user info
          .withClaim("name",userDto.getName())
          .withClaim("position",userDto.getPosition())
          .withClaim("email",userDto.getEmail())
          .withClaim("phone",userDto.getPhone())
          .sign(algorithm);
    } catch (JWTCreationException e) {
      return null;
    }
  }

  /**
   * 生成三种令牌
   * @param userDto 用户信息
   * @param expire 过期时间字典，至少含 accessExpire refreshExpire idExpire
   * @return token 字典，含有 accessToken refreshToken idToken
   */
  public HashMap<String,String> sign(UserDto userDto, HashMap<String,Integer> expire) {
    String accessToken = signAccess(userDto.getPassword(),userDto.getId(),userDto.getRoles(),userDto.getPermissions(),expire.get("accessExpire"));
    String refreshToken = signRefresh(userDto.getPassword(),userDto.getId(),expire.get("refreshExpire"));
    String idToken = signId(userDto.getPassword(),userDto.getId(),userDto,expire.get("idExpire"));
    HashMap<String,String> token = new HashMap<>();
    token.put("accessToken",accessToken);
    token.put("refreshToken",refreshToken);
    token.put("idToken",idToken);
    // 将 RefreshToken 存储到 Redis 中
    ValueOperations<String,String> valueOperations = redisTemplate.opsForValue();
    Assert.notNull(refreshToken,"令牌不能为空");
    valueOperations.set(userDto.getId(),refreshToken,expire.get("refreshExpire"), TimeUnit.MINUTES);
    return token;
  }
}
