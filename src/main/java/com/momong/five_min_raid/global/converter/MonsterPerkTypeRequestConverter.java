package com.momong.five_min_raid.global.converter;

import com.momong.five_min_raid.global.common.constant.MonsterPerkType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

public class MonsterPerkTypeRequestConverter implements Converter<String, MonsterPerkType> {

    @Override
    public MonsterPerkType convert(String source) {
        if (!StringUtils.hasText(source)) {
            return null;
        }
        return MonsterPerkType.ignoreUnderscoreValueOf(source);
    }
}
