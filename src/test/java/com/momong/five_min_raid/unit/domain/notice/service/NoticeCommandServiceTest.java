package com.momong.five_min_raid.unit.domain.notice.service;

import com.momong.five_min_raid.domain.member.constant.RoleType;
import com.momong.five_min_raid.domain.member.entity.Member;
import com.momong.five_min_raid.domain.member.service.MemberQueryService;
import com.momong.five_min_raid.domain.notice.constant.NoticeType;
import com.momong.five_min_raid.domain.notice.dto.NoticeDto;
import com.momong.five_min_raid.domain.notice.dto.request.PostNoticeRequest;
import com.momong.five_min_raid.domain.notice.entity.Notice;
import com.momong.five_min_raid.domain.notice.repository.NoticeRepository;
import com.momong.five_min_raid.domain.notice.service.NoticeCommandService;
import com.momong.five_min_raid.global.common.exception.NotAdminException;
import jakarta.validation.constraints.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Constructor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@DisplayName("[Unit] Service(command) - Notice")
@ExtendWith(MockitoExtension.class)
class NoticeCommandServiceTest {

    @InjectMocks
    private NoticeCommandService sut;

    @Mock
    private MemberQueryService memberQueryService;

    @Mock
    private NoticeRepository noticeRepository;

    @Test
    void 공지를_등록한다() throws Exception {
        // given
        long memberId = 1L;
        long postedNoticeId = 2L;
        PostNoticeRequest postNoticeRequest = createPostNoticeRequest();
        Notice expectedResult = createNotice(postedNoticeId, postNoticeRequest);
        given(memberQueryService.getById(memberId)).willReturn(createAdmin(memberId));
        given(noticeRepository.save(any(Notice.class))).willReturn(expectedResult);

        // when
        NoticeDto actualResult = sut.postNotice(memberId, postNoticeRequest);

        // then
        then(memberQueryService).should().getById(memberId);
        then(noticeRepository).should().save(any(Notice.class));
        verifyEveryMocksShouldHaveNoMoreInteractions();
        assertThat(actualResult)
                .hasFieldOrPropertyWithValue("id", expectedResult.getId())
                .hasFieldOrPropertyWithValue("title", expectedResult.getTitle())
                .hasFieldOrPropertyWithValue("content", expectedResult.getContent())
                .hasFieldOrPropertyWithValue("startAt", expectedResult.getStartAt())
                .hasFieldOrPropertyWithValue("expiresAt", expectedResult.getExpiresAt());
    }

    @Test
    void 관리자가_아닌_회원이_공지를_등록하면_예외가_발생한다() {
        // given
        long memberId = 1L;
        PostNoticeRequest postNoticeRequest = createPostNoticeRequest();
        given(memberQueryService.getById(memberId)).willReturn(createUser(memberId));

        // when
        Throwable t = catchThrowable(() -> sut.postNotice(memberId, postNoticeRequest));

        // then
        then(memberQueryService).should().getById(memberId);
        verifyEveryMocksShouldHaveNoMoreInteractions();
        assertThat(t).isInstanceOf(NotAdminException.class);
    }

    private void verifyEveryMocksShouldHaveNoMoreInteractions() {
        then(memberQueryService).shouldHaveNoMoreInteractions();
        then(noticeRepository).shouldHaveNoMoreInteractions();
    }

    private Member createUser(Long memberId) {
        return createMember(memberId, String.valueOf(memberId), Set.of(RoleType.USER), String.valueOf(memberId), LocalDate.of(0, 1, 1));
    }

    private Member createAdmin(Long memberId) {
        return createMember(memberId, String.valueOf(memberId), Set.of(RoleType.USER, RoleType.ADMIN), String.valueOf(memberId), LocalDate.of(0, 1, 1));
    }

    private Member createMember(Long memberId, String socialUid, Set<RoleType> roleTypes, String nickname, @NotNull LocalDate nicknameLastUpdatedAt) {
        return new Member(
                memberId,
                socialUid,
                roleTypes,
                nickname,
                nicknameLastUpdatedAt
        );
    }

    private Notice createNotice(Long id, PostNoticeRequest postNoticeRequest) throws Exception {
        return createNotice(
                id,
                postNoticeRequest.getType(),
                postNoticeRequest.getTitle(),
                postNoticeRequest.getContent(),
                postNoticeRequest.getStartAt(),
                postNoticeRequest.getExpiresAt()
        );
    }

    private Notice createNotice(Long id, NoticeType type, String title, String content, LocalDateTime startedAt, LocalDateTime expiresAt) throws Exception {
        Constructor<Notice> noticeConstructor = Notice.class.getDeclaredConstructor(Long.class, NoticeType.class, String.class, String.class, LocalDateTime.class, LocalDateTime.class);
        noticeConstructor.setAccessible(true);
        return noticeConstructor.newInstance(id, type, title, content, startedAt, expiresAt);
    }

    private PostNoticeRequest createPostNoticeRequest() {
        return new PostNoticeRequest(
                NoticeType.NOTICE,
                "title",
                "content",
                LocalDateTime.of(2024, 1, 1, 0, 0),
                LocalDateTime.of(2024, 12, 31, 0, 0)
        );
    }
}