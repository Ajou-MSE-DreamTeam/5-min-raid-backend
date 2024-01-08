package com.momong.five_min_raid.domain.member.exception;

import com.momong.five_min_raid.global.common.exception.UnprocessableEntityException;
import com.momong.five_min_raid.global.exception.constant.CustomExceptionType;

public class InvalidMemberNicknameException extends UnprocessableEntityException {

    public InvalidMemberNicknameException(String nickname) {
        super(CustomExceptionType.INVALID_MEMBER_NICKNAME, "nickname=" + nickname);
    }
}
