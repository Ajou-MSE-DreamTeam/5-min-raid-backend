package com.momong.five_min_raid.global.exception.dto.response;

import java.util.List;

public record ValidationErrorResponse(
        Integer code,
        String message,
        List<ValidationErrorDetailResponse> errors
) {
}
