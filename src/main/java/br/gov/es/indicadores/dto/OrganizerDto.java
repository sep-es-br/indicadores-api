package br.gov.es.indicadores.dto;

import java.util.List;

import lombok.Builder;

@Builder
public record OrganizerDto(
    Integer startOfAdministrationYear,
	Integer endOfAdministrationYear,
    String administrationName,
    String id,
    String name,
    String description,
    String modelName,
    String modelNameInPlural,
    String icon,
    Integer indicator,
    List<ChallengeDto> challenge
) {
    public OrganizerDto(){
        this(0,0,"","","","","","","", 0, null);
    }

    
}
