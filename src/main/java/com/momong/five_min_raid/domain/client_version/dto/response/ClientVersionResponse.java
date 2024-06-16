package com.momong.five_min_raid.domain.client_version.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record ClientVersionResponse(
        @Schema(description = "클라이언트 버전", example = "1.0.0") String version
) {
}
