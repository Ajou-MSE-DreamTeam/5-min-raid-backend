package com.momong.backend.domain.game_record.repository;

import com.momong.backend.domain.game_record.entity.GameRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRecordRepository extends JpaRepository<GameRecord, Long> {
}
