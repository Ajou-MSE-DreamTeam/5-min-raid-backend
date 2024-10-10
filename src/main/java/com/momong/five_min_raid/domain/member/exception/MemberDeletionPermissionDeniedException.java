package com.momong.five_min_raid.domain.member.exception;

import com.momong.five_min_raid.global.common.exception.ForbiddenException;
import com.momong.five_min_raid.global.exception.constant.CustomExceptionType;

public class MemberDeletionPermissionDeniedException extends ForbiddenException {
    public MemberDeletionPermissionDeniedException() {
        super(CustomExceptionType.INVALID_MEMBER_NICKNAME);
    }
}
