package com.momong.five_min_raid.global.common.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

public enum MonsterPerkType {
    PHASE1_BLOOD_SUCKING,
    PHASE1_GASH,
    PHASE1_LAST_DANCE,
    PHASE1_ENHANCE,

    PHASE2_PROTECTION_TOTEM,
    PHASE2_DEATH_MARK,
    PHASE2_SLEEPING_GAS,
    PHASE2_FAKE_RAGE,

    PHASE3_FIELD_ATTACK,
    PHASE3_MAGNETIC_FIELD,
    PHASE3_THORN_BUSH,
    PHASE3_MAZE,
    ;

    private final String noUnderscoreName;

    MonsterPerkType() {
        this.noUnderscoreName = name().replace("_", "");
    }

    /**
     * Enum value의 underscore를 무시한 상태로 value of 기능을 수행한다.
     *
     * @param value Enum value로 변환하고자 하는 문자열
     * @return 전달받은 문자열과 일치하는 enum value
     * @throws IllegalArgumentException 전달받은 문자열에 대해 일치하는 enum value가 없는 경우
     */
    @JsonCreator
    public static MonsterPerkType ignoreUnderscoreValueOf(@Nullable String value) {
        if (!StringUtils.hasText(value)) {
            throw new IllegalArgumentException("Invalid value: " + value);
        }
        value = value.replace("_", "");

        for (MonsterPerkType perkType : values()) {
            if (perkType.noUnderscoreName.equalsIgnoreCase(value)) {
                return perkType;
            }
        }
        throw new IllegalArgumentException(String.format("No matching enum constant for '%s'", value));
    }
}
