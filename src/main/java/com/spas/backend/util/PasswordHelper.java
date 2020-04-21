package com.spas.backend.util;

import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户注册：密码Hash.
 *
 * @author Yuhan Liu
 * @since 2020-03-25
 */
@Component
public class PasswordHelper {
  private final RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();
  public static final String ALGORITHM = "SHA-256";
  public static final int HASH_ITERATIONS = 2; // 散列次数

  /**
   * 生成密码和盐值.
   * @param password 密码
   * @return 密码和盐值的字典
   */
  public Map<String, String> createPassword(String password){
    String salt = randomNumberGenerator.nextBytes().toHex();
    String newPassword = new SimpleHash(ALGORITHM, password,
        ByteSource.Util.bytes(salt),HASH_ITERATIONS).toHex();
    Map<String, String> map = new HashMap<>();
    map.put("salt",salt);
    map.put("password",newPassword);
    return map;
  }

  /**
   * 将原始密码和盐值一起Hash.
   * @param salt 盐值
   * @param password 密码
   * @return Hash 后的结果
   */
  public String hashPassword(String password, String salt) {
    return new SimpleHash(ALGORITHM, password,
        ByteSource.Util.bytes(salt),HASH_ITERATIONS).toHex();
  }
}
