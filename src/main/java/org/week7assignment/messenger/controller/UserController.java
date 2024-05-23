package org.week7assignment.messenger.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.week7assignment.messenger.authentication.annotation.AuthenticatedUser;
import org.week7assignment.messenger.dto.request.user.UserUpdateDto;
import org.week7assignment.messenger.dto.response.ResponseDto;
import org.week7assignment.messenger.model.User;
import org.week7assignment.messenger.service.UserService;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    // 사용자 정보 수정
    @PatchMapping
    public ResponseEntity<ResponseDto<Void>> updateUserInfo(@AuthenticatedUser User user,
                                                        @Valid @RequestBody UserUpdateDto userUpdateDto) {
        userService.updateUserInfo(user, userUpdateDto);
        return new ResponseEntity<>(ResponseDto.success(HttpStatus.OK, "사용자 수정 완료"), HttpStatus.OK);
    }

    // 사용자 삭제
    @DeleteMapping
    public ResponseEntity<ResponseDto<Void>> deleteUser(@AuthenticatedUser User user) {
        userService.deleteUser(user);
        return new ResponseEntity<>(ResponseDto.success(HttpStatus.OK, "사용자 삭제 완료"), HttpStatus.OK);
    }
}
