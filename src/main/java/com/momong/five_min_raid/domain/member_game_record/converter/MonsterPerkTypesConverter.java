package com.momong.five_min_raid.domain.member_game_record.converter;

import com.momong.five_min_raid.global.common.constant.MonsterPerkType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

@Converter
public class MonsterPerkTypesConverter implements AttributeConverter<List<MonsterPerkType>, String> {

    private static final String DELIMITER = ";";

    @Override
    public String convertToDatabaseColumn(List<MonsterPerkType> attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return "";
        }
        List<String> monsterPerkTypeNames = attribute.stream()
                .map(MonsterPerkType::name)
                .sorted()
                .toList();
        return String.join(DELIMITER, monsterPerkTypeNames);
    }

    @Override
    public List<MonsterPerkType> convertToEntityAttribute(String dbData) {
        if (!StringUtils.hasText(dbData)) {
            return List.of();
        }
        return Arrays.stream(dbData.split(DELIMITER))
                .map(MonsterPerkType::valueOf)
                .toList();
    }
}
