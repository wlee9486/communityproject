package com.zerobase.community_.member.repository;


import com.zerobase.community_.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
@Repository
@Transactional
public interface MemberRepository extends JpaRepository<Member, String> {
    Optional<Member> findByUserId(String userId);
    Optional<Member> findByEmailAuthKey(String emailAuthKey);
    Optional<Member> findByUserIdAndUserName(String userId, String userName);
    Optional<Member> findByPasswordResetKey(String resetPasswordKey);
}
