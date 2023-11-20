package com.momong.backend.domain.member_game_record.exception;

import com.momong.backend.global.common.exception.BadRequestException;
import com.momong.backend.global.exception.constant.CustomExceptionType;

public class InvalidGuardianPerksSizeException extends BadRequestException {

    public InvalidGuardianPerksSizeException() {
        super(CustomExceptionType.INVALID_GUARDIAN_PERKS_SIZE);
    }
}
