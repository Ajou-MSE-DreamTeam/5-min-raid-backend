package com.momong.five_min_raid.domain.member.exception;

import com.momong.five_min_raid.global.common.exception.NotFoundException;
import com.momong.five_min_raid.global.exception.constant.CustomExceptionType;

public class MemberNotFoundByIdException extends NotFoundException {

    public MemberNotFoundByIdException(Long memberId) {
        super(CustomExceptionType.MEMBER_NOT_FOUND, "memberId=" + memberId);
    }
}
