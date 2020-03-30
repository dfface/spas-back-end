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

  @Override
  protected void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
        .allowedMethods("PUT","DELETE","POST","GET")
        .allowedHeaders("*")
        .allowedOrigins(CROSOrigin)
        .allowCredentials(true)
        .exposedHeaders("access-control-allow-headers",
        "access-control-allow-methods",
        "access-control-allow-origin",
        "access-control-max-age",
        "X-Frame-Options");
    super.addCorsMappings(registry);
  }
}
