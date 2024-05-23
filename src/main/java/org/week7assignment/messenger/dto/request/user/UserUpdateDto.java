package org.week7assignment.messenger.dto.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserUpdateDto {
    @NotNull(message = "새로운 사용자 이메일이 비어있습니다.")
    @Email(message = "이메일 형식이어야합니다.")
    private String email;
    @NotNull(message = "새로운 사용자 이름이 비어있습니다.")
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9-_]{2,10}$", message = "사용자 이름은 특수문자를 제외한 2~10자리여야 합니다.")
    private String name;
}
