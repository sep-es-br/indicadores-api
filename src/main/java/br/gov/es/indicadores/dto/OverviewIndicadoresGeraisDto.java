package br.gov.es.indicadores.dto;

import lombok.Builder;

@Builder
public record OverviewIndicadoresGeraisDto(
    Number areasEstrategicas,
    Number desafios,
    Number indicadores
) {
    public OverviewIndicadoresGeraisDto(){
        this(0, 0, 0);
    }
}