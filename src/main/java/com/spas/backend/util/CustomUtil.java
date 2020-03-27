package com.spas.backend.util;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spas.backend.common.ApiResponse;
import com.spas.backend.common.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.codec.json.Jackson2JsonEncoder;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
public class CustomUtil {

  public static void sendApiResponse(ServletResponse response, HttpStatus httpStatus, ApiResponse apiResponse) {
    HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
    httpServletResponse.setStatus(httpStatus.value());
    httpServletResponse.setCharacterEncoding("UTF-8");
    httpServletResponse.setContentType("application/json; charset=utf-8");
    // PrintWriter 字符流，而 OutputStream 字节流
    try (PrintWriter out = httpServletResponse.getWriter()) {
//      // Jackson  Object 转 JSON
//      String data = new ObjectMapper().writeValueAsString(apiResponse);
      // fastjson
      String data = JSON.toJSONString(apiResponse);
      out.append(data);
    } catch (IOException io) {
      log.error("返回ApiResponse时产生异常：" + io.getMessage());
      throw new CustomException("返回ApiResponse时产生异常：" + io.getMessage());
    }
  }
}
