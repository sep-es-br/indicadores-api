package br.gov.es.indicadores.dto;

import lombok.Builder;

@Builder
public record OverviewAreaDto(
    String id,
    String name,
    String icon,
    Integer indicator,
    Number challenge
) {
    public OverviewAreaDto(){
        this("","","", 0, 0);
    }

    
}
