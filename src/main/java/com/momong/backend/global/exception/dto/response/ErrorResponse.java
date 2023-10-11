package com.momong.backend.global.exception.dto.response;

public record ErrorResponse(
        Integer code,
        String message
) {
}
