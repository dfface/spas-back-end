package com.spas.backend.common.exception;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * 自定义异常.
 *
 * @author Yuhan Liu
 * @since 2020-03-26
 */
public class CustomException extends RuntimeException {

  public CustomException(){
    super();
  }

  public CustomException(String msg){
    super(msg);
  }
}
