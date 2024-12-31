package com.momong.five_min_raid.unit.domain.member_game_record.service;

import com.momong.five_min_raid.domain.game_record.entity.GameRecord;
import com.momong.five_min_raid.domain.member.constant.RoleType;
import com.momong.five_min_raid.domain.member.entity.Member;
import com.momong.five_min_raid.domain.member_game_record.entity.GuardianGameRecord;
import com.momong.five_min_raid.domain.member_game_record.entity.GuardianPerkTypes;
import com.momong.five_min_raid.domain.member_game_record.entity.MonsterGameRecord;
import com.momong.five_min_raid.domain.member_game_record.entity.MonsterPerkTypes;
import com.momong.five_min_raid.domain.member_game_record.repository.MemberGameRecordRepository;
import com.momong.five_min_raid.domain.member_game_record.service.MemberGameRecordService;
import com.momong.five_min_raid.global.common.constant.GuardianType;
import com.momong.five_min_raid.global.common.constant.MonsterType;
import com.momong.five_min_raid.global.common.constant.TeamType;
import jakarta.validation.constraints.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static com.momong.five_min_raid.global.common.constant.GuardianPerkType.*;
import static com.momong.five_min_raid.global.common.constant.MonsterPerkType.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@DisplayName("[Unit] Service(command) - Member game record")
@ExtendWith(MockitoExtension.class)
class MemberGameRecordServiceTest {

    @InjectMocks
    private MemberGameRecordService sut;

    @Mock
    private MemberGameRecordRepository memberGameRecordRepository;

    @DisplayName("저장할 MonsterGameRecord entity가 주어지고, entity를 저장한다.")
    @Test
    void givenMonsterGameRecordForSave_whenSaving_thenSaveEntity() {
        // given
        Member member = createMember(1L, "1", "test_user");
        GameRecord gameRecord = createGameRecord(2L);
        MonsterGameRecord monsterGameRecordForSave = createNewMonsterGameRecord(member, gameRecord);
        given(memberGameRecordRepository.save(any(MonsterGameRecord.class)))
                .willReturn(createMonsterGameRecord(3L, member, gameRecord));

        // when
        sut.saveMonsterGameRecord(monsterGameRecordForSave);

        // then
        then(memberGameRecordRepository).should().save(monsterGameRecordForSave);
        verifyEveryMocksShouldHaveNoMoreInteractions();
    }

    @DisplayName("저장할 GuardianGameRecord entity들이 주어지고, 주어진 entity들을 전부 저장한다.")
    @Test
    void givenGuardianGameRecordsForSave_whenSavingAll_thenSaveEntities() {
        // given
        Member member = createMember(1L, "1", "test_user");
        GameRecord gameRecord = createGameRecord(2L);
        List<GuardianGameRecord> guardianGameRecordsForSave = List.of(createNewGuardianGameRecord(member, gameRecord));
        given(memberGameRecordRepository.saveAll(ArgumentMatchers.<List<GuardianGameRecord>>any()))
                .willReturn(List.of(createGuardianGameRecord(3L, member, gameRecord)));

        // when
        sut.saveGuardianGameRecords(guardianGameRecordsForSave);

        // then
        then(memberGameRecordRepository).should().saveAll(guardianGameRecordsForSave);
        verifyEveryMocksShouldHaveNoMoreInteractions();
    }

    private void verifyEveryMocksShouldHaveNoMoreInteractions() {
        then(memberGameRecordRepository).shouldHaveNoMoreInteractions();
    }

    private Member createMember(
            @NotNull Long memberId,
            @NotNull String socialUid,
            @NotNull String nickname
    ) {
        return createMember(memberId, socialUid, Set.of(RoleType.USER), nickname);
    }

    private Member createMember(
            @NotNull Long memberId,
            @NotNull String socialUid,
            @NotNull Set<RoleType> roleTypes,
            @NotNull String nickname
    ) {
        return new Member(
                memberId,
                socialUid,
                roleTypes,
                nickname
        );
    }

    private GameRecord createGameRecord(@NotNull Long gameRecordId) {
        return new GameRecord(
                gameRecordId,
                TeamType.MONSTER,
                LocalDateTime.of(2024, 1, 1, 0, 0),
                LocalDateTime.of(2024, 1, 1, 0, 5),
                100,
                60,
                60,
                60
        );
    }

    private MonsterGameRecord createNewMonsterGameRecord(
            @NotNull Member member,
            @NotNull GameRecord gameRecord
    ) {
        return MonsterGameRecord.create(
                member,
                gameRecord,
                MonsterType.PIGEON,
                List.of(PHASE1_ENHANCE, PHASE2_DEATH_MARK, PHASE3_FIELD_ATTACK),
                500,
                300
        );
    }

    private MonsterGameRecord createMonsterGameRecord(
            @NotNull Long memberGameRecordId,
            @NotNull Member member,
            @NotNull GameRecord gameRecord
    ) {
        return new MonsterGameRecord(
                memberGameRecordId,
                member,
                gameRecord,
                MonsterType.PIGEON,
                MonsterPerkTypes.create(List.of(PHASE1_ENHANCE, PHASE2_DEATH_MARK, PHASE3_FIELD_ATTACK)),
                500,
                300
        );
    }

    private GuardianGameRecord createNewGuardianGameRecord(
            @NotNull Member member,
            @NotNull GameRecord gameRecord
    ) {
        return GuardianGameRecord.create(
                member,
                gameRecord,
                GuardianType.SHIELDER,
                List.of(DEFENSE_REDUCTION, DEFENSE_SHIELD, ATTACK_CHARGE_ATTACK),
                100,
                150,
                0,
                2,
                1,
                false,
                0,
                0
        );
    }

    private GuardianGameRecord createGuardianGameRecord(
            @NotNull Long guardianGameRecordId,
            @NotNull Member member,
            @NotNull GameRecord gameRecord
    ) {
        return new GuardianGameRecord(
                guardianGameRecordId,
                member,
                gameRecord,
                GuardianType.SHIELDER,
                GuardianPerkTypes.create(List.of(DEFENSE_REDUCTION, DEFENSE_SHIELD, ATTACK_CHARGE_ATTACK)),
                100,
                150,
                0,
                2,
                1,
                false,
                0,
                0
        );
    }
}