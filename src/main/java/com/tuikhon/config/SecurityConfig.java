package com.tuikhon.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuikhon.constant.AppConstant;
import com.tuikhon.constant.MessageConstant;
import com.tuikhon.dto.response.ApiResponse;
import com.tuikhon.security.JwtAuthenticationFilter;
import com.tuikhon.security.TraceIdFilter;
import com.tuikhon.util.ResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security configuration for stateless JWT authentication and public endpoint permissions.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    /**
     * JWT authentication filter component.
     */
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * Trace ID MDC filter component.
     */
    private final TraceIdFilter traceIdFilter;

    /**
     * JSON object mapper component.
     */
    private final ObjectMapper objectMapper;

    /**
     * Password encoder bean utilizing BCrypt algorithm.
     *
     * @return BCryptPasswordEncoder instance.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Authentication manager bean.
     *
     * @param config Spring AuthenticationConfiguration.
     * @return AuthenticationManager instance.
     * @throws Exception If authentication manager configuration fails.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Configures SecurityFilterChain with stateless session policy and public endpoints.
     *
     * @param http HttpSecurity configuration builder.
     * @return Configured SecurityFilterChain.
     * @throws Exception If security configuration fails.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(customAuthenticationEntryPoint())
                        .accessDeniedHandler(customAccessDeniedHandler())
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(AppConstant.PUBLIC_ENDPOINTS).permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(traceIdFilter, JwtAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Custom authentication entry point for 401 Unauthorized responses.
     *
     * @return AuthenticationEntryPoint lambda handler.
     */
    @Bean
    public AuthenticationEntryPoint customAuthenticationEntryPoint() {
        return (request, response, authException) -> {
            if (!response.isCommitted()) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.setCharacterEncoding("UTF-8");

                ResponseEntity<ApiResponse<Void>> errorResponse = ResponseUtils.error(
                        HttpStatus.UNAUTHORIZED,
                        MessageConstant.UNAUTHORIZED_ACCESS,
                        request.getRequestURI()
                );
                response.getWriter().write(objectMapper.writeValueAsString(errorResponse.getBody()));
            }
        };
    }

    /**
     * Custom access denied handler for 403 Forbidden responses.
     *
     * @return AccessDeniedHandler lambda handler.
     */
    @Bean
    public AccessDeniedHandler customAccessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            if (!response.isCommitted()) {
                response.setStatus(HttpStatus.FORBIDDEN.value());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.setCharacterEncoding("UTF-8");

                ResponseEntity<ApiResponse<Void>> errorResponse = ResponseUtils.error(
                        HttpStatus.FORBIDDEN,
                        MessageConstant.ACCESS_DENIED,
                        request.getRequestURI()
                );
                response.getWriter().write(objectMapper.writeValueAsString(errorResponse.getBody()));
            }
        };
    }
}
