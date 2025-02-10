package com.momong.five_min_raid.domain.client_version.service;

import com.momong.five_min_raid.domain.client_version.entity.ClientVersion;
import com.momong.five_min_raid.domain.client_version.repository.ClientVersionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ClientVersionService {

    private final ClientVersionRepository clientVersionRepository;

    @Transactional(readOnly = true)
    public String getLatestClientVersion() {
        return getLatestClientVersionEntity().getVersion();
    }

    @Transactional
    public String updateLatestClientVersion(String version) {
        ClientVersion clientVersion = getLatestClientVersionEntity();
        clientVersion.updateVersion(version);
        return clientVersion.getVersion();
    }

    private ClientVersion getLatestClientVersionEntity() {
        return clientVersionRepository.findTop1ByOrderByCreatedAtDesc().orElseThrow();
    }
}
