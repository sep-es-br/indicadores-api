package br.gov.es.indicadores.dto;

import lombok.Data;
import java.util.List;

@Data
public class NewIndicatorDto {
    private String name;
    private String polarity;
    private String measureUnit;
    private List<ChallengeOrgan> organizationAcronym;
    private List<String> ods;
    private List<IndicatorValue> targetsFor;
    private List<IndicatorValue> resultedIn;

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

