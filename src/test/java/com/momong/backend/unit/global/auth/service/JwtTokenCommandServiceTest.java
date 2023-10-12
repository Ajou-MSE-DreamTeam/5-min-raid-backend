package com.momong.backend.unit.global.auth.service;

import com.momong.backend.domain.member.constant.RoleType;
import com.momong.backend.domain.member.dto.MemberDto;
import com.momong.backend.domain.member.service.MemberQueryService;
import com.momong.backend.global.auth.JwtTokenProvider;
import com.momong.backend.global.auth.dto.AccessAndRefreshTokensInfoDto;
import com.momong.backend.global.auth.dto.JwtTokenInfoDto;
import com.momong.backend.global.auth.entity.RefreshToken;
import com.momong.backend.global.auth.exception.RefreshTokenValidateException;
import com.momong.backend.global.auth.exception.TokenValidateException;
import com.momong.backend.global.auth.repository.RefreshTokenRepository;
import com.momong.backend.global.auth.service.JwtTokenCommandService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@DisplayName("[Unit] Service(Command) - Jwt token")
@ExtendWith(MockitoExtension.class)
class JwtTokenCommandServiceTest {

    @InjectMocks
    private JwtTokenCommandService sut;

    @Mock
    private MemberQueryService memberQueryService;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @DisplayName("회원 정보가 담긴 dto가 주어지고, 주어진 회원 정보로 access token과 refresh token을 생성하면, 생성된 token들을 반환한다.")
    @Test
    void givenMemberDto_whenCreateAccessAndRefreshToken_thenReturnTokensCreated() {
        // given
        long memberId = 1L;
        MemberDto memberDto = createMemberDto(memberId, "social-uid");
        JwtTokenInfoDto expectedAccessToken = createJwtTokenInfoDto("access-token");
        JwtTokenInfoDto expectedRefreshToken = createJwtTokenInfoDto("refresh-token");
        given(jwtTokenProvider.createAccessToken(memberDto)).willReturn(expectedAccessToken);
        given(jwtTokenProvider.createRefreshToken(memberDto)).willReturn(expectedRefreshToken);
        given(refreshTokenRepository.save(any(RefreshToken.class))).willReturn(new RefreshToken("refresh-token", memberId));

        // when
        AccessAndRefreshTokensInfoDto result = sut.createAccessAndRefreshToken(memberDto);

        // then
        then(jwtTokenProvider).should().createAccessToken(memberDto);
        then(jwtTokenProvider).should().createRefreshToken(memberDto);
        then(refreshTokenRepository).should().save(any(RefreshToken.class));
        verifyEveryMocksShouldHaveNoMoreInteractions();
        assertThat(result)
                .hasFieldOrPropertyWithValue("accessToken.token", expectedAccessToken.token())
                .hasFieldOrPropertyWithValue("accessToken.expiresAt", expectedAccessToken.expiresAt())
                .hasFieldOrPropertyWithValue("refreshToken.token", expectedRefreshToken.token())
                .hasFieldOrPropertyWithValue("refreshToken.expiresAt", expectedRefreshToken.expiresAt());
    }

