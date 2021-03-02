package com.leyou.gateway.fifter;

import com.leyou.auth.common.untils.JwtUtils;
import com.leyou.auth.common.untils.UserInfo;
import com.leyou.common.untils.CookieUtils;
import com.leyou.gateway.config.FilterProperties;
import com.leyou.gateway.config.JwtProperties;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Component
@EnableConfigurationProperties(JwtProperties.class)
public class LoginFifter extends ZuulFilter {

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private FilterProperties filterProperties;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 10;
    }

    @Override
    public boolean shouldFilter() {
        //获取上下文
        RequestContext context = RequestContext.getCurrentContext();
       //获取request
        HttpServletRequest request = context.getRequest();
       //获取请求路径
        String url = request.getRequestURI();
       //获取白名单集合
        List<String> allowPaths = filterProperties.getAllowPaths();
       //判断路径中是否包含白名单路径，有即放行，无则拦截
        for (String allowPath : allowPaths) {
            if (StringUtils.contains(url, allowPath)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        //获取s上下文
        RequestContext context = RequestContext.getCurrentContext();
       //获取request
        HttpServletRequest request = context.getRequest();
        // 获取token
        String token = CookieUtils.getCookieValue(request, this.jwtProperties.getCookieName());
        //解析token令牌
        try {
            UserInfo user=JwtUtils.getInfoFromToken(token,this.jwtProperties.getPublicKey());

        } catch (Exception e) {
            e.printStackTrace();
            // 校验出现异常，返回403
            context.setSendZuulResponse(false);
            context.setResponseStatusCode(HttpStatus.FORBIDDEN.value());
        }
        return null;
    }
}
