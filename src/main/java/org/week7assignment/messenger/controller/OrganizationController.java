package org.week7assignment.messenger.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.week7assignment.messenger.authentication.annotation.AuthenticatedUser;
import org.week7assignment.messenger.authentication.annotation.OrganizationMember;
import org.week7assignment.messenger.dto.request.organization.OrganizationCreateDto;
import org.week7assignment.messenger.dto.response.ResponseDto;
import org.week7assignment.messenger.model.User;
import org.week7assignment.messenger.service.OrganizationService;

import java.util.UUID;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/organizations")
public class OrganizationController {

    private final OrganizationService organizationService;

    // 소속 생성
    @PostMapping
    public ResponseEntity<ResponseDto<Void>> createOrganization(@Valid @RequestBody OrganizationCreateDto organizationCreateDto) {
        organizationService.createOrganization(organizationCreateDto);
        return new ResponseEntity<>(ResponseDto.success(HttpStatus.CREATED, "소속 생성 완료"), HttpStatus.CREATED);
    }

    // 소속 가입
    @PostMapping("{id}/join")
    public ResponseEntity<ResponseDto<Void>> joinOrganization(@PathVariable("id") UUID organizationId,
                                                              @AuthenticatedUser User user) {
        organizationService.joinOrganization(user, organizationId);
        return new ResponseEntity<>(ResponseDto.success(HttpStatus.OK, "소속 가입 완료"), HttpStatus.OK);
    }

    // 소속 탈퇴
    @DeleteMapping("/exit")
    public ResponseEntity<ResponseDto<Void>> exitOrganization(@OrganizationMember User user) {
        organizationService.exitOrganization(user);
        return new ResponseEntity<>(ResponseDto.success(HttpStatus.OK, "소속 탈퇴 완료"), HttpStatus.OK);
    }
}
