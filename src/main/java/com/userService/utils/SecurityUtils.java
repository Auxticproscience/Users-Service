package com.userService.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.UUID;
import com.auth0.jwt.interfaces.DecodedJWT;

public class SecurityUtils {

    private static Authentication getAuth() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || auth.getPrincipal() == null) {
            throw new IllegalStateException("No hay autenticación en el contexto");
        }

        return auth;
    }

    public static UUID getCurrentUserId() {
        return UUID.fromString((String) getAuth().getPrincipal());
    }

    public static String getCurrentRole() {
        return getAuth().getAuthorities()
                .stream()
                .findFirst()
                .map(a -> a.getAuthority())
                .orElse(null);
    }

    public static String getToken() {
        Object details = getAuth().getDetails();
        return details != null ? details.toString() : null;
    }
}


