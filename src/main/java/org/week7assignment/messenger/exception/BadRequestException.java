package org.week7assignment.messenger.exception;

import org.week7assignment.messenger.exception.errorCode.ErrorCode;

public class BadRequestException extends CustomException{
    public BadRequestException(ErrorCode errorCode) {
        super(errorCode);
    }
}
