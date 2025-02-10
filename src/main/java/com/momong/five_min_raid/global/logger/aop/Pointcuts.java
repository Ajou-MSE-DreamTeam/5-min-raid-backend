package com.momong.five_min_raid.global.logger.aop;

import org.aspectj.lang.annotation.Pointcut;

public class Pointcuts {

    @Pointcut("execution(* com.momong.five_min_raid.*..api..*(..))")
    public void controllerPoint() {
    }

    @Pointcut("execution(* com.momong.five_min_raid.*..service..*(..))")
    public void servicePoint() {
    }

    @Pointcut("execution(* com.momong.five_min_raid.*..repository..*(..))")
    public void repositoryPoint() {
    }
}
