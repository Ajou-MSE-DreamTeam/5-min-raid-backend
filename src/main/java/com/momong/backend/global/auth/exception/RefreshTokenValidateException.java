package com.momong.backend.global.auth.exception;

import com.momong.backend.global.common.exception.UnauthorizedException;
import com.momong.backend.global.exception.constant.CustomExceptionType;

public class RefreshTokenValidateException extends UnauthorizedException {

    public RefreshTokenValidateException() {
        super(CustomExceptionType.REFRESH_TOKEN_VALIDATE);
    }
}
