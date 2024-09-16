package br.gov.es.indicadores.dto;

import lombok.Builder;
import java.util.List;

import br.gov.es.indicadores.model.TargetsForRelationship;

@Builder
public record testeDto(
    Double value,
    String showValue
    // Integer year
) {
    public testeDto(){
        this(0.0,"");
    }
}