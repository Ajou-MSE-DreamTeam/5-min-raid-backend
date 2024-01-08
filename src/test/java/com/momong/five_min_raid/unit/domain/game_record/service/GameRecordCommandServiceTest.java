package com.momong.five_min_raid.unit.domain.game_record.service;

import com.momong.five_min_raid.domain.game_record.dto.GameRecordDto;
import com.momong.five_min_raid.domain.game_record.dto.request.SaveGameRecordRequest;
import com.momong.five_min_raid.domain.game_record.entity.GameRecord;
import com.momong.five_min_raid.domain.game_record.exception.GuardianGameRecordDuplicateException;
import com.momong.five_min_raid.domain.game_record.exception.InvalidGuardianGameRecordSizeException;
import com.momong.five_min_raid.domain.game_record.repository.GameRecordRepository;
import com.momong.five_min_raid.domain.game_record.service.GameRecordCommandService;
import com.momong.five_min_raid.domain.member.constant.RoleType;
import com.momong.five_min_raid.domain.member.entity.Member;
import com.momong.five_min_raid.domain.member.service.MemberQueryService;
import com.momong.five_min_raid.domain.member_game_record.dto.request.SaveGuardianGameRecordRequest;
import com.momong.five_min_raid.domain.member_game_record.dto.request.SaveMonsterGameRecordRequest;
import com.momong.five_min_raid.domain.member_game_record.entity.GuardianGameRecord;
import com.momong.five_min_raid.domain.member_game_record.entity.MonsterGameRecord;
import com.momong.five_min_raid.domain.member_game_record.service.MemberGameRecordCommandService;
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
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@DisplayName("[Unit] Service(command) - Game record")
@ExtendWith(MockitoExtension.class)
class GameRecordCommandServiceTest {

    @InjectMocks
    private GameRecordCommandService sut;

    @Mock
    private MemberQueryService memberQueryService;
    @Mock
    private MemberGameRecordCommandService memberGameRecordCommandService;
    @Mock
    private GameRecordRepository gameRecordRepository;

    @DisplayName("게임 결과 정보가 주어지면, 게임 결과를 저장한다.")
    @Test
    void givenGameRecordInfo_whenSavingGameRecord_thenSaveEntities() {
        // given
        long gameRecordId = 1L;
        long monsterMemberId = 2L;
        List<Long> guardianMemberIds = List.of(3L, 4L, 5L);
        SaveGameRecordRequest saveGameRecordRequest = createSaveGameRecordRequest(monsterMemberId, guardianMemberIds);
        GameRecord expectedResult = createGameRecord(gameRecordId);
        given(gameRecordRepository.save(any(GameRecord.class))).willReturn(expectedResult);
        given(memberQueryService.getById(monsterMemberId)).willReturn(createMember(monsterMemberId));
        willDoNothing().given(memberGameRecordCommandService).saveMonsterGameRecord(any(MonsterGameRecord.class));
        guardianMemberIds.forEach(
                id -> given(memberQueryService.getById(id)).willReturn(createMember(id))
        );
        willDoNothing().given(memberGameRecordCommandService).saveGuardianGameRecords(ArgumentMatchers.<List<GuardianGameRecord>>any());

        // when
        GameRecordDto actualResult = sut.saveGameRecord(saveGameRecordRequest);

        // then
        then(gameRecordRepository).should().save(any(GameRecord.class));
        then(memberQueryService).should().getById(monsterMemberId);
        then(memberGameRecordCommandService).should().saveMonsterGameRecord(any(MonsterGameRecord.class));
        guardianMemberIds.forEach(
                id -> then(memberQueryService).should().getById(id)
        );
        then(memberGameRecordCommandService).should().saveGuardianGameRecords(ArgumentMatchers.<List<GuardianGameRecord>>any());
        verifyEveryMocksShouldHaveNoMoreInteractions();
        assertThat(actualResult.getId()).isEqualTo(expectedResult.getId());
        assertThat(actualResult.getWinnerTeam()).isEqualTo(expectedResult.getWinnerTeam());
        assertThat(actualResult.getStartedAt()).isEqualTo(expectedResult.getStartedAt());
        assertThat(actualResult.getEndedAt()).isEqualTo(expectedResult.getEndedAt());
    }

    @DisplayName("유효하지 않은 가디언 개수가 담긴 게임 결과 정보가 주어지고, 게임 결과를 저장하려고 하면, 예외가 발생한다.")
    @Test
    void givenGameRecordInfoContainingInvalidGuardianSize_whenSavingGameRecord_thenThrowException() {
        // given
        long gameRecordId = 1L;
        long monsterMemberId = 2L;
        List<Long> guardianMemberIds = List.of(3L, 4L);
        SaveGameRecordRequest saveGameRecordRequest = createSaveGameRecordRequest(monsterMemberId, guardianMemberIds);
        GameRecord expectedResult = createGameRecord(gameRecordId);
        given(gameRecordRepository.save(any(GameRecord.class))).willReturn(expectedResult);
        given(memberQueryService.getById(monsterMemberId)).willReturn(createMember(monsterMemberId));
        willDoNothing().given(memberGameRecordCommandService).saveMonsterGameRecord(any(MonsterGameRecord.class));

        // when
        Throwable t = catchThrowable(() -> sut.saveGameRecord(saveGameRecordRequest));

        // then
        then(gameRecordRepository).should().save(any(GameRecord.class));
        then(memberQueryService).should().getById(monsterMemberId);
        then(memberGameRecordCommandService).should().saveMonsterGameRecord(any(MonsterGameRecord.class));
        verifyEveryMocksShouldHaveNoMoreInteractions();
        assertThat(t).isInstanceOf(InvalidGuardianGameRecordSizeException.class);
    }

    @DisplayName("유효하지 않은 가디언 개수가 담긴 게임 결과 정보가 주어지고, 게임 결과를 저장하려고 하면, 예외가 발생한다.")
    @Test
    void givenGameRecordInfoContainingDuplicateGuardianInfo_whenSavingGameRecord_thenThrowException() {
        // given
        long gameRecordId = 1L;
        long monsterMemberId = 2L;
        List<Long> guardianMemberIds = List.of(3L, 4L, 3L);
        SaveGameRecordRequest saveGameRecordRequest = createSaveGameRecordRequest(monsterMemberId, guardianMemberIds);
        GameRecord expectedResult = createGameRecord(gameRecordId);
        given(gameRecordRepository.save(any(GameRecord.class))).willReturn(expectedResult);
        given(memberQueryService.getById(monsterMemberId)).willReturn(createMember(monsterMemberId));
        willDoNothing().given(memberGameRecordCommandService).saveMonsterGameRecord(any(MonsterGameRecord.class));

        // when
        Throwable t = catchThrowable(() -> sut.saveGameRecord(saveGameRecordRequest));

        // then
        then(gameRecordRepository).should().save(any(GameRecord.class));
        then(memberQueryService).should().getById(monsterMemberId);
        then(memberGameRecordCommandService).should().saveMonsterGameRecord(any(MonsterGameRecord.class));
        verifyEveryMocksShouldHaveNoMoreInteractions();
        assertThat(t).isInstanceOf(GuardianGameRecordDuplicateException.class);
    }

    private void verifyEveryMocksShouldHaveNoMoreInteractions() {
        then(memberQueryService).shouldHaveNoMoreInteractions();
        then(memberGameRecordCommandService).shouldHaveNoMoreInteractions();
        then(gameRecordRepository).shouldHaveNoMoreInteractions();
    }

    private Member createMember(@NotNull Long memberId) {
        return createMember(memberId, String.valueOf(memberId), Set.of(RoleType.USER), "test" + memberId);
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

    private SaveMonsterGameRecordRequest createSaveMonsterGameRecordRequest(@NotNull Long memberId) {
        return new SaveMonsterGameRecordRequest(
                memberId,
                MonsterType.PIGEON,
                List.of(TEMP1, TEMP2, TEMP3),
                300,
                500
        );
    }

    private SaveGuardianGameRecordRequest createSaveGuardianGameRecordRequest(@NotNull Long memberId) {
        return new SaveGuardianGameRecordRequest(
                memberId,
                GuardianType.SHIELDER,
                List.of(DEFENSE_DODGE, DEFENSE_SHIELD, ATTACK_CHARGE_ATTACK),
                100,
                150,
                0,
                2,
                1,
                false
        );
    }

    private SaveGameRecordRequest createSaveGameRecordRequest(
            @NotNull Long monsterMemberId,
            @NotNull List<Long> guardianMemberIds
    ) {
        return new SaveGameRecordRequest(
                TeamType.MONSTER,
                createSaveMonsterGameRecordRequest(monsterMemberId),
                guardianMemberIds.stream()
                        .map(this::createSaveGuardianGameRecordRequest)
                        .toList(),
                LocalDateTime.of(2024, 1, 1, 0, 0),
                LocalDateTime.of(2024, 1, 1, 0, 5)
        );
    }

    private GameRecord createGameRecord(@NotNull Long gameRecordId) {
        return new GameRecord(
                gameRecordId,
                TeamType.MONSTER,
                LocalDateTime.of(2024, 1, 1, 0, 0),
                LocalDateTime.of(2024, 1, 1, 0, 5)
        );
    }
}