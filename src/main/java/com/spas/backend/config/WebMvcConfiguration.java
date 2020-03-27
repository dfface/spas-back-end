//package com.spas.backend.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
//
///**
// * 针对 Mvc 进行高级配置.
// * 千万不要忘记，@Configuration .
// *
// * @author Yuhan Liu
// * @since 2020-03-26
// */
//@Configuration
//public class WebMvcConfiguration implements WebMvcConfigurer {
//
//  /**
//   * Override this method to add resource handlers for serving static resources.
//   * @param registry
//   */
//  @Override
//  public void addResourceHandlers(ResourceHandlerRegistry registry) {
//    WebMvcConfigurer.super.addResourceHandlers(registry);
//    // 注册静态资源地址，可在 application.yml 中配置[]
//    registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
//    registry.addResourceHandler("/swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
//    registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
//  }
//}
