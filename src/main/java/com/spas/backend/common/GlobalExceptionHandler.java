package com.spas.backend.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器.
 *
 * @author Yuhan Liu
 * @since 2020-03-25
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  /**
   * 参数不合法.
   */
  @ExceptionHandler(IllegalArgumentException.class)
  public ApiResponse IllegalArgumentExceptionHandler(IllegalArgumentException e) {
    return new ApiResponse(ApiCode.IllegalArgument,e.getMessage());
  }

  /**
   * 空指针异常.
   */
  @ExceptionHandler(NullPointerException.class)
  public ApiResponse NullPointerExceptionHandler(NullPointerException e) {
    return new ApiResponse(ApiCode.NullPointer);
  }

  /**
   * 用户密码错误.
   */
  @ExceptionHandler(IncorrectCredentialsException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public ApiResponse IncorrectCredentialsExceptionHandler(IncorrectCredentialsException e) {
    return new ApiResponse(ApiCode.IncorrectCredentials);
  }
}
