package com.spas.backend.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 更新用户基本信息及角色信息，与 UserRoleVo 对应.
 */
@Data
public class UserRoleUpdateDto implements Serializable {

  private static final long serialVersionUID = 1L;

  private String userId;  // 用户id

  private String name; // 用户姓名

  private String position;

  private String email;

  private String state;

  private String officeId;  // 方便后端写入

  private List<String> roles;  // 用户的角色，顶多有三个呗，都是id表示
}
