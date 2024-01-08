package com.momong.five_min_raid.global.logger.aop;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Aspect
public class LogTraceAspect {

    private final LogTrace logTrace;

    @Around("com.momong.five_min_raid.global.logger.aop.Pointcuts.controllerPoint() || " +
            "com.momong.five_min_raid.global.logger.aop.Pointcuts.servicePoint() || " +
            "com.momong.five_min_raid.global.logger.aop.Pointcuts.repositoryPoint()")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
        TraceStatus status = null;
        try {
            String message = joinPoint.getSignature().toShortString();
            status = logTrace.begin(message);

            // Logic call
            Object result = joinPoint.proceed();

            logTrace.end(status);
            return result;
        } catch (Exception ex) {
            logTrace.exception(status, ex);
            throw ex;
        }
    }
}
