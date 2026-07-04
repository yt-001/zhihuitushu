package com.yt.zhihuitushu.filter;

import com.yt.zhihuitushu.entity.LoginUser;
import com.yt.zhihuitushu.service.TokenService;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Resource
    private TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String token = tokenService.getTokenFromCookie(request);
        
        if (token != null) {
            LoginUser loginUser = tokenService.parseToken(token);
            
            if (loginUser != null) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    loginUser,
                    null,
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + loginUser.getRole()))
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
                request.setAttribute("loginUser", loginUser);
            }
        }
        
        filterChain.doFilter(request, response);
    }
}