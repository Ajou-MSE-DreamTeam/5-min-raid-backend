package com.momong.five_min_raid.domain.member.api;

import com.momong.five_min_raid.domain.member.dto.MemberDto;
import com.momong.five_min_raid.domain.member.dto.request.UpdateMemberNicknameRequest;
import com.momong.five_min_raid.domain.member.dto.response.MemberResponse;
import com.momong.five_min_raid.domain.member.service.MemberCommandService;
import com.momong.five_min_raid.global.auth.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.momong.five_min_raid.global.common.constant.GlobalConstants.API_MINOR_VERSION_HEADER_NAME;

@Tag(name = "회원 관련 API")
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
@RestController
public class MemberControllerV1 {

    private final MemberCommandService memberCommandService;

    @Operation(
            summary = "회원 닉네임 수정하기",
            description = """
                    <p><strong>Latest version: v1.1</strong>
                    <p>회원 닉네임을 수정합니다.</p>
                    <p>이때, 가능한 닉네임 정책은 다음과 같습니다.</p>
                    <ul>
                        <li>영문, 한글, 숫자만 허용 (특수 문자 불가능)</li>
                        <li>최소 2글자, 최대 12글자</li>
                    </ul>
                    """,
            security = @SecurityRequirement(name = "access-token")
    )
    @ApiResponses({
            @ApiResponse(description = "OK", responseCode = "200"),
            @ApiResponse(description = "[2004] 마지막 닉네임 설정 후 14일이 지나지 않은 경우", responseCode = "403", content = @Content),
            @ApiResponse(description = "[2002] 이미 사용중인 닉네임인 경우", responseCode = "409", content = @Content),
            @ApiResponse(description = "[2001] 정책에 맞지 않는 닉네임인 경우", responseCode = "422", content = @Content)
    })
    @PatchMapping(value = "/me/nickname", headers = API_MINOR_VERSION_HEADER_NAME + "=1")
    public MemberResponse updateMemberNicknameV1_1(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody UpdateMemberNicknameRequest request
    ) {
        MemberDto memberDto = memberCommandService.updateMemberNickname(userPrincipal.getMemberId(), request.getNickname());
        return MemberResponse.from(memberDto);
    }

    @Operation(
            summary = "회원 탈퇴",
            description = "회원 탈퇴(내 계정 삭제)를 진행합니다.",
            security = @SecurityRequirement(name = "access-token")
    )
    @ApiResponses({
            @ApiResponse(description = "OK", responseCode = "200"),
            @ApiResponse(description = "[2003] API 요청자와 탈퇴할 회원 정보가 상이한 경우", responseCode = "403", content = @Content),
    })
    @DeleteMapping(value = "/me", headers = API_MINOR_VERSION_HEADER_NAME + "=1")
    public ResponseEntity<Void> deleteMemberV1_1(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        long requestUserId = userPrincipal.getMemberId();
        memberCommandService.deleteMember(requestUserId, requestUserId);
        return ResponseEntity.noContent().build();
    }
}
