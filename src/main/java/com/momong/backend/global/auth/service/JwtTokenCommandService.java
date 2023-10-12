package com.momong.backend.global.auth.service;

import com.momong.backend.domain.member.dto.MemberDto;
import com.momong.backend.global.auth.JwtTokenProvider;
import com.momong.backend.global.auth.dto.AccessAndRefreshTokensInfoDto;
import com.momong.backend.global.auth.dto.JwtTokenInfoDto;
import com.momong.backend.global.auth.entity.RefreshToken;
import com.momong.backend.global.auth.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class JwtTokenCommandService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Access token과 refresh token을 생성한다.
     * 생성된 refresh token은 redis에 저장한다.
     * 이후, 생성된 access token과 refresh token 정보를 반환한다.
     *
     * @param memberDto 회원 정보가 담긴 dto
     * @return 생성된 access token과 refresh token 정보가 담긴 <code>AccessAndRefreshTokenDto</code> 객체
     */
    public AccessAndRefreshTokensInfoDto createAccessAndRefreshToken(MemberDto memberDto) {
        JwtTokenInfoDto accessTokenInfo = jwtTokenProvider.createAccessToken(memberDto);
        JwtTokenInfoDto refreshTokenInfo = jwtTokenProvider.createRefreshToken(memberDto);
        refreshTokenRepository.save(new RefreshToken(refreshTokenInfo.token(), memberDto.getId()));

        return new AccessAndRefreshTokensInfoDto(accessTokenInfo, refreshTokenInfo);
    }
}
