package com.spas.backend.config;

import com.spas.backend.common.JWTFilter;
import com.spas.backend.util.JWTHelper;
import com.spas.backend.util.PasswordHelper;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.DefaultWebSubjectFactory;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.subject.support.DefaultWebSubjectContext;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfiguration {

  @Bean
  RealmConfiguration myRealm() {
    return new RealmConfiguration();
  }

  @Bean
  DefaultWebSecurityManager securityManager() {
    DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
    // 使用自定义的 Realm
    manager.setRealm(myRealm());
    // 关闭自带的 Session

    DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
    DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
    defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
    subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
    manager.setSubjectDAO(subjectDAO);

    // 禁用会话调度器
    DefaultSessionManager sessionManager = new DefaultSessionManager();
    sessionManager.setSessionValidationSchedulerEnabled(false);
    manager.setSessionManager(sessionManager);

    return manager;
  }


//  @Bean
//  DefaultWebSessionManager sessionManager() {
//    DefaultWebSessionManager webSessionManager = new DefaultWebSessionManager();
//    Cookie cookie = webSessionManager.getSessionIdCookie();
//    cookie.setName("NEW");
//    return webSessionManager;
//  }

//  /**
//   * URL 过滤器
//   * @return 过滤器定义
//   */
//  @Bean
//  ShiroFilterChainDefinition shiroFilterChainDefinition() {
//    DefaultShiroFilterChainDefinition definition = new DefaultShiroFilterChainDefinition();
//    definition.addPathDefinition("/doLogin", "anon");
//    // 放行 静态资源
//    definition.addPathDefinition("/static/**", "anon");
//    // 放行 swagger
//    definition.addPathDefinition("/swagger-ui.html","anon");
//    definition.addPathDefinition("/swagger/**","anon");
//    definition.addPathDefinition("/swagger-resources/**", "anon");
//    definition.addPathDefinition("/v2/**","anon");
//    definition.addPathDefinition("/webjars/**","anon");
//    definition.addPathDefinition("/configuration/**", "anon");
//    definition.addPathDefinition("/**","authcBasic");
//    return definition;
//  }
  /**
   * 添加自己的过滤器，自定义url规则
   * Shiro自带拦截器配置规则
   * rest：比如/admins/user/**=rest[user],根据请求的方法，相当于/admins/user/**=perms[user：method] ,其中method为post，get，delete等
   * port：比如/admins/user/**=port[8081],当请求的url的端口不是8081是跳转到schemal：//serverName：8081?queryString,其中schmal是协议http或https等，serverName是你访问的host,8081是url配置里port的端口，queryString是你访问的url里的？后面的参数
   * perms：比如/admins/user/**=perms[user：add：*],perms参数可以写多个，多个时必须加上引号，并且参数之间用逗号分割，比如/admins/user/**=perms["user：add：*,user：modify：*"]，当有多个参数时必须每个参数都通过才通过，想当于isPermitedAll()方法
   * roles：比如/admins/user/**=roles[admin],参数可以写多个，多个时必须加上引号，并且参数之间用逗号分割，当有多个参数时，比如/admins/user/**=roles["admin,guest"],每个参数通过才算通过，相当于hasAllRoles()方法。//要实现or的效果看http://zgzty.blog.163.com/blog/static/83831226201302983358670/
   * anon：比如/admins/**=anon 没有参数，表示可以匿名使用
   * authc：比如/admins/user/**=authc表示需要认证才能使用，没有参数
   * authcBasic：比如/admins/user/**=authcBasic没有参数表示httpBasic认证
   * ssl：比如/admins/user/**=ssl没有参数，表示安全的url请求，协议为https
   * user：比如/admins/user/**=user没有参数表示必须存在用户，当登入操作时不做检查
   * 详情见文档 http://shiro.apache.org/web.html#urls-
   * @param securityManager
   * @return org.apache.shiro.spring.web.ShiroFilterFactoryBean
   * @author Yuhan Liu
   * @date 2020-03-27
   */
  @Bean(name = "shiroFilter") //如果没有此name,将会找不到shiroFilter的Bean
  public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultWebSecurityManager securityManager) {
    ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
    // 添加自己的过滤器取名为jwt
    Map<String, Filter> filterMap = new HashMap<>(16);
    filterMap.put("jwt", new JWTFilter());
    factoryBean.setFilters(filterMap);
    factoryBean.setSecurityManager(securityManager);
    // 自定义url规则使用LinkedHashMap有序Map
    LinkedHashMap<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>(16);
    // Swagger接口文档
    // 静态资源其实已经去掉了，所以之前手动配置 MVC 好像不必要哦
     filterChainDefinitionMap.put("/v2/**", "anon");
     filterChainDefinitionMap.put("/webjars/**", "anon");
     filterChainDefinitionMap.put("/swagger/**", "anon");
     filterChainDefinitionMap.put("/swagger-resources/**", "anon");
     filterChainDefinitionMap.put("/swagger-ui.html/**", "anon");
     filterChainDefinitionMap.put("/configuration/**", "anon");
    // 公开接口
    // filterChainDefinitionMap.put("/api/**", "anon");
    // 登录接口放开
    filterChainDefinitionMap.put("/login", "anon");
    filterChainDefinitionMap.put("/isLogged", "anon");
    filterChainDefinitionMap.put("/init", "anon");
    // 所有请求通过我们自己的JWTFilter
    // 过滤链定义，从上向下顺序执行，一般将放在最为下边
    filterChainDefinitionMap.put("/**", "jwt");
    factoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
    return factoryBean;
  }
//  @Bean
//  HashedCredentialsMatcher hashedCredentialsMatcher() {
//    HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
//    hashedCredentialsMatcher.setHashAlgorithmName(PasswordHelper.ALGORITHM);
//    hashedCredentialsMatcher.setHashIterations(PasswordHelper.HASH_ITERATIONS);
//    return hashedCredentialsMatcher;
//  }

  /**
   * 为了保证实现了Shiro内部lifecycle函数的bean执行 也是shiro的生命周期
   */
  @Bean
  public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
    return new LifecycleBeanPostProcessor();
  }

  @Bean
  @DependsOn("lifecycleBeanPostProcessor")
  public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
    DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
    // 强制使用cglib，防止重复代理和可能引起代理出错的问题
    // https://zhuanlan.zhihu.com/p/29161098
    defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
    return defaultAdvisorAutoProxyCreator;
  }

  @Bean
  public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
    AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
    advisor.setSecurityManager(securityManager);
    return advisor;
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
