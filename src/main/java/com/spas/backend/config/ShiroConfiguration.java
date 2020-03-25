package com.spas.backend.config;

import com.spas.backend.util.PasswordHelper;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
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
    definition.addPathDefinition("/**","anon");
    return definition;
  }

  @Bean
  HashedCredentialsMatcher hashedCredentialsMatcher() {
    HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
    hashedCredentialsMatcher.setHashAlgorithmName(PasswordHelper.ALGORITHM);
    hashedCredentialsMatcher.setHashIterations(PasswordHelper.HASH_ITERATIONS);
    return hashedCredentialsMatcher;
  }

}
