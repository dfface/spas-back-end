package com.spas.backend.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserRoleDto implements Serializable {
  private static final long serialVersionUID = 1L;

  private String rolId;
}
