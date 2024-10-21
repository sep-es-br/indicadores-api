package br.gov.es.indicadores.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.savedrequest.Enumerator;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTVerificationException;

import br.gov.es.indicadores.exception.mensagens.MensagemErroRest;
import br.gov.es.indicadores.service.TokenService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;


import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Component
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenService tokenService;

    @Value("${frontend.painel}")
    private String frontPainel;

    @Value("${frontend.admin}")
    private String frontAdmin;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getRequestURI().endsWith("/user-info")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = recoverToken(request);
        if (token != null) {
            try {
                String subject = tokenService.validarToken(token);

                List<String> roles = tokenService.getRoleFromToken(token); 

                String applicationName = recoverApplicationName(request);

                if (applicationName != null && applicationName.equals("PAINEL")) {
                
                if (!roles.contains("INDICADORES_ADMIN") && !roles.contains("PAINEL_INDICADORES")) {
                    sendErrorMessage(List.of("Acesso negado ao painel. Role inválida."), response);
                    return;
                }
            
            } else  {

                if (!roles.contains("INDICADORES_ADMIN")) {
                    sendErrorMessage(List.of("Acesso negado ao admin. Role inválida."), response);
                    return;
                }
            
            }

                SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + roles);

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        subject, null, List.of(authority));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (JWTVerificationException e) {
                var expiresAt = LocalDateTime.ofInstant(JWT.decode(token).getExpiresAt().toInstant(), ZoneOffset.of("-03:00"));
                List<String> erros = new ArrayList<>();
                erros.add("Por favor, faça o login novamente.");
                if (LocalDateTime.now().isAfter(expiresAt))
                    erros.add("Token expirado em " + expiresAt);
                    sendErrorMessage(erros, response);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

   private String recoverToken(HttpServletRequest request) {
       var authHeader = request.getHeader("Authorization");
       if (authHeader == null) return null;
       return authHeader.replace("Bearer ", "");
   }

   private String recoverApplicationName(HttpServletRequest request) {
    var authHeader = request.getHeader("Application");
    if (authHeader == null) return null;
    return authHeader;
    }
   

   private void sendErrorMessage(List<String> erros, HttpServletResponse response) throws IOException {
       String mensagem = ToStringBuilder.reflectionToString(new MensagemErroRest(UNAUTHORIZED, "Token Inválido", erros), ToStringStyle.JSON_STYLE);
       response.setHeader("Content-Type", "application/json");
       response.setStatus(UNAUTHORIZED.value());
       response.getWriter().write(mensagem);
   }
}
