package com.momong.five_min_raid.global.common.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

public enum GuardianPerkType {

    ATTACK_BERSERK,
    ATTACK_CHARGE_ATTACK,
    ATTACK_CRITICAL,
    ATTACK_RAPID,

    UTIL_ATTACK_COST,
    UTIL_HASTE,
    UTIL_FAST_HELP,
    UTIL_FAST_STAMINA_RECOVERY,

    DEFENSE_SHIELD,
    DEFENSE_RECOVERY,
    DEFENSE_REDUCTION,
    DEFENSE_GIANT,
    ;

    private final String noUnderscoreName;

    GuardianPerkType() {
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
    public static GuardianPerkType ignoreUnderscoreValueOf(@Nullable String value) {
        if (!StringUtils.hasText(value)) {
            throw new IllegalArgumentException("Invalid value: " + value);
        }
        value = value.replace("_", "");

        for (GuardianPerkType perkType : values()) {
            if (perkType.noUnderscoreName.equalsIgnoreCase(value)) {
                return perkType;
            }
        }
        throw new IllegalArgumentException(String.format("No matching enum constant for '%s'", value));
    }
}
