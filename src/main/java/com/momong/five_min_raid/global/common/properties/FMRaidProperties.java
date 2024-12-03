package com.momong.five_min_raid.global.common.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("fmraid")
public record FMRaidProperties(
        Long aiMemberId
) {
}
