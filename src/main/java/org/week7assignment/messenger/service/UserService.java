package org.week7assignment.messenger.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.week7assignment.messenger.authentication.PasswordHashEncryption;
import org.week7assignment.messenger.authentication.jwt.JwtTokenProvider;
import org.week7assignment.messenger.dto.request.user.UserLoginDto;
import org.week7assignment.messenger.dto.request.user.UserSignInDto;
import org.week7assignment.messenger.dto.request.user.UserUpdateDto;
import org.week7assignment.messenger.dto.response.token.TokenResponseDto;
import org.week7assignment.messenger.dto.response.user.UserResponseData;
import org.week7assignment.messenger.exception.ConflictException;
import org.week7assignment.messenger.exception.NotFoundException;
import org.week7assignment.messenger.exception.UnauthorizedException;
import org.week7assignment.messenger.exception.errorCode.ErrorCode;
import org.week7assignment.messenger.model.User;
import org.week7assignment.messenger.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordHashEncryption passwordHashEncryption;
    private final JwtTokenProvider jwtTokenProvider;

    // 회원가입
    public UserResponseData signIn(UserSignInDto userSignInDto) {
        // 존재하는 이메일인지 검증
        validateUserByEmail(userSignInDto.getEmail());

        String plainPassword = userSignInDto.getPassword();
        String encryptPassword = passwordHashEncryption.encrypt(plainPassword);

        User user = User.builder()
                .email(userSignInDto.getEmail())
                .password(encryptPassword)
                .name(userSignInDto.getName())
                .build();
        userRepository.save(user);

        return new UserResponseData(user);
    }

    // 로그인
    public TokenResponseDto login(UserLoginDto userLoginDto) {
        // 이메일로 유저 객체 가져오기
        User user = getUserByEmail(userLoginDto.getEmail());

        // 비밀번호 검증
        String plainPassword = userLoginDto.getPassword();
        if (!passwordHashEncryption.matches(plainPassword, user.getPassword())) {
            throw new UnauthorizedException(ErrorCode.INVALID_PASSWORD);
        }

        String token = jwtTokenProvider.createToken(String.valueOf(user.getId()));

        return new TokenResponseDto(token);
    }

    // 회원 정보 수정
    public UserResponseData updateUserInfo(User user, UserUpdateDto userUpdateDto) {
        // 수정된 이메일 중복 검사
        validateUserByEmail(userUpdateDto.getEmail());

        user.setEmail(userUpdateDto.getEmail());
        user.setName(user.getName());

        userRepository.save(user);

        return new UserResponseData(user);
    }

    // 회원 탈퇴
    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    private void validateUserByEmail(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new ConflictException(ErrorCode.DUPLICATED_EMAIL);
        }
    }

    private User getUserByEmail(String  email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
    }
}
