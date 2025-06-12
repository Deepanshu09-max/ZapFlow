package com.zapflow.primarybackend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = getJwtFromRequest(request);
            
            logger.debug("Processing request: {} {}", request.getMethod(), request.getRequestURI());
            logger.debug("Authorization header: {}", request.getHeader("Authorization"));
            logger.debug("Extracted JWT: {}", jwt != null ? jwt.substring(0, Math.min(jwt.length(), 20)) + "..." : "null");
            
            if (StringUtils.hasText(jwt) && jwtUtil.validateToken(jwt)) {
                Integer userId = jwtUtil.getUserIdFromToken(jwt);
                logger.debug("JWT validated successfully for userId: {}", userId);
                
                // Create authentication token
                UsernamePasswordAuthenticationToken authentication = 
                    new UsernamePasswordAuthenticationToken(userId, null, new ArrayList<>());
                
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                logger.debug("JWT validation failed or no JWT found");
            }
        } catch (Exception ex) {
            logger.error("Could not set user authentication in security context", ex);
        }
        
        filterChain.doFilter(request, response);
    }
    
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        
        if (StringUtils.hasText(bearerToken)) {
            // Check if it starts with "Bearer " and remove it
            if (bearerToken.startsWith("Bearer ")) {
                return bearerToken.substring(7);
            }
            // If it doesn't start with "Bearer ", assume it's the raw token
            return bearerToken;
        }
        return null;
    }
}
