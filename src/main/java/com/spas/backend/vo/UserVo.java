package com.spas.backend.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Set;

@Data
public class UserVo implements Serializable {
  private static final long serialVersionUID = 1L;

  private String id;

  private String name;

  private String password;

  private String position;

  private String email;

  private String departmentId;

  private String departmentName;

  private String officeId;

  private String officeName;

  private String officeUrl;

  private String officePhone;

  private String officeEmail;

  // 增加用户角色
  private Set<String> roles;

  // 增加用户操作
  private Set<String> permissions;
}
