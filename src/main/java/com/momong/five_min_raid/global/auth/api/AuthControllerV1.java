package com.momong.five_min_raid.global.auth.api;

import com.momong.five_min_raid.domain.member.dto.MemberDto;
import com.momong.five_min_raid.domain.member.service.MemberService;
import com.momong.five_min_raid.global.auth.dto.AccessAndRefreshTokensInfoDto;
import com.momong.five_min_raid.global.auth.dto.request.RefreshAccessAndRefreshTokensRequest;
import com.momong.five_min_raid.global.auth.dto.request.UnityLoginRequest;
import com.momong.five_min_raid.global.auth.dto.response.AccessAndRefreshTokensResponse;
import com.momong.five_min_raid.global.auth.dto.response.LoginResponse;
import com.momong.five_min_raid.global.auth.dto.response.TokenValidationResponse;
import com.momong.five_min_raid.global.auth.service.JwtTokenCommandService;
import com.momong.five_min_raid.global.auth.service.JwtTokenQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;

import static com.momong.five_min_raid.global.common.constant.GlobalConstants.API_MINOR_VERSION_HEADER_NAME;

@Tag(name = "로그인 등 인증 관련")
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/v1/auth")
@RestController
public class AuthControllerV1 {

    private final MemberService memberService;
    private final JwtTokenCommandService jwtTokenCommandService;
    private final JwtTokenQueryService jwtTokenQueryService;

    @Operation(
            summary = "Unity login",
            description = """
                    <p><strong>Latest version: v1.1</strong>
                    <p>Unity authentication에서 획득한 id로 로그인합니다.
                    <p>전달받은 id와 일치하는 기존 회원이 존재하지 않는다면 회원가입 후 로그인을 수행합니다.
                    <p>로그인에 성공하면 로그인 사용자 정보, access token, refresh token을 응답합니다. 만약 신규 회원이라면 <code>nickname</code>이 <code>null</code>입니다.
                    <p>이후 로그인 권한이 필요한 API를 호출할 때는 HTTP header의 <code>Authorization</code> attribute에 access token을 담아서 요청해야 합니다.
                    <p>Access token의 만료기한은 12시간, refresh token의 만료기한은 일주일입니다.
                    """
    )
    @ApiResponses({
            @ApiResponse(description = "OK. 기존 회원이 정상적으로 로그인 한 경우", responseCode = "200"),
            @ApiResponse(description = "Created. 신규 회원이 로그인하여 회원가입이 된 경우", responseCode = "201")
    })
    @PostMapping(value = "/login/unity", headers = API_MINOR_VERSION_HEADER_NAME + "=1")
    public ResponseEntity<LoginResponse> unityLoginV1_1(@RequestBody @Valid UnityLoginRequest request) {
        boolean memberCreationFlag = false;

        Optional<MemberDto> optionalMemberDto = memberService.findDtoBySocialUid(request.getId());
        MemberDto memberDto;
        if (optionalMemberDto.isEmpty()) {
            memberCreationFlag = true;
            memberDto = memberService.createNewMember(request.getId());
        } else {
            memberDto = optionalMemberDto.get();
        }

        AccessAndRefreshTokensInfoDto accessAndRefreshTokensInfoDto = jwtTokenCommandService.createAccessAndRefreshToken(memberDto);

        LoginResponse response = LoginResponse.from(memberDto, accessAndRefreshTokensInfoDto);
        if (memberCreationFlag) {
            return ResponseEntity.created(URI.create("/api/v1/members/" + memberDto.getId())).body(response);
        } else {
            return ResponseEntity.ok().body(response);
        }
    }

    @Operation(
            summary = "토큰 갱신하기",
            description = "<p><strong>Latest version: v1.1</strong>" +
                          "<p>기존 발급받은 refresh token으로 새로운 access token과 refresh token을 발급 받습니다."
    )
    @ApiResponses({
            @ApiResponse(description = "OK", responseCode = "200"),
            @ApiResponse(description = "[1504] 유효하지 않은 refresh token으로 요청한 경우. Token 값이 잘못되었거나 만료되어 유효하지 않은 경우로 token 갱신 필요", responseCode = "401", content = @Content),
    })
    @PostMapping(value = "/tokens/refresh", headers = API_MINOR_VERSION_HEADER_NAME + "=1")
    public AccessAndRefreshTokensResponse refreshAccessAndRefreshTokensV1_1(@RequestBody @Valid RefreshAccessAndRefreshTokensRequest request) {
        AccessAndRefreshTokensInfoDto accessAndRefreshTokensInfoDto = jwtTokenCommandService.refreshAccessAndRefreshToken(request.getRefreshToken());
        return AccessAndRefreshTokensResponse.from(accessAndRefreshTokensInfoDto);
    }

    @Operation(
            summary = "Refresh token 유효성 검사",
            description = """
                    <p><strong>Latest version: v1.1</strong>
                    <p>Refresh token의 유효성을 확인합니다.</p>
                    <p>유효하지 않은 refresh token이란 다음과 같은 경우를 말합니다.</p>
                    <ul>
                        <li>Refresh token의 값이 잘못된 경우</li>
                        <li>Refresh token이 만료된 경우</li>
                        <li>Refresh token의 발행 기록을 찾을 수 없는 경우</li>
                    </ul>
                    """
    )
    @GetMapping(value = "/refresh-token/validity", headers = API_MINOR_VERSION_HEADER_NAME + "=1")
    public TokenValidationResponse isRefreshTokenValidV1_1(@RequestParam @NotBlank String refreshToken) {
        boolean validity = jwtTokenQueryService.isRefreshTokenValid(refreshToken);
        return new TokenValidationResponse(validity);
    }
}
