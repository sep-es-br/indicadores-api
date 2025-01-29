package br.gov.es.indicadores.dto;

import lombok.Builder;

@Builder
public record CountOrganizerDto(
    String name,
    String nameInPlural,
    Long countOrganizer,
    String relationshipType
) {
    public CountOrganizerDto(){
        this("","", null,"");
    }
}