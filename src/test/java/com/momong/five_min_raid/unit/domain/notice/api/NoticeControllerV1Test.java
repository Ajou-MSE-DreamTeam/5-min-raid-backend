package com.momong.five_min_raid.unit.domain.notice.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.momong.five_min_raid.config.ControllerTestConfig;
import com.momong.five_min_raid.domain.member.constant.RoleType;
import com.momong.five_min_raid.domain.member.dto.MemberDto;
import com.momong.five_min_raid.domain.notice.api.NoticeControllerV1;
import com.momong.five_min_raid.domain.notice.constant.NoticeType;
import com.momong.five_min_raid.domain.notice.dto.NoticeDto;
import com.momong.five_min_raid.domain.notice.dto.request.PostNoticeRequest;
import com.momong.five_min_raid.domain.notice.service.NoticeCommandService;
import com.momong.five_min_raid.domain.notice.service.NoticeQueryService;
import com.momong.five_min_raid.global.auth.UserPrincipal;
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

import static com.momong.five_min_raid.domain.member.constant.RoleType.ADMIN;
import static com.momong.five_min_raid.domain.member.constant.RoleType.USER;
import static com.momong.five_min_raid.global.common.constant.GlobalConstants.API_MINOR_VERSION_HEADER_NAME;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("[Unit] Controller - Notice")
@Import(ControllerTestConfig.class)
@WebMvcTest(controllers = NoticeControllerV1.class)
class NoticeControllerV1Test {

    @MockBean
    private NoticeCommandService noticeCommandService;

    @MockBean
    private NoticeQueryService noticeQueryService;

    private final MockMvc mvc;
    private final ObjectMapper mapper;

    @Autowired
    public NoticeControllerV1Test(MockMvc mvc, ObjectMapper mapper) {
        this.mvc = mvc;
        this.mapper = mapper;
    }

    @Test
    void 공지_정보가_주어지고_공지를_등록한다() throws Exception {
        // given
        long loginMemberId = 1L;
        long noticeId = 2L;
        PostNoticeRequest postNoticeRequest = createPostNoticeRequest();
        NoticeDto expectedResult = createNoticeDto(noticeId);
        given(noticeCommandService.postNotice(eq(loginMemberId), any(PostNoticeRequest.class))).willReturn(expectedResult);

        // when & then
        mvc.perform(
                        post("/api/v1/notices")
                                .header(API_MINOR_VERSION_HEADER_NAME, 1)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(postNoticeRequest))
                                .with(user(createTestAdminDetails(loginMemberId)))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(expectedResult.getId()))
                .andExpect(jsonPath("$.title").value(expectedResult.getTitle()))
                .andExpect(jsonPath("$.content").value(expectedResult.getContent()))
                .andExpect(jsonPath("$.startAt").value(expectedResult.getStartAt().toString()))
                .andExpect(jsonPath("$.expiresAt").value(expectedResult.getExpiresAt().toString()));
        then(noticeCommandService).should().postNotice(eq(loginMemberId), any(PostNoticeRequest.class));
        verifyEveryMocksShouldHaveNoMoreInteractions();
    }

    @Test
    void 공지_정보가_주어지고_관리자가_아닌_일반_유저가_공지를_등록하면_403_에러가_발생한다() throws Exception {
        // given

        // when & then
        mvc.perform(
                        post("/api/v1/notices")
                                .header(API_MINOR_VERSION_HEADER_NAME, 1)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(createPostNoticeRequest()))
                                .with(user(createTestUserDetails(1L)))
                )
                .andExpect(status().isForbidden());
        verifyEveryMocksShouldHaveNoMoreInteractions();
    }

    @Test
    void 현재_게시중인_공지_목록을_조회한다() throws Exception {
        // given
        List<NoticeDto> expectedResult = List.of(createNoticeDto(1L));
        given(noticeQueryService.findActiveNotices(any(LocalDateTime.class))).willReturn(expectedResult);

        // when & then
        mvc.perform(
                        get("/api/v1/notices")
                                .header(API_MINOR_VERSION_HEADER_NAME, 1)
                                .with(user(createTestUserDetails(1L)))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.notices").isArray())
                .andExpect(jsonPath("$.notices", hasSize(expectedResult.size())));
        then(noticeQueryService).should().findActiveNotices(any(LocalDateTime.class));
        verifyEveryMocksShouldHaveNoMoreInteractions();
    }

    private void verifyEveryMocksShouldHaveNoMoreInteractions() {
        then(noticeCommandService).shouldHaveNoMoreInteractions();
        then(noticeQueryService).shouldHaveNoMoreInteractions();
    }

    private UserDetails createTestUserDetails(Long memberId) {
        return new UserPrincipal(createMemberDto(memberId, Set.of(USER)));
    }

    private UserDetails createTestAdminDetails(Long memberId) {
        return new UserPrincipal(createMemberDto(memberId, Set.of(USER, ADMIN)));
    }

    private MemberDto createMemberDto(Long memberId, Set<RoleType> roleTypes) {
        return new MemberDto(
                memberId,
                String.valueOf(memberId),
                roleTypes,
                String.valueOf(memberId)
        );
    }

    private static PostNoticeRequest createPostNoticeRequest() {
        return new PostNoticeRequest(
                NoticeType.NOTICE,
                "title",
                "contents...",
                LocalDateTime.of(2024, 1, 1, 1, 1, 1),
                LocalDateTime.of(2024, 1, 1, 1, 1, 1)
        );
    }

    private static NoticeDto createNoticeDto(Long noticeId) {
        return new NoticeDto(
                noticeId,
                "title",
                "contents...",
                LocalDateTime.of(2024, 1, 1, 1, 1, 1),
                LocalDateTime.of(2024, 1, 1, 1, 1, 1)
        );
    }
}