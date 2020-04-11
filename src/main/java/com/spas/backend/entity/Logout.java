package com.spas.backend.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class Logout implements Serializable {

  private static final long SerialVersionUID = 1L;

  @Id
  String id;

  String uid;

  LocalDateTime time;

  String ip;

  String userAgent;

  String osName;
}
