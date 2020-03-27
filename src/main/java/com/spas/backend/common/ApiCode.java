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
  LOGIN_SUCCESS(201,"登录成功"),
  ILLEGAL_ARGUMENT(501, "参数异常"),
  NullPointer(502,"空指针异常"),
  IncorrectCredentials(503,"密码错误"),
  UNAUTHORIZED(504,"无权访问"),
  ACCESS_TOKEN_EXPIRED(505,"访问令牌过期"),
  UNKNOWN_ACCOUNT(506,"该用户未注册"),
  PASSWORD_INVALID(507,"密码错误"),
  ALGORITHM_MISMATCH(601,"令牌中的算法不匹配"),
  SIGNATURE_INVALID(602,"令牌签名无效"),
  TOKEN_EXPIRED(603,"令牌过期"),
  CLAIM_INVALID(604,"令牌信息无效");
  @Getter
  private  int index;
  @Getter
  private  String msg;

  ApiCode(int i, String s) {
    this.index = i;
    this.msg = s;
  }
}
