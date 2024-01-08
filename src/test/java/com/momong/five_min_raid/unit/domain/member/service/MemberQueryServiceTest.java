package com.momong.five_min_raid.unit.domain.member.service;

import com.momong.five_min_raid.domain.member.constant.RoleType;
import com.momong.five_min_raid.domain.member.dto.MemberDto;
import com.momong.five_min_raid.domain.member.entity.Member;
import com.momong.five_min_raid.domain.member.exception.MemberNotFoundByIdException;
import com.momong.five_min_raid.domain.member.repository.MemberRepository;
import com.momong.five_min_raid.domain.member.service.MemberQueryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@DisplayName("[Unit] Service(Query) - Member")
@ExtendWith(MockitoExtension.class)
class MemberQueryServiceTest {

    @InjectMocks
    private MemberQueryService sut;

    @Mock
    private MemberRepository memberRepository;

    @DisplayName("회원의 social uid가 주어지고, 주어진 social uid로 회원의 dto를 조회하면, 조회된 member dto를 반환한다.")
    @Test
    void givenSocialUid_whenFindMemberDtoWithSocialUid_thenReturnMemberDto() {
        // given
        String socialUid = "social-uid";
        Member expectedResult = createMember(1L, socialUid);
        given(memberRepository.findBySocialUid(socialUid)).willReturn(Optional.of(expectedResult));

        // when
        Optional<MemberDto> actualResult = sut.findDtoBySocialUid(socialUid);

        // then
        then(memberRepository).should().findBySocialUid(socialUid);
        verifyEveryMocksShouldHaveNoMoreInteractions();
        assertThat(actualResult).isNotEmpty();
        assertThat(actualResult.get())
                .hasFieldOrPropertyWithValue("id", expectedResult.getId())
                .hasFieldOrPropertyWithValue("socialUid", expectedResult.getSocialUid());
    }

    @DisplayName("회원의 id가 주어지고, 주어진 id로 회원 entity를 조회하면, 조회된 entity를 반환한다.")
    @Test
    void givenMemberId_whenGetMemberEntity_thenReturnMemberEntity() {
        // given
        long memberId = 1L;
        Member expectedResult = createMember(memberId);
        given(memberRepository.findById(memberId)).willReturn(Optional.of(expectedResult));

        // when
        Member actualResult = sut.getById(memberId);

        // then
        then(memberRepository).should().findById(memberId);
        verifyEveryMocksShouldHaveNoMoreInteractions();
        assertThat(actualResult)
                .hasFieldOrPropertyWithValue("id", expectedResult.getId())
                .hasFieldOrPropertyWithValue("nickname", expectedResult.getNickname());
    }

    @DisplayName("존재하지 않는 회원의 id가 주어지고, 주어진 id로 회원 entity를 조회하면, 조회된 entity를 반환한다.")
    @Test
    void givenNotExistentMemberId_whenGetMemberEntity_thenThrowMemberNotFoundByIdException() {
        // given
        long memberId = 1L;
        given(memberRepository.findById(memberId)).willReturn(Optional.empty());

        // when
        Throwable t = catchThrowable(() -> sut.getById(memberId));

        // then
        then(memberRepository).should().findById(memberId);
        verifyEveryMocksShouldHaveNoMoreInteractions();
        assertThat(t).isInstanceOf(MemberNotFoundByIdException.class);
    }

    @DisplayName("회원의 id가 주어지고, 주어진 id로 회원 dto를 조회하면, 조회된 member dto를 반환한다.")
    @Test
    void givenSocialUid_whenGetMemberDtoWithSocialUid_thenReturnMemberDto() {
        // given
        long memberId = 1L;
        Member expectedResult = createMember(memberId, "social-uid");
        given(memberRepository.findById(memberId)).willReturn(Optional.of(expectedResult));

        // when
        MemberDto actualResult = sut.getDtoById(memberId);

        // then
        then(memberRepository).should().findById(memberId);
        verifyEveryMocksShouldHaveNoMoreInteractions();
        assertThat(actualResult)
                .hasFieldOrPropertyWithValue("id", expectedResult.getId())
                .hasFieldOrPropertyWithValue("socialUid", expectedResult.getSocialUid());
    }

    @DisplayName("닉네임이 주어지고, 주어진 닉네임으로 회원 존재 여부를 조회하면, 조회 결과를 반환한다.")
    @Test
    void givenNickname_whenGetExistenceOfMember_thenReturnExistence() {
        // given
        String nickname = "nickname";
        boolean expectedResult = false;
        given(memberRepository.existsByNickname(nickname)).willReturn(expectedResult);

        // when
        boolean actualResult = sut.existsByNickname(nickname);

        // then
        then(memberRepository).should().existsByNickname(nickname);
        verifyEveryMocksShouldHaveNoMoreInteractions();
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    private void verifyEveryMocksShouldHaveNoMoreInteractions() {
        then(memberRepository).shouldHaveNoMoreInteractions();
    }

    private Member createMember(Long memberId) {
        return createMember(memberId, String.valueOf(memberId));
    }

    private Member createMember(Long memberId, String socialUid) {
        return new Member(
                memberId,
                socialUid,
                Set.of(RoleType.USER),
                socialUid
        );
    }
}