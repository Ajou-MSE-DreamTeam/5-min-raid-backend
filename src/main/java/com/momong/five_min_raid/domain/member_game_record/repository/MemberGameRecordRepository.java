package com.momong.five_min_raid.domain.member_game_record.repository;

import com.momong.five_min_raid.domain.member.entity.Member;
import com.momong.five_min_raid.domain.member_game_record.entity.MemberGameRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberGameRecordRepository extends JpaRepository<MemberGameRecord, Long> {
    List<MemberGameRecord> findByMember(Member member);
}
