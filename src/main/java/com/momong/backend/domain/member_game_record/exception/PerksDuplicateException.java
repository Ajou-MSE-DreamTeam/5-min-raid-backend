package com.momong.backend.domain.member_game_record.exception;

import com.momong.backend.global.common.exception.BadRequestException;
import com.momong.backend.global.exception.constant.CustomExceptionType;

public class PerksDuplicateException extends BadRequestException {

    public PerksDuplicateException() {
        super(CustomExceptionType.PERKS_DUPLICATE);
    }
}
