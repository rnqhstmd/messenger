package org.week7assignment.messenger.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.week7assignment.messenger.dto.response.ResponseDto;
import org.week7assignment.messenger.exception.*;
import org.week7assignment.messenger.exception.errorCode.ErrorCode;

@Slf4j
@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ResponseDto> handleConflictException(BadRequestException badRequestException) {
        writeLog(badRequestException);
        return res(badRequestException, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ResponseDto> handleConflictException(ConflictException conflictException) {
        writeLog(conflictException);
        return res(conflictException, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ResponseDto> handleNotFoundException(NotFoundException notFoundException) {
        writeLog(notFoundException);
        return res(notFoundException, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ResponseDto> handleUnauthorizedException(UnauthorizedException unauthorizedException) {
        writeLog(unauthorizedException);
        return res(unauthorizedException, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDto> handleException(Exception exception) {
        this.writeLog(exception);
        return res(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException methodArgumentNotValidException) {
        FieldError fieldError = methodArgumentNotValidException.getBindingResult().getFieldError();
        if (fieldError == null) {
            return res(methodArgumentNotValidException.getMessage(), HttpStatus.BAD_REQUEST);
        }

        ErrorCode errorCode = ErrorCode.dtoValidationErrorCode(fieldError.getCode());
        String detail = fieldError.getDefaultMessage();
        DtoValidationException dtoValidationException = new DtoValidationException(errorCode, detail);

        writeLog(dtoValidationException);

        return res(errorCode.getMessage(), HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<ResponseDto> res(CustomException customException, HttpStatus status) {
        return new ResponseEntity<>(ResponseDto.error(status, customException.getErrorCode().getMessage()), status);
    }

    private ResponseEntity<ResponseDto> res(String message, HttpStatus status) {
        return new ResponseEntity<>(ResponseDto.error(status, message), status);
    }

    private void writeLog(CustomException customException) {
        String exceptionName = customException.getClass().getSimpleName();
        ErrorCode errorCode = customException.getErrorCode();
        String detail = customException.getDetail();

        log.error("({}){}: {}", exceptionName, errorCode.getMessage(), detail);
    }

    private void writeLog(Exception exception) {
        log.error("({}){}", exception.getClass().getSimpleName(), exception.getMessage());
    }
}
