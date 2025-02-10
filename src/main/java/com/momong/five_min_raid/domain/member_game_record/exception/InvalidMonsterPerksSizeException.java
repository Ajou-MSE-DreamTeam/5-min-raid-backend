package com.momong.five_min_raid.domain.member_game_record.exception;

import com.momong.five_min_raid.global.common.exception.BadRequestException;
import com.momong.five_min_raid.global.exception.constant.CustomExceptionType;

public class InvalidMonsterPerksSizeException extends BadRequestException {

    public InvalidMonsterPerksSizeException() {
        super(CustomExceptionType.INVALID_MONSTER_PERKS_SIZE);
    }
}
