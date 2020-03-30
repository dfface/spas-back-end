package com.spas.backend.common;

import com.alibaba.druid.support.json.JSONUtils;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.spas.backend.common.exception.JWTException;
import com.spas.backend.util.CustomUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

/**
 * JWTFilter.
 * 重写过滤器，继承 BasicHttpAuthenticationFilter(为什么不是 FormAuthenticationFilter ？)
 * 因为 Form 的话，一旦没有登录（准确的说，这里应该是JWT令牌），就直接导向登录页面，而 HTTP Basic 是通过 Authorization 字段验证的
 * preHandle -> isAccessAllowed -> isLoginAttempt -> executeLogin
 */
@Slf4j
public class JWTFilter extends BasicHttpAuthenticationFilter {

  @Value("${user.cros.origin}")
  private String CROSOrigin;
  /**
   * 处理请求之前.
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  @Override
  protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
    log.info("在处理之前");
    // HttpServletRequest对象代表客户端的请求，当客户端通过HTTP协议访问服务器时，HTTP请求头中的所有信息都封装在这个对象中
    HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
    // 这个对象中封装了向客户端发送数据、发送响应头，发送响应状态码的方法。
    HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
//    // 设置跨域支持
//    httpServletResponse.setHeader("Access-control-Allow-Origin", CROSOrigin);  // httpServletRequest.getHeader("Origin")  不能是 *（所有来源），否则 credential 失效
//    httpServletResponse.setHeader("Access-Control-Allow-Credentials", "true"); //true代表允许携带cookie
//    httpServletResponse.setHeader("Access-control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
//    httpServletResponse.setHeader("Access-Control-Allow-Headers",httpServletRequest.getHeader("Access-Control-Request-Headers"));
//    log.debug("Origin: " + httpServletRequest.getHeader("Origin"));
//    log.debug("Access-Control-Request-Headers: " + httpServletRequest.getHeader("Access-Control-Request-Headers"));
    // 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
    if(httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
      log.info("收到options请求");
      httpServletResponse.setStatus(HttpStatus.OK.value());
      return false;
    }
    return super.preHandle(request, response);
  }

  /**
   * 这里我们详细说明下为什么重写
   * 可以对比父类方法，只是将executeLogin方法调用去除了
   * 如果没有去除将会循环调用doGetAuthenticationInfo方法
   */
  @Override
  protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
    log.info("当访问被拒绝");
    this.sendChallenge(request, response);
    return false;
  }

  /**
   * isLoginAttempt.
   * 判断有没有 Authorization 字段
   * @param request
   * @param response
   * @return 真则应该去校验，假则不用鉴权了
   */
  @Override
  protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
//    HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
//    String authorization = httpServletRequest.getHeader(AUTHORIZATION_HEADER);
    log.info("是否要校验JWT："+ (getAuthzHeader(request) != null));
    return getAuthzHeader(request) != null;
  }

  /**
   * executeLogin，执行登录.
   * 如果有该Authorization字段，那么就执行校验令牌
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  @Override
  protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
//    HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
//    String authorization = httpServletRequest.getHeader(AUTHORIZATION_HEADER);
    // 拿到当前Header中Authorization的AccessToken(Shiro中getAuthzHeader方法已经实现)
    JWTToken token = new JWTToken(getAuthzHeader(request));
    // 提交给 realm 进行登录
    log.info("执行登录：提交给 realm 进行登录");
    getSubject(request, response).login(token);
    // 没有抛出异常则返回 true
    log.info("登录成功");
    return true;
  }

  /**
   * isAccessAllowed, 是否允许访问.
   * 都是返回 true
   * 例如我们提供一个地址 GET /article
   * 登入用户和游客看到的内容是不同的
   * 如果在这里返回了false，请求会被直接拦截，用户看不到任何东西
   * 所以我们在这里返回true，Controller中可以通过 subject.isAuthenticated() 来判断用户是否登入
   * 如果有些资源 只有 登入用户才能访问，我们只需要在方法上面加上 @RequiresAuthentication 注解即可
   * @param request
   * @param response
   * @param mappedValue
   * @return 真
   */
  @Override
  protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
    log.info("是否允许访问");
    if (isLoginAttempt(request, response)) {
      log.info("有Authorization字段");
      // 有 Authorization 字段
      try {
        executeLogin(request, response);
      } catch (JWTException e){
        log.error("JWT异常");
      }
      catch (Exception e) {
        // 认证出现异常，传递错误信息
        String msg = e.getMessage();
        // 获取具体的异常
        Throwable throwable = e.getCause();
        try {
          throw throwable;
        } catch (JWTException jwtEx) {
          log.info("JWTException 校验失败："+jwtEx.getApiCode().getMsg());
          CustomUtil.sendApiResponse(response, HttpStatus.UNAUTHORIZED, new ApiResponse(jwtEx.getApiCode()));
        } catch (IllegalArgumentException illEx) {
          log.info("IllegalArgumentException 校验失败："+illEx.getMessage());
          CustomUtil.sendApiResponse(response, HttpStatus.UNAUTHORIZED, new ApiResponse(ApiCode.ILLEGAL_ARGUMENT));
        } catch (Throwable thEx) {
          log.info("Throwable 校验失败："+thEx.getMessage());
        }
        return false;
      }
      return true;
    }
    // 之前：没有携带 Token 也让过，目的就是让他能访问一些东西，如果不让过，怎么弄？
    // 现在：没有携带 Token 不通过，否则报错
    else {
      HttpServletRequest httpServletRequest = WebUtils.getHttpRequest(request);
      log.info("请求地址：" + httpServletRequest);
      log.info(ApiCode.UNAUTHORIZED.getMsg() + " 没有 Token");
      // 是否任何请求必须登录
      final Boolean mustLoginFlag = true;
      // 发送了这个响应，就不能再发送URL请求的响应了
      CustomUtil.sendApiResponse(response,HttpStatus.UNAUTHORIZED,new ApiResponse(ApiCode.UNAUTHORIZED,"没有令牌！"));
      return false;
    }
  }
}
