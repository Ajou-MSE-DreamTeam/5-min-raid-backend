package com.momong.five_min_raid.domain.member.dto;

import com.momong.five_min_raid.domain.member.constant.NicknameUnavailableReason;

public record NicknameAvailability(
        boolean isAvailable,
        NicknameUnavailableReason unavailableReason
) {
}
