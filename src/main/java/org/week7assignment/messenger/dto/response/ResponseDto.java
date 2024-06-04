package org.week7assignment.messenger.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class ResponseDto<T> {

    private HttpStatus status;
    private String message;
    private T data;

    // 성공 응답용 생성자
    public static <T> ResponseDto<T> success(final HttpStatus status, final String message) {
        return new ResponseDto<>(status, message, null);
    }

    // 성공 응답용 생성자 (메시지 포함)
    public static <T> ResponseDto<T> success(final HttpStatus status, final String message, final T data) {
        return new ResponseDto<>(status, message, data);
    }

    // 에러 응답용 생성자
    public static <T> ResponseDto<T> error(HttpStatus status, String message) {
        return new ResponseDto<>(status, message, null);
    }
}
