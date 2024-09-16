package br.gov.es.indicadores.dto;

import java.util.List;

import br.gov.es.indicadores.model.Indicator;
import lombok.Builder;

@Builder
public record ChallengeDto(
    String uuId,
    String name,
    List<IndicatorDto> indicatorList
) {
    public ChallengeDto(){
        this("","",null);
    }
}
