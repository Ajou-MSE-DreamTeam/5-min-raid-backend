package com.momong.backend.domain.member.service;

import com.momong.backend.domain.member.dto.MemberDto;
import com.momong.backend.domain.member.entity.Member;
import com.momong.backend.domain.member.exception.MemberNotFoundByIdException;
import com.momong.backend.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberQueryService {

    private final MemberRepository memberRepository;

    /**
     * 회원의 social uid 값을 통해 optional member dto를 조회한다.
     *
     * @param socialUid 조회하고자 하는 회원의 social uid
     * @return 조회된 회원 정보가 담긴 optional dto
     */
    public Optional<MemberDto> findDtoBySocialUid(String socialUid) {
        return memberRepository.findBySocialUid(socialUid).map(MemberDto::from);
    }

    /**
     * 회원의 id 값을 통해 member entity를 조회한다.
     *
     * @param memberId 조회하고자 하는 회원의 id
     * @return 조회된 member entity
     */
    public Member getById(long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new MemberNotFoundByIdException(memberId));
    }

    /**
     * 회원의 id 값을 통해 member dto를 조회한다.
     *
     * @param memberId 조회하고자 하는 회원의 id
     * @return 조회된 회원 정보가 담긴 dto
     */
    public MemberDto getDtoById(long memberId) {
        return MemberDto.from(getById(memberId));
    }

    public boolean existsByNickname(String nickname) {
        return memberRepository.existsByNickname(nickname);
    }
}
