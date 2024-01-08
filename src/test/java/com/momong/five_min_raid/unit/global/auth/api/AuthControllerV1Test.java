package com.momong.five_min_raid.unit.global.auth.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.momong.five_min_raid.config.ControllerTestConfig;
import com.momong.five_min_raid.domain.member.constant.RoleType;
import com.momong.five_min_raid.domain.member.dto.MemberDto;
import com.momong.five_min_raid.domain.member.service.MemberCommandService;
import com.momong.five_min_raid.domain.member.service.MemberQueryService;
import com.momong.five_min_raid.global.auth.api.AuthControllerV1;
import com.momong.five_min_raid.global.auth.dto.AccessAndRefreshTokensInfoDto;
import com.momong.five_min_raid.global.auth.dto.JwtTokenInfoDto;
import com.momong.five_min_raid.global.auth.dto.request.RefreshAccessAndRefreshTokensRequest;
import com.momong.five_min_raid.global.auth.dto.request.UnityLoginRequest;
import com.momong.five_min_raid.global.auth.service.JwtTokenCommandService;
import com.momong.five_min_raid.global.auth.service.JwtTokenQueryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import static com.momong.five_min_raid.global.common.constant.GlobalConstants.API_MINOR_VERSION_HEADER_NAME;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("[Unit] Controller - Auth")
@Import(ControllerTestConfig.class)
@WebMvcTest(controllers = AuthControllerV1.class)
class AuthControllerV1Test {

    @MockBean
    private MemberCommandService memberCommandService;

    @MockBean
    private MemberQueryService memberQueryService;

    @MockBean
    private JwtTokenCommandService jwtTokenCommandService;

    @MockBean
    private JwtTokenQueryService jwtTokenQueryService;

    private final MockMvc mvc;
    private final ObjectMapper mapper;

    @Autowired
    public AuthControllerV1Test(MockMvc mvc, ObjectMapper mapper) {
        this.mvc = mvc;
        this.mapper = mapper;
    }

    @DisplayName("기존 회원의 socialUid가 주어지고, 주어진 socialUid로 unity login을 수행하면, 로그인 한 후 access token과 refresh token을 응답한다.")
    @Test
    void givenSocialUidOfExistingMember_whenUnityLogin_thenLoggingInAndReturnTokens() throws Exception {
        // given
        String unityId = "123abc";
        UnityLoginRequest unityLoginRequest = createUnityLoginRequest(unityId);

        MemberDto memberDto = createMemberDto(1L, unityId);
        JwtTokenInfoDto expectedAccessToken = new JwtTokenInfoDto("access-token", LocalDateTime.of(2023, 1, 1, 0, 0));
        JwtTokenInfoDto expectedRefreshToken = new JwtTokenInfoDto("refresh-token", LocalDateTime.of(2023, 12, 31, 23, 59));
        AccessAndRefreshTokensInfoDto expectedTokens = new AccessAndRefreshTokensInfoDto(expectedAccessToken, expectedRefreshToken);

        given(memberQueryService.findDtoBySocialUid(unityId)).willReturn(Optional.of(memberDto));
        given(jwtTokenCommandService.createAccessAndRefreshToken(memberDto)).willReturn(expectedTokens);

        // when & then
        mvc.perform(
                        post("/api/v1/auth/login/unity")
                                .header(API_MINOR_VERSION_HEADER_NAME, 1)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(unityLoginRequest))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.loggedInMember.id").value(memberDto.getId()))
                .andExpect(jsonPath("$.loggedInMember.nickname").value(memberDto.getNickname()))
                .andExpect(jsonPath("$.tokens.accessToken.token").value(expectedAccessToken.token()))
                .andExpect(jsonPath("$.tokens.accessToken.expiresAt").value("2023-01-01T00:00:00"))
                .andExpect(jsonPath("$.tokens.refreshToken.token").value(expectedRefreshToken.token()))
                .andExpect(jsonPath("$.tokens.refreshToken.expiresAt").value("2023-12-31T23:59:00"));
        then(memberQueryService).should().findDtoBySocialUid(unityId);
        then(jwtTokenCommandService).should().createAccessAndRefreshToken(memberDto);
        then(memberCommandService).shouldHaveNoInteractions();
        verifyEveryMocksShouldHaveNoMoreInteractions();
    }

