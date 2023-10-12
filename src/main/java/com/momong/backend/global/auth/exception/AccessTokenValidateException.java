package com.momong.backend.global.auth.exception;

import com.momong.backend.global.common.exception.UnauthorizedException;
import com.momong.backend.global.exception.constant.CustomExceptionType;

public class AccessTokenValidateException extends UnauthorizedException {

    public AccessTokenValidateException(Throwable cause) {
        super(CustomExceptionType.ACCESS_TOKEN_VALIDATE, cause);
    }
}
