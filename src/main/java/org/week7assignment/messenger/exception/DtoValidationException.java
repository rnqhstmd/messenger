package org.week7assignment.messenger.exception;


import org.week7assignment.messenger.exception.errorCode.ErrorCode;

public class DtoValidationException extends CustomException {
    public DtoValidationException(ErrorCode errorCode, String detail) {
        super(errorCode, detail);
    }
}
