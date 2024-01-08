package com.momong.five_min_raid.global.exception.dto.response;

public record ErrorResponse(
        Integer code,
        String message
) {
}
