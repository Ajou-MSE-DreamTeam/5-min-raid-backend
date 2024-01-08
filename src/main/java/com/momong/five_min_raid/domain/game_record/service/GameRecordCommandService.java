package com.momong.five_min_raid.domain.game_record.service;

import com.momong.five_min_raid.domain.game_record.dto.GameRecordDto;
import com.momong.five_min_raid.domain.game_record.dto.request.SaveGameRecordRequest;
import com.momong.five_min_raid.domain.game_record.entity.GameRecord;
import com.momong.five_min_raid.domain.game_record.exception.GuardianGameRecordDuplicateException;
import com.momong.five_min_raid.domain.game_record.exception.InvalidGuardianGameRecordSizeException;
import com.momong.five_min_raid.domain.game_record.repository.GameRecordRepository;
import com.momong.five_min_raid.domain.member.entity.Member;
import com.momong.five_min_raid.domain.member.service.MemberQueryService;
import com.momong.five_min_raid.domain.member_game_record.dto.request.SaveGuardianGameRecordRequest;
import com.momong.five_min_raid.domain.member_game_record.dto.request.SaveMonsterGameRecordRequest;
import com.momong.five_min_raid.domain.member_game_record.service.MemberGameRecordCommandService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class GameRecordCommandService {

    public static final int GUARDIAN_SIZE_REQUIRED = 3;

    private final MemberQueryService memberQueryService;
    private final MemberGameRecordCommandService memberGameRecordCommandService;
    private final GameRecordRepository gameRecordRepository;

    /**
     * 게임 결과를 기록한다.
     *
     * @param saveGameRecordRequest 기록하고자 하는 인게임 정보
     * @return 저장된 GameRecord의 dto
     * @throws InvalidGuardianGameRecordSizeException 저장하고자 하는 가디언 정보들의 개수가 유효하지 않은 경우.
     * @throws GuardianGameRecordDuplicateException   중복된 데이터가 있는 경우. 즉, 동일한 memberId가 존재하는 경우.
     */
    public GameRecordDto saveGameRecord(@NotNull SaveGameRecordRequest saveGameRecordRequest) {
        GameRecord gameRecord = gameRecordRepository.save(saveGameRecordRequest.toEntity());
        saveMonsterGameRecord(gameRecord, saveGameRecordRequest.getMonster());
        saveGuardianGameRecords(gameRecord, saveGameRecordRequest.getGuardians());

        return GameRecordDto.from(gameRecord);
    }

    /**
     * Id로 Member entity를 조회한다.
     *
     * @param memberId 조회하고자 하는 Member의 id
     * @return 조회된 Member entity
     */
    private Member getMemberById(Long memberId) {
        return memberQueryService.getById(memberId);
    }

    /**
     * 몬스터의 인게임 정보를 기록한다.
     *
     * @param gameRecord                   저장된 게임 기록 entity
     * @param saveMonsterGameRecordRequest 저장하고자 하는 몬스터의 인게임 정보
     */
    private void saveMonsterGameRecord(GameRecord gameRecord, SaveMonsterGameRecordRequest saveMonsterGameRecordRequest) {
        Member member = getMemberById(saveMonsterGameRecordRequest.getMemberId());
        memberGameRecordCommandService.saveMonsterGameRecord(
                saveMonsterGameRecordRequest.toEntity(member, gameRecord)
        );
    }

    /**
     * 가디언들의 인게임 정보를 기록한다.
     *
     * @param gameRecord                     저장된 게임 기록 entity
     * @param saveGuardianGameRecordRequests 저장하고자 하는 가디언들의 인게임 정보들이 담긴 리스트
     * @throws InvalidGuardianGameRecordSizeException 저장하고자 하는 가디언 정보들의 개수가 유효하지 않은 경우.
     * @throws GuardianGameRecordDuplicateException   중복된 데이터가 있는 경우. 즉, 동일한 memberId가 존재하는 경우.
     */
    private void saveGuardianGameRecords(GameRecord gameRecord, List<SaveGuardianGameRecordRequest> saveGuardianGameRecordRequests) {
        validateNumOfGuardians(saveGuardianGameRecordRequests);
        validateNoDuplicateGuardians(saveGuardianGameRecordRequests);
        memberGameRecordCommandService.saveGuardianGameRecords(
                saveGuardianGameRecordRequests.stream()
                        .map(guardianGameRecordRequest -> {
                            Member member = getMemberById(guardianGameRecordRequest.getMemberId());
                            return guardianGameRecordRequest.toEntity(member, gameRecord);
                        })
                        .toList()
        );
    }

    /**
     * 저장하고자 하는 가디언 정보들의 개수가 유효한지 검증한다.
     *
     * @param saveGuardianGameRecordRequests 저장하고자 하는 가디언 정보들이 담긴 리스트
     * @throws InvalidGuardianGameRecordSizeException 저장하고자 하는 가디언 정보들의 개수가 유효하지 않은 경우.
     */
    private void validateNumOfGuardians(List<SaveGuardianGameRecordRequest> saveGuardianGameRecordRequests) {
        if (saveGuardianGameRecordRequests.size() != GUARDIAN_SIZE_REQUIRED) {
            throw new InvalidGuardianGameRecordSizeException();
        }
    }

    /**
     * 저장하고자 하는 가디언 정보들 중 중복된 데이터가 있지 않음을 검증한다.
     * 각 가디언 정보는 서로 다른 회원(memberId)에 대한 것이어야 한다.
     *
     * @param saveGuardianGameRecordRequests 저장하고자 하는 가디언 정보들이 담긴 리스트
     * @throws GuardianGameRecordDuplicateException 중복된 데이터가 있는 경우. 즉, 동일한 memberId가 존재하는 경우.
     */
    private void validateNoDuplicateGuardians(List<SaveGuardianGameRecordRequest> saveGuardianGameRecordRequests) {
        long uniqueMemberIdCount = saveGuardianGameRecordRequests.stream()
                .map(SaveGuardianGameRecordRequest::getMemberId)
                .distinct()
                .count();
        if (uniqueMemberIdCount != saveGuardianGameRecordRequests.size()) {
            throw new GuardianGameRecordDuplicateException();
        }
    }
}
