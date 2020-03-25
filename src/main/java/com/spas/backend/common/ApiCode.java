package com.spas.backend.common;

import lombok.Getter;

/**
 * Api Code.
 *
 * @author Yuhan Liu
 * @since 2020-03-25
 */
public enum ApiCode {

  OK(200,"成功"),
  IllegalArgument(501, "参数异常");

  @Getter
  private  int index;
  @Getter
  private  String msg;

  ApiCode(int i, String s) {
    this.index = i;
    this.msg = s;
  }
}
