package br.gov.es.indicadores.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.gov.es.indicadores.dto.AdministrationDto;
import br.gov.es.indicadores.dto.IndicatorAdminDto;
import br.gov.es.indicadores.dto.IndicatorDto;
import br.gov.es.indicadores.dto.ManagementOrganizerChallengeDto;
import br.gov.es.indicadores.dto.OrganizerChallengeDto;
import br.gov.es.indicadores.model.Administration;
import br.gov.es.indicadores.model.Indicator;
import br.gov.es.indicadores.model.TargetAndResultRelation;
import br.gov.es.indicadores.repository.AdministrationRepository;
import br.gov.es.indicadores.repository.IndicatorRepository;

@Service
public class IndicatorService {
    
    @Autowired
    private IndicatorRepository indicatorRepository;

    @Autowired
    private AdministrationRepository administrationRepository;

    public Integer indicatorAmountByAdministration(String administrationId){
        return indicatorRepository.indicatorAmountByAdministration(administrationId);
    }

    public Integer indicatorAmountByChallenge(String organizerUuId){
        return indicatorRepository.indicatorAmountByChallenge(organizerUuId);
    }

    public List<IndicatorDto> getIndicatorByChallenge(String challengeUuId){
        return indicatorRepository.indicatorByChallenge(challengeUuId);
    }

    public IndicatorDto[] getListIndicatorsByChallengeId(String challengeId){

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

    public Page<IndicatorAdminDto> indicatorPage(Pageable pageable, String search) throws Exception {
        return indicatorRepository.indicatorPage(search, pageable);
    }

    public List<ManagementOrganizerChallengeDto> findManagementOrganizerChallenges() throws Exception{

        List<Administration> administrationList = administrationRepository.findAll();

        Collections.sort(administrationList, new Comparator<Administration>() {
            @Override
            public int compare(Administration a1, Administration a2) {
                return Boolean.compare(a2.getActive(), a1.getActive());
            }
        });

        List<ManagementOrganizerChallengeDto> returnList = new ArrayList<>();

        for (Administration administration : administrationList) {
            List<OrganizerChallengeDto> organizersChallenges =  administrationRepository.findOrganizerChallengesByAdministration(administration.getId());

            ManagementOrganizerChallengeDto dto = new ManagementOrganizerChallengeDto(administration.getName(), organizersChallenges);

            returnList.add(dto);
        }

        return returnList;
    }

    public List<String> getDistinctMeasureUnits() {
        return indicatorRepository.findDistinctMeasureUnits();
    }

    public List<String> getDistinctOrganizationAcronyms() {
        return indicatorRepository.findDistinctOrganizationAcronyms();
    }
}
