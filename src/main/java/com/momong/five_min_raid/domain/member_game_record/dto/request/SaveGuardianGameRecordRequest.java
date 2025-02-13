package com.momong.five_min_raid.domain.member_game_record.dto.request;

import com.momong.five_min_raid.global.common.constant.GuardianType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class SaveGuardianGameRecordRequest {

    @Schema(description = "PK of member", example = "1")
    @NotNull
    private Long memberId;

    @Schema(description = "플레이한 가디언의 종류")
    @NotNull
    private GuardianType guardianType;

    @Schema(description = "가디언이 선택한 perks")
    @NotNull
    private List<GuardianPerkRequest> perks;

    @Schema(description = "총 가한 데미지", example = "500")
    @NotNull
    private Integer totalDamageDealt;

    @Schema(description = "총 받은 데미지", example = "300")
    @NotNull
    private Integer totalDamageTaken;

    @Schema(description = "아군에게 가한 총 힐량", example = "50")
    @NotNull
    private Integer totalHealingAmount;

    @Schema(description = "쓰러진 횟수", example = "2")
    @NotNull
    private Integer numOfDowns;

    @Schema(description = "아군을 살린 횟수", example = "1")
    @NotNull
    private Integer numOfRevives;

    @Schema(description = "연결이 끊겼는지", example = "false")
    @NotNull
    private Boolean isDisconnected;

    @Schema(description = "미니언을 처치한 횟수", example = "3")
    private Integer minionKillCount;

    @Schema(description = "미니언에게 가한 데미지", example = "250")
    private Integer minionDamageDealt;
}
