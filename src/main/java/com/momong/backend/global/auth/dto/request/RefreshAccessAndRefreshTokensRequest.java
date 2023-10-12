package com.momong.backend.global.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class RefreshAccessAndRefreshTokensRequest {

    @Schema(description = "토큰 갱신을 위한 refresh token", example = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwicm9sZSI6IlJPTEVfVVNFUiIsImxvZ2luVHlwZSI6IktBS0FPIiwiaWF0IjoxNjc3NTc1MTgwLCJleHAiOjE2ODAxNjcxODB9.SaUn_nlZxKiWhwm2GxGCJeC3t9XU7Gl1dLdjPc_mBFo")
    @NotBlank
    private String refreshToken;
}
