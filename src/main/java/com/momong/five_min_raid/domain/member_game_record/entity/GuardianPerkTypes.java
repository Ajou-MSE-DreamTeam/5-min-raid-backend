package com.momong.five_min_raid.domain.member_game_record.entity;

import com.momong.five_min_raid.domain.member_game_record.converter.GuardianPerkTypesConverter;
import com.momong.five_min_raid.domain.member_game_record.exception.PerksDuplicateException;
import com.momong.five_min_raid.domain.member_game_record.exception.InvalidGuardianPerksSizeException;
import com.momong.five_min_raid.global.common.constant.GuardianPerkType;
import jakarta.persistence.Convert;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class GuardianPerkTypes {

    public static final int SELECTABLE_GUARDIAN_PERKS_SIZE = 3;

    @NotNull
    @Convert(converter = GuardianPerkTypesConverter.class)
    private List<GuardianPerkType> perkTypes;

    public static GuardianPerkTypes create(List<GuardianPerkType> perkTypes) {
        validatePerkTypesSize(perkTypes);
        validateNoDuplicatePerks(perkTypes);
        return new GuardianPerkTypes(perkTypes);
    }

    public List<GuardianPerkType> getPerkTypes() {
        return Collections.unmodifiableList(perkTypes);
    }

    private static void validatePerkTypesSize(List<GuardianPerkType> perkTypes) {
        if (perkTypes == null || perkTypes.size() != SELECTABLE_GUARDIAN_PERKS_SIZE) {
            throw new InvalidGuardianPerksSizeException();
        }
    }

    private static void validateNoDuplicatePerks(List<GuardianPerkType> perkTypes) {
        if (perkTypes.stream().distinct().count() != perkTypes.size()) {
            throw new PerksDuplicateException();
        }
    }
}
