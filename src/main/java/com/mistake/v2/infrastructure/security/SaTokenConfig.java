package com.mistake.v2.infrastructure.security;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Sa-Token 鉴权配置 — 注册路由拦截器，配置放行路径。
 *
 * <p>注意：server.servlet.context-path 已配置为 /api/v2，因此以下路径均为
 * context-relative（无需再拼 /api/v2 前缀）。
 *
 * @author mistake-team
 * @since 2.0.0
 */
@Configuration
public class SaTokenConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SaInterceptor(handle -> StpUtil.checkLogin()))
                .addPathPatterns("/**")
                .excludePathPatterns(
                        // 登录注册
                        "/auth/login",
                        "/auth/register",
                        // Knife4j / Swagger
                        "/doc.html",
                        "/v3/api-docs/**",
                        "/swagger-ui/**"
                );
    }
}
