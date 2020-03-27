package com.spas.backend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;
import java.util.HashMap;

@SpringBootTest
public class RedisTests {

  @Autowired
  private StringRedisTemplate redisTemplate;

//  @Test
  public void testHashOps() {
    System.out.println(redisTemplate.toString());
    HashOperations<String,String,String> ops = redisTemplate.opsForHash();
    ops.put("id","refreshToken","token");
    Object aa = ops.get("id","refreshToken");
    System.out.println(aa);
  }
}
