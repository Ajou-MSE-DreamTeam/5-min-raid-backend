package com.momong.backend.domain.member.exception;

import com.momong.backend.global.common.exception.ConflictException;
import com.momong.backend.global.exception.constant.CustomExceptionType;

public class MemberNicknameDuplicationException extends ConflictException {

    public MemberNicknameDuplicationException(String nickname) {
        super(CustomExceptionType.MEMBER_NICKNAME_DUPLICATION, "nickname=" + nickname);
    }
}
