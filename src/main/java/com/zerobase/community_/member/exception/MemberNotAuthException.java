package com.zerobase.community_.member.exception;

public class MemberNotAuthException extends RuntimeException {
    public MemberNotAuthException(String error) {
        super(error);
    }
}
