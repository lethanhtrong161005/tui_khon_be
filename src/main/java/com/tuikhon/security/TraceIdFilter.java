package com.tuikhon.security;

import com.tuikhon.constant.AppConstant;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Filter generating or forwarding MDC trace IDs for distributed request tracking.
 */
@Component
public class TraceIdFilter extends OncePerRequestFilter {

    /**
     * Intercepts HTTP requests to inject trace ID into MDC logging context and response headers.
     *
     * @param request     current HttpServletRequest
     * @param response    current HttpServletResponse
     * @param filterChain filter chain instance
     * @throws ServletException if servlet execution error occurs
     * @throws IOException      if input/output error occurs
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String traceId = request.getHeader(AppConstant.TRACE_ID_HEADER);
            if (Objects.isNull(traceId) || traceId.isBlank()) {
                traceId = UUID.randomUUID().toString();
            }

            MDC.put(AppConstant.TRACE_ID_KEY, traceId);
            response.setHeader(AppConstant.TRACE_ID_HEADER, traceId);

            filterChain.doFilter(request, response);
        } finally {
            MDC.remove(AppConstant.TRACE_ID_KEY);
        }
    }
}
