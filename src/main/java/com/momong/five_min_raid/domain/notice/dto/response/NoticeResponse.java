package com.momong.five_min_raid.domain.notice.dto.response;

import com.momong.five_min_raid.domain.notice.dto.NoticeDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class NoticeResponse {

    @Schema(description = "Id of notice", example = "3")
    private Long id;

    @Schema(description = "제목", example = "0.0.1 업데이트 공지")
    private String title;

    @Schema(description = "내용", example = "이번 업데이트에서는...")
    private String content;

    @Schema(description = "공지 개시 시작 시각")
    private LocalDateTime startAt;

    @Schema(description = "공지 마감 시각")
    private LocalDateTime expiresAt;

    public static NoticeResponse from(NoticeDto noticeDto) {
        return new NoticeResponse(
                noticeDto.getId(),
                noticeDto.getTitle(),
                noticeDto.getContent(),
                noticeDto.getStartAt(),
                noticeDto.getExpiresAt()
        );
    }
}
