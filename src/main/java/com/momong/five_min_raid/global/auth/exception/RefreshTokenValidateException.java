package com.momong.five_min_raid.global.auth.exception;

import com.momong.five_min_raid.global.common.exception.UnauthorizedException;
import com.momong.five_min_raid.global.exception.constant.CustomExceptionType;

public class RefreshTokenValidateException extends UnauthorizedException {

    public RefreshTokenValidateException() {
        super(CustomExceptionType.REFRESH_TOKEN_VALIDATE);
    }

    public RefreshTokenValidateException(Throwable cause) {
        super(CustomExceptionType.REFRESH_TOKEN_VALIDATE, cause);
    }
}
