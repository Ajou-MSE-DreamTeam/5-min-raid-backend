package com.momong.five_min_raid.unit.domain.member.service;

import com.momong.five_min_raid.domain.member.constant.RoleType;
import com.momong.five_min_raid.domain.member.dto.MemberDto;
import com.momong.five_min_raid.domain.member.entity.Member;
import com.momong.five_min_raid.domain.member.exception.InvalidMemberNicknameException;
import com.momong.five_min_raid.domain.member.exception.MemberNicknameDuplicationException;
import com.momong.five_min_raid.domain.member.repository.MemberRepository;
import com.momong.five_min_raid.domain.member.service.MemberCommandService;
import com.momong.five_min_raid.domain.member.service.MemberQueryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@DisplayName("[Unit] Service(Command) - Member")
@ExtendWith(MockitoExtension.class)
class MemberCommandServiceTest {

    @InjectMocks
    private MemberCommandService sut;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private MemberQueryService memberQueryService;

    @DisplayName("social uid가 주어지고, 주어진 social uid로 새로운 회원을 생성 후 저장하면, 저장된 회원 정보를 반환한다.")
    @Test
    void givenSocialUid_whenCreateNewMember_thenReturnSavedMemberDto() {
        // given
        long memberId = 1L;
        String socialUid = "social-uid";
        Member expectedResult = createMember(memberId, socialUid, Set.of(RoleType.USER), null);
        given(memberRepository.save(any(Member.class))).willReturn(expectedResult);

        // when
        MemberDto actualResult = sut.createNewMember(socialUid);

        // then
        then(memberRepository).should().save(any(Member.class));
        verifyEveryMocksShouldHaveNoMoreInteractions();
        assertThat(actualResult)
                .hasFieldOrPropertyWithValue("id", expectedResult.getId())
                .hasFieldOrPropertyWithValue("socialUid", expectedResult.getSocialUid())
                .hasFieldOrPropertyWithValue("roleTypes", Set.of(RoleType.USER))
                .hasFieldOrPropertyWithValue("nickname", null);
    }

    @DisplayName("닉네임이 주어지고, 회원 닉네임을 수정하면, 닉네임이 수정되고 수정된 회원 정보를 반환한다.")
    @Test
    void givenNickname_whenUpdateMemberNickname_thenUpdateAndReturnUpdatedMemberInfo() {
        // given
        long memberId = 1L;
        String nicknameForUpdate = "nickname";
        Member member = createMember(memberId, "social-uid", null);
        given(memberQueryService.getById(memberId)).willReturn(member);
        given(memberQueryService.existsByNickname(nicknameForUpdate)).willReturn(false);

        // when
        MemberDto updatedMember = sut.updateMemberNickname(memberId, nicknameForUpdate);

        // then
        then(memberQueryService).should().getById(memberId);
        then(memberQueryService).should().existsByNickname(nicknameForUpdate);
        verifyEveryMocksShouldHaveNoMoreInteractions();
        assertThat(updatedMember)
                .hasFieldOrPropertyWithValue("id", memberId)
                .hasFieldOrPropertyWithValue("nickname", nicknameForUpdate);
    }

    @DisplayName("특수문자가 포함된 닉네임이 주어지고, 회원 닉네임을 수정하면, 예외가 발생한다.")
    @Test
    void givenNicknameContainingSpecialCharacters_whenUpdateMemberNickname_thenThrowInvalidMemberNicknameException() {
        // given

        // when
        Throwable t = catchThrowable(() -> sut.updateMemberNickname(1L, "nickname!"));

        // then
        verifyEveryMocksShouldHaveNoMoreInteractions();
        assertThat(t).isInstanceOf(InvalidMemberNicknameException.class);
    }

    @DisplayName("제한된 글자수에 맞지 않는(두 글자 미만) 닉네임이 주어지고, 회원 닉네임을 수정하면, 예외가 발생한다.")
    @Test
    void givenNicknameOfLessThanTwoCharacters_whenUpdateMemberNickname_thenThrowInvalidMemberNicknameException() {
        // given

        // when
        Throwable t = catchThrowable(() -> sut.updateMemberNickname(1L, "1"));

        // then
        verifyEveryMocksShouldHaveNoMoreInteractions();
        assertThat(t).isInstanceOf(InvalidMemberNicknameException.class);
    }

    @DisplayName("제한된 글자수에 맞지 않는(12 글자 초과) 닉네임이 주어지고, 회원 닉네임을 수정하면, 예외가 발생한다.")
    @Test
    void givenNicknameOfGreaterThanTwoCharacters_whenUpdateMemberNickname_thenThrowInvalidMemberNicknameException() {
        // given

        // when
        Throwable t = catchThrowable(() -> sut.updateMemberNickname(1L, "123456789012345"));

        // then
        verifyEveryMocksShouldHaveNoMoreInteractions();
        assertThat(t).isInstanceOf(InvalidMemberNicknameException.class);
    }

    @DisplayName("이미 사용중인 닉네임이 주어지고, 회원 닉네임을 수정하면, 예외가 발생한다.")
    @Test
    void givenNicknameAlreadyUsed_whenUpdateMemberNickname_thenMemberNicknameDuplicationException() {
        // given
        String nicknameForUpdate = "nickname";
        given(memberQueryService.existsByNickname(nicknameForUpdate)).willReturn(true);

        // when
        Throwable t = catchThrowable(() -> sut.updateMemberNickname(1L, nicknameForUpdate));

        // then
        verifyEveryMocksShouldHaveNoMoreInteractions();
        assertThat(t).isInstanceOf(MemberNicknameDuplicationException.class);
    }

    private void verifyEveryMocksShouldHaveNoMoreInteractions() {
        then(memberRepository).shouldHaveNoMoreInteractions();
    }

    private Member createMember(Long memberId, String socialUid, String nickname) {
        return createMember(memberId, socialUid, Set.of(RoleType.USER), nickname, LocalDate.of(0, 1, 1));
    }

    private Member createMember(Long memberId, String socialUid, Set<RoleType> roleTypes, String nickname) {
        return new Member(memberId, socialUid, roleTypes, nickname, LocalDate.of(0, 1, 1));
    }

    private Member createMember(Long memberId, String socialUid, Set<RoleType> roleTypes, String nickname, LocalDate nicknameLastUpdatedAt) {
        return new Member(
                memberId,
                socialUid,
                roleTypes,
                nickname,
                nicknameLastUpdatedAt
        );
    }
}