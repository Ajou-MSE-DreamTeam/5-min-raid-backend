package com.momong.backend.domain.member_game_record.exception;

import com.momong.backend.global.common.exception.BadRequestException;
import com.momong.backend.global.exception.constant.CustomExceptionType;

public class InvalidMonsterPerksSizeException extends BadRequestException {

    public InvalidMonsterPerksSizeException() {
        super(CustomExceptionType.INVALID_MONSTER_PERKS_SIZE);
    }
}
