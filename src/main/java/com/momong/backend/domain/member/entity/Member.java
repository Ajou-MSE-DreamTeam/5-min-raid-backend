package com.momong.backend.domain.member.entity;

import com.momong.backend.domain.member.constant.RoleType;
import com.momong.backend.domain.member.converter.RoleTypesConverter;
import com.momong.backend.global.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

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

    public Member(Long id, String socialUid, Set<RoleType> roleTypes, String nickname, LocalDateTime createdAt, LocalDateTime updatedAt) {
        super(createdAt, updatedAt);
        this.id = id;
        this.socialUid = socialUid;
        this.roleTypes = roleTypes;
        this.nickname = nickname;
    }

    public static Member createNewMember(String socialUid) {
        return new Member(null, socialUid, Set.of(RoleType.USER), null, null, null);
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
