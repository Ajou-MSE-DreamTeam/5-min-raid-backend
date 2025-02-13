package com.momong.five_min_raid.domain.member.service;

import com.momong.five_min_raid.domain.member.constant.NicknameUnavailableReason;
import com.momong.five_min_raid.domain.member.dto.MemberDto;
import com.momong.five_min_raid.domain.member.dto.NicknameAvailability;
import com.momong.five_min_raid.domain.member.entity.Member;
import com.momong.five_min_raid.domain.member.exception.*;
import com.momong.five_min_raid.domain.member.repository.MemberRepository;
import com.momong.five_min_raid.domain.member_game_record.service.MemberGameRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class MemberService {

    private static final int NICKNAME_CHANGE_COOLDOWN_DAYS = 14;

    private final MemberGameRecordService memberGameRecordService;
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

    /**
     * 닉네임 이용 가능 여부를 조회한다.
     *
     * @param reqUser  요청한 유저
     * @param nickname 이용 가능 여부를 확인할 닉네임
     * @return 닉네임 이용 가능 여부와 (만약 이용 불가능한 경우) 이용 불가능한 이유
     */
    public NicknameAvailability checkNicknameAvailability(MemberDto reqUser, String nickname) {
        if (isNicknamePolicyViolated(nickname)) {
            return new NicknameAvailability(false, NicknameUnavailableReason.POLICY_VIOLATION);
        }

        if (existsByNickname(nickname)) {
            return new NicknameAvailability(false, NicknameUnavailableReason.DUPLICATE);
        }

        if (reqUser != null) {
            long daysSinceNicknameUpdate = ChronoUnit.DAYS.between(reqUser.getNicknameLastUpdatedAt(), LocalDate.now());
            if (daysSinceNicknameUpdate < NICKNAME_CHANGE_COOLDOWN_DAYS) {
                return new NicknameAvailability(false, NicknameUnavailableReason.CHANGE_DELAY_NOT_OVER);
            }
        }

        return new NicknameAvailability(true, null);
    }

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
        if (isNicknamePolicyViolated(nickname)) {
            throw new InvalidMemberNicknameException(nickname);
        }

        if (existsByNickname(nickname)) {
            throw new MemberNicknameDuplicationException(nickname);
        }

        Member member = getById(memberId);
        long daysSinceNicknameUpdate = ChronoUnit.DAYS.between(member.getNicknameLastUpdatedAt(), LocalDate.now());
        if (daysSinceNicknameUpdate < NICKNAME_CHANGE_COOLDOWN_DAYS) {
            throw new NicknameChangeCooldownException();
        }

        member.updateNickname(nickname);
        return MemberDto.from(member);
    }

    public void deleteMember(long requestUserId, long deleteMemberId) {
        checkMemberDeletionPermission(requestUserId, deleteMemberId);
        Member deleteMember = getById(deleteMemberId);
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
    private boolean isNicknamePolicyViolated(String nickname) {
        return !nickname.matches("^[0-9a-zA-Zㄱ-ㅎ가-힣ぁ-んァ-ン一-龯]*$")
                || nickname.length() < 2
                || nickname.length() > 12;
    }

    private void checkMemberDeletionPermission(long requestUserId, long deleteMemberId) {
        if (requestUserId != deleteMemberId) {
            throw new MemberDeletionPermissionDeniedException();
        }
    }
}
