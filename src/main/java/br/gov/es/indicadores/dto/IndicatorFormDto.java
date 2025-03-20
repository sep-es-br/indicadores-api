package br.gov.es.indicadores.dto;

import lombok.Data;
import java.util.List;


@Data
public class IndicatorFormDto {
    private String id;
    private String name;
    private String polarity;
    private String measureUnit;
    private List<ChallengeOrgan> organizationAcronym;
    private List<String> ods;
    private List<TargetResultDto> targetsFor;
    private List<TargetResultDto> resultedIn;

    @Data
    public static class ChallengeOrgan {
        private String challengeId;
        private String organ;
    }
}

