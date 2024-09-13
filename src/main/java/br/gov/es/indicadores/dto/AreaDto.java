package br.gov.es.indicadores.dto;

import java.util.List;

import br.gov.es.indicadores.model.Challenge;
import lombok.Builder;

@Builder
public record AreaDto(
    String id,
    String name,
    String description,
    String icon,
    Integer indicator,
    List<Challenge> challenge
) {
    public AreaDto(){
        this("","","","", 0, null);
    }

    
}
