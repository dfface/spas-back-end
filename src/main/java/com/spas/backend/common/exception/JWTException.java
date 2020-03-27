package com.spas.backend.common.exception;

import com.spas.backend.common.ApiCode;
import lombok.Data;
import lombok.Getter;

public class JWTException extends RuntimeException {
  @Getter
  private ApiCode apiCode;

  public JWTException(){
    super();
  }

  public JWTException(ApiCode apiCode){
    super();
    this.apiCode = apiCode;
  }
}
