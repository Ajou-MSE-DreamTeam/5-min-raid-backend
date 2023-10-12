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

    public Optional<MemberDto> findDtoBySocialUid(String socialUid) {
        return memberRepository.findBySocialUid(socialUid).map(MemberDto::from);
    }

    private Member getById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new MemberNotFoundByIdException(memberId));
    }

    public MemberDto getDtoById(Long memberId) {
        return MemberDto.from(getById(memberId));
    }
}
