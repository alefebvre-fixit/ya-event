package com.ya.yaevent.security;

import com.ya.yaevent.config.Constants;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

/**
 * Implementation of AuditorAware based on Spring Security.
 */
@Component
public class SpringSecurityAuditorAware implements AuditorAware<String> {

    @Override
    public String getCurrentAuditor() {
        String userName = SecurityUtils.getCurrentUsername();
        return (userName != null ? userName : Constants.SYSTEM_ACCOUNT);
    }
}
