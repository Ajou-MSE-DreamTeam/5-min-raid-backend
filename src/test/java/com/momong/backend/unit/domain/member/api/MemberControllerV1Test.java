package com.momong.backend.unit.domain.member.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.momong.backend.config.ControllerTestConfig;
import com.momong.backend.domain.member.api.MemberControllerV1;
import com.momong.backend.domain.member.constant.RoleType;
import com.momong.backend.domain.member.dto.MemberDto;
import com.momong.backend.domain.member.dto.request.UpdateMemberNicknameRequest;
import com.momong.backend.domain.member.service.MemberCommandService;
import com.momong.backend.global.security.UserPrincipal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static com.momong.backend.global.common.constant.GlobalConstants.API_MINOR_VERSION_HEADER_NAME;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("[Unit] Controller - Member")
@Import(ControllerTestConfig.class)
@WebMvcTest(controllers = MemberControllerV1.class)
class MemberControllerV1Test {

    @MockBean
    private MemberCommandService memberCommandService;

    private final MockMvc mvc;
    private final ObjectMapper mapper;

    @Autowired
    public MemberControllerV1Test(MockMvc mvc, ObjectMapper mapper) {
        this.mvc = mvc;
        this.mapper = mapper;
    }

    @DisplayName("닉네임이 주어지고, 회원 닉네임을 변경하면, 닉네임이 변경되고 변경된 회원 정보를 반환한다.")
    @Test
    void givenNickname_whenUpdateMemberNickname_thenUpdateNicknameAndReturnUpdatedMemberInfo() throws Exception {
        // given
        long memberId = 1L;
        String nicknameForUpdate = "nickname";
        MemberDto expectedResult = createMemberDto(memberId, nicknameForUpdate);
        given(memberCommandService.updateMemberNickname(memberId, nicknameForUpdate)).willReturn(expectedResult);

        // when & then
        mvc.perform(
                        patch("/api/v1/members/me/nickname")
                                .header(API_MINOR_VERSION_HEADER_NAME, 1)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(new UpdateMemberNicknameRequest(nicknameForUpdate)))
                                .with(user(createTestUserDetails(memberId)))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(memberId))
                .andExpect(jsonPath("$.nickname").value(nicknameForUpdate));
        then(memberCommandService).should().updateMemberNickname(memberId, nicknameForUpdate);
        verifyEveryMocksShouldHaveNoMoreInteractions();

    }

    private void verifyEveryMocksShouldHaveNoMoreInteractions() {
        then(memberCommandService).shouldHaveNoMoreInteractions();
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
}