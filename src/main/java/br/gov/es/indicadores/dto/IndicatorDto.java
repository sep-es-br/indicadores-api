package br.gov.es.indicadores.dto;

import java.util.List;

import br.gov.es.indicadores.model.ResultedInRelationship;
import br.gov.es.indicadores.model.TargetsForRelationship;
import lombok.Builder;

@Builder
public record IndicatorDto(
    String id,
    String name,
    String measureUnit,
    String organizationAcronym,
    String polarity,
    List<Integer> ods,
    List<TargetResultDto> targetFor,
    List<TargetResultDto> resultedIn
) {
    public IndicatorDto(){
        this("","","","","",null, null, null);
    }
}


