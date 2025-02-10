package com.momong.five_min_raid.domain.member_game_record.entity;

import com.momong.five_min_raid.domain.game_record.entity.GameRecord;
import com.momong.five_min_raid.domain.member.entity.Member;
import com.momong.five_min_raid.global.common.constant.MonsterPerkType;
import com.momong.five_min_raid.global.common.constant.MonsterType;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@DiscriminatorValue("MONSTER")
@Entity
public class MonsterGameRecord extends MemberGameRecord {

    @NotNull
    @Enumerated(EnumType.STRING)
    private MonsterType monsterType;

    @NotNull
    @Embedded
    private MonsterPerkTypes perks;

    @NotNull
    private Integer totalDamageDealt;

    @NotNull
    private Integer totalDamageTaken;

    public MonsterGameRecord(
            @NotNull Long id,
            @NotNull Member member,
            @NotNull GameRecord gameRecord,
            @NotNull MonsterType monsterType,
            @NotNull MonsterPerkTypes perks,
            @NotNull Integer totalDamageDealt,
            @NotNull Integer totalDamageTaken
    ) {
        super(id, member, gameRecord);
        this.monsterType = monsterType;
        this.perks = perks;
        this.totalDamageDealt = totalDamageDealt;
        this.totalDamageTaken = totalDamageTaken;
    }

    public static MonsterGameRecord create(
            @Nullable Member member,
            @NotNull GameRecord gameRecord,
            @NotNull MonsterType monsterType,
            @NotNull List<MonsterPerkType> perks,
            @NotNull Integer totalDamageDealt,
            @NotNull Integer totalDamageTaken
    ) {
        return new MonsterGameRecord(
                null,
                member,
                gameRecord,
                monsterType,
                MonsterPerkTypes.create(perks),
                totalDamageDealt,
                totalDamageTaken
        );
    }

    public List<MonsterPerkType> getPerks() {
        return this.perks.getPerkTypes();
    }
}
