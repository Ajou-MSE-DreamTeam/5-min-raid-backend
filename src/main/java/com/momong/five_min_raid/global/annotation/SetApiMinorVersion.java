package com.momong.five_min_raid.global.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@RequestMapping
public @interface SetApiMinorVersion {

    @AliasFor(annotation = RequestMapping.class, attribute = "headers")
    String[] headers() default {};

    int version() default 1;
}