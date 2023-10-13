package com.momong.backend.unit.global.auth.service;

import com.momong.backend.global.auth.JwtTokenProvider;
import com.momong.backend.global.auth.exception.TokenValidateException;
import com.momong.backend.global.auth.repository.RefreshTokenRepository;
import com.momong.backend.global.auth.service.JwtTokenQueryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;


@DisplayName("[Unit] Service(Query) - Jwt token")
@ExtendWith(MockitoExtension.class)
class JwtTokenQueryServiceTest {

    @InjectMocks
    private JwtTokenQueryService sut;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @DisplayName("유효한 refresh token이 주어지고, 주어진 token의 유효성을 검사하면, true를 반환한다.")
    @Test
    void givenValidRefreshToken_whenValidateRefreshToken_thenReturnTrue() {
        // given
        String refreshToken = "refresh-token";
        willDoNothing().given(jwtTokenProvider).validateToken(refreshToken);
        given(refreshTokenRepository.existsById(refreshToken)).willReturn(true);

        // when
        boolean result = sut.isRefreshTokenValid(refreshToken);

        // then
        then(jwtTokenProvider).should().validateToken(refreshToken);
        then(refreshTokenRepository).should().existsById(refreshToken);
        verifyEveryMocksShouldHaveNoMoreInteractions();
        assertThat(result).isTrue();
    }

    @DisplayName("값이 잘못되었거나 만료되는 등의 이유로 유효하지 않은 유효하지 않은 refresh token이 주어지고, 주어진 token의 유효성을 검사하면, false를 반환한다.")
    @Test
    void givenInvalidRefreshToken_whenValidateRefreshToken_thenReturnFalse() {
        // given
        String refreshToken = "refresh-token";
        willThrow(TokenValidateException.class).given(jwtTokenProvider).validateToken(refreshToken);

        // when
        boolean result = sut.isRefreshTokenValid(refreshToken);

        // then
        then(jwtTokenProvider).should().validateToken(refreshToken);
        verifyEveryMocksShouldHaveNoMoreInteractions();
        assertThat(result).isFalse();
    }

    @DisplayName("Redis에 존재하지 않는 refresh token이 주어지고, 주어진 token의 유효성을 검사하면, false를 반환한다.")
    @Test
    void givenRefreshTokenNotExistInRedis_whenValidateRefreshToken_thenReturnFalse() {
        // given
        String refreshToken = "refresh-token";
        willDoNothing().given(jwtTokenProvider).validateToken(refreshToken);
        given(refreshTokenRepository.existsById(refreshToken)).willReturn(false);

        // when
        boolean result = sut.isRefreshTokenValid(refreshToken);

        // then
        then(jwtTokenProvider).should().validateToken(refreshToken);
        then(refreshTokenRepository).should().existsById(refreshToken);
        verifyEveryMocksShouldHaveNoMoreInteractions();
        assertThat(result).isFalse();
    }

    private void verifyEveryMocksShouldHaveNoMoreInteractions() {
        then(refreshTokenRepository).shouldHaveNoMoreInteractions();
        then(jwtTokenProvider).shouldHaveNoMoreInteractions();
    }
}