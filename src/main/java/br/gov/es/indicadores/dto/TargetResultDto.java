package br.gov.es.indicadores.dto;

import lombok.Builder;

@Builder
public record TargetResultDto(
    Double value,
    String showValue,
    Integer year
) {
    public TargetResultDto(){
        this(0.0,"",0);
    }
}