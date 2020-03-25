package com.spas.backend.config;

import com.spas.backend.util.PasswordHelper;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShiroConfiguration {

  @Bean
  RealmConfiguration myRealm() {
    RealmConfiguration shiroRealm = new RealmConfiguration();
    shiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());
    return shiroRealm;
  }

  @Bean
  DefaultWebSecurityManager securityManager() {
    DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
    manager.setRealm(myRealm());
    return manager;
  }

  /**
   * URL 过滤器
   * @return 过滤器定义
   */
  @Bean
  ShiroFilterChainDefinition shiroFilterChainDefinition() {
    DefaultShiroFilterChainDefinition definition = new DefaultShiroFilterChainDefinition();
    definition.addPathDefinition("/doLogin", "anon");
    // 放行 静态资源
    definition.addPathDefinition("/static/**", "anon");
    // 放行 swagger
    definition.addPathDefinition("/swagger-ui.html","anon");
    definition.addPathDefinition("/swagger/**","anon");
    definition.addPathDefinition("/swagger-resources/**", "anon");
    definition.addPathDefinition("/v2/**","anon");
    definition.addPathDefinition("/webjars/**","anon");
    definition.addPathDefinition("/configuration/**", "anon");
    definition.addPathDefinition("/**","authc");
    return definition;
  }

  @Bean
  HashedCredentialsMatcher hashedCredentialsMatcher() {
    HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
    hashedCredentialsMatcher.setHashAlgorithmName(PasswordHelper.ALGORITHM);
    hashedCredentialsMatcher.setHashIterations(PasswordHelper.HASH_ITERATIONS);
    return hashedCredentialsMatcher;
  }

  /**
   * 为了保证实现了Shiro内部lifecycle函数的bean执行 也是shiro的生命周期
   */
  @Bean
  public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
    return new LifecycleBeanPostProcessor();
  }

  @Bean
  public static DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
    DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
    /*
     * setUsePrefix(true)用于解决一个奇怪的bug。在引入spring aop的情况下。
     * 在@Controller注解的类的方法中加入@RequiresRole等shiro注解，会导致该方法无法映射请求，
     * 导致返回404。加入这项配置能解决这个bug
     */
    defaultAdvisorAutoProxyCreator.setUsePrefix(true);
    return defaultAdvisorAutoProxyCreator;
  }

}
