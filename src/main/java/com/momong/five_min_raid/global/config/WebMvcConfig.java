package com.momong.five_min_raid.global.config;

import com.momong.five_min_raid.global.converter.GuardianPerkTypeRequestConverter;
import com.momong.five_min_raid.global.converter.MonsterPerkTypeRequestConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new GuardianPerkTypeRequestConverter());
        registry.addConverter(new MonsterPerkTypeRequestConverter());
    }
}
