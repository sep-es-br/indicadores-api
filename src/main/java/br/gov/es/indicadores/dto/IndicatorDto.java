package br.gov.es.indicadores.dto;

import lombok.Builder;

@Builder
public record IndicatorDto(
    String name,
    String measurementUnit,
    String organizationAcronym,
    String organizationName,
    String polarity
) {
}
