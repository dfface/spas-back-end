package com.spas.backend.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.*;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.spas.backend.common.ApiCode;
import com.spas.backend.dto.UserDto;
import com.spas.backend.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
public class JWTHelper {
  // 过期时间 2分钟
//  private static final long EXPIRE_TIME = 2*60*1000;
  @Value("${user.site.url}")
  private static String siteUrl;

  /**
   * 校验 token 是否有效.
   * @param token 令牌
   * @param secret 秘密
   * @return 正确与否
   */
  public static ApiCode verify(String secret, String token) {
    try {
      Algorithm algorithm = Algorithm.HMAC256(secret);
      JWTVerifier verifier = JWT.require(algorithm)
          .build(); //Reusable verifier instance
      DecodedJWT jwt = verifier.verify(token);  // 当验证令牌时，时间验证自动发生，当值无效时抛出JWTVerificationException。如果前面的字段中有任何一个丢失，则不会在此验证中考虑它们。
      return ApiCode.OK;
    } catch (AlgorithmMismatchException e){
      return ApiCode.ALGORITHM_MISMATCH;
    } catch (SignatureVerificationException e){
      return ApiCode.SIGNATURE_INVALID;
    } catch (TokenExpiredException e){
      return ApiCode.TOKEN_EXPIRED;
    } catch (InvalidClaimException e){
      return ApiCode.CLAIM_INVALID;
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
   * @param  userVo 用户信息
   * @param expire 过期时间，以分钟计算
   * @return 签发失败返回 null
   */
  public static String signAccess(UserVo userVo, int expire) {
    try {
      Date date = new Date(System.currentTimeMillis() + expire * 60 * 1000);
      Algorithm algorithm = Algorithm.HMAC256(userVo.getPassword());
      return JWT.create()
          .withKeyId(userVo.getId())
          .withIssuer(siteUrl)
          .withNotBefore(new Date(System.currentTimeMillis()))
          .withIssuedAt(new Date(System.currentTimeMillis()))
          .withExpiresAt(date)
          .withArrayClaim("roles", userVo.getRoles().toArray(new String[0]))
          .withArrayClaim("permissions", userVo.getPermissions().toArray(new String[0]))
          .sign(algorithm);
    } catch (JWTCreationException e) {
      //Invalid Signing configuration / Couldn't convert Claims.
      return null;
    }
  }

  /**
   * 生成访问令牌.
   * @param  userDto 用户信息
   * @param expire 过期时间，以分钟计算
   * @return 签发失败返回 null
   */
  public static String signAccess(UserDto userDto, int expire) {
    try {
      Date date = new Date(System.currentTimeMillis() + expire * 60 * 1000);
      Algorithm algorithm = Algorithm.HMAC256(userDto.getPassword());
      return JWT.create()
          .withKeyId(userDto.getId())
          .withIssuer(siteUrl)
          .withNotBefore(new Date(System.currentTimeMillis()))
          .withIssuedAt(new Date(System.currentTimeMillis()))
          .withExpiresAt(date)
          .withArrayClaim("roles", userDto.getRoles().toArray(new String[0]))
          .withArrayClaim("permissions", userDto.getPermissions().toArray(new String[0]))
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
  public static String signRefresh(String secret, String id, int expire) {
    try {
      Date date = new Date(System.currentTimeMillis() + expire * 60 * 1000);
      Algorithm algorithm = Algorithm.HMAC256(secret);
      return JWT.create()
          .withKeyId(id)
          .withIssuer(siteUrl)
          .withNotBefore(new Date(System.currentTimeMillis()))
          .withIssuedAt(new Date(System.currentTimeMillis()))
          .withExpiresAt(date)
          .sign(algorithm);
    } catch (JWTCreationException e) {
      return null;
    }
  }

  /**
   * 生成 ID 令牌.
   * @param expire 以分钟为单位
   * @return 签发失败返回 null
   */
  public static String signId(UserVo userVo, int expire) {
    try {
      Date date = new Date(System.currentTimeMillis() + expire * 60 * 1000);
      Algorithm algorithm = Algorithm.HMAC256(userVo.getPassword());  // 密码作为秘密
      return JWT.create()
          // header
          .withKeyId(userVo.getId())
          // payload
          .withIssuer(siteUrl)
          .withNotBefore(new Date(System.currentTimeMillis()))
          .withIssuedAt(new Date(System.currentTimeMillis()))
          .withExpiresAt(date)
          // user info
          .withClaim("id",userVo.getId())
          .withClaim("name",userVo.getName())
          .withClaim("position",userVo.getPosition())
          .withClaim("email",userVo.getEmail())
          .withClaim("officeUrl",userVo.getOfficeUrl())
          .withClaim("officeId",userVo.getOfficeId())
          .withClaim("officeEmail",userVo.getOfficeEmail())
          .withClaim("officeName",userVo.getOfficeName())
          .sign(algorithm);
    } catch (JWTCreationException e) {
      return null;
    }
  }

  /**
   * 生成三种令牌
   * @param userVo 用户信息
   * @param expire 过期时间字典，至少含 accessExpire refreshExpire idExpire
   * @return token 字典，含有 accessToken refreshToken idToken
   */
  public static HashMap<String,String> sign(UserVo userVo, HashMap<String,Integer> expire) {
    String accessToken = signAccess(userVo,expire.get("accessExpire"));
    String refreshToken = signRefresh(userVo.getPassword(),userVo.getId(),expire.get("refreshExpire"));
    String idToken = signId(userVo,expire.get("idExpire"));
    HashMap<String,String> token = new HashMap<>();
    token.put("accessToken",accessToken);
    token.put("refreshToken",refreshToken);
    token.put("idToken",idToken);
    return token;
  }
}
