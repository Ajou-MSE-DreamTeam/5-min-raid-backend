package com.momong.backend.unit.global.auth.service;

import com.momong.backend.domain.member.constant.RoleType;
import com.momong.backend.domain.member.dto.MemberDto;
import com.momong.backend.global.auth.JwtTokenProvider;
import com.momong.backend.global.auth.dto.AccessAndRefreshTokensInfoDto;
import com.momong.backend.global.auth.dto.JwtTokenInfoDto;
import com.momong.backend.global.auth.entity.RefreshToken;
import com.momong.backend.global.auth.repository.RefreshTokenRepository;
import com.momong.backend.global.auth.service.JwtTokenCommandService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@DisplayName("[Unit] Service(Command) - Jwt token")
@ExtendWith(MockitoExtension.class)
class JwtTokenCommandServiceTest {

    @InjectMocks
    private JwtTokenCommandService sut;

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

    private void verifyEveryMocksShouldHaveNoMoreInteractions() {
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

    private JwtTokenInfoDto createJwtTokenInfoDto(String token) {
        return new JwtTokenInfoDto(token, LocalDateTime.of(2023, 1, 1, 0, 0));
    }
}