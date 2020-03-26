package com.spas.backend;

import com.spas.backend.util.JWTHelper;
import com.spas.backend.util.PasswordHelper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@MapperScan("com.spas.backend.mapper")
public class BackEndApplication {

  public static void main(String[] args) {
        SpringApplication.run(BackEndApplication.class, args);
    }
}
