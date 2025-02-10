package com.momong.five_min_raid.domain.member_game_record.dto.request;

import com.momong.five_min_raid.domain.game_record.entity.GameRecord;
import com.momong.five_min_raid.domain.member.entity.Member;
import com.momong.five_min_raid.domain.member_game_record.entity.MonsterGameRecord;
import com.momong.five_min_raid.global.common.constant.MonsterPerkType;
import com.momong.five_min_raid.global.common.constant.MonsterType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
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
public class SaveMonsterGameRecordRequest {

    @Schema(description = "PK of member", example = "1")
    @NotNull
    private Long memberId;

    @Schema(description = "플레이한 몬스터의 종류")
    @NotNull
    private MonsterType monsterType;

    @Schema(description = "몬스터가 선택한 perks. 중복되지 않은 3개여야 한다.", example = "[\"TEMP1\", \"TEMP2\", \"TEMP3\"]")
    @Size(min = 3, max = 3)
    @NotNull
    private List<MonsterPerkType> perks;

    @Schema(description = "총 가한 데미지", example = "500")
    @NotNull
    private Integer totalDamageDealt;

    @Schema(description = "총 받은 데미지", example = "300")
    @NotNull
    private Integer totalDamageTaken;

    public MonsterGameRecord toEntity(
            @Nullable Member member,
            @NotNull GameRecord gameRecord
    ) {
        return MonsterGameRecord.create(
                member,
                gameRecord,
                this.monsterType,
                this.perks,
                this.totalDamageDealt,
                this.totalDamageTaken
        );
    }
}
