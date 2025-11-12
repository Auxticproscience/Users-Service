package com.userService.security.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.userService.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
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

        String header = request.getHeader("Authorization");

        System.out.println("========================================");
        System.out.println("REQUEST: " + request.getMethod() + " " + request.getRequestURI());
        System.out.println("Authorization header: " + (header != null ? "SI" : "NO"));

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);

            try {
                DecodedJWT decodedJWT = jwtUtils.validateToken(token);

                String userId = decodedJWT.getClaim("userId").asString();
                String role = jwtUtils.extractRole(decodedJWT);

                System.out.println("✅ Token válido");
                System.out.println("User ID: " + userId);
                System.out.println("Rol original del JWT: " + role);

                if (role == null || role.isEmpty()) {
                    role = "ROLE_USER";
                    System.out.println("⚠️ No había rol, usando: " + role);
                } else if (!role.startsWith("ROLE_")) {
                    role = "ROLE_" + role;
                    System.out.println("✅ Rol con prefijo: " + role);
                }

                List<GrantedAuthority> authorities =
                        Collections.singletonList(new SimpleGrantedAuthority(role));

                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(userId, null, authorities);

                SecurityContextHolder.getContext().setAuthentication(auth);

                System.out.println("✅ Autenticación configurada con rol: " + role);
                System.out.println("========================================");

            } catch (Exception e) {
                System.out.println("❌ Error validando token: " + e.getMessage());
                System.out.println("========================================");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        } else {
            System.out.println("⚠️ No hay token Bearer");
            System.out.println("========================================");
        }

        filterChain.doFilter(request, response);
    }
}