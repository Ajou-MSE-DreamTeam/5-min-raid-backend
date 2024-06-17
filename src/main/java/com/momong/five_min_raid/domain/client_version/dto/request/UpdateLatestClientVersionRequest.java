package com.momong.five_min_raid.domain.client_version.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

// TODO: 버전 형식 검증
public record UpdateLatestClientVersionRequest(
        @Schema(description = "수정할 버전", example = "1.0.2") String version
) {
}
