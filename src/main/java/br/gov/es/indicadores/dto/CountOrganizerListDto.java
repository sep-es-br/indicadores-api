package br.gov.es.indicadores.dto;

import lombok.Builder;
import java.util.List;

@Builder
public record CountOrganizerListDto(
    List<CountOrganizerDto> parentOrganizer,
    List<CountOrganizerDto> childOrganizer
) {
    public CountOrganizerListDto(){
        this(null, null);
    }
}