package com.usermanagement.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    /*
     * service, controller, mapper의 Start, Finish 로그
     */
    @Around("execution(* com.usermanagement.service..*(..)) || execution(* com.usermanagement.controller..*(..)) || execution(* com.usermanagement.mapper..*(..))")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String simpleClassName = className.substring(className.lastIndexOf('.') + 1); // 클래스 이름만 추출

        log.info("*** Start {}.{} => arguments: {}", simpleClassName, methodName, joinPoint.getArgs());
        Object result = joinPoint.proceed();
        log.info("*** Finish {}.{} => result: {}", simpleClassName, methodName, result);
        return result;
    }
}
