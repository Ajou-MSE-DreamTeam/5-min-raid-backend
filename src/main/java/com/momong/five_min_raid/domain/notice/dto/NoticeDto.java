package com.momong.five_min_raid.domain.notice.dto;

import com.momong.five_min_raid.domain.notice.entity.Notice;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class NoticeDto {

    private Long id;
    private String title;
    private String content;
    private LocalDateTime startAt;
    private LocalDateTime expiresAt;

    public static NoticeDto from(Notice notice) {
        return new NoticeDto(
                notice.getId(),
                notice.getTitle(),
                notice.getContent(),
                notice.getStartAt(),
                notice.getExpiresAt()
        );
    }
}
