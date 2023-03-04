package com.zerobase.community_.member.exception;

public class MemberSuspendedException extends RuntimeException {
    public MemberSuspendedException(String error) {
        super(error);
    }
}
