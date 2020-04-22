package com.spas.backend.vo;

import com.spas.backend.entity.Role;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class UserRoleVo implements Serializable {

  private static final long serialVersionUID = 1L;

  private String userId;  // 用户id

  private String name; // 用户姓名

  private String position;

  private String email;

  private String state;

  private List<Role> roles;  // 用户的角色，顶多有三个呗
}
