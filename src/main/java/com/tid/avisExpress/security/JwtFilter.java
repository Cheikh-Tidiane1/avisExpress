package com.tid.avisExpress.security;
import com.tid.avisExpress.model.Jwt;
import com.tid.avisExpress.services.UtilisateurService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.Objects;

@Service
public class JwtFilter extends OncePerRequestFilter {

    private final HandlerExceptionResolver handlerExceptionResolver;
    private final UtilisateurService utilisateurService;
    private final JwtService jwtService;

    @Autowired
    public JwtFilter(UtilisateurService utilisateurService,JwtService jwtService, HandlerExceptionResolver handlerExceptionResolver) {
        this.utilisateurService = utilisateurService;
        this.jwtService = jwtService;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        Jwt jwtBd = null;
        String token;
        String username = null;
        Boolean isTokenExpired = true;

        try{
        String authorization = request.getHeader("Authorization");
        if (authorization != null && authorization.startsWith("Bearer ")) {
            token = authorization.substring(7);
            jwtBd = this.jwtService.checkTokenValue(token);
            isTokenExpired = jwtService.isTokenExpired(token);
            username = jwtService.ExtractUsername(token);
        }

        if (!isTokenExpired && jwtBd.getUtilisateur().getEmail().equals(username) && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails = this.utilisateurService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
        }catch (Exception e){
            this.handlerExceptionResolver.resolveException(request, response, null, e);
        }
    }
}
