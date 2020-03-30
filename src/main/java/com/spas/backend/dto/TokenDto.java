package com.spas.backend.dto;

import lombok.Data;

@Data
public class TokenDto {

  private String idToken;

  private String refreshToken;

  private String accessToken;
}
