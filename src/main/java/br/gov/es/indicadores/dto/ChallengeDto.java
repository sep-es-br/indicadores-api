package br.gov.es.indicadores.dto;

import lombok.Builder;

@Builder
public record ChallengeDto(
    String id,
    String name,
    IndicatorDto[] indicatorList
) {
    public ChallengeDto(){
        this("","",null);
    }
}
