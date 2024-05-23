package org.week7assignment.messenger.dto.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Getter
@AllArgsConstructor
public class UserSignInDto {

    @NotNull(message = "사용자 이메일이 비어있습니다.")
    @Email(message = "이메일 형식이어야합니다.")
    private String email;
    @NotNull(message = "사용자 이름이 비어있습니다.")
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9-_]{2,10}$", message = "사용자 이름은 특수문자를 제외한 2~10자리여야 합니다.")
    private String name;
    @NotNull(message = "사용자 비밀번호가 비어있습니다.")
    @Length(min = 8, max = 20, message = "사용자 비밀번호는 최소 8글자 이상, 최대 20글자 이하로 작성해주세요.")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "사용자 비밀번호는 알파벳 대소문자(a~z, A~Z), 숫자(0~9)만 입력 가능합니다.")
    private String password;
}
