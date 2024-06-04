package org.week7assignment.messenger.authentication;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;
import org.week7assignment.messenger.model.User;

@Getter
@Setter
@RequestScope
@Component
public class AuthenticationContext {
    private User principal;
}
