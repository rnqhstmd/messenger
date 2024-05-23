package org.week7assignment.messenger.authentication.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.week7assignment.messenger.authentication.AuthenticationInterceptor;
import org.week7assignment.messenger.authentication.argumentResolver.AuthenticatedUserArgumentResolver;
import org.week7assignment.messenger.authentication.argumentResolver.OrganizationMemberArgumentResolver;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class AuthenticationConfig implements WebMvcConfigurer {
    private final AuthenticationInterceptor authenticationInterceptor;
    private final AuthenticatedUserArgumentResolver authenticatedUserArgumentResolver;
    private final OrganizationMemberArgumentResolver organizationMemberArgumentResolver;

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor)
                .addPathPatterns("/users") // 유저 수정, 삭제 시
                .addPathPatterns("/messages/**") // 모든 메세지 기능 사용시
                .addPathPatterns("/organizations/**") // 모든 소속 기능 사용시
                .excludePathPatterns("/organizations"); // 소속 생성시는 제외
    }

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authenticatedUserArgumentResolver);
        resolvers.add(organizationMemberArgumentResolver);
    }
}
