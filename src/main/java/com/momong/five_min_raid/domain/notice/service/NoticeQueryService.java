package com.momong.five_min_raid.domain.notice.service;

import com.momong.five_min_raid.domain.notice.dto.NoticeDto;
import com.momong.five_min_raid.domain.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class NoticeQueryService {

    private final NoticeRepository noticeRepository;

    /**
     * 기준 시각에 대해 게시중인 공지 목록 조회
     *
     * @param time 기준 시각
     * @return 조회된 공지 리스트
     */
    public List<NoticeDto> findActiveNotices(LocalDateTime time) {
        return noticeRepository
                .findAllByStartAtBeforeAndExpiresAtAfter(time, time)
                .stream()
                .map(NoticeDto::from)
                .toList();
    }
}
