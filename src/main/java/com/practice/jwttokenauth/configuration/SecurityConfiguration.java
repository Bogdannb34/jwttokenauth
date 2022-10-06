package com.practice.jwttokenauth.configuration;

import com.practice.jwttokenauth.constants.Cors;
import com.practice.jwttokenauth.constants.Security;
import com.practice.jwttokenauth.security.jwt.JwtAccessDeniedHandler;
import com.practice.jwttokenauth.security.jwt.JwtAuthEntryPoint;
import com.practice.jwttokenauth.security.jwt.JwtAuthFilter;
import com.practice.jwttokenauth.services.user.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.Collections;

import static com.practice.jwttokenauth.constants.Security.PRIVATE_URLS;
import static com.practice.jwttokenauth.constants.Security.PUBLIC_URLS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Getter
public class SecurityConfiguration {

    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtAccessDeniedHandler accessDeniedHandler;
    private final JwtAuthEntryPoint authEntryPoint;
    private final JwtAuthFilter authFilter;

    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
        http
                .cors().and().csrf().disable()
                .exceptionHandling((exceptionHandling) ->
                        exceptionHandling
                                .accessDeniedHandler(getAccessDeniedHandler())
                                .authenticationEntryPoint(getAuthEntryPoint())
                        )
                .authorizeRequests((authorizeRequests) ->
                        authorizeRequests
                                .antMatchers(PUBLIC_URLS).permitAll()
                                .antMatchers(PRIVATE_URLS).hasRole("ADMIN")
                                .anyRequest().authenticated()
                        )
                .formLogin((formLogin) ->
                        formLogin
                                .loginPage("/login")
                                .usernameParameter("email")
                                .defaultSuccessUrl("/home")
                                .permitAll()
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(getAuthFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        final DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(getUserService());
        authProvider.setPasswordEncoder(getPasswordEncoder());

        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(final AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedOrigins(Collections.singletonList(Cors.ALLOWED_ORIGINS[0]));
        corsConfiguration.setAllowedHeaders(Arrays.asList(Cors.ALLOWED_HEADERS));
        corsConfiguration.setExposedHeaders(Arrays.asList(Cors.EXPOSED_HEADERS));
        corsConfiguration.setAllowedMethods(Arrays.asList(Cors.ALLOWED_METHODS));
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(urlBasedCorsConfigurationSource);
    }
}
