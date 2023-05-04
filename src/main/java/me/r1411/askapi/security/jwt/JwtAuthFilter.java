package me.r1411.askapi.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtUtils jwtUtils;

    @Autowired
    public JwtAuthFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        processRequest(request, response);
        filterChain.doFilter(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null)
            return;

        if (authHeader.isBlank() || !authHeader.startsWith("Bearer "))
            return;

        String token = authHeader.substring(7);

        if (!jwtUtils.isTokenValid(token)) {
            return;
        }

        Authentication authentication = jwtUtils.getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
