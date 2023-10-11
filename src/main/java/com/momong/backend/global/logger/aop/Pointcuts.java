package com.momong.backend.global.logger.aop;

import org.aspectj.lang.annotation.Pointcut;

public class Pointcuts {

    @Pointcut("execution(* com.momong.backend.controller..*(..))")
    public void controllerPoint() {
    }

    @Pointcut("execution(* com.momong.backend.service..*(..))")
    public void servicePoint() {
    }

    @Pointcut("execution(* com.momong.backend.repository..*(..))")
    public void repositoryPoint() {
    }
}
