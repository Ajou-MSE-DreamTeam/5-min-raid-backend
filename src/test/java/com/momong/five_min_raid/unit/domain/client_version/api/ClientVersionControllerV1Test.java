package com.momong.five_min_raid.unit.domain.client_version.api;

import com.momong.five_min_raid.config.ControllerTestConfig;
import com.momong.five_min_raid.domain.client_version.api.ClientVersionControllerV1;
import com.momong.five_min_raid.domain.client_version.service.ClientVersionService;
import com.momong.five_min_raid.domain.member.constant.RoleType;
import com.momong.five_min_raid.domain.member.dto.MemberDto;
import com.momong.five_min_raid.global.auth.UserPrincipal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static com.momong.five_min_raid.global.common.constant.GlobalConstants.API_MINOR_VERSION_HEADER_NAME;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("[Unit] Controller - Client Version")
@Import(ControllerTestConfig.class)
@WebMvcTest(controllers = ClientVersionControllerV1.class)
class ClientVersionControllerV1Test {

    @MockBean
    private ClientVersionService clientVersionService;

    private final MockMvc mvc;

    @Autowired
    public ClientVersionControllerV1Test(MockMvc mvc) {
        this.mvc = mvc;
    }

    @DisplayName("최신 클라이언트 버전을 조회하면, 조회된 버전이 반환된다.")
    @Test
    void given_whenGetLatestClientVersion_thenReturnVersion() throws Exception {
        // given
        String expectedResult = "1.0.0";
        given(clientVersionService.getLatestClientVersion())
                .willReturn(expectedResult);

        // when & then
        mvc.perform(
                        get("/api/v1/client-versions/latest")
                                .header(API_MINOR_VERSION_HEADER_NAME, 1)
                                .with(user(createAdminUserDetails(1L)))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.version").value(expectedResult));
        then(clientVersionService).should().getLatestClientVersion();
        then(clientVersionService).shouldHaveNoMoreInteractions();
    }

    private UserDetails createAdminUserDetails(Long memberId) {
        return new UserPrincipal(createMemberDto(memberId, Set.of(RoleType.USER, RoleType.ADMIN)));
    }

    private MemberDto createMemberDto(Long memberId) {
        return createMemberDto(memberId, Set.of(RoleType.USER));
    }

    private MemberDto createMemberDto(Long memberId, Set<RoleType> roleTypes) {
        return new MemberDto(
                memberId,
                String.valueOf(memberId),
                roleTypes,
                String.valueOf(memberId)
        );
    }
}