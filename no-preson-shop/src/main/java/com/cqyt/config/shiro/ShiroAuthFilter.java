package com.cqyt.config.shiro;

import com.alibaba.fastjson.JSONObject;
import com.cqyt.utils.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class ShiroAuthFilter extends FormAuthenticationFilter {
    /**
     * 在访问controller前判断是否登录，返回json，不进行重定向。
     * @param request
     * @param response
     * @return true-继续往下执行，false-该filter过滤器已经处理，不继续执行其他过滤器
     * @throws Exception
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        boolean authenticated = SecurityUtils.getSubject().isAuthenticated();
        if (!authenticated){
            if ("OPTIONS".equals(((HttpServletRequest) request).getMethod())) {// 浏览器的跨域请求，预检请求，直接通过
            }else{
                // 取出当前Header中token有没有值
                String authToken = httpServletRequest.getHeader("token");
                // System.out.println(authToken);
                // 拿到sessionDao对象
                DefaultWebSecurityManager defaultWebSecurityManager = (DefaultWebSecurityManager) SecurityUtils.getSecurityManager();
                DefaultWebSessionManager defaultWebSessionManager = (DefaultWebSessionManager) defaultWebSecurityManager.getSessionManager();
                SessionDAO sessionDAO = defaultWebSessionManager.getSessionDAO();
                // 取出当前存在shiro中所有的sessionID，即当前登录的人数
                Collection<Session> activeSessions = sessionDAO.getActiveSessions();
                log.info("当前在线的人数为:"+activeSessions.size()+"人");
                List<Session> collect = activeSessions.stream().filter(a -> a.getId().equals(authToken)).collect(Collectors.toList());
                if (null==collect||collect.size()==0){ // 说明没有登录
                    httpServletResponse.sendRedirect("/login");
                    //httpServletResponse.setCharacterEncoding("UTF-8");// 回传数据编码
                    //httpServletResponse.setContentType("application/json");// 设置回传为json格式
                    //httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");// 设置允许的所有域名来访
                    //httpServletResponse.getWriter().write(JSONObject.toJSONString(ResponseUtils.fail("您还没有登录!", null, 401, 401)));
                }
                // 判断当前用户是否一个人登录
            }
            return true;
        }
        return true;
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {

        return super.isAccessAllowed(request, response, mappedValue);
    }
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws IOException {

        //Subject subject = SecurityUtils.getSubject();
        //// 没有登录授权 且没有记住我
        //if (!subject.isAuthenticated() && !subject.isRemembered()) {
        //    // 如果没有登录，直接进行之后的流程
        //    return true;
        //}
        //Session session = subject.getSession();
        //log.info("当前session中userId："+session.getAttribute("userId"));
        //log.info("当前session中userName："+session.getAttribute("userName"));
        //log.info("会话时间为:"+String.valueOf(session.getTimeout()));
        ////获得当前用户
        //String principal = (String) subject.getPrincipal();
        //System.out.println(principal);

        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        String requestedWith = httpServletRequest.getHeader("X-Requested-With");
        if (!StringUtils.isEmpty(requestedWith) && "XMLHttpRequest".equals(requestedWith)) {//如果是ajax返回指定格式数据
            httpServletResponse.setCharacterEncoding("UTF-8");
            httpServletResponse.setContentType("application/json");
            httpServletResponse.getWriter().write("未授权");
        } else {//如果是普通请求进行重定向
            httpServletResponse.sendRedirect("/403");
        }
        return false;
    }
}
