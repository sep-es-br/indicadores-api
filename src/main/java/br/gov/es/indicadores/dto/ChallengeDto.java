package br.gov.es.indicadores.dto;

import java.util.List;

import lombok.Builder;

@Builder
public record ChallengeDto(
    String uuId,
    String name,
    List<IndicatorDto> indicatorList
) {
}
