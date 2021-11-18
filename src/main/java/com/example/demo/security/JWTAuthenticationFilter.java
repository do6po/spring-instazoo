package com.example.demo.security;

import com.example.demo.entities.User;
import com.example.demo.services.CustomerUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

public class JWTAuthenticationFilter extends OncePerRequestFilter {
    public Logger logger = LoggerFactory.getLogger(JWTAuthenticationFilter.class);

    @Autowired
    private Environment env;

    @Autowired
    private JWTTokenProvider jwtTokenProvider;

    @Autowired
    private CustomerUserDetailsService customerUserDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            String jwt = getJWTFromRequest(request);
            if (!jwtTokenProvider.validateToken(jwt)) {
                throw new Exception("Token not valid!");
            }

            Long userId = jwtTokenProvider.getUserIdFromToken(jwt);
            User userDetails = customerUserDetailsService.loadUserById(userId);

            var authenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    Collections.emptyList()
            );

            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        } catch (Throwable e) {
            logger.error(e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    private String getJWTFromRequest(HttpServletRequest request) throws Exception {
        String bearerToken = request.getHeader(env.getProperty("app.jwt.header-token-name"));
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(env.getProperty("app.jwt.header-token-word"))) {
            return bearerToken.replace("Bearer ", "");
        }

        throw new Exception("Get token error!");
    }
}
