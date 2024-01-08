package com.momong.five_min_raid.domain.game_record.exception;

import com.momong.five_min_raid.global.common.exception.BadRequestException;
import com.momong.five_min_raid.global.exception.constant.CustomExceptionType;

public class InvalidGuardianGameRecordSizeException extends BadRequestException {

    public InvalidGuardianGameRecordSizeException() {
        super(CustomExceptionType.INVALID_GUARDIAN_GAME_RECORD_SIZE);
    }
}
