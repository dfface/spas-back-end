package com.spas.backend.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class UserRegisterVo implements Serializable {

  private static final long serialVersionUID = 1L;

  @NotBlank
  private String name;

  @NotBlank
  private String password;

  @NotBlank
  private String email;

  @NotBlank
  private String position;

  @NotBlank
  private String officeId;

  @NotBlank
  private String officeName;

  @NotBlank
  private String roleId;
}
