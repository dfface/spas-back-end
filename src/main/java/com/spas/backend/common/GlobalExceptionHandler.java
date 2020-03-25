package com.spas.backend.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
    return new ApiResponse(ApiCode.IllegalArgument);
  }
}
