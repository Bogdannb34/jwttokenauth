package com.practice.jwttokenauth.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.practice.jwttokenauth.security.CustomUserDetails;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import static com.practice.jwttokenauth.constants.Security.*;
import static java.util.Arrays.stream;

@Component
@Getter
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;

    public String generateJwtToken(final CustomUserDetails userPrincipal) {
        final String[] claims = getClaimsFromUser(userPrincipal);
        return JWT.create()
                .withIssuer(VERIFICATION)
                .withIssuedAt(new Date())
                .withSubject(userPrincipal.getUsername())
                .withArrayClaim(ROLES, claims)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(Algorithm.HMAC256(getJwtSecret().getBytes()));
    }

    public Authentication getAuthentication(final String email, final Collection<GrantedAuthority> authorities, final HttpServletRequest request) {
        final UsernamePasswordAuthenticationToken userPassAuthToken = new UsernamePasswordAuthenticationToken(email, null, authorities);
        userPassAuthToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return userPassAuthToken;
    }

    public Collection<GrantedAuthority> getAuthorities(final String token) {
        final String[] claims = getClaimsFromToken(token);
        return stream(claims).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    public boolean isTokenValid(final String email, final String token) {
        final JWTVerifier verifier = getJWTVerifier();
        return StringUtils.isNoneEmpty(email) && !isTokenExpired(verifier, token);
    }

    public String getSubject(final String token) {
        final JWTVerifier verifier = getJWTVerifier();
        return verifier.verify(token).getSubject();
    }

    private boolean isTokenExpired(final JWTVerifier verifier, final String token) {
        final Date expiration = verifier.verify(token).getExpiresAt();
        return expiration.before(new Date());
    }

    private String[] getClaimsFromUser(final CustomUserDetails userPrincipal) {
        final Collection<String> authorities = new ArrayList<>();
        for (GrantedAuthority grantedAuthority : userPrincipal.getAuthorities()) {
            authorities.add(grantedAuthority.getAuthority());
        }
        return authorities.toArray(new String[0]);
    }

    private String[] getClaimsFromToken(final String token) {
        final JWTVerifier verifier = getJWTVerifier();
        return verifier.verify(token).getClaim(ROLES).asArray(String.class);
    }

    private JWTVerifier getJWTVerifier() {
        final JWTVerifier verifier;
        try {
            final Algorithm algorithm = Algorithm.HMAC512(getJwtSecret());
            verifier = JWT.require(algorithm).withIssuer(VERIFICATION).build();
        } catch (JWTVerificationException ex) {
            throw new JWTVerificationException(TOKEN_CANNOT_BE_VERIFIED);
        }
        return verifier;
    }
}
