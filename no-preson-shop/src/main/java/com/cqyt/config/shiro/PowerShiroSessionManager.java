package com.cqyt.config.shiro;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;
import java.util.logging.Logger;

@Slf4j
public class PowerShiroSessionManager extends DefaultWebSessionManager {
    /**
     * 获取请求头中key为“Authorization”的value == sessionId
     */
    private static final String AUTHORIZATION ="token";
    private static final String REFERENCED_SESSION_ID_SOURCE = "cookie";
    @Override
    protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
        // TODO Auto-generated method stub
        String sessionId = WebUtils.toHttp(request).getHeader(AUTHORIZATION);
        //log.info("token="+sessionId);
        if (sessionId!=null) {
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE, ShiroHttpServletRequest.COOKIE_SESSION_ID_SOURCE);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, sessionId);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);
            return sessionId;
        }
        // 若header获取不到token则尝试从cookie中获取
        return super.getSessionId(request, response);
    }
}
