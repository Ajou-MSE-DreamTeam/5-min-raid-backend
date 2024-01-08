package com.momong.five_min_raid.domain.member_game_record.service;

import com.momong.five_min_raid.domain.member_game_record.entity.GuardianGameRecord;
import com.momong.five_min_raid.domain.member_game_record.entity.MonsterGameRecord;
import com.momong.five_min_raid.domain.member_game_record.repository.MemberGameRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@RequiredArgsConstructor
@Transactional
@Service
public class MemberGameRecordCommandService {

    private final MemberGameRecordRepository memberGameRecordRepository;

    /**
     * MonsterGameRecord entity를 저장한다.
     *
     * @param monsterGameRecord 저장하고자 하는 MonsterGameRecord entity
     */
    public void saveMonsterGameRecord(MonsterGameRecord monsterGameRecord) {
        memberGameRecordRepository.save(monsterGameRecord);
    }

    /**
     * GuardianGameRecord entity들을 저장한다.
     *
     * @param guardianGameRecords 저장하고자 하는 GuardianGameRecord entities
     */
    public void saveGuardianGameRecords(Collection<GuardianGameRecord> guardianGameRecords) {
        memberGameRecordRepository.saveAll(guardianGameRecords);
    }
}
