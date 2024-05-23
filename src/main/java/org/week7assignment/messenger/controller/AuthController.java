package org.week7assignment.messenger.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.week7assignment.messenger.authentication.AuthenticationExtractor;
import org.week7assignment.messenger.authentication.jwt.JwtEncoder;
import org.week7assignment.messenger.dto.request.user.UserLoginDto;
import org.week7assignment.messenger.dto.request.user.UserSignInDto;
import org.week7assignment.messenger.dto.response.ResponseDto;
import org.week7assignment.messenger.dto.response.token.TokenResponseDto;
import org.week7assignment.messenger.dto.response.user.UserResponseData;
import org.week7assignment.messenger.service.UserService;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class AuthController {
    private final UserService userService;

    // 회원가입
    @PostMapping("/signin")
    public ResponseEntity<ResponseDto<UserResponseData>> signIn(@Valid @RequestBody UserSignInDto userSignInDto) {
        UserResponseData userResponseData = userService.signIn(userSignInDto);
        return new ResponseEntity<>(ResponseDto.success(HttpStatus.CREATED, "사용자 생성 완료", userResponseData), HttpStatus.CREATED);
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<ResponseDto<TokenResponseDto>> login(@Valid @RequestBody UserLoginDto userLoginDto,
                                                               HttpServletResponse response) {
        TokenResponseDto tokenResponseDto = userService.login(userLoginDto);

        String bearerToken = JwtEncoder.encodeJwtToken(tokenResponseDto.getAccessToken());
        log.info("컨트롤러 accessToken = {}", bearerToken);

        // 쿠키 생성
        addCookie(bearerToken, response);
        return new ResponseEntity<>(ResponseDto.success(HttpStatus.OK, "사용자 로그인 완료", tokenResponseDto), HttpStatus.OK);
    }

    public static void addCookie(String token, HttpServletResponse response) {
        Cookie cookie = new Cookie(AuthenticationExtractor.TOKEN_COOKIE_NAME, token);
        cookie.setPath("/"); // 모든 경로에서 쿠키 사용 가능
        cookie.setHttpOnly(true); // 쿠키는 HTTP 요청과 함께여야지만 전송 가능하게
        log.info("addCookie 메서드 cookie={}", cookie.getValue());

        response.addCookie(cookie);
    }

    // 로그아웃
    @GetMapping("/logout")
    public ResponseEntity<ResponseDto<Void>> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie(AuthenticationExtractor.TOKEN_COOKIE_NAME, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        return new ResponseEntity<>(ResponseDto.success(HttpStatus.OK, "사용자 로그아웃 완료"), HttpStatus.OK);
    }
}
