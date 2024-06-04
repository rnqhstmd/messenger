package org.week7assignment.messenger.exception;


import org.week7assignment.messenger.exception.errorCode.ErrorCode;

public class ConflictException extends CustomException {
    public ConflictException(ErrorCode errorCode) {
        super(errorCode);
    }
}
