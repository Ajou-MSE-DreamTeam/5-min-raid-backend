package com.momong.backend.unit.domain.member.service;

import com.momong.backend.domain.member.constant.RoleType;
import com.momong.backend.domain.member.dto.MemberDto;
import com.momong.backend.domain.member.entity.Member;
import com.momong.backend.domain.member.repository.MemberRepository;
import com.momong.backend.domain.member.service.MemberQueryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
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

    private void verifyEveryMocksShouldHaveNoMoreInteractions() {
        then(memberRepository).shouldHaveNoMoreInteractions();
    }

    private Member createMember(Long memberId, String socialUid) {
        return new Member(
                memberId,
                socialUid,
                Set.of(RoleType.USER),
                socialUid,
                LocalDateTime.of(2023, 1, 1, 0, 0),
                LocalDateTime.of(2023, 1, 1, 0, 0)
        );
    }
}