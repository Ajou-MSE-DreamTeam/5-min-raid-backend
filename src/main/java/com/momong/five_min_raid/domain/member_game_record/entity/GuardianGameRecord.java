package com.momong.five_min_raid.domain.member_game_record.entity;

import com.momong.five_min_raid.domain.game_record.entity.GameRecord;
import com.momong.five_min_raid.domain.member.entity.Member;
import com.momong.five_min_raid.global.common.constant.GuardianPerkType;
import com.momong.five_min_raid.global.common.constant.GuardianType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@DiscriminatorValue("GUARDIAN")
@Entity
public class GuardianGameRecord extends MemberGameRecord {

    @NotNull
    @Enumerated(EnumType.STRING)
    private GuardianType guardianType;

    @NotNull
    @Embedded
    private GuardianPerkTypes perks;

    @NotNull
    private Integer totalDamageDealt;

    @NotNull
    private Integer totalDamageTaken;

    @NotNull
    private Integer totalHealingAmount;

    @NotNull
    private Integer numOfDowns;

    @NotNull
    private Integer numOfRevives;

    @NotNull
    private Boolean isDisconnected;

    public GuardianGameRecord(
            @NotNull Long id,
            @NotNull Member member,
            @NotNull GameRecord gameRecord,
            @NotNull GuardianType guardianType,
            @NotNull GuardianPerkTypes perks,
            @NotNull Integer totalDamageDealt,
            @NotNull Integer totalDamageTaken,
            @NotNull Integer totalHealingAmount,
            @NotNull Integer numOfDowns,
            @NotNull Integer numOfRevives,
            @NotNull Boolean isDisconnected
    ) {
        super(id, member, gameRecord);
        this.guardianType = guardianType;
        this.perks = perks;
        this.totalDamageDealt = totalDamageDealt;
        this.totalDamageTaken = totalDamageTaken;
        this.totalHealingAmount = totalHealingAmount;
        this.numOfDowns = numOfDowns;
        this.numOfRevives = numOfRevives;
        this.isDisconnected = isDisconnected;
    }

    public static GuardianGameRecord create(
            @NotNull Member member,
            @NotNull GameRecord gameRecord,
            @NotNull GuardianType guardianType,
            @NotNull List<GuardianPerkType> perks,
            @NotNull Integer totalDamageDealt,
            @NotNull Integer totalDamageTaken,
            @NotNull Integer totalHealingAmount,
            @NotNull Integer numOfDowns,
            @NotNull Integer numOfRevives,
            @NotNull Boolean isDisconnected
    ) {
        return new GuardianGameRecord(
                null,
                member,
                gameRecord,
                guardianType,
                GuardianPerkTypes.create(perks),
                totalDamageDealt,
                totalDamageTaken,
                totalHealingAmount,
                numOfDowns,
                numOfRevives,
                isDisconnected
        );
    }

    public List<GuardianPerkType> getPerks() {
        return this.perks.getPerkTypes();
    }
}
