package com.mistake.v2.common.util;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * 统一日志切面
 *
 * <p>拦截所有 Controller 方法，记录请求 URL、方法、参数、响应结果及耗时。
 * 敏感字段（password、token、secret）自动脱敏。
 *
 * @author mistake-team
 * @since 2.0.0
 */
@Aspect
@Component
public class LogAspect {

    private static final Logger log = LoggerFactory.getLogger(LogAspect.class);

    /**
     * 切点：拦截 interfaces.controller 包下所有 public 方法
     */
    @Pointcut("execution(* com.mistake.v2.interfaces.controller..*.*(..))")
    public void controllerPointcut() {
    }

    /**
     * 环绕通知：记录请求与响应日志
     */
    @Around("controllerPointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String url = "";
        String method = "";
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            url = request.getRequestURI();
            method = request.getMethod();
        }

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String className = signature.getDeclaringType().getSimpleName();
        String methodName = signature.getName();

        // 参数脱敏处理
        Object[] args = joinPoint.getArgs();
        String argsStr = Arrays.stream(args)
                .map(arg -> arg != null ? SensitiveUtil.mask(arg.toString()) : "null")
                .collect(Collectors.joining(", "));

        log.info("[Controller] {}.{} | {} {} | 入参: [{}]", className, methodName, method, url, argsStr);

        Object result;
        try {
            result = joinPoint.proceed();
            long elapsed = System.currentTimeMillis() - start;

            // 响应脱敏处理
            String resultStr = result != null ? SensitiveUtil.mask(result.toString()) : "null";
            log.info("[Controller] {}.{} | 耗时: {}ms | 响应: {}", className, methodName, elapsed, resultStr);
        } catch (Throwable e) {
            long elapsed = System.currentTimeMillis() - start;
            log.error("[Controller] {}.{} | 耗时: {}ms | 异常: {}", className, methodName, elapsed, e.getMessage(), e);
            throw e;
        }

        return result;
    }

}
