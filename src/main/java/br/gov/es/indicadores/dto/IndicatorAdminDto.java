package br.gov.es.indicadores.dto;

import java.util.List;

import br.gov.es.indicadores.model.OdsGoal;
import lombok.Builder;
import lombok.Data;

@Builder
public record IndicatorAdminDto(
        String uuId,
        String name,
        String measureUnit,
        String polarity,
        String justificationBase,
        String observations,
        List<ChallengeOrgan> measures,
        List<OdsGoal> odsgoal,
        List<TimeDto> times,
        String originalFileName) {
    public IndicatorAdminDto() {
         this("", "", "", "", "", "", null, null, null, null);
    }

    @Data
    public static class ChallengeOrgan {
        private String challengeId;
        private String organ;
    }
}
