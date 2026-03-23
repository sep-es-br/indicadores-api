package br.gov.es.indicadores.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TimeDto {

    private String type;
    private String year;
    private int period;
    private Double valueGoal;
    private String showValueGoal;
    private Double valueResult;
    private String showValueResult;
    private String justificationGoal;
    private String justificationResult;
}
