package org.week7assignment.messenger.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.week7assignment.messenger.dto.request.organization.OrganizationCreateDto;
import org.week7assignment.messenger.exception.ConflictException;
import org.week7assignment.messenger.exception.NotFoundException;
import org.week7assignment.messenger.exception.errorCode.ErrorCode;
import org.week7assignment.messenger.model.Organization;
import org.week7assignment.messenger.model.User;
import org.week7assignment.messenger.repository.OrganizationRepository;
import org.week7assignment.messenger.repository.UserRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final UserRepository userRepository;

    // 소속 생성
    public void createOrganization(OrganizationCreateDto organizationCreateDto) {

        // 소속 이름이 이미 존재하는지 검증
        validateOrganizationByName(organizationCreateDto.getName());

        Organization organization = Organization.builder()
                .name(organizationCreateDto.getName())
                .introduction(organizationCreateDto.getIntroduction())
                .build();
        organizationRepository.save(organization);
    }

    // 소속 가입
    public void joinOrganization(User user, UUID organizationId) {
        // 소속 존재 검증
        Organization organization = validOrganization(organizationId);

        // 소속에 이미 존재하는 유저인지 검증
        if (user.getOrganization() != null && user.getOrganization().equals(organization)) {
            throw new ConflictException(ErrorCode.USER_ALREADY_JOINED);
        }
        user.setOrganization(organization);
        userRepository.save(user);
    }

    // 소속 탈퇴
    public void exitOrganization(User user) {
        user.setOrganization(null);
        userRepository.save(user);
    }

    private Organization validOrganization(UUID organizationId) {
        return organizationRepository.findById(organizationId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.ORGANIZATION_NOT_FOUND));
    }

    private void validateOrganizationByName(String name) {
        if (organizationRepository.existsByName(name)) {
            throw new ConflictException(ErrorCode.ORGANIZATION_ALREADY_EXISTS);
        }
    }
}
