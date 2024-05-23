package org.week7assignment.messenger.authentication.argumentResolver;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.week7assignment.messenger.authentication.AuthenticationContext;
import org.week7assignment.messenger.authentication.annotation.OrganizationMember;
import org.week7assignment.messenger.exception.NotFoundException;
import org.week7assignment.messenger.exception.UnauthorizedException;
import org.week7assignment.messenger.exception.errorCode.ErrorCode;
import org.week7assignment.messenger.model.Organization;
import org.week7assignment.messenger.model.User;
import org.week7assignment.messenger.repository.OrganizationRepository;
@Slf4j
@Component
@RequiredArgsConstructor
public class OrganizationMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final AuthenticationContext authenticationContext;
    private final OrganizationRepository organizationRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(OrganizationMember.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        User user = authenticationContext.getPrincipal();
        log.info("Resolver user={}",user.getId());
        Organization organization = organizationRepository.findById(user.getOrganization().getId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.ORGANIZATION_NOT_FOUND));

        if (organization.getUsers()
                .stream()
                .anyMatch(u -> u.getId().equals(user.getId()))) {
            return user;
        }
        throw new UnauthorizedException(ErrorCode.UNAUTHORIZED_USER, "소속을 찾으려 했으나 찾을 수 없습니다.");
    }
}
