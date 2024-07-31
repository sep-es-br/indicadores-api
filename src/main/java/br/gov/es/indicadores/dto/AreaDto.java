package br.gov.es.indicadores.dto;

import lombok.Builder;

@Builder
public record AreaDto(
    Number id,
    String name,
    String description,
    String icon,
    Integer indicator,
    Integer challenge
) {
    public AreaDto(){
        this(0,"","","", 0, 0);
    }

    
}
