package br.gov.es.indicadores.dto;

import lombok.Builder;

@Builder
public record IndicadoresGeraisDto(
    String name,
    Boolean status,
    Number startYear,
    Number endYear,
    Number referenceYear,
    String description,
    OverviewIndicadoresGeraisDto overview,
    OverviewAreaDto[] areas
) {

    public IndicadoresGeraisDto() {
        this(null, null, null, null, null, null, null,null);
    }
}


