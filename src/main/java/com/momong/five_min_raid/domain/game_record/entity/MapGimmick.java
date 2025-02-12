package com.momong.five_min_raid.domain.game_record.entity;

import com.momong.five_min_raid.global.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MapGimmick extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_record_id", nullable = false)
    private GameRecord gameRecord;

    @Column(nullable = false)
    private String name;

    public static MapGimmick create(GameRecord gameRecord, String name) {
        return new MapGimmick(null, gameRecord, name);
    }
}
