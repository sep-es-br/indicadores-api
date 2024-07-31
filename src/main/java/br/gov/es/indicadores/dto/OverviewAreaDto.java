package br.gov.es.indicadores.dto;

import lombok.Builder;

@Builder
public record OverviewAreaDto(
    Number id,
    String name,
    String icon,
    Integer indicator,
    Number challenge
) {
    public OverviewAreaDto(){
        this(0,"","", 0, 0);
    }

    
}
