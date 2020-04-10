package com.spas.backend.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LoginVo {

  @NotBlank
  private String officeId;

  @NotBlank
  private String password;

  @NotBlank
  private String email;
}
