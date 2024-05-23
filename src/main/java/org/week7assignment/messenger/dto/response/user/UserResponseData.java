package org.week7assignment.messenger.dto.response.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.week7assignment.messenger.model.User;

@Getter
@AllArgsConstructor
public class UserResponseData {
    private String email;
    private String name;

    public UserResponseData(User user) {
        this.email = user.getEmail();
        this.name = user.getName();
    }
}
