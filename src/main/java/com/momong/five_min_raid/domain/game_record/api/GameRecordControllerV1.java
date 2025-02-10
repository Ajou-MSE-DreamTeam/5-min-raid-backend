package com.momong.five_min_raid.domain.game_record.api;

import com.momong.five_min_raid.domain.game_record.dto.GameRecordDto;
import com.momong.five_min_raid.domain.game_record.dto.request.SaveGameRecordRequest;
import com.momong.five_min_raid.domain.game_record.dto.response.GameRecordResponse;
import com.momong.five_min_raid.domain.game_record.service.GameRecordCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

import static com.momong.five_min_raid.global.common.constant.GlobalConstants.API_MINOR_VERSION_HEADER_NAME;

@Tag(name = "게임 기록 관련 API")
@RequiredArgsConstructor
@RequestMapping("/api/v1/game-records")
@RestController
public class GameRecordControllerV1 {

    private final GameRecordCommandService gameRecordCommandService;

    @Operation(
            summary = "게임 결과 저장",
            description = "게임 결과를 저장한다.",
            security = @SecurityRequirement(name = "access-token")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(
                    responseCode = "400",
                    description = """
                            <p>[2100] 몬스터 또는 가디언이 선택한 perk 중 중복된 항목이 있는 경우
                            <p>[2101] 가디언이 선택한 perk 개수가 유효하지 않은 경우
                            <p>[2102] 몬스터가 선택한 perk 개수가 유효하지 않은 경우
                            <p>[2103] 기록하고자 하는 가디언의 인게임 정보 개수가 유효하지 않은 경우
                            <p>[2104] 기록하고자 하는 가디언의 인게임 정보 중 중복된 항목이 있는 경우
                            """,
                    content = @Content
            )
    })
    @PostMapping(headers = API_MINOR_VERSION_HEADER_NAME + "=1")
    public ResponseEntity<GameRecordResponse> saveGameRecord(@RequestBody @Valid SaveGameRecordRequest saveGameRecordRequest) {
        GameRecordDto gameRecordDto = gameRecordCommandService.saveGameRecord(saveGameRecordRequest);
        return ResponseEntity
                .created(URI.create("/api/v1/game-records/" + gameRecordDto.getId()))
                .body(GameRecordResponse.from(gameRecordDto));
    }
}
