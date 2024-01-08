package com.momong.five_min_raid.global.exception.dto.response;

public record ValidationErrorDetailResponse(
        Integer code,
        String field,
        String message
) {
}
