package br.gov.es.indicadores.dto;

import java.util.List;

import br.gov.es.indicadores.model.OdsGoal;
import lombok.Builder;

@Builder
public record IndicatorDto(
    String uuId,
    String name,
    String measureUnit,
    String organizationAcronym,
    String polarity,
    List<OdsGoal> odsgoal,
    List<TargetResultDto> targetFor,
    List<TargetResultDto> resulted
) {
    public IndicatorDto(){
        this("","","","","",null, null, null);
    }
}


