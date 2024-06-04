package org.week7assignment.messenger.dto.request.organization;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Getter
@AllArgsConstructor
public class OrganizationCreateDto {
    @NotNull(message = "소속 이름이 비어있습니다.")
    @Pattern(regexp = "^[가-힣a-z0-9-\\s_]{2,25}$", message = "소속 이름은 특수문자를 제외한 2~25자리여야 합니다.")
    private final String name;
    @NotNull(message = "소속 소개가 비어있습니다.")
    @Length(min = 2, max = 50, message = "소속 소개는 최소 2글자 이상, 50글자 이하로 작성해주세요.")
    private final String introduction;
}
