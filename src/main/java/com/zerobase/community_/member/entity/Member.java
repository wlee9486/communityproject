package com.zerobase.community_.member.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member implements MemberStatus {
    @Id
    private String userId;

    private String userName;
    private String password;
    private String phone;
    private LocalDateTime registeredAt;

    private boolean emailAuthYn;
    private String emailAuthKey;
    private LocalDateTime emailAuthAt;

    private String passwordResetKey;
    private LocalDateTime passwordResetTimeLimit;

    private boolean adminYn;

    private String userStatus;
}