    @DisplayName("Refresh token이 주어지고, access token과 refresh token을 새로 발급받으면, 새로 생성된 token들을 반환한다.")
    @Test
    void givenRefreshToken_whenRefreshAccessAndRefreshToken_thenReturnCreatedTokens() {
        // given
        String refreshToken = "refresh-token";
        Long memberId = 1L;
        RefreshToken refreshTokenEntity = createRefreshToken(memberId);
        MemberDto memberDto = createMemberDto(memberId, "social-uid");
        JwtTokenInfoDto expectedAccessToken = createJwtTokenInfoDto("access-token");
        JwtTokenInfoDto expectedRefreshToken = createJwtTokenInfoDto("refresh-token");
        willDoNothing().given(jwtTokenProvider).validateToken(refreshToken);
        given(refreshTokenRepository.findById(refreshToken)).willReturn(Optional.of(refreshTokenEntity));
        willDoNothing().given(refreshTokenRepository).delete(refreshTokenEntity);
        given(memberQueryService.getDtoById(memberId)).willReturn(memberDto);
        given(jwtTokenProvider.createAccessToken(memberDto)).willReturn(expectedAccessToken);
        given(jwtTokenProvider.createRefreshToken(memberDto)).willReturn(expectedRefreshToken);
        given(refreshTokenRepository.save(any(RefreshToken.class))).willReturn(new RefreshToken("refresh-token", memberId));

        // when
        AccessAndRefreshTokensInfoDto result = sut.refreshAccessAndRefreshToken(refreshToken);

        // then
        then(jwtTokenProvider).should().validateToken(refreshToken);
        then(refreshTokenRepository).should().findById(refreshToken);
        then(refreshTokenRepository).should().delete(refreshTokenEntity);
        then(memberQueryService).should().getDtoById(memberId);
        then(jwtTokenProvider).should().createAccessToken(memberDto);
        then(jwtTokenProvider).should().createRefreshToken(memberDto);
        then(refreshTokenRepository).should().save(any(RefreshToken.class));
        verifyEveryMocksShouldHaveNoMoreInteractions();
        assertThat(result)
                .hasFieldOrPropertyWithValue("accessToken.token", expectedAccessToken.token())
                .hasFieldOrPropertyWithValue("accessToken.expiresAt", expectedAccessToken.expiresAt())
                .hasFieldOrPropertyWithValue("refreshToken.token", expectedRefreshToken.token())
                .hasFieldOrPropertyWithValue("refreshToken.expiresAt", expectedRefreshToken.expiresAt());
    }

    @DisplayName("값이 잘못되었거나 만료되는 등의 이유로 유효하지 않은 refresh token이 주어지고, access token과 refresh token을 새로 발급받으면, 예외가 발생한다.")
    @Test
    void givenInvalidRefreshToken_whenRefreshAccessAndRefreshToken_thenThrowRefreshTokenValidateException() {
        // given
        String refreshToken = "refresh-token";
        willThrow(TokenValidateException.class).given(jwtTokenProvider).validateToken(refreshToken);

        // when
        Throwable t = catchThrowable(() -> sut.refreshAccessAndRefreshToken(refreshToken));

        // then
        then(jwtTokenProvider).should().validateToken(refreshToken);
        verifyEveryMocksShouldHaveNoMoreInteractions();
        assertThat(t).isInstanceOf(RefreshTokenValidateException.class);
    }

    @DisplayName("Refresh token이 주어지고, access token과 refresh token을 새로 발급받으려 했으나 refresh token을 redis에서 찾지 못한다면, 예외가 발생한다.")
    @Test
    void givenRefreshToken_whenRefreshAccessAndRefreshTokenButRefreshTokenWasNotFoundByRedis_thenThrowRefreshTokenValidateException() {
        // given
        String refreshToken = "refresh-token";
        willDoNothing().given(jwtTokenProvider).validateToken(refreshToken);
        given(refreshTokenRepository.findById(refreshToken)).willReturn(Optional.empty());

        // when
        Throwable t = catchThrowable(() -> sut.refreshAccessAndRefreshToken(refreshToken));

        // then
        then(jwtTokenProvider).should().validateToken(refreshToken);
        verifyEveryMocksShouldHaveNoMoreInteractions();
        assertThat(t).isInstanceOf(RefreshTokenValidateException.class);
    }

    private void verifyEveryMocksShouldHaveNoMoreInteractions() {
        then(memberQueryService).shouldHaveNoMoreInteractions();
        then(refreshTokenRepository).shouldHaveNoMoreInteractions();
        then(jwtTokenProvider).shouldHaveNoMoreInteractions();
    }

    private MemberDto createMemberDto(Long memberId, String socialUid) {
        return new MemberDto(
                memberId,
                socialUid,
                Set.of(RoleType.USER),
                socialUid
        );
    }

    private RefreshToken createRefreshToken(Long memberId) {
        return new RefreshToken(UUID.randomUUID().toString(), memberId);
    }

    private JwtTokenInfoDto createJwtTokenInfoDto(String token) {
        return new JwtTokenInfoDto(token, LocalDateTime.of(2023, 1, 1, 0, 0));
    }
}