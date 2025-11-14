package com.userService.security.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.userService.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            try {
                DecodedJWT decodedJWT = jwtUtils.validateToken(token);

                String userId = decodedJWT.getClaim("userId").asString();
                String role = jwtUtils.extractRole(decodedJWT);

                if (role == null || role.isEmpty()) {
                    role = "ROLE_USER";
                } else if (!role.startsWith("ROLE_")) {
                    role = "ROLE_" + role;
                }

                List<GrantedAuthority> authorities =
                        Collections.singletonList(new SimpleGrantedAuthority(role));

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userId, null, authorities);

                SecurityContextHolder.getContext().setAuthentication(authentication);

                log.debug("User authenticated successfully: userId={}, role={}", userId, role);

            } catch (Exception e) {
                SecurityContextHolder.clearContext();
                log.warn("JWT validation failed for request to {}: {}",
                        request.getRequestURI(), e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }
}