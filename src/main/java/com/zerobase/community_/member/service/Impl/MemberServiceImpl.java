package com.zerobase.community_.member.service.Impl;

import com.zerobase.community_.components.MailComponents;
import com.zerobase.community_.member.entity.Member;
import com.zerobase.community_.member.entity.MemberStatus;
import com.zerobase.community_.member.exception.MemberNotAuthException;
import com.zerobase.community_.member.exception.MemberSuspendedException;
import com.zerobase.community_.member.model.MemberInput;
import com.zerobase.community_.member.model.ResetPasswordInput;
import com.zerobase.community_.member.repository.MemberRepository;
import com.zerobase.community_.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final MailComponents mailComponents;
    @Override
    public boolean register(MemberInput memberInput) {
        Optional<Member> optionalMember = memberRepository.findById(memberInput.getUserId());
        if (optionalMember.isPresent()) {
            return false;
        }

        String uuid = UUID.randomUUID().toString();
        String encPassword = BCrypt.hashpw(memberInput.getPassword(), BCrypt.gensalt());

        Member member = Member.builder()
                .userId(memberInput.getUserId())
                .userName(memberInput.getUserName())
                .password(encPassword)
                .phone(memberInput.getPhone())
                .registeredAt(LocalDateTime.now())
                .emailAuthYn(false)
                .emailAuthKey(uuid)
                .userStatus(Member.MEMBER_INACTIVE)
                .build();
        memberRepository.save(member);

        String email = memberInput.getUserId();
        String subject = "community 사이트에 가입을 축하드립니다.";
        String text = "<p> community 사이트에 가입을 축하드립니다. </p> <p> 아래 링크를 클릭하셔서 가입을 완료하세요. </p>" + "<div> <a href = 'http://localhost:8080/member/email-auth?id=" + uuid + "'> 가입 완료 </a></div>";
        mailComponents.sendMail(email, subject, text);

        return true;
    }

    @Override
    public boolean emailAuth(String uuid) {
        Optional<Member> optionalMember = memberRepository.findByEmailAuthKey(uuid);
        if (!optionalMember.isPresent()) {
            return false;
        }

        Member member = optionalMember.get();

        if (member.isEmailAuthYn()) {
            return false;
        }

        member.setUserStatus(MemberStatus.MEMBER_ACTIVE);
        member.setEmailAuthYn(true);
        member.setEmailAuthAt(LocalDateTime.now());
        memberRepository.save(member);

        return true;
    }

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        Optional<Member> optionalMember = memberRepository.findById(userId);
        if (!optionalMember.isPresent()) {
            throw new UsernameNotFoundException("회원 정보가 존재하지 않습니다.");
        }

        Member member = optionalMember.get();

        if (Member.MEMBER_INACTIVE.equals(member.getUserStatus())) {
            throw new MemberNotAuthException("이메일 활성화 이후에 로그인을 해주세요.");
        }

        if (Member.MEMBER_SUSPENDED.equals(member.getUserStatus())) {
            throw new MemberSuspendedException("정지된 회원입니다.");
        }

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        if (member.isAdminYn()) {
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }

        return new User(member.getUserId(), member.getPassword(), grantedAuthorities);
    }

    @Override
    public boolean sendResetPassword(ResetPasswordInput resetPasswordInput) {
        Optional<Member> optionalMember = memberRepository.findByUserIdAndUserName(resetPasswordInput.getUserId(), resetPasswordInput.getUserName());
        if (!optionalMember.isPresent()) {
            throw new UsernameNotFoundException("회원 정보가 존재하지 않습니다.");
        }

        Member member = optionalMember.get();
        String uuid = UUID.randomUUID().toString();

        member.setPasswordResetKey(uuid);
        member.setPasswordResetTimeLimit(LocalDateTime.now().plusDays(1));
        memberRepository.save(member);

        String email = resetPasswordInput.getUserId();
        String subject = "[community] 비밀번호 초기화 메일입니다.";
        String text = "<p> community 비밀번호 초기화 메일입니다. </p> <p> 아래 링크를 클릭하셔서 비밀번호를 초기화 해주세요. </p>" + "<div> <a href = 'http://localhost:8080/member/reset/password?id=" + uuid + "'> 비밀번호 초기화 링크 </a></div>";
        mailComponents.sendMail(email, subject, text);

        return true;
    }

    @Override
    public boolean resetPassword(String uuid, String password) {
        Optional<Member> optionalMember = memberRepository.findByPasswordResetKey(uuid);
        if (!optionalMember.isPresent()) {
            throw new UsernameNotFoundException("회원 정보가 존재하지 않습니다.");
        }

        Member member = optionalMember.get();

        if (member.getPasswordResetTimeLimit() == null) {
            throw new RuntimeException("유효한 비밀 번호 변경 기간이 아닙니다.");
        }

        if (member.getPasswordResetTimeLimit().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("유효한 비밀 번호 변경 기간이 아닙니다.");
        }

        String encPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        member.setPassword(encPassword);
        member.setPasswordResetKey("");
        member.setPasswordResetTimeLimit(null);
        memberRepository.save(member);

        return true;
    }

    @Override
    public boolean checkResetPassword(String uuid) {
        Optional<Member> optionalMember = memberRepository.findByPasswordResetKey(uuid);
        if (!optionalMember.isPresent()) {
            return false;
        }

        Member member = optionalMember.get();

        if (member.getPasswordResetTimeLimit() == null) {
            throw new RuntimeException("유효한 비밀 번호 변경 기간이 아닙니다.");
        }

        if (member.getPasswordResetTimeLimit().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("유효한 비밀 번호 변경 기간이 아닙니다.");
        }
        return true;
    }
}
