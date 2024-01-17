package com.momong.five_min_raid.global.common.exception;

import com.momong.five_min_raid.global.exception.constant.CustomExceptionType;

public class NotAdminException extends ForbiddenException {

    public NotAdminException() {
        super(CustomExceptionType.NOT_ADMIN);
    }
}
