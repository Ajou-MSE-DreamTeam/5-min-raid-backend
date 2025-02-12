package com.momong.five_min_raid.domain.game_record.entity;

import com.momong.five_min_raid.global.common.constant.TeamType;
import com.momong.five_min_raid.global.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class GameRecord extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "game_record_id", nullable = false)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TeamType winnerTeam;

    @NotNull
    private LocalDateTime startedAt;

    @NotNull
    private LocalDateTime endedAt;

    private Integer exp;

    private Integer phase1Time;

    private Integer phase2Time;

    private Integer phase3Time;

    @OneToMany(mappedBy = "gameRecord", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MapGimmick> mapGimmicks;

    public static GameRecord create(
            @NotNull TeamType winnerTeam,
            @NotNull LocalDateTime startedAt,
            @NotNull LocalDateTime endedAt,
            Integer exp,
            Integer phase1Time,
            Integer phase2Time,
            Integer phase3Time
    ) {
        return new GameRecord(null, winnerTeam, startedAt, endedAt, exp, phase1Time, phase2Time, phase3Time, new LinkedList<>());
    }

    public void addMapGimmicks(List<MapGimmick> mapGimmicks) {
        this.mapGimmicks.addAll(mapGimmicks);
    }
}
