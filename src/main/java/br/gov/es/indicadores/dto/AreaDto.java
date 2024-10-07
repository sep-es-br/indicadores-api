package br.gov.es.indicadores.dto;

import java.util.List;

import lombok.Builder;

@Builder
public record AreaDto(
    Integer startOfAdministrationYear,
	Integer endOfAdministrationYear,
    String administrationName,
    String id,
    String name,
    String description,
    String icon,
    Integer indicator,
    List<ChallengeDto> challenge
) {
    public AreaDto(){
        this(0,0,"","","","","", 0, null);
    }

    
}
