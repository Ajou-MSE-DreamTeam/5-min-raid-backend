package com.momong.five_min_raid.domain.member_game_record.exception;

import com.momong.five_min_raid.global.common.exception.BadRequestException;
import com.momong.five_min_raid.global.exception.constant.CustomExceptionType;

public class InvalidGuardianPerksSizeException extends BadRequestException {

    public InvalidGuardianPerksSizeException() {
        super(CustomExceptionType.INVALID_GUARDIAN_PERKS_SIZE);
    }
}
