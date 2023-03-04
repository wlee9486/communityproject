package com.zerobase.community_.member.service;

import com.zerobase.community_.member.model.MemberInput;
import com.zerobase.community_.member.model.ResetPasswordInput;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface MemberService extends UserDetailsService {
    boolean register(MemberInput memberInput);

    boolean emailAuth(String uuid);

    boolean sendResetPassword(ResetPasswordInput resetPasswordInput);

    boolean resetPassword(String uuid, String password);

    boolean checkResetPassword(String uuid);
}
