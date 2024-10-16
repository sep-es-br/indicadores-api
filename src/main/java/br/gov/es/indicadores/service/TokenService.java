package br.gov.es.indicadores.service;

import br.gov.es.indicadores.dto.ACUserInfoDto;
import br.gov.es.indicadores.exception.service.IndicadoresServiceException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TokenService {
    private static final String ISSUER = "SEP Indicadores API";

    @Value("${token.secret}")
    private String secret;

    public String gerarToken(ACUserInfoDto userInfo) {
        try {
            Algorithm algoritmo = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer(ISSUER)
                    .withSubject(userInfo.sub())
                    .withClaim("name", userInfo.apelido())
                    .withClaim("email", userInfo.email())
                    .withClaim("roles", new ArrayList<>(userInfo.role()))
                    .withExpiresAt(getDataExpiracao())
                    .sign(algoritmo);
        } catch (JWTCreationException exception) {
            throw new IndicadoresServiceException(List.of("Erro ao gerar o token", exception.getMessage()));
        }
    }

    public String validarToken(String token) {
        Algorithm algoritmo = Algorithm.HMAC256(secret);
        return JWT.require(algoritmo)
                .withIssuer(ISSUER)
                .build()
                .verify(token)
                .getSubject();
    }

    public List<String> getRoleFromToken(String token) {
        DecodedJWT decodedJWT = JWT.decode(token);
        return decodedJWT.getClaim("roles").asList(String.class);
    }



    public Map<String, Object> getClaimsFromToken(String token) {
        try {
            String subject = this.validarToken(token);

            DecodedJWT decodedJWT = JWT.decode(token);
    
            Map<String, Object> claims = new HashMap<>();
            
            claims.put("name", decodedJWT.getClaim("name").asString());
            claims.put("email", decodedJWT.getClaim("email").asString());
            claims.put("role", decodedJWT.getClaim("roles").asList(String.class));

            return claims;
        
        } catch (JWTVerificationException e) {
            var expiresAt = LocalDateTime.ofInstant(JWT.decode(token).getExpiresAt().toInstant(), ZoneOffset.of("-03:00"));
            List<String> erros = new ArrayList<>();
            erros.add("Por favor, faça o login novamente");
            if (LocalDateTime.now().isAfter(expiresAt))
                erros.add("Token expirado em " + expiresAt);
            throw new RuntimeException(String.join(", ", erros));
        }
    
    }


    private Instant getDataExpiracao() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
