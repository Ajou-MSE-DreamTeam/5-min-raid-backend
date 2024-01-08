package com.momong.five_min_raid.domain.member_game_record.entity;

import com.momong.five_min_raid.domain.member_game_record.converter.MonsterPerkTypesConverter;
import com.momong.five_min_raid.domain.member_game_record.exception.InvalidMonsterPerksSizeException;
import com.momong.five_min_raid.domain.member_game_record.exception.PerksDuplicateException;
import com.momong.five_min_raid.global.common.constant.MonsterPerkType;
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
public class MonsterPerkTypes {

    public static final int SELECTABLE_MONSTER_PERKS_SIZE = 3;

    @NotNull
    @Convert(converter = MonsterPerkTypesConverter.class)
    private List<MonsterPerkType> perkTypes;

    public static MonsterPerkTypes create(List<MonsterPerkType> perkTypes) {
        validatePerkTypesSize(perkTypes);
        validateNoDuplicatePerks(perkTypes);
        return new MonsterPerkTypes(perkTypes);
    }

    public List<MonsterPerkType> getPerkTypes() {
        return Collections.unmodifiableList(perkTypes);
    }

    private static void validatePerkTypesSize(List<MonsterPerkType> perkTypes) {
        if (perkTypes == null || perkTypes.size() != SELECTABLE_MONSTER_PERKS_SIZE) {
            throw new InvalidMonsterPerksSizeException();
        }
    }

    private static void validateNoDuplicatePerks(List<MonsterPerkType> perkTypes) {
        if (perkTypes.stream().distinct().count() != perkTypes.size()) {
            throw new PerksDuplicateException();
        }
    }
}
