package com.cqyt.utils.clientIp;

import com.cqyt.utils.redis.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
@Slf4j
@Component
public class ClientIpUtil {
    @Resource
    private RedisUtil redisUtil;
    /**
     * 获取客户端ip地址
     * @param request
     * @return
     */
    public String getClientIp(HttpServletRequest request)
    {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.trim() == "" || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.trim() == "" || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.trim() == "" || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        // 多个路由时，取第一个非unknown的ip
        final String[] arr = ip.split(",");
        for (final String str : arr) {
            if (!"unknown".equalsIgnoreCase(str)) {
                ip = str;
                break;
            }
        }
        return ip;
    }

    /**
     * 用户在60秒内请求数量大于三十，判定此ip请求不正常
     * @param response
     * @return
     */
    public boolean normalIP(HttpServletRequest response){
        String clientIp = getClientIp(response);
        log.info("当前访问ip为："+clientIp);
        Object object = redisUtil.get(clientIp);
        if (object==null) {
            //添加记录
            redisUtil.set(clientIp,1,60);
            return true;
        }else {
            //记录加一
            redisUtil.incr(clientIp,1);
            Integer client= (Integer) redisUtil.get(clientIp);
            if ( Integer.valueOf(client) > 30) {
                return false;
            }
        }
        return true;
    }
}
