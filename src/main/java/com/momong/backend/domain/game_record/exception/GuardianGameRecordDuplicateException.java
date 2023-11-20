package com.momong.backend.domain.game_record.exception;

import com.momong.backend.global.common.exception.BadRequestException;
import com.momong.backend.global.exception.constant.CustomExceptionType;

public class GuardianGameRecordDuplicateException extends BadRequestException {

    public GuardianGameRecordDuplicateException() {
        super(CustomExceptionType.GUARDIAN_GAME_RECORD_DUPLICATE);
    }
}
