package com.momong.five_min_raid.domain.notice.repository;

import com.momong.five_min_raid.domain.notice.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
}
