package com.momong.five_min_raid.domain.client_version.entity;

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
@Entity
public class ClientVersion extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "client_version_id", nullable = false)
    private Long id;

    @NotNull
    private String version;

    public void updateVersion(String version) {
        this.version = version;
    }
}
