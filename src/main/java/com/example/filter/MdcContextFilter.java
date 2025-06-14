package com.example.filter;

import com.example.utils.MdcContextHolder;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class MdcContextFilter extends OncePerRequestFilter {

    @Value("${spring.application.name:unknown}")
    private String applicationName;

    @Value("${spring.profiles.active:unknown}")
    private String environment;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            Map<String, String> contextMap = new HashMap<>();
            contextMap.put(MdcContextHolder.REQUEST_ID, UUID.randomUUID().toString());
            contextMap.put(MdcContextHolder.SESSION_ID, request.getSession().getId());
            contextMap.put(MdcContextHolder.MODULE, applicationName);
            contextMap.put(MdcContextHolder.ENVIRONMENT, environment);

            String userId = request.getHeader("X-User-ID");
            if (userId != null && !userId.isEmpty()) {
                contextMap.put(MdcContextHolder.USER_ID, userId);
            } else {
                contextMap.put(MdcContextHolder.USER_ID, "anonymous");
            }

            MdcContextHolder.setMdcContext(contextMap);
            response.setHeader("X-Request-ID", contextMap.get(MdcContextHolder.REQUEST_ID));
            
            filterChain.doFilter(request, response);
        } finally {
            MdcContextHolder.clearMdcContext();
        }
    }
}
