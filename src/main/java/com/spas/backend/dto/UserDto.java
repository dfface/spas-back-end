package com.spas.backend.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Set;

@Data
public class UserDto implements Serializable {

  private static final long serialVersionUID = 1L;

  private String id;

  private String name;

  private String salt;

  private String password;

  private String position;

  private String email;

  private String phone;

  private String departmentId;

  private String officeId;

  private String avatar;

  // 增加用户角色
  private Set<String> roles;

  // 增加用户操作
  private Set<String> permissions;
}
