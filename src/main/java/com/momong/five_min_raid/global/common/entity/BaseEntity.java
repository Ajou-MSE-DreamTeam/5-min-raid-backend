package com.momong.five_min_raid.global.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@MappedSuperclass
public abstract class BaseEntity extends BaseTimeEntity {

    @CreatedBy
    @Column(nullable = false, updatable = false)
    protected Long createdBy;

    @LastModifiedBy
    @Column(nullable = false)
    protected Long updatedBy;

    protected BaseEntity(LocalDateTime createdAt, LocalDateTime updatedAt, Long createdBy, Long updatedBy) {
        super(createdAt, updatedAt);
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
    }
}
