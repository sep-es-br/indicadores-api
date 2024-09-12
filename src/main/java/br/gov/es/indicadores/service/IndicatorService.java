package br.gov.es.indicadores.service;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.es.indicadores.dto.IndicatorDto;
import br.gov.es.indicadores.model.Indicator;
import br.gov.es.indicadores.repository.IndicatorRepository;

@Service
public class IndicatorService {
    
    @Autowired
    private IndicatorRepository indicatorRepository;

    public Integer indicatorAmountByAdministration(Long administrationId){
        return indicatorRepository.indicatorAmountByAdministration(administrationId);
    }

    public Integer indicatorAmountByChallenge(Long areaId){
        return indicatorRepository.indicatorAmountByChallenge(areaId);
    }

    public IndicatorDto[] getListIndicatorsByChallengeId(Long challengeId){

        IndicatorDto[] indicators = indicatorRepository.getIndicatorsByChallenge(challengeId);
        

        // IndicatorDto[] indicatorDtos = new IndicatorDto[indicators.length];

        // for (int i = 0; i < indicators.length; i++) {
        //     Indicator indicator = indicators[i];
        //     indicatorDtos[i] = IndicatorDto.builder()
        //         .name(indicator.getName())
        //         .measurementUnit(indicator.getMeasurementUnit())
        //         .organizationAcronym(indicator.getOrganizationAcronym())
        //         .organizationName(indicator.getOrganizationName())
        //         .polarity(indicator.getPolarity())
        //         .build();
        // }
        return indicators;
    }
}
