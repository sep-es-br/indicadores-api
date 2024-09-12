package br.gov.es.indicadores.dto;

import java.util.List;

import br.gov.es.indicadores.model.StrategicGoal;
import lombok.Builder;

@Builder
public record IndicatorDto(
    String name,
    String measurementUnit,
    String organizationAcronym,
    String organizationName,
    String polarity,
    List<StrategicGoal> strategicGoalList
) {
}
