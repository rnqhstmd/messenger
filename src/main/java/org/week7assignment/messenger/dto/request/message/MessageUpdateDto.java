package org.week7assignment.messenger.dto.request.message;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Getter
@AllArgsConstructor
public class MessageUpdateDto {
    @NotNull(message = "수정될 메세지 제목이 비어있습니다.")
    @Length(min = 2, max = 20, message = "수정될 메세지 제목은 최소 2글자 이상, 20글자 이하로 작성해주세요.")
    private final String title;
    @NotNull(message = "수정될 메세지가 비어있습니다.")
    @Length(min = 1, max = 100, message = "수정될 메세지는 최소 1글자 이상, 100글자 이하로 작성해주세요.")
    private final String content;
}
