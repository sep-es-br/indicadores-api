package br.gov.es.indicadores.dto;

import lombok.Builder;

@Builder
public record OdsGoalDto(
    String order,
    String description
) {
    public OdsGoalDto(){
        this("","");
    }
}
