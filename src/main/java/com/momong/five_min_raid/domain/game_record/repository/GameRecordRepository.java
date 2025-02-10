package com.momong.five_min_raid.domain.game_record.repository;

import com.momong.five_min_raid.domain.game_record.entity.GameRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRecordRepository extends JpaRepository<GameRecord, Long> {
}
