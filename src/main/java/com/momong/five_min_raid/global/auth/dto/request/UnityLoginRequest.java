package com.momong.five_min_raid.global.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class UnityLoginRequest {

    @Schema(description = "Unity authentication을 통해 받은 id", example = "12ab34cd56ef78gh90")
    String id;
}
