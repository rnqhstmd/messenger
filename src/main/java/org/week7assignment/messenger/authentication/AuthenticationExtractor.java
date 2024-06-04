package org.week7assignment.messenger.authentication;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.week7assignment.messenger.authentication.jwt.JwtEncoder;
import org.week7assignment.messenger.exception.UnauthorizedException;
import org.week7assignment.messenger.exception.errorCode.ErrorCode;

@Component
public class AuthenticationExtractor {

    public static final String TOKEN_COOKIE_NAME = "AccessToken";

    public static String extract(final HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (TOKEN_COOKIE_NAME.equals(cookie.getName())) {
                    return JwtEncoder.decodeJwtBearerToken(cookie.getValue());
                }
            }
        }
        throw new UnauthorizedException(ErrorCode.COOKIE_NOT_FOUND);
    }
}
