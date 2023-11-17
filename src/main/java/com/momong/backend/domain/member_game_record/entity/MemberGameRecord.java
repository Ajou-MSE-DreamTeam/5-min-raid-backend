package com.momong.backend.domain.member_game_record.entity;

import com.momong.backend.domain.member.entity.Member;
import com.momong.backend.domain.game_record.entity.GameRecord;
import com.momong.backend.global.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@DiscriminatorColumn(name = "team_type")
@Inheritance(strategy = InheritanceType.JOINED)
@Entity
public abstract class MemberGameRecord extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_game_record_id", nullable = false)
    private Long id;

    @NotNull
    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @NotNull
    @JoinColumn(name = "game_record_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private GameRecord gameRecord;
}
