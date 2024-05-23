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

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ResponseDto> handleConflictException(ConflictException conflictException) {
        writeLog(conflictException);
        return new ResponseEntity<>(ResponseDto.error(HttpStatus.CONFLICT,
                conflictException.getErrorCode().getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ResponseDto> handleNotFoundException(NotFoundException notFoundException) {
        writeLog(notFoundException);
        return new ResponseEntity<>(ResponseDto.error(HttpStatus.NOT_FOUND,
                notFoundException.getErrorCode().getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ResponseDto> handleUnauthorizedException(UnauthorizedException unauthorizedException) {
        writeLog(unauthorizedException);
        return new ResponseEntity<>(ResponseDto.error(HttpStatus.UNAUTHORIZED,
                unauthorizedException.getErrorCode().getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDto> handleException(Exception exception) {
        this.writeLog(exception);
        return new ResponseEntity<>(ResponseDto.error(HttpStatus.INTERNAL_SERVER_ERROR,
                exception.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException methodArgumentNotValidException) {
        FieldError fieldError = methodArgumentNotValidException.getBindingResult().getFieldError();
        if (fieldError == null) {
            return new ResponseEntity<>(ResponseDto.error(HttpStatus.BAD_REQUEST, methodArgumentNotValidException.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }

        ErrorCode errorCode = ErrorCode.dtoValidationErrorCode(fieldError.getCode());
        String detail = fieldError.getDefaultMessage();
        DtoValidationException dtoValidationException = new DtoValidationException(errorCode, detail);

        writeLog(dtoValidationException);

        return new ResponseEntity<>(ResponseDto.error(HttpStatus.BAD_REQUEST,
                errorCode.getMessage() +"***"+ detail), HttpStatus.BAD_REQUEST);
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
