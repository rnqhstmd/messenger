package org.week7assignment.messenger.exception;


import org.week7assignment.messenger.exception.errorCode.ErrorCode;

public class UnauthorizedException extends CustomException {
    public UnauthorizedException(ErrorCode errorCode, String detail) {
        super(errorCode, detail);
    }
}
