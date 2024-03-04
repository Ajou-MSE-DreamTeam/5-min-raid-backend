package com.momong.five_min_raid.domain.notice.repository;

import com.momong.five_min_raid.domain.notice.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    // TODO: Querydsl custom query method로 구현 변경 예정
    List<Notice> findAllByStartAtAfterAndExpiresAtBefore(LocalDateTime currentTime1, LocalDateTime currentTime2);
}
