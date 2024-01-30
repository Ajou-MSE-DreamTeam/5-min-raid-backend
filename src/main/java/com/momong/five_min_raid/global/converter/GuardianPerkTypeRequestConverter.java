package com.momong.five_min_raid.global.converter;

import com.momong.five_min_raid.global.common.constant.GuardianPerkType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

public class GuardianPerkTypeRequestConverter implements Converter<String, GuardianPerkType> {

    @Override
    public GuardianPerkType convert(String source) {
        if (!StringUtils.hasText(source)) {
            return null;
        }
        return GuardianPerkType.ignoreUnderscoreValueOf(source);
    }
}
