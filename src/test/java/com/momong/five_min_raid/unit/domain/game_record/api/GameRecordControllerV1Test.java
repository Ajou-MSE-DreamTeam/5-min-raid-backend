package com.momong.five_min_raid.unit.domain.game_record.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.momong.five_min_raid.config.ControllerTestConfig;
import com.momong.five_min_raid.domain.game_record.api.GameRecordControllerV1;
import com.momong.five_min_raid.domain.game_record.dto.GameRecordDto;
import com.momong.five_min_raid.domain.game_record.dto.request.SaveGameRecordRequest;
import com.momong.five_min_raid.domain.game_record.service.GameRecordCommandService;
import com.momong.five_min_raid.domain.member.constant.RoleType;
import com.momong.five_min_raid.domain.member.dto.MemberDto;
import com.momong.five_min_raid.domain.member_game_record.dto.request.SaveGuardianGameRecordRequest;
import com.momong.five_min_raid.domain.member_game_record.dto.request.SaveMonsterGameRecordRequest;
import com.momong.five_min_raid.global.auth.UserPrincipal;
import com.momong.five_min_raid.global.common.constant.GuardianType;
import com.momong.five_min_raid.global.common.constant.MonsterType;
import com.momong.five_min_raid.global.common.constant.TeamType;
import jakarta.validation.constraints.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static com.momong.five_min_raid.global.common.constant.GlobalConstants.API_MINOR_VERSION_HEADER_NAME;
import static com.momong.five_min_raid.global.common.constant.GuardianPerkType.*;
import static com.momong.five_min_raid.global.common.constant.MonsterPerkType.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("[Unit] Controller - Game record")
@Import(ControllerTestConfig.class)
@WebMvcTest(controllers = GameRecordControllerV1.class)
class GameRecordControllerV1Test {

    @MockBean
    private GameRecordCommandService gameRecordCommandService;

    private final MockMvc mvc;
    private final ObjectMapper mapper;

    @Autowired
    public GameRecordControllerV1Test(MockMvc mvc, ObjectMapper mapper) {
        this.mvc = mvc;
        this.mapper = mapper;
    }

    @DisplayName("")
    @Test
    void given_when_then() throws Exception {
        // given
        long loginMemberId = 1L;
        SaveGameRecordRequest saveGameRecordRequest = createSaveGameRecordRequest(1L, List.of(2L, 3L, 4L));
        GameRecordDto expectedResult = createGameRecordDto(2L);
        given(gameRecordCommandService.saveGameRecord(any(SaveGameRecordRequest.class))).willReturn(expectedResult);

        // when & then
        mvc.perform(
                        post("/api/v1/game-records")
                                .header(API_MINOR_VERSION_HEADER_NAME, 1)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(saveGameRecordRequest))
                                .with(user(createTestUserDetails(loginMemberId)))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(expectedResult.getId()))
                .andExpect(jsonPath("$.winnerTeam").value(expectedResult.getWinnerTeam().toString()));
        then(gameRecordCommandService).should().saveGameRecord(any(SaveGameRecordRequest.class));
        verifyEveryMocksShouldHaveNoMoreInteractions();
    }

    private void verifyEveryMocksShouldHaveNoMoreInteractions() {
        then(gameRecordCommandService).shouldHaveNoMoreInteractions();
    }

    private UserDetails createTestUserDetails(Long memberId) {
        return new UserPrincipal(createMemberDto(memberId));
    }

    private MemberDto createMemberDto(Long memberId) {
        return new MemberDto(
                memberId,
                String.valueOf(memberId),
                Set.of(RoleType.USER),
                String.valueOf(memberId)
        );
    }

    private MemberDto createMemberDto(Long memberId, String nickname) {
        return new MemberDto(
                memberId,
                String.valueOf(memberId),
                Set.of(RoleType.USER),
                nickname
        );
    }

    private SaveMonsterGameRecordRequest createSaveMonsterGameRecordRequest(@NotNull Long memberId) {
        return new SaveMonsterGameRecordRequest(
                memberId,
                MonsterType.PIGEON,
                List.of(PHASE1_ENHANCE, PHASE2_DEATH_MARK, PHASE3_FIELD_ATTACK),
                300,
                500
        );
    }

    private SaveGuardianGameRecordRequest createSaveGuardianGameRecordRequest(@NotNull Long memberId) {
        return new SaveGuardianGameRecordRequest(
                memberId,
                GuardianType.SHIELDER,
                List.of(DEFENSE_DODGE, DEFENSE_SHIELD, ATTACK_CHARGE_ATTACK),
                100,
                150,
                0,
                2,
                1,
                false
        );
    }

    private SaveGameRecordRequest createSaveGameRecordRequest(
            @NotNull Long monsterMemberId,
            @NotNull List<Long> guardianMemberIds
    ) {
        return new SaveGameRecordRequest(
                TeamType.MONSTER,
                createSaveMonsterGameRecordRequest(monsterMemberId),
                guardianMemberIds.stream()
                        .map(this::createSaveGuardianGameRecordRequest)
                        .toList(),
                LocalDateTime.of(2024, 1, 1, 0, 0),
                LocalDateTime.of(2024, 1, 1, 0, 5)
        );
    }

    private GameRecordDto createGameRecordDto(@NotNull Long gameRecordId) {
        return new GameRecordDto(
                gameRecordId,
                TeamType.MONSTER,
                LocalDateTime.of(2024, 1, 1, 0, 0),
                LocalDateTime.of(2024, 1, 1, 0, 5)
        );
    }
}