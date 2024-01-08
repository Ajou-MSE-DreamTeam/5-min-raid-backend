package com.momong.five_min_raid.domain.member.repository;

import com.momong.five_min_raid.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findBySocialUid(String socialUid);

    boolean existsByNickname(String nickname);
}
