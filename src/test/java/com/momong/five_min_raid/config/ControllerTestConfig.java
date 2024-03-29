package com.momong.five_min_raid.config;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;

@MockBean(JpaMetamodelMappingContext.class)
@Import(TestSecurityConfig.class)
public class ControllerTestConfig {
}
