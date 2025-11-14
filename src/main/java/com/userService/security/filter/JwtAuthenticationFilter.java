package com.userService.security.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.userService.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;

    public JwtAuthenticationFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        String authHeader = request.getHeader("Authorization");

        logger.info("========================================");
        logger.info("JWT Filter - Processing: " + requestURI);
        logger.info("Authorization header present: " + (authHeader != null));

        if (authHeader != null) {
            logger.info("Authorization header value: " + authHeader.substring(0, Math.min(30, authHeader.length())) + "...");
        }

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            logger.info("Token extracted, length: " + token.length());

            try {
                DecodedJWT decodedJWT = jwtUtils.validateToken(token);

                String userId = decodedJWT.getClaim("userId").asString();
                String role = jwtUtils.extractRole(decodedJWT);

                logger.info("✅ Token VALID - userId: " + userId + ", role: " + role);

                if (role == null || role.isEmpty()) {
                    role = "ROLE_USER";
                } else if (!role.startsWith("ROLE_")) {
                    role = "ROLE_" + role;
                }

                List<GrantedAuthority> authorities =
                        Collections.singletonList(new SimpleGrantedAuthority(role));

                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(userId, null, authorities);

                SecurityContextHolder.getContext().setAuthentication(auth);

                logger.info("✅ Authentication set in SecurityContext");

            } catch (Exception e) {
                SecurityContextHolder.clearContext();
                logger.error("❌ Token validation FAILED: " + e.getMessage());
                logger.error("Exception type: " + e.getClass().getName());
            }
        } else {
            logger.warn("⚠️ No valid Authorization header found");
        }

        logger.info("Continuing filter chain...");
        logger.info("========================================");

        filterChain.doFilter(request, response);
    }
}