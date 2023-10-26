package com.momong.backend.global.auth.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@RedisHash(value = "refresh_token", timeToLive = 7 * 24 * 60 * 60)
public class RefreshToken {

    @NotBlank
    @Id
    private String token;

    @NotNull
    private Long memberId;
}
