package com.momong.five_min_raid.domain.member.exception;

import com.momong.five_min_raid.global.common.exception.ConflictException;
import com.momong.five_min_raid.global.exception.constant.CustomExceptionType;

public class MemberNicknameDuplicationException extends ConflictException {

    public MemberNicknameDuplicationException(String nickname) {
        super(CustomExceptionType.MEMBER_NICKNAME_DUPLICATION, "nickname=" + nickname);
    }
}
