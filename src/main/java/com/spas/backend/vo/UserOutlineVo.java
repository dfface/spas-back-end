package com.spas.backend.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 用于用户管理，修改简单信息.
 *
 * @author Yuhan Liu
 * @since 2020-04-21
 */
@Data
public class UserOutlineVo implements Serializable {
  private static final long serialVersionUID = 1L;

  private String id;

  private String name;

  private String position;

  private String email;

  private String state;
}
