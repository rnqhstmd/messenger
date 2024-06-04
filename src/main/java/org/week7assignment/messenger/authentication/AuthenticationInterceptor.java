package org.week7assignment.messenger.authentication;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.week7assignment.messenger.authentication.jwt.JwtTokenProvider;
import org.week7assignment.messenger.exception.NotFoundException;
import org.week7assignment.messenger.exception.errorCode.ErrorCode;
import org.week7assignment.messenger.model.User;
import org.week7assignment.messenger.repository.UserRepository;

import java.util.UUID;

@Component
@AllArgsConstructor
public class AuthenticationInterceptor implements HandlerInterceptor {

    private final UserRepository userRepository;
    private final AuthenticationContext authenticationContext;
    private JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String accessToken = AuthenticationExtractor.extract(request);
        UUID userid = UUID.fromString(jwtTokenProvider.getPayload(accessToken));
        User user = getUserByUserid(userid);

        authenticationContext.setPrincipal(user);

        return true;
    }

    private User getUserByUserid(UUID userid) {
        return userRepository.findById(userid).orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
    }
}
