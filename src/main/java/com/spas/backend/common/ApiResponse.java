package com.spas.backend.common;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.io.Serializable;

@Data
public class ApiResponse implements Serializable {

  private static final long serialVersionUID = 1L;

  private int code;
  private String msg;
  private Object data;

  public ApiResponse(){
    this.code = ApiCode.OK.getIndex();
    this.msg = ApiCode.OK.getMsg();
    this.data = null;
  }

  public ApiResponse(ApiCode apiCode){
    this.code = apiCode.getIndex();
    this.msg = apiCode.getMsg();
    this.data = null;
  }

  public ApiResponse(ApiCode apiCode, String msg){
    this.code = apiCode.getIndex();
    this.msg = msg;
    this.data = null;
  }

  public ApiResponse(ApiCode apiCode, Object data){
    this.code = apiCode.getIndex();
    this.msg = apiCode.getMsg();
    this.data = JSON.toJSON(data);
  }

  public ApiResponse(Object data){
    this.code = ApiCode.OK.getIndex();
    this.msg = ApiCode.OK.getMsg();
    this.data = JSON.toJSON(data);
  }

  public ApiResponse(String data){
    this.code = ApiCode.OK.getIndex();
    this.msg = ApiCode.OK.getMsg();
    this.data = data;
  }
}
