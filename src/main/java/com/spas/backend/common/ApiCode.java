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
  ALREADY_REGISTERED(207, "该邮箱已经注册过了"),
  USER_AUDIT_NOT_PASSED(208, "用户审核未通过"),
  LOGOUT_SUCCESS(209,"登出成功"),
  LOGOUT_FAILED(210,"登出失败"),
  // 3XX 业务相关码
  UNAUDITED(301,"未经审核"),
  REPORT_HAS_BEEN_JUDGED(302,"已经被评价过的报告不许修改"),
  OFFICE_DELETE_FAILED(303,"检察院不能被删除，因为还有用户使用"),
  OFFICE_NOT_FOUND(304,"未注册的检察院"),
  USER_DELETE_FAILED(305, "用户不能被删除，因为还有与之相关的案件等"),
  USER_STATE_FAILED(306,"用户状态更改失败"),
  // 6XX 令牌相关码
  ALGORITHM_MISMATCH(601,"令牌中的算法不匹配"),
  SIGNATURE_INVALID(602,"令牌签名无效"),
  TOKEN_EXPIRED(603,"令牌过期"),
  CLAIM_INVALID(604,"令牌信息无效"),
  // 公共码，全局异常码
  ILLEGAL_ARGUMENT(901, "参数异常"),
  NULL_POINTER(902,"空指针异常"),
  UNAUTHORIZED(903,"无权访问");
  @Getter
  private final int index;
  @Getter
  private final String msg;

  ApiCode(int i, String s) {
    this.index = i;
    this.msg = s;
  }
}
