package com.tuikhon.config;

import java.util.Optional;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Component providing auditor information for JPA Entity Auditing fields.
 */
@Component
public class AuditorAwareImpl implements AuditorAware<String> {

    /**
     * Retrieves the current authenticated auditor username or defaults to SYSTEM.
     *
     * @return Optional containing the auditor username string
     */
    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {
            return Optional.of("SYSTEM");
        }

        return Optional.ofNullable(authentication.getName());
    }
}
