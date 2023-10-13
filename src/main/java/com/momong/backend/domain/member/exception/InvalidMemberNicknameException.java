package com.momong.backend.domain.member.exception;

import com.momong.backend.global.common.exception.UnprocessableEntityException;
import com.momong.backend.global.exception.constant.CustomExceptionType;

public class InvalidMemberNicknameException extends UnprocessableEntityException {

    public InvalidMemberNicknameException(String nickname) {
        super(CustomExceptionType.INVALID_MEMBER_NICKNAME, "nickname=" + nickname);
    }
}
