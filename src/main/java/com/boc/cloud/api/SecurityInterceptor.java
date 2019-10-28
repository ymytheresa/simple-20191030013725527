package com.boc.cloud.api;

import com.boc.cloud.api.exception.Http401Exception;
import com.boc.cloud.api.exception.Http403Exception;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class SecurityInterceptor implements HandlerInterceptor {
    final static Logger logger = LoggerFactory.getLogger(SecurityInterceptor.class);

    /**
     * 是否爲開發模式, 開發模式方便在本机测试, 不走OAuth流程, 可直接調用後臺API.
     */
    @Value("${dev}")
    private boolean isDev;

    /**
     * 由gateway向後台API注入的私有變量, 只有提供此header信息, 才可以使用後台API.
     */
    @Value("${x-app-sharedsecret}")
    private String appSharedSecret;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
        throws Exception {
        if (isDev) {
            return true;
        }

        String api = request.getRequestURI();
        logger.info("getRequestURI={}", api);

        if (!api.startsWith("/api/")) {
            // auth
            // login form, etc.
            return true;
        }

        String secret = request.getHeader("x-app-sharedsecret");
        logger.info("x-app-sharedsecret={}", secret);

        if (appSharedSecret.equals(secret)) {

            // user name can be customer username or client key depending on grant type.
            String username = request.getHeader("ibm-app-user");
            logger.info("ibm-app-user={}", username);

            boolean authCodeProtected = !api.startsWith("/api/market-info")
                && !api.startsWith("/api/services/appointments") && !api.startsWith("/api/bank-info");

            if (authCodeProtected) {
                // cust0101
                if (!username.matches("cust\\d{4}")) {
                    throw new Http401Exception(MsgKeys.ERROR_AUTH_CODE_GRANT_REQUIRED, MsgKeys.GRANT_TYPE_AUTH_CODE_REQUIRED);
                }
            } else {
                // 81bdaa23-a7c3-4fdc-ac21-f308dd5c7703
                if (username.length() != 36) {
                    throw new Http401Exception(MsgKeys.ERROR_CLIENT_CRED_GRANT_REQUIRED, MsgKeys.GRANT_TYPE_CLIENT_CRENDENTIALS_REQUIRED);
                }
            }

            return true;

        } else {
            throw new Http403Exception(MsgKeys.ERROR_FORBIDDEN, MsgKeys.FORBIDDEN);
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
        throws Exception {
    }

}
