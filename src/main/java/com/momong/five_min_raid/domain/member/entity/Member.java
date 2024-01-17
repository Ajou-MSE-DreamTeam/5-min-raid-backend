package com.momong.five_min_raid.domain.member.entity;

import com.momong.five_min_raid.domain.member.constant.RoleType;
import com.momong.five_min_raid.domain.member.converter.RoleTypesConverter;
import com.momong.five_min_raid.global.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @NotBlank
    @Column(nullable = false, unique = true)
    private String socialUid;

    @NotNull
    @Column(nullable = false)
    @Convert(converter = RoleTypesConverter.class)
    private Set<RoleType> roleTypes;

    @Column(unique = true)
    private String nickname;

    public static Member createNewMember(String socialUid) {
        return new Member(null, socialUid, Set.of(RoleType.USER), null);
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public boolean isAdmin() {
        return roleTypes.contains(RoleType.ADMIN);
    }
}
