package br.gov.es.indicadores.dto;

import lombok.Builder;

@Builder
public record OverviewDto(
    CountOrganizerListDto organizer,
    Number desafios,
    Number indicadores
) {
    public OverviewDto(){
        this(null, 0, 0);
    }
}

