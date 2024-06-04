package org.week7assignment.messenger.authentication.jwt;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.week7assignment.messenger.exception.UnauthorizedException;
import org.week7assignment.messenger.exception.errorCode.ErrorCode;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenProvider {
    private final SecretKey key;
    private final long tokenValidMillisecond;

    public JwtTokenProvider(@Value("${security.jwt.token.secret-key}") final String key,
                            @Value("${security.jwt.token.expire-length}") final long tokenValidMillisecond) {
        this.key = Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));
        this.tokenValidMillisecond = tokenValidMillisecond;
    }

    public String createToken(final String payload) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + tokenValidMillisecond);

        return Jwts.builder()
                .setSubject(payload)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }

    public String getPayload(final String token) {
        try {
            String payload = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
            return payload;
        } catch (JwtException e) {
            throw new UnauthorizedException(ErrorCode.INVALID_TOKEN);
        }
    }
}
