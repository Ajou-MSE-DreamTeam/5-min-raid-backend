package com.momong.five_min_raid.domain.notice.entity;

import com.momong.five_min_raid.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(indexes = {
        @Index(name = "idx__notice__expires_at", columnList = "expiresAt")
})
@Entity
public class Notice extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", nullable = false)
    private Long id;

    @NotBlank
    private String title;

    @Column(columnDefinition = "TEXT")
    @NotBlank
    private String content;

    @NotNull
    private LocalDateTime startAt;

    @NotNull
    private LocalDateTime expiresAt;

    public static Notice create(
            @NotBlank String title,
            @NotBlank String content,
            @NotNull LocalDateTime startAt,
            @NotNull LocalDateTime expiresAt
    ) {
        return new Notice(null, title, content, startAt, expiresAt);
    }
}
