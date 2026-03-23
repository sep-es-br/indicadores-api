package br.gov.es.indicadores.config.security;

import br.gov.es.indicadores.exception.mensagens.MensagemErroRest;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException
    ) throws IOException, ServletException {
//        response.setHeader("Content-Type", "application/json");
//        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//        response.getWriter().write(ToStringBuilder.reflectionToString(new MensagemErroRest(HttpStatus.FORBIDDEN,
//                "Usuário sem permissão.", List.of("Recuso não permitido para o seu nível de usuário.")),
//                ToStringStyle.JSON_STYLE));

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        MensagemErroRest erro = new MensagemErroRest(
                HttpStatus.FORBIDDEN,
                "Usuário sem permissão.",
                List.of("Recurso não permitido para o seu nível de usuário.")
        );

        objectMapper.writeValue(response.getWriter(), erro);
    }
}
