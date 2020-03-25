package com.spas.backend.util;

import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户注册：密码Hash.
 *
 * @author Yuhan Liu
 * @since 2020-03-25
 */
public class PasswordHelper {
  private RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();
  public static final String ALGORITHM = "SHA-256";
  public static final int HASH_ITERATIONS = 2; // 散列次数

  public Map<String, String> hashPassword(String password){
    String salt = randomNumberGenerator.nextBytes().toHex();
    String newPassword = new SimpleHash(ALGORITHM, password,
        ByteSource.Util.bytes(salt),HASH_ITERATIONS).toHex();
    Map<String, String> map = new HashMap<>();
    map.put("salt",salt);
    map.put("password",newPassword);
    return map;
  }
}
