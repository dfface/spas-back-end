package com.spas.backend.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserDto implements Serializable {

  private static final long serialVersionUID = 1L;

  private String name;

  private String salt;

  private String password;

  private String position;

  private String email;

  private String phone;

  private String departmentId;

  private String officeId;

  private String avatar;
}
