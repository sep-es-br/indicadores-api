package br.gov.es.indicadores.dto;

import lombok.Builder;

@Builder
public record ChallengeDto(
    Number id,
    String name,
    Integer year,
    Integer score
) {
}
