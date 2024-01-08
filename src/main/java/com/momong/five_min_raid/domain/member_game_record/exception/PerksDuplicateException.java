package com.momong.five_min_raid.domain.member_game_record.exception;

import com.momong.five_min_raid.global.common.exception.BadRequestException;
import com.momong.five_min_raid.global.exception.constant.CustomExceptionType;

public class PerksDuplicateException extends BadRequestException {

    public PerksDuplicateException() {
        super(CustomExceptionType.PERKS_DUPLICATE);
    }
}
