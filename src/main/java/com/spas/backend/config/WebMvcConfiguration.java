package com.spas.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

/**
 * 针对 Mvc 进行高级配置.
 * 千万不要忘记，@Configuration .
 *
 * @author Yuhan Liu
 * @since 2020-03-26
 */
@Configuration
public class WebMvcConfiguration  extends WebMvcConfigurationSupport{

  @Value("${user.cros.origin}")
  private String CROSOrigin;

  /**
   * CROS 配置
   * @param registry
   */
  @Override
  protected void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
        .allowedMethods("PUT","DELETE","POST","GET","PATCH", "OPTIONS")
        .allowedHeaders("*")
        .allowedOrigins("*")
        .allowCredentials(true)
        .exposedHeaders("Access-Control-Allow-Headers",
        "Access-Control-Allow-Methods",
        "Access-Control-Allow-Origin",
        "Access-Control-Max-Age",
        "X-Frame-Options");
    super.addCorsMappings(registry);
  }

  /**
   * 添加静态资源
   * @param registry
   */
  @Override
  protected void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/**").addResourceLocations(
        "classpath:/static/");
    registry.addResourceHandler("swagger-ui.html").addResourceLocations(
        "classpath:/META-INF/resources/");
    registry.addResourceHandler("/webjars/**").addResourceLocations(
        "classpath:/META-INF/resources/webjars/");
    super.addResourceHandlers(registry);
  }
}
