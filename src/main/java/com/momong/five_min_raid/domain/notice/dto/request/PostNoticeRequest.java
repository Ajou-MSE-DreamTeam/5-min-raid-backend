package com.momong.five_min_raid.domain.notice.dto.request;

import com.momong.five_min_raid.domain.notice.entity.Notice;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class PostNoticeRequest {

    @Schema(description = "제목", example = "0.0.1 업데이트 공지")
    @NotBlank
    private String title;

    @Schema(description = "내용", example = "이번 업데이트에서는...")
    @NotBlank
    private String content;

    @Schema(description = "공지 개시 시작 시각")
    @NotNull
    private LocalDateTime startAt;

    @Schema(description = "공지 마감 시각")
    @NotNull
    private LocalDateTime expiresAt;

    public Notice toEntity() {
        return Notice.create(title, content, startAt, expiresAt);
    }
}
