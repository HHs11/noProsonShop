package com.cqyt.config.shiro;

import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.session.mgt.ExecutorServiceSessionValidationScheduler;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@SpringBootConfiguration
public class ShiroConfig {
    /**
     * cookie对象;
     * rememberMeCookie()方法是设置Cookie的生成模版，比如cookie的name，cookie的有效时间等等。
     * @return
     */
    @Bean
    public SimpleCookie rememberMeCookie(){
        //System.out.println("ShiroConfiguration.rememberMeCookie()");
        //这个参数是cookie的名称，对应前端的checkbox的name = rememberMe
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        //<!-- 记住我cookie生效时间30天 ,单位秒;-->
        simpleCookie.setMaxAge(259200);
        return simpleCookie;
    }
    /**
     * cookie管理对象;
     * rememberMeManager()方法是生成rememberMe管理器，而且要将这个rememberMe管理器设置到securityManager中
     * @return
     */
    @Bean
    public CookieRememberMeManager rememberMeManager(){
        //System.out.println("ShiroConfiguration.rememberMeManager()");
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberMeCookie());
        //rememberMe cookie加密的密钥 建议每个项目都不一样 默认AES算法 密钥长度(128 256 512 位)
        cookieRememberMeManager.setCipherKey(Base64.decode("2AvVhdsgUs0FSA3SDFAdag=="));
        return cookieRememberMeManager;
    }
    @Bean
    public EhCacheManager cacheManager(){
        EhCacheManager ehCacheManager = new EhCacheManager();
        // ehCacheManager.setCacheManagerConfigFile("classpath:ehcache.xml");
        return ehCacheManager;
    }
    @Bean
    public DefaultWebSecurityManager securityManager(SessionManager sessionManager) {
        DefaultWebSecurityManager sm = new DefaultWebSecurityManager();
        //注入记住我管理器
        sm.setRememberMeManager(rememberMeManager());
        // 配置shiro的缓存
        sm.setCacheManager(cacheManager());
        //注入自定义sessionManager
        sm.setSessionManager(sessionManager);
        return sm;
    }
    /**
     * 开启线程去循环检测Session的状态的
     *
     * @return
     */
    @Bean
    public ExecutorServiceSessionValidationScheduler getExecutorServiceSessionValidationScheduler() {
        ExecutorServiceSessionValidationScheduler sessionValidationScheduler = new ExecutorServiceSessionValidationScheduler();
        sessionValidationScheduler.setInterval(1800000);// 设置循环检测的时间
        return sessionValidationScheduler;
    }
    /**
     * SessionId 生成器 给SessionDAO用的
     * @return
     */
    @Bean
    public SessionIdGenerator getSessionIdGenerator() { // 3
        return new JavaUuidSessionIdGenerator();
    }
    /**
     * 对ShiSession的状态的增删改查
     *
     * @param sessionIdGenerator
     * @return
     */
    @Bean
    public SessionDAO getSessionDAO(SessionIdGenerator sessionIdGenerator) { // 4
        EnterpriseCacheSessionDAO sessionDAO = new EnterpriseCacheSessionDAO();
        // 配置Shiro的名字，实际上没有多大个作用
        sessionDAO.setActiveSessionsCacheName("shiro-activeSessionCache");
        sessionDAO.setSessionIdGenerator(sessionIdGenerator);
        return sessionDAO;
    }
    //自定义sessionManager
    @Bean
    public SessionManager sessionManager(SessionDAO sessionDAO) {
        DefaultWebSessionManager sessionManager=new PowerShiroSessionManager();
        sessionManager.setGlobalSessionTimeout(1800000);// 设置Session全局存在时间
        sessionManager.setDeleteInvalidSessions(true);// 如果检测到过期就删除Session
        sessionManager.setSessionValidationScheduler(getExecutorServiceSessionValidationScheduler());// 设置循环检测器
        sessionManager.setSessionValidationSchedulerEnabled(true);// 开启循环检测
        sessionManager.setSessionDAO(sessionDAO);// 设置SessionDAO
        SimpleCookie sessionIdCookie = new SimpleCookie("athena-java-session-id");//设置一个SessionCookie
        sessionIdCookie.setHttpOnly(true);// 仅支持Http
        sessionIdCookie.setMaxAge(-1);// -1永不过期，0表示删除
        sessionManager.setSessionIdCookie(sessionIdCookie);
        sessionManager.setSessionIdCookieEnabled(true);// 开启SessCookie
        return sessionManager;
    }
   public ShiroAuthFilter shiroAuthFilter(){
       return new ShiroAuthFilter();
   }
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(DefaultWebSecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        shiroFilter.setSecurityManager(securityManager);
        // 没有登陆的跳转
        // shiroFilter.setLoginUrl("/landed");
        // 没有授权的跳转
        // shiroFilter.setUnauthorizedUrl("/unauth");

        // 自定义过滤器(实现放行预检请求的功能加拦截路径的作用)
        Map<String, Filter> filters = new HashMap<>();
        filters.put("authc",shiroAuthFilter());
        shiroFilter.setFilters(filters);
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();

        //配置不会被拦截的路径，顺序判断
        // filterChainDefinitionMap.put("/pages/back/user/departmenttype", "anon");
        // filterChainDefinitionMap.put("/sendcode", "anon");
//         filterChainDefinitionMap.put("/Login", "anon");
//        filterChainDefinitionMap.put("/img/**", "anon");

        // 以下路径没有登陆就会被拦截
        // authc:所有url必须通过认证才能访问，anon:所有url都可以匿名访问
       // filterChainDefinitionMap.put("/page/**", "authc");
        shiroFilter.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilter;
    }
    /**
     * 对Shiro的配置进行切面管理(开启注解支持)
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }
}