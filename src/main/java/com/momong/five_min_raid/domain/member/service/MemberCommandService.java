package com.momong.five_min_raid.domain.member.service;

import com.momong.five_min_raid.domain.member.dto.MemberDto;
import com.momong.five_min_raid.domain.member.entity.Member;
import com.momong.five_min_raid.domain.member.exception.InvalidMemberNicknameException;
import com.momong.five_min_raid.domain.member.exception.MemberNicknameDuplicationException;
import com.momong.five_min_raid.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class MemberCommandService {

    private final MemberQueryService memberQueryService;
    private final MemberRepository memberRepository;

    /**
     * 신규 회원을 생성한 후 저장한다.
     *
     * @param socialUid 신규 회원의 social uid
     * @return 생성된 신규 회원 정보가 담긴 dto
     */
    public MemberDto createNewMember(String socialUid) {
        Member member = memberRepository.save(Member.createNewMember(socialUid));
        return MemberDto.from(member);
    }

    /**
     * 회원의 닉네임을 수정한다.
     *
     * @param memberId 닉네임을 수정하고자 하는 회원의 id
     * @param nickname 수정고하자 하는 닉네임
     * @return 변경된 회원 정보가 담긴 dto
     */
    public MemberDto updateMemberNickname(long memberId, String nickname) {
        validateNicknamePolicy(nickname);

        if (memberQueryService.existsByNickname(nickname)) {
            throw new MemberNicknameDuplicationException(nickname);
        }

        Member member = memberQueryService.getById(memberId);
        member.setNickname(nickname);
        return MemberDto.from(member);
    }

    /**
     * <p>닉네임의 유효성을 검증한다.
     * <p>닉네임은 2글자 이상, 12글자 이하의 영문, 한글, 숫자로만 구성된다.
     *
     * @param nickname 유효한지 확인하고자 하는 닉네임
     * @throws InvalidMemberNicknameException 닉네임이 정책에 위반하는 경우.
     */
    private void validateNicknamePolicy(String nickname) {
        if (!nickname.matches("^[0-9a-zA-Zㄱ-ㅎ가-힣]*$")
            || nickname.length() < 2
            || nickname.length() > 12) {
            throw new InvalidMemberNicknameException(nickname);
        }
    }
}
