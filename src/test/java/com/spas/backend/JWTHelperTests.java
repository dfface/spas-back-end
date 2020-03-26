package com.spas.backend;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.spas.backend.dto.UserDto;
import com.spas.backend.mapper.UserMapper;
import com.spas.backend.util.JWTHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;

@SpringBootTest
public class JWTHelperTests {
  // 过期时间 2分钟
  private static final long EXPIRE_TIME = 2*60*1000;
  private String secret = "secret";
  private String id = "6bf8719eec5bec13aa61d6eacdd74818";
  private HashSet<String> roles = new HashSet<>();
  private HashSet<String> permissions = new HashSet<>();

  @Autowired
  private JWTHelper jwtHelper;

  @Resource
  private UserMapper userMapper;

  @Test
  public void sign() {
      roles.add("chief_procurator");
      permissions.add("user:addByDto");
      Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
      Algorithm algorithm = Algorithm.HMAC256(secret);
      String token = JWT.create()
          .withKeyId(id)
          .withIssuer("http://www.spas.com/")
          .withClaim("sub",id)
          .withNotBefore(new Date(System.currentTimeMillis()))
          .withIssuedAt(new Date(System.currentTimeMillis()))
          .withExpiresAt(date)
          .withArrayClaim("roles", roles.toArray(new String[0]))
          .withArrayClaim("permissions", permissions.toArray(new String[0]))
          .sign(algorithm);
      System.out.println(token);
  }

  @Test
  public void decode() {
    String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI2YmY4NzE5ZWVjNWJlYzEzYWE2MWQ2ZWFjZGQ3NDgxOCIsInBlcm1pc3Npb25zIjpbInVzZXI6YWRkQnlEdG8iXSwicm9sZXMiOlsiY2hpZWZfcHJvY3VyYXRvciJdLCJleHAiOjE1ODUyMTU0Nzd9.NSffOTqj9R48LpxqEghaJnMIfBjD2SKouOA8T7Uzi7w\n";
    String[] header = JWT.decode(token).getClaim("roles").asArray(String.class);
    String[] payload = JWT.decode(token).getClaim("permissions").asArray(String.class);
    System.out.println(Arrays.toString(header));
    System.out.println(Arrays.toString(payload));
  }

  @Test
  public void testAccessToken() {
    UserDto userDto = userMapper.selectUserById(id);
    String token = jwtHelper.signId(userDto.getPassword(),userDto.getId(),userDto,2);
    System.out.println(token);
  }
}
