package br.gov.es.indicadores.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
public record IndicatorDto(
        String uuId,
        String name,
        String measureUnit,
        String organizationAcronym,
        String polarity,
        String justificationBase,
        String fileName,
        String originalFileName,
        List<Integer> ods,
        List<TimeDto> times
) {
    public IndicatorDto() {
        this("", "", "", "", "", "", "", "", null,null);
    }
}
