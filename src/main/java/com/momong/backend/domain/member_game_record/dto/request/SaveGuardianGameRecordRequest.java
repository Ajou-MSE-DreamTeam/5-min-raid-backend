package com.momong.backend.domain.member_game_record.dto.request;

import com.momong.backend.domain.game_record.entity.GameRecord;
import com.momong.backend.domain.member.entity.Member;
import com.momong.backend.domain.member_game_record.entity.GuardianGameRecord;
import com.momong.backend.global.common.constant.GuardianPerkType;
import com.momong.backend.global.common.constant.GuardianType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    @Schema(description = "가디언이 선택한 perks. 중복되지 않은 3개여야 한다.", example = "[\"ATTACK_BERSERK\", \"ATTACK_CRITICAL\", \"UTIL_HASTE\"]")
    @Size(min = 3, max = 3)
    @NotNull
    private List<GuardianPerkType> perks;

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

    public GuardianGameRecord toEntity(
            @NotNull Member member,
            @NotNull GameRecord gameRecord
    ) {
        return GuardianGameRecord.create(
                member,
                gameRecord,
                this.guardianType,
                this.perks,
                this.totalDamageDealt,
                this.totalDamageTaken,
                this.totalHealingAmount,
                this.numOfDowns,
                this.numOfRevives,
                this.isDisconnected
        );
    }
}
