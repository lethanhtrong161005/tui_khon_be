package com.tuikhon.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuikhon.constant.AppConstant;
import com.tuikhon.constant.MessageConstant;
import com.tuikhon.dto.response.ApiResponse;
import com.tuikhon.entity.UserEntity;
import com.tuikhon.repository.UserRepository;
import com.tuikhon.service.RedisService;
import com.tuikhon.util.ResponseUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Filter to extract Bearer JWT token, verify against JTI-based Redis blacklist, and authenticate requests.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    /**
     * JWT provider component.
     */
    private final JwtProvider jwtProvider;

    /**
     * User persistence repository.
     */
    private final UserRepository userRepository;

    /**
     * Global Redis service wrapper.
     */
    private final RedisService redisService;

    /**
     * JSON object mapper component.
     */
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String jwt = parseJwt(request);
            if (Objects.nonNull(jwt) && jwtProvider.validateToken(jwt)) {
                String jti = jwtProvider.getJtiFromToken(jwt);

                // 1. Check if token's JTI is blacklisted in Redis
                if (Objects.nonNull(jti) && redisService.hasKey(AppConstant.BLACKLIST_JTI_PREFIX + jti)) {
                    log.warn("Rejected blacklisted access token JTI: {}", jti);
                    sendUnauthorizedError(response, request, MessageConstant.TOKEN_REVOKED);
                    return;
                }

                // 2. Set Security Context
                UUID userId = jwtProvider.getUserIdFromToken(jwt);
                UserEntity user = userRepository.findById(userId).orElse(null);

                if (Objects.nonNull(user) && !Boolean.TRUE.equals(user.getIsDeleted())) {
                    String roleName = Objects.nonNull(user.getRole()) ? user.getRole().name() : null;

                    List<SimpleGrantedAuthority> authorities = Objects.nonNull(roleName)
                            ? Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + roleName))
                            : Collections.emptyList();

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(user, null, authorities);
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (Exception e) {
            log.error("Cannot set user authentication: {}", e.getMessage(), e);
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Parses Bearer token string from HTTP request Authorization header.
     *
     * @param request HttpServletRequest instance.
     * @return JWT token string or null if absent.
     */
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }

    /**
     * Writes standardized JSON 401 Unauthorized response to client output stream.
     *
     * @param response HttpServletResponse instance.
     * @param request  HttpServletRequest instance.
     * @param message  Error message.
     * @throws IOException If writing to output stream fails.
     */
    private void sendUnauthorizedError(
            HttpServletResponse response, HttpServletRequest request, String message)
            throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        ResponseEntity<ApiResponse<Void>> errorResponse = ResponseUtils.error(
                HttpStatus.UNAUTHORIZED,
                message,
                request.getRequestURI()
        );
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse.getBody()));
    }
}