    @DisplayName("기존에 존재하지 않는 회원의 socialUid가 주어지고, 주어진 socialUid로 unity login을 수행하면, 회원가입과 로그인 처리를 하고 access token과 refresh token을 응답한다.")
    @Test
    void givenSocialUidOfNonExistingMember_whenUnityLogin_thenSignUpAndLoginAndReturnTokens() throws Exception {
        // given
        String unityId = "123abc";
        UnityLoginRequest unityLoginRequest = createUnityLoginRequest(unityId);

        MemberDto memberDto = createMemberDto(1L, unityId);
        JwtTokenInfoDto expectedAccessToken = new JwtTokenInfoDto("access-token", LocalDateTime.of(2023, 1, 1, 0, 0));
        JwtTokenInfoDto expectedRefreshToken = new JwtTokenInfoDto("refresh-token", LocalDateTime.of(2023, 12, 31, 23, 59));
        AccessAndRefreshTokensInfoDto expectedTokens = new AccessAndRefreshTokensInfoDto(expectedAccessToken, expectedRefreshToken);

        given(memberQueryService.findDtoBySocialUid(unityId)).willReturn(Optional.empty());
        given(memberCommandService.createNewMember(unityId)).willReturn(memberDto);
        given(jwtTokenCommandService.createAccessAndRefreshToken(memberDto)).willReturn(expectedTokens);

        // when & then
        mvc.perform(
                        post("/api/v1/auth/login/unity")
                                .header(API_MINOR_VERSION_HEADER_NAME, 1)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(unityLoginRequest))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.loggedInMember.id").value(memberDto.getId()))
                .andExpect(jsonPath("$.loggedInMember.nickname").value(memberDto.getNickname()))
                .andExpect(jsonPath("$.tokens.accessToken.token").value(expectedAccessToken.token()))
                .andExpect(jsonPath("$.tokens.accessToken.expiresAt").value("2023-01-01T00:00:00"))
                .andExpect(jsonPath("$.tokens.refreshToken.token").value(expectedRefreshToken.token()))
                .andExpect(jsonPath("$.tokens.refreshToken.expiresAt").value("2023-12-31T23:59:00"));
        then(memberQueryService).should().findDtoBySocialUid(unityId);
        then(memberCommandService).should().createNewMember(unityId);
        then(jwtTokenCommandService).should().createAccessAndRefreshToken(memberDto);
        verifyEveryMocksShouldHaveNoMoreInteractions();
    }

    @DisplayName("Refresh token이 주어지고, access token과 refresh token을 새로 발급받으면, 새로 생성된 token들을 반환한다.")
    @Test
    void givenRefreshToken_whenRefreshAccessAndRefreshTokens_thenReturnCreatedTokens() throws Exception {
        // given
        RefreshAccessAndRefreshTokensRequest request = createRefreshAccessAndRefreshTokensRequest();
        JwtTokenInfoDto expectedAccessToken = new JwtTokenInfoDto("access-token", LocalDateTime.of(2023, 1, 1, 0, 0));
        JwtTokenInfoDto expectedRefreshToken = new JwtTokenInfoDto("refresh-token", LocalDateTime.of(2023, 12, 31, 23, 59));
        AccessAndRefreshTokensInfoDto expectedTokens = new AccessAndRefreshTokensInfoDto(expectedAccessToken, expectedRefreshToken);
        given(jwtTokenCommandService.refreshAccessAndRefreshToken(request.getRefreshToken())).willReturn(expectedTokens);

        // when & then
        mvc.perform(
                        post("/api/v1/auth/tokens/refresh")
                                .header(API_MINOR_VERSION_HEADER_NAME, 1)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken.token").value(expectedAccessToken.token()))
                .andExpect(jsonPath("$.accessToken.expiresAt").value("2023-01-01T00:00:00"))
                .andExpect(jsonPath("$.refreshToken.token").value(expectedRefreshToken.token()))
                .andExpect(jsonPath("$.refreshToken.expiresAt").value("2023-12-31T23:59:00"));
        then(jwtTokenCommandService).should().refreshAccessAndRefreshToken(request.getRefreshToken());
        verifyEveryMocksShouldHaveNoMoreInteractions();
    }

    @DisplayName("Refresh token이 주어지고, 주어진 refresh token의 유효성을 검사하면, 유효함 여부를 반환한다.")
    @Test
    void givenRefreshToken_whenValidateRefreshToken_thenReturnValidityOfRefreshToken() throws Exception {
        // given
        String refreshToken = "refresh-token";
        boolean expectedResult = true;
        given(jwtTokenQueryService.isRefreshTokenValid(refreshToken)).willReturn(expectedResult);

        // when & then
        mvc.perform(
                        get("/api/v1/auth/refresh-token/validity")
                                .header(API_MINOR_VERSION_HEADER_NAME, 1)
                                .param("refreshToken", refreshToken)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.validity").value(expectedResult));
        then(jwtTokenQueryService).should().isRefreshTokenValid(refreshToken);
        verifyEveryMocksShouldHaveNoMoreInteractions();
    }

    private void verifyEveryMocksShouldHaveNoMoreInteractions() {
        then(memberCommandService).shouldHaveNoMoreInteractions();
        then(memberQueryService).shouldHaveNoMoreInteractions();
        then(jwtTokenCommandService).shouldHaveNoMoreInteractions();
        then(jwtTokenQueryService).shouldHaveNoMoreInteractions();
    }

    private MemberDto createMemberDto(Long memberId, String socialUid) {
        return new MemberDto(
                memberId,
                socialUid,
                Set.of(RoleType.USER),
                socialUid
        );
    }

    private UnityLoginRequest createUnityLoginRequest(String id) {
        return new UnityLoginRequest(id);
    }

    private RefreshAccessAndRefreshTokensRequest createRefreshAccessAndRefreshTokensRequest() {
        return new RefreshAccessAndRefreshTokensRequest("refresh-token");
    }
}