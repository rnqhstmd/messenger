package org.week7assignment.messenger.exception;


import org.week7assignment.messenger.exception.errorCode.ErrorCode;

public class NotFoundException extends CustomException {
    public NotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
