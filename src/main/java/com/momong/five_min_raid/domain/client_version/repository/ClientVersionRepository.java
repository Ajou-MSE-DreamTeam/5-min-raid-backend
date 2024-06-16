package com.momong.five_min_raid.domain.client_version.repository;

import com.momong.five_min_raid.domain.client_version.entity.ClientVersion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientVersionRepository extends JpaRepository<ClientVersion, Long> {
    Optional<ClientVersion> findTop1ByOrderByCreatedAtDesc();
}
