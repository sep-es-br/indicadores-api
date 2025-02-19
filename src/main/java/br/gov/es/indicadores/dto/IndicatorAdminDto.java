package br.gov.es.indicadores.dto;

import java.util.List;

import br.gov.es.indicadores.model.OdsGoal;
import lombok.Builder;

@Builder
public record IndicatorAdminDto(
    String uuId,
    String name,
    String measureUnit,
    String organizationAcronym,
    String polarity,
    List<OdsGoal> odsgoal,
    List<TargetResultDto> targetsFor,
    List<TargetResultDto> resultedIn
) {
    public IndicatorAdminDto(){
        this("","","","","",null, null, null);
    }
}
