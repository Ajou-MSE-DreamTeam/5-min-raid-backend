package com.momong.backend.global.auth.exception;

import com.momong.backend.global.common.exception.UnauthorizedException;
import com.momong.backend.global.exception.constant.CustomExceptionType;

public class TokenValidateException extends UnauthorizedException {

    public TokenValidateException() {
        super(CustomExceptionType.TOKEN_VALIDATE);
    }

    public TokenValidateException(String optionalMessage) {
        super(CustomExceptionType.TOKEN_VALIDATE, optionalMessage);
    }

    public TokenValidateException(Throwable cause) {
        super(CustomExceptionType.TOKEN_VALIDATE, cause);
    }
}
