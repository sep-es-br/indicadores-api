package br.gov.es.indicadores.dto;

import lombok.Builder;

@Builder
public record TargetResultDto(
    Double value,
    String showValue,
    Integer year,
    String justificationGoal, // adicionado
    String justificationResult // adicionado

) {
    public TargetResultDto(){
        this(0.0,"",0,"","");
    }
}