package br.gov.es.indicadores.dto;

import java.util.List;
import java.util.Map;

import lombok.Builder;

@Builder
public record GeneralIndicatorsDto(
    String name,
    Boolean active,
    Number adminId,
    Number startYear,
    Number endYear,
    Number referenceYear,
    String description,
    OverviewDto overview,
    Map<String, OverviewOrganizerDto[]> organizers
) {

    public GeneralIndicatorsDto() {
        this(null, null, null, null, null, null, null, null,null);
    }
}


