package br.gov.es.indicadores.dto;

import java.util.List;

import lombok.Builder;

@Builder
public record OdsDto(
    String name,
    String description,
    Integer order,
    List<OdsGoalDto> odsGoals
) {
    public OdsDto(){
        this("","",0, null);
    }
}