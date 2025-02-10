package com.momong.five_min_raid.domain.member.entity;

import com.momong.five_min_raid.domain.member.constant.RoleType;
import com.momong.five_min_raid.domain.member.converter.RoleTypesConverter;
import com.momong.five_min_raid.global.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(indexes = {
        @Index(name = "idx__member__social_uid", columnList = "socialUid"),
        @Index(name = "idx__member__nickname", columnList = "nickname")
})
@Entity
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", nullable = false)
    private Long id;

    @Column(nullable = false, unique = true)
    private String socialUid;

    @Column(nullable = false)
    @Convert(converter = RoleTypesConverter.class)
    private Set<RoleType> roleTypes;

    @Column(unique = true)
    private String nickname;

    @Column(nullable = false)
    private LocalDate nicknameLastUpdatedAt;

    public static Member createNewMember(String socialUid) {
        return new Member(null, socialUid, Set.of(RoleType.USER), null, LocalDate.of(0, 1, 1));
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
        this.nicknameLastUpdatedAt = LocalDate.now();
    }

    public boolean isAdmin() {
        return roleTypes.contains(RoleType.ADMIN);
    }
}
