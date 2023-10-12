package com.momong.backend.domain.member.service;

import com.momong.backend.domain.member.dto.MemberDto;
import com.momong.backend.domain.member.entity.Member;
import com.momong.backend.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class MemberCommandService {

    private final MemberRepository memberRepository;

    public MemberDto createNewMember(String socialUid) {
        Member member = memberRepository.save(Member.createNewMember(socialUid));
        return MemberDto.from(member);
    }
}
