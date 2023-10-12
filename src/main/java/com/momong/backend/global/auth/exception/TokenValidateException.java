package com.momong.backend.global.auth.exception;

import com.momong.backend.global.common.exception.UnauthorizedException;
import com.momong.backend.global.exception.constant.CustomExceptionType;

public class TokenValidateException extends UnauthorizedException {

    public TokenValidateException(String optionalMessage, Throwable cause) {
        super(CustomExceptionType.TOKEN_VALIDATE, optionalMessage, cause);
    }
}
