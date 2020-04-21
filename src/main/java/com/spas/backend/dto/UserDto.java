package com.spas.backend.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Set;

/**
 * 仅用于注册用户.
 */
@Data
public class UserDto implements Serializable {

  private static final long serialVersionUID = 1L;

  private String id;

  private String name;

  private String salt;

  private String password;

  private String position;

  private String email;

  private String officeId;

  private Integer state;

  // 添加了检察院名称
  private String officeName;

  // 增加用户角色
  private Set<String> roles;

  // 增加用户操作
  private Set<String> permissions;
}
