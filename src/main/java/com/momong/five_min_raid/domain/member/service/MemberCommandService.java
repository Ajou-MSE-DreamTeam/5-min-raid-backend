package com.momong.five_min_raid.domain.member.service;

import com.momong.five_min_raid.domain.member.dto.MemberDto;
import com.momong.five_min_raid.domain.member.entity.Member;
import com.momong.five_min_raid.domain.member.exception.InvalidMemberNicknameException;
import com.momong.five_min_raid.domain.member.exception.MemberDeletionPermissionDeniedException;
import com.momong.five_min_raid.domain.member.exception.MemberNicknameDuplicationException;
import com.momong.five_min_raid.domain.member.exception.NicknameChangeCooldownException;
import com.momong.five_min_raid.domain.member.repository.MemberRepository;
import com.momong.five_min_raid.domain.member_game_record.service.MemberGameRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@RequiredArgsConstructor
@Transactional
@Service
public class MemberCommandService {

    private static final int NICKNAME_CHANGE_COOLDOWN_DAYS = 14;

    private final MemberQueryService memberQueryService;
    private final MemberGameRecordService memberGameRecordService;
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

        long daysSinceNicknameUpdate = ChronoUnit.DAYS.between(member.getNicknameLastUpdatedAt(), LocalDate.now());
        if (daysSinceNicknameUpdate < NICKNAME_CHANGE_COOLDOWN_DAYS) {
            throw new NicknameChangeCooldownException();
        }

        member.updateNickname(nickname);
        return MemberDto.from(member);
    }

    public void deleteMember(long requestUserId, long deleteMemberId) {
        checkMemberDeletionPermission(requestUserId, deleteMemberId);
        Member deleteMember = memberQueryService.getById(deleteMemberId);
        memberGameRecordService.removeMemberFromRecords(deleteMember);
        memberRepository.delete(deleteMember);
    }

    /**
     * <p>닉네임의 유효성을 검증한다.
     * <p>닉네임은 2글자 이상, 12글자 이하의 영문, 한글, 숫자로만 구성된다.
     *
     * @param nickname 유효한지 확인하고자 하는 닉네임
     * @throws InvalidMemberNicknameException 닉네임이 정책에 위반하는 경우.
     */
    private void validateNicknamePolicy(String nickname) {
        if (!nickname.matches("^[0-9a-zA-Zㄱ-ㅎ가-힣ぁ-んァ-ン一-龯]*$")
                || nickname.length() < 2
                || nickname.length() > 12) {
            throw new InvalidMemberNicknameException(nickname);
        }
    }

    private void checkMemberDeletionPermission(long requestUserId, long deleteMemberId) {
        if (requestUserId != deleteMemberId) {
            throw new MemberDeletionPermissionDeniedException();
        }
    }
}
