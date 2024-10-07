package br.gov.es.indicadores.dto;

import java.util.Set;

public record UsuarioDto(
        String token,
        String name,
        String email,
        Set<String> role) {
}
