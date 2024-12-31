package com.momong.five_min_raid.domain.member_game_record.entity;

import com.momong.five_min_raid.domain.member.entity.Member;
import com.momong.five_min_raid.domain.game_record.entity.GameRecord;
import com.momong.five_min_raid.global.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Inheritance(strategy = InheritanceType.JOINED)
@Entity
public abstract class MemberGameRecord extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_game_record_id", nullable = false)
    private Long id;

    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @NotNull
    @JoinColumn(name = "game_record_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private GameRecord gameRecord;

    public void removeMember() {
        this.member = null;
    }
}
