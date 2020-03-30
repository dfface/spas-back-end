package com.spas.backend.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authz.AuthorizationException;
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
    return new ApiResponse(ApiCode.ILLEGAL_ARGUMENT,e.getMessage());
  }

  /**
   * 空指针异常.
   */
  @ExceptionHandler(NullPointerException.class)
  public ApiResponse NullPointerExceptionHandler() {
    return new ApiResponse(ApiCode.NULL_POINTER);
  }

  /**
   * 用户密码错误.
   */
  @ExceptionHandler(IncorrectCredentialsException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public ApiResponse IncorrectCredentialsExceptionHandler(IncorrectCredentialsException e) {
    return new ApiResponse(ApiCode.INCORRECT_CREDENTIALS);
  }

  /**
   * 没有权限访问.
   */
  @ExceptionHandler(AuthorizationException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public ApiResponse AuthorizationExceptionHandler(AuthorizationException e) {
    return new ApiResponse(ApiCode.UNAUTHORIZED);
  }
}
