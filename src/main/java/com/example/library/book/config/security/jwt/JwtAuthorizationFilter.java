package com.example.library.book.config.security.jwt;

import com.example.library.book.models.Client;
import com.example.library.book.services.users.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log
@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;
    private final CustomUserDetailsService userDetailsService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String token = getJwtFromRequest(request);
            if (StringUtils.hasText(token) && tokenProvider.validateToken(token)) {

                Long userId = tokenProvider.getUserIdFromJWT(token);

                Client user = userDetailsService.loadUserById(userId);

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user,
                        user.getRoles(), user.getAuthorities());
                // le vamos a pasar información detro del contexto: dirección remota, session ID, etc.
                authentication.setDetails(new WebAuthenticationDetails(request));
                // Guardamos este objeto autetificación en elcontexto de seguridad.
                SecurityContextHolder.getContext().setAuthentication(authentication);

            }
        } catch (Exception ex) {
            log.info("No se ha podido establecer la autenticación de cliente en el contexto de seguridad");
        }

        filterChain.doFilter(request, response);

    }


    private String getJwtFromRequest(HttpServletRequest request) {

        String bearerToken = request.getHeader(JwtTokenProvider.TOKEN_HEADER);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(JwtTokenProvider.TOKEN_PREFIX)) {
            return bearerToken.substring(JwtTokenProvider.TOKEN_PREFIX.length());
        }
        return null;
    }

}
