package com.momong.backend.domain.member_game_record.converter;

import com.momong.backend.global.common.constant.GuardianPerkType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

@Converter
public class GuardianPerkTypesConverter implements AttributeConverter<List<GuardianPerkType>, String> {

    private static final String DELIMITER = ";";

    @Override
    public String convertToDatabaseColumn(List<GuardianPerkType> attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return "";
        }
        List<String> monsterPerkTypeNames = attribute.stream()
                .map(GuardianPerkType::name)
                .sorted()
                .toList();
        return String.join(DELIMITER, monsterPerkTypeNames);
    }

    @Override
    public List<GuardianPerkType> convertToEntityAttribute(String dbData) {
        if (!StringUtils.hasText(dbData)) {
            return List.of();
        }
        return Arrays.stream(dbData.split(DELIMITER))
                .map(GuardianPerkType::valueOf)
                .toList();
    }
}
