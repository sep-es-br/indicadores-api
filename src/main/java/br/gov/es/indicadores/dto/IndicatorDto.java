package br.gov.es.indicadores.dto;

import java.util.List;

import lombok.Builder;

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
