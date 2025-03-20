package br.gov.es.indicadores.controller;

import br.gov.es.indicadores.dto.UsuarioDto;
import br.gov.es.indicadores.service.AutenticacaoService;
import br.gov.es.indicadores.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Base64;
import java.util.Map;


@CrossOrigin(origins = { "${frontend.painel}", "${frontend.admin}" })
@RestController
@RequestMapping("/signin")
@RequiredArgsConstructor
public class AutenticacaoController {

    @Value("${frontend.painel}")
    private String frontPainel;

    @Value("${frontend.admin}")
    private String frontAdmin;

    private final AutenticacaoService service;

    private final TokenService tokenService;

    @GetMapping("/acesso-cidadao-admin-response")
    public RedirectView acessoCidadaoAdminResponse(String accessToken) {
        String tokenEmBase64 = Base64.getEncoder().encodeToString(accessToken.getBytes());
        return new RedirectView(String.format("%s/#/token?token=%s", frontAdmin, tokenEmBase64));
    }

    @GetMapping("/acesso-cidadao-painel-response")
    public RedirectView acessoCidadaoPainelResponse(String accessToken) {
        String tokenEmBase64 = Base64.getEncoder().encodeToString(accessToken.getBytes());
        return new RedirectView(String.format("%s/#/token?token=%s", frontPainel, tokenEmBase64));
    }

    @GetMapping("/user-info")
    public UsuarioDto montarUsuarioDto(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        return service.autenticar( authorization.replace("Bearer ", ""));
    }

    @GetMapping("/token-info")
    public Map<String,Object> getTokenInfo(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        return tokenService.getClaimsFromToken(authorization.replace("Bearer ", ""));
    }
    
}
