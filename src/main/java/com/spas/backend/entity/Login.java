package com.spas.backend.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class Login implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  String id;

  String uid;

  LocalDateTime time;

  String ip;

  String userAgent;

  String osName;

}
