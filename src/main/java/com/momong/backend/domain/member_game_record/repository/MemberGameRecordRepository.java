package com.momong.backend.domain.member_game_record.repository;

import com.momong.backend.domain.member_game_record.entity.MemberGameRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberGameRecordRepository extends JpaRepository<MemberGameRecord, Long> {
}
