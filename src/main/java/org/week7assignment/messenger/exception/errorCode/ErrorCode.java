package org.week7assignment.messenger.exception.errorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // NotFoundException
    USER_NOT_FOUND("4040", "유저를 찾을 수 없습니다."),
    ORGANIZATION_NOT_FOUND("4043", "소속을 찾을 수 없습니다."),
    MESSAGE_NOT_FOUND("4044","메세지를 찾을 수 없습니다."),

    // ConflictException
    USER_ALREADY_EXISTS("4090", "이미 존재하는 유저입니다."),
    DUPLICATED_EMAIL("4091","존재하는 이메일입니다."),
    MESSAGE_ALREADY_READ("4092", "이미 읽은 메세지입니다."),
    ORGANIZATION_ALREADY_EXISTS("4093", "이미 존재하는 조직입니다."),

    // UnauthorizedException
    UNAUTHORIZED_USER("4010","승인되지 않은 유저입니다."),
    INVALID_PASSWORD("4011", "비밀번호가 일치하지 않습니다."),
    INVALID_TOKEN("4012", "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN("4013", "만료된 토큰입니다"),
    TOKEN_NOT_FOUND("4014", "토큰을 찾을 수 없습니다."),
    COOKIE_NOT_FOUND("4015","쿠키를 찾을 수 없습니다."),

    // ValidationException
    NOT_NULL("9001", "필수값이 누락되었습니다."),
    NOT_BLANK("9002", "필수값이 빈 값이거나 공백으로 되어있습니다."),
    REGEX("9003", "형식에 맞지 않습니다."),
    LENGTH("9004", "길이가 유효하지 않습니다."),
    EMAIL("9005", "이메일 형식이 올바르지 않습니다.");

    private final String code;
    private final String message;

    public static ErrorCode dtoValidationErrorCode(String code) {
        return switch (code) {
            case "NOTNULL" -> NOT_NULL;
            case "NotBlank" -> NOT_BLANK;
            case "Pattern" -> REGEX;
            case "Length" -> LENGTH;
            case "Email" -> EMAIL;
            default -> throw new IllegalStateException("Unexpected value: " + code);
        };
    }
}
