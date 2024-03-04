package com.momong.five_min_raid.unit.domain.notice.service;

import com.momong.five_min_raid.domain.notice.constant.NoticeType;
import com.momong.five_min_raid.domain.notice.dto.NoticeDto;
import com.momong.five_min_raid.domain.notice.entity.Notice;
import com.momong.five_min_raid.domain.notice.repository.NoticeRepository;
import com.momong.five_min_raid.domain.notice.service.NoticeQueryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Constructor;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@DisplayName("[Unit] Service(Query) - Notice")
@ExtendWith(MockitoExtension.class)
class NoticeQueryServiceTest {

    @InjectMocks
    private NoticeQueryService sut;

    @Mock
    private NoticeRepository noticeRepository;

    @DisplayName("시각이 주어지고, 해당 시각을 기준으로 게시중인 공지 목록을 조회한다.")
    @Test
    void givenTime_whenFindAllActiveNotices_thenReturnNotices() throws Exception {
        // given
        LocalDateTime time = LocalDateTime.now();
        List<Notice> expectedResult = List.of(createNotice(1L));
        given(noticeRepository.findAllByStartAtAfterAndExpiresAtBefore(time, time)).willReturn(expectedResult);

        // when
        List<NoticeDto> actualResult = sut.findActiveNotices(time);

        // then
        then(noticeRepository).should().findAllByStartAtAfterAndExpiresAtBefore(time, time);
        verifyEveryMocksShouldHaveNoMoreInteractions();
        assertThat(actualResult).hasSize(expectedResult.size());
    }

    private void verifyEveryMocksShouldHaveNoMoreInteractions() {
        then(noticeRepository).shouldHaveNoMoreInteractions();
    }

    private Notice createNotice(Long id, NoticeType type, String title, String content, LocalDateTime startedAt, LocalDateTime expiresAt) throws Exception {
        Constructor<Notice> noticeConstructor = Notice.class.getDeclaredConstructor(Long.class, NoticeType.class, String.class, String.class, LocalDateTime.class, LocalDateTime.class);
        noticeConstructor.setAccessible(true);
        return noticeConstructor.newInstance(id, type, title, content, startedAt, expiresAt);
    }

    private Notice createNotice(Long id) throws Exception {
        return createNotice(
                id,
                NoticeType.NOTICE,
                "title",
                "content",
                LocalDateTime.of(2024, 1, 1, 0, 0),
                LocalDateTime.of(2024, 12, 31, 0, 0)
        );
    }

}