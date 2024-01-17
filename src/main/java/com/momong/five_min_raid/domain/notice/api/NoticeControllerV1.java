package com.momong.five_min_raid.domain.notice.api;

import com.momong.five_min_raid.domain.notice.dto.NoticeDto;
import com.momong.five_min_raid.domain.notice.dto.request.PostNoticeRequest;
import com.momong.five_min_raid.domain.notice.dto.response.NoticeResponse;
import com.momong.five_min_raid.domain.notice.service.NoticeCommandService;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@Tag(name = "게임 공지 API")
@RequiredArgsConstructor
@RequestMapping("/api/v1/notices")
@RestController
public class NoticeControllerV1 {

    private final NoticeCommandService noticeCommandService;

    @Operation(
            summary = "[관리자용] 공지 등록하기",
            description = "새로운 공지를 등록합니다.",
            security = @SecurityRequirement(name = "access-token")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "403", description = "[1001] 관리자 권한이 없는 사용자가 요청한 경우", content = @Content)
    })
    @PostMapping
    public ResponseEntity<NoticeResponse> postNotice(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody @Valid PostNoticeRequest postNoticeRequest
    ) {
        NoticeDto noticePosted = noticeCommandService.postNotice(userPrincipal.getMemberId(), postNoticeRequest);
        return ResponseEntity
                .created(URI.create("/api/v1/notices"))
                .body(NoticeResponse.from(noticePosted));
    }
}
