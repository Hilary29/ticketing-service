package com.neero.ticketing_service.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class SecurityUtils {

    @Value("${app.security.user-id-claim:sub}")
    private String userIdClaim;

    @Value("${app.security.roles-claim:roles}")
    private String rolesClaim;

    /**
     * Extract external user ID from JWT token
     */
    public Optional<String> getCurrentUserExternalId() {
        return getCurrentJwt()
                .map(jwt -> jwt.getClaimAsString(userIdClaim));
    }

    /**
     * Extract user roles from JWT token
     */
    public List<String> getCurrentUserRoles() {
        return getCurrentJwt()
                .map(jwt -> jwt.getClaimAsStringList(rolesClaim))
                .orElse(List.of());
    }

    /**
     * Extract user email from JWT token
     */
    public Optional<String> getCurrentUserEmail() {
        return getCurrentJwt()
                .map(jwt -> jwt.getClaimAsString("email"));
    }

    /**
     * Extract public user ID from JWT token (for superapp integration)
     */
    public Optional<String> getCurrentUserPublicId() {
        return getCurrentJwt()
                .map(jwt -> jwt.getClaimAsString("publicUserId"));
    }

    /**
     * Check if current user has a specific role
     */
    public boolean hasRole(String role) {
        return getCurrentUserRoles().contains(role) || 
               getCurrentUserRoles().contains("ROLE_" + role);
    }

    /**
     * Check if current user is admin
     */
    public boolean isAdmin() {
        return hasRole("ADMIN");
    }

    /**
     * Get current JWT token
     */
    private Optional<Jwt> getCurrentJwt() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
            return Optional.of(jwt);
        }
        return Optional.empty();
    }

    /**
     * Check if user is authenticated
     */
    public boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated();
    }
}