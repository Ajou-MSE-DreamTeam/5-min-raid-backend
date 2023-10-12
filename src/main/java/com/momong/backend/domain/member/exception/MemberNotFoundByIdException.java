package com.momong.backend.domain.member.exception;

import com.momong.backend.global.common.exception.NotFoundException;
import com.momong.backend.global.exception.constant.CustomExceptionType;

public class MemberNotFoundByIdException extends NotFoundException {

    public MemberNotFoundByIdException(Long memberId) {
        super(CustomExceptionType.MEMBER_NOT_FOUND, "memberId=" + memberId);
    }
}
