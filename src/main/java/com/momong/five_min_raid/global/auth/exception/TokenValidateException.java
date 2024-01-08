package com.momong.five_min_raid.global.auth.exception;

import com.momong.five_min_raid.global.common.exception.UnauthorizedException;
import com.momong.five_min_raid.global.exception.constant.CustomExceptionType;

public class TokenValidateException extends UnauthorizedException {

    public TokenValidateException(String optionalMessage, Throwable cause) {
        super(CustomExceptionType.TOKEN_VALIDATE, optionalMessage, cause);
    }
}
