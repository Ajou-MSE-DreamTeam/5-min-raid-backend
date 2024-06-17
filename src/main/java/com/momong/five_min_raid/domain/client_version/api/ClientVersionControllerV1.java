package com.momong.five_min_raid.domain.client_version.api;

import com.momong.five_min_raid.domain.client_version.dto.request.UpdateLatestClientVersionRequest;
import com.momong.five_min_raid.domain.client_version.dto.response.ClientVersionResponse;
import com.momong.five_min_raid.domain.client_version.service.ClientVersionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.momong.five_min_raid.global.common.constant.GlobalConstants.API_MINOR_VERSION_HEADER_NAME;

@Tag(name = "클라이언트 버전 정보 관련 API")
@RequiredArgsConstructor
@RequestMapping("/api/v1/client-versions")
@RestController
public class ClientVersionControllerV1 {

    private final ClientVersionService clientVersionService;

    @Operation(
            summary = "최신 클라이언트 버전 조회",
            description = "가장 최신의 클라이언트 버전을 조회한다."
    )
    @GetMapping(value = "/latest", headers = API_MINOR_VERSION_HEADER_NAME + "=1")
    public ClientVersionResponse getLatestClientVersion() {
        String latestClientVersion = clientVersionService.getLatestClientVersion();
        return new ClientVersionResponse(latestClientVersion);
    }

    @Operation(
            summary = "최신 클라이언트 버전 수정",
            description = "최신의 클라이언트 버전을 수정한다.",
            security = @SecurityRequirement(name = "access-token")
    )
    @PutMapping(value = "/latest", headers = API_MINOR_VERSION_HEADER_NAME + "=1")
    public ClientVersionResponse updateLatestClientVersion(@RequestBody UpdateLatestClientVersionRequest updateVersionRequest) {
        String versionUpdated = clientVersionService.updateLatestClientVersion(updateVersionRequest.version());
        return new ClientVersionResponse(versionUpdated);
    }
}
