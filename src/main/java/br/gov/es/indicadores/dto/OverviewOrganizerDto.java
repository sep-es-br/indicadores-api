package br.gov.es.indicadores.dto;

import lombok.Builder;

@Builder
public record OverviewOrganizerDto(
    String id,
    String name,
    String icon,
    Integer indicator,
    Number challenge
) {
    public OverviewOrganizerDto(){
        this("","","",0, 0);
    }

    
}
