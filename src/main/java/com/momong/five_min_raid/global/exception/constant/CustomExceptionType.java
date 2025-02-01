package com.momong.five_min_raid.global.exception.constant;

import com.momong.five_min_raid.domain.game_record.entity.GameRecord;
import com.momong.five_min_raid.domain.game_record.service.GameRecordCommandService;
import com.momong.five_min_raid.domain.member.entity.Member;
import com.momong.five_min_raid.domain.member_game_record.entity.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Error code 목록
 *
 * <ul>
 *     <li>1001 ~ 1999: 일반 예외. 아래 항목에 해당하지 않는 대부분의 예외가 여기에 해당한다.</li>
 *     <li>15XX: 인증 관련 예외</li>
 *     <li>20XX: 회원({@link Member}) 관련 예외</li>
 *     <li>21XX: 게임 기록({@link GameRecord}, {@link MemberGameRecord}, {@link MonsterGameRecord}, {@link GuardianGameRecord}) 관련 예외</li>
 * </ul>
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum CustomExceptionType {

    /**
     * Common
     */
    NOT_ADMIN(1001, "관리자 권한이 필요한 기능입니다."),

    /**
     * 로그인, 인증 관련 예외
     */
    ACCESS_DENIED(1500, "접근이 거부되었습니다."),
    UNAUTHORIZED(1501, "유효하지 않은 인증 정보로 인해 인증 과정에서 문제가 발생하였습니다."),
    TOKEN_VALIDATE(1502, "유효하지 않은 token입니다. Token 값이 잘못되었거나 만료되어 유효하지 않은 경우로 token 갱신이 필요합니다."),
    ACCESS_TOKEN_VALIDATE(1503, "유효하지 않은 access token입니다. Token 값이 잘못되었거나 만료되어 유효하지 않은 경우로 token 갱신이 필요합니다."),
    REFRESH_TOKEN_VALIDATE(1504, "유효하지 않은 refresh token입니다. Token 값이 잘못되었거나 만료되어 유효하지 않은 경우로 token 갱신이 필요합니다."),

    /**
     * 회원({@link Member}) 관련 예외
     */
    MEMBER_NOT_FOUND(2000, "회원을 찾을 수 없습니다."),
    INVALID_MEMBER_NICKNAME(2001, "유효하지 않은 닉네임입니다. 닉네임은 2~12 글자의 영어, 한글, 숫자만 사용할 수 있습니다."),
    MEMBER_NICKNAME_DUPLICATION(2002, "이미 사용중인 닉네임입니다."),
    MEMBER_DELETION_PERMISSION_DENIED(2003, "회원 삭제 권한이 없습니다."),
    NICKNAME_CHANGE_COOLDOWN(2004, "마지막 닉네임 설정 후 14일이 지나지 않았습니다. 닉네임은 14일 간격으로 수정 가능합니다."),

    /**
     * 게임 기록({@link GameRecord}, {@link MemberGameRecord}, {@link MonsterGameRecord}, {@link GuardianGameRecord}) 관련 예외
     */
    PERKS_DUPLICATE(2100, "중복된 perk이 있습니다."),
    INVALID_GUARDIAN_PERKS_SIZE(2101, String.format("가디언이 선택한 perk 개수가 유효하지 않습니다. 가디언은 반드시 %d개의 perk을 선택해야 합니다.", GuardianPerkTypes.SELECTABLE_GUARDIAN_PERKS_SIZE)),
    INVALID_MONSTER_PERKS_SIZE(2102, String.format("몬스터가 선택한 perk 개수가 유효하지 않습니다. 몬스터는 반드시 %d개의 perk을 선택해야 합니다.", MonsterPerkTypes.SELECTABLE_MONSTER_PERKS_SIZE)),
    INVALID_GUARDIAN_GAME_RECORD_SIZE(2103, String.format("기록하고자 하는 가디언의 인게임 정보 개수가 유효하지 않습니다. 한 게임에 가디언은 %d개이므로 동일한 개수의 정보가 필요합니다.", GameRecordCommandService.GUARDIAN_SIZE_REQUIRED)),
    GUARDIAN_GAME_RECORD_DUPLICATE(2104, "기록하고자 하는 가디언의 인게임 정보 중 중복된 항목이 있습니다."),
    ;

    private final Integer code;
    private final String message;
}
