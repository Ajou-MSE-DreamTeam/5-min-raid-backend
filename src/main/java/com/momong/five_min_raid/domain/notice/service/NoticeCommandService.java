package com.momong.five_min_raid.domain.notice.service;

import com.momong.five_min_raid.domain.member.entity.Member;
import com.momong.five_min_raid.domain.member.service.MemberService;
import com.momong.five_min_raid.domain.notice.dto.NoticeDto;
import com.momong.five_min_raid.domain.notice.dto.request.PostNoticeRequest;
import com.momong.five_min_raid.domain.notice.entity.Notice;
import com.momong.five_min_raid.domain.notice.repository.NoticeRepository;
import com.momong.five_min_raid.global.common.exception.NotAdminException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class NoticeCommandService {

    private final MemberService memberService;
    private final NoticeRepository noticeRepository;

    /**
     * 공지 등록하기
     *
     * @param memberId          id of api consumer
     * @param postNoticeRequest 등록할 공지 정보가 담긴 request dto
     * @return 등록된 공지 정보가 담긴 dto
     * @throws NotAdminException 공지를 등록하는 회원이 관리자(admin)이 아닌 경우
     */
    public NoticeDto postNotice(Long memberId, PostNoticeRequest postNoticeRequest) {
        Member member = memberService.getById(memberId);
        if (!member.isAdmin()) {
            throw new NotAdminException();
        }

        Notice noticePosted = noticeRepository.save(postNoticeRequest.toEntity());
        return NoticeDto.from(noticePosted);
    }
}
