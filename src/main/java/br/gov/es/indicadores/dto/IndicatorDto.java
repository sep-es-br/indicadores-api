package br.gov.es.indicadores.dto;

import java.util.List;

import lombok.Builder;

@Builder
public record IndicatorDto(
    String name,
    String measurementUnit,
    String organizationAcronym,
    String organizationName,
    String polarity
    // List<RelationshipWithProperties> targetsFor,
    // List<RelationshipWithProperties> resultedIn
) {
}
