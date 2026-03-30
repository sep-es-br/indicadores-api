package br.gov.es.indicadores.model;

import br.gov.es.indicadores.dto.IndicatorDto;
import br.gov.es.indicadores.dto.TimeDto;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class IndicatorMapper {

    public List<IndicatorDto> toIndicatorDtoList(List<Map<String, Object>> raw) {
        if (raw == null) return Collections.emptyList();
        return raw.stream()
                .filter(Objects::nonNull)
                .map(this::toIndicatorDto)
                .collect(Collectors.toList());
    }

    private IndicatorDto toIndicatorDto(Map<String, Object> map) {
        return IndicatorDto.builder()
                .uuId((String) map.get("uuId"))
                .name((String) map.get("name"))
                .measureUnit((String) map.get("measureUnit"))
                .organizationAcronym((String) map.get("organizationAcronym"))
                .polarity((String) map.get("polarity"))
                .justificationBase((String) map.get("justificationBase"))
                .fileName((String) map.get("fileName"))
                .originalFileName((String) map.get("originalFileName"))
                .ods(toIntegerList(map.get("ods")))
                .times(toTimeDtoList(map.get("times")))
                .build();
    }

    @SuppressWarnings("unchecked")
    private List<TimeDto> toTimeDtoList(Object raw) {
        if (raw == null) return Collections.emptyList();
        return ((List<Map<String, Object>>) raw).stream()
                .filter(Objects::nonNull)
                .map(this::toTimeDto)
                .collect(Collectors.toList());
    }

    private TimeDto toTimeDto(Map<String, Object> map) {
        return TimeDto.builder()
                .type((String) map.get("type"))
                .year((Long) map.get("year"))
                .period(toInt(map.get("period")))
                .valueResult(toDouble(map.get("valueResult")))
                .showValueResult((String) map.get("showValueResult"))
                .valueGoal(toDouble(map.get("valueGoal")))
                .justificationGoal((String) map.get("justificationGoal"))
                .justificationResult((String) map.get("justificationResult"))
                .showValueGoal((String) map.get("showValueGoal"))
                .build();
    }

    @SuppressWarnings("unchecked")
    private List<Integer> toIntegerList(Object raw) {
        if (raw == null) return Collections.emptyList();
        return ((List<Object>) raw).stream()
                .filter(Objects::nonNull)
                .map(v -> ((Number) v).intValue())
                .collect(Collectors.toList());
    }

    private int toInt(Object value) {
        return value != null ? ((Number) value).intValue() : 0;
    }

    private Double toDouble(Object value) {
        return value != null ? ((Number) value).doubleValue() : null;
    }
}
