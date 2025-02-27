package br.gov.es.indicadores.dto.acessocidadaoapi;

public record TokenResponseDto(
    String access_token,
    int expires_in,
    String token_type
) {
}
