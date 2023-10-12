package com.momong.backend.unit.domain.member.service;

import com.momong.backend.domain.member.constant.RoleType;
import com.momong.backend.domain.member.dto.MemberDto;
import com.momong.backend.domain.member.entity.Member;
import com.momong.backend.domain.member.repository.MemberRepository;
import com.momong.backend.domain.member.service.MemberCommandService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
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

    private void verifyEveryMocksShouldHaveNoMoreInteractions() {
        then(memberRepository).shouldHaveNoMoreInteractions();
    }

    private Member createMember(Long memberId, String socialUid, Set<RoleType> roleTypes, String nickname) {
        return new Member(
                memberId,
                socialUid,
                roleTypes,
                nickname,
                LocalDateTime.of(2023, 1, 1, 0, 0),
                LocalDateTime.of(2023, 1, 1, 0, 0)
        );
    }
}