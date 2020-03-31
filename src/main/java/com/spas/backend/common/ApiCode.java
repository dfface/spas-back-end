package com.spas.backend.common;

import lombok.Getter;

/**
 * Api Code.
 *
 * @author Yuhan Liu
 * @since 2020-03-25
 */
public enum ApiCode {

  // 10以内，统一通用状态码
  OK(1,"成功"),
  // 2XX USER 相关码
  LOGIN_SUCCESS(201,"登录成功"),
  IS_LOGGED_FALSE(202,"未登录"),
  IS_LOGGED_TRUE(203,"已登录"),
  UNKNOWN_ACCOUNT(204,"该用户未注册"),
  PASSWORD_INVALID(205,"密码错误"),
  INCORRECT_CREDENTIALS(206,"密码错误"),
  // 3XX 业务相关码
  UNAUDITED(301,"未经审核"),

  ACCESS_TOKEN_EXPIRED(505,"访问令牌过期"),
  ALGORITHM_MISMATCH(601,"令牌中的算法不匹配"),
  SIGNATURE_INVALID(602,"令牌签名无效"),
  TOKEN_EXPIRED(603,"令牌过期"),
  CLAIM_INVALID(604,"令牌信息无效"),
  // 公共码，全局异常码
  ILLEGAL_ARGUMENT(901, "参数异常"),
  NULL_POINTER(902,"空指针异常"),
  UNAUTHORIZED(903,"无权访问");
  @Getter
  private  int index;
  @Getter
  private  String msg;

  ApiCode(int i, String s) {
    this.index = i;
    this.msg = s;
  }
}
