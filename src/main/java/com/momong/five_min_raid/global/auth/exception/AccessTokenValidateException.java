package com.momong.five_min_raid.global.auth.exception;

import com.momong.five_min_raid.global.common.exception.UnauthorizedException;
import com.momong.five_min_raid.global.exception.constant.CustomExceptionType;

public class AccessTokenValidateException extends UnauthorizedException {

    public AccessTokenValidateException(Throwable cause) {
        super(CustomExceptionType.ACCESS_TOKEN_VALIDATE, cause);
    }
}
