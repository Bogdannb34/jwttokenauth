package com.practice.jwttokenauth.security.jwt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

import static com.practice.jwttokenauth.constants.Security.OPTIONS_HTTP_METHOD;
import static com.practice.jwttokenauth.constants.Security.TOKEN_PREFIX;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
@RequiredArgsConstructor
@Getter
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain filterChain) throws ServletException, IOException {
        try {
            if (request.getMethod().equals(OPTIONS_HTTP_METHOD)) {
                response.setStatus(OK.value());
            } else {
                final String authHeader = request.getHeader(AUTHORIZATION);
                if (authHeader == null || !authHeader.startsWith(TOKEN_PREFIX)) {
                    filterChain.doFilter(request, response);
                    return;
                }
                final String token = authHeader.substring(TOKEN_PREFIX.length());
                final String email = getTokenProvider().getSubject(token);
                if (getTokenProvider().isTokenValid(email, token) &&
                        SecurityContextHolder.getContext().getAuthentication() == null) {
                    final Collection<GrantedAuthority> authorities = getTokenProvider().getAuthorities(token);
                    final Authentication authentication = getTokenProvider().getAuthentication(email, authorities, request);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    SecurityContextHolder.clearContext();
                }
            }
        }catch (Exception e) {
            log.error("Cannot set user authentication: {}", e.getMessage());
        }
        filterChain.doFilter(request, response);
    }
}
