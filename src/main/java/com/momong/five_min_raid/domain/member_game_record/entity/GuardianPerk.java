package com.momong.five_min_raid.domain.member_game_record.entity;

import com.momong.five_min_raid.global.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "guardian_perk")
@Entity
public class GuardianPerk extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guardian_game_record_id", nullable = false)
    private GuardianGameRecord guardianGameRecord;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer level;

    public static GuardianPerk create(GuardianGameRecord guardianGameRecord, String name, Integer level) {
        return new GuardianPerk(null, guardianGameRecord, name, level);
    }
}
