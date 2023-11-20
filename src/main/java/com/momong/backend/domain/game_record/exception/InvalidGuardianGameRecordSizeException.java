package com.momong.backend.domain.game_record.exception;

import com.momong.backend.global.common.exception.BadRequestException;
import com.momong.backend.global.exception.constant.CustomExceptionType;

public class InvalidGuardianGameRecordSizeException extends BadRequestException {

    public InvalidGuardianGameRecordSizeException() {
        super(CustomExceptionType.INVALID_GUARDIAN_GAME_RECORD_SIZE);
    }
}
