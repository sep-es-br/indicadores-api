package br.gov.es.indicadores.dto;

import java.util.List;

import br.gov.es.indicadores.model.MeasuresRelationship;
import br.gov.es.indicadores.model.OdsGoal;
import lombok.Builder;
import lombok.Data;

@Builder
public record IndicatorAdminDto(
    String uuId,
    String name,
    String measureUnit,
    String polarity,
    List<ChallengeOrgan> measures,
    List<OdsGoal> odsgoal,
    List<TargetResultDto> targetsFor,
    List<TargetResultDto> resultedIn
) {
    public IndicatorAdminDto(){
        this("","","","",null,null, null, null);
    }
    
    @Data
    public static class ChallengeOrgan {
        private String challengeId;
        private String organ;
    }
    
    @Data
    public static class IndicatorValue {
        private int year;
        private String showValue;
        private double value;
    }
}
