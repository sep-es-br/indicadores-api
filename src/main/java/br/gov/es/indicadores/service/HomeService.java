package br.gov.es.indicadores.service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.es.indicadores.dto.*;
import br.gov.es.indicadores.model.Administration;
import br.gov.es.indicadores.model.Organizer;
import br.gov.es.indicadores.model.Challenge;
import br.gov.es.indicadores.model.ODS;
import br.gov.es.indicadores.repository.AdministrationRepository;
import br.gov.es.indicadores.repository.AreaRepository;
import br.gov.es.indicadores.repository.OdsRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HomeService {

    @Autowired
    private AdministrationRepository administrationRepository;

    @Autowired
    private AreaRepository areaRepository;

    @Autowired
    private AreaService areaService;

    @Autowired
    private final ChallengeService challengeService;

    @Autowired
    private IndicatorService indicatorService;

    @Autowired
    OdsRepository odsRepository;

    
    public IndicadoresGeraisDto getData(String administrationId){


        Administration administrationData = administrationRepository.getAdministrationByUuId(administrationId);
        Organizer[] areaData = areaRepository.getAreasByAdministration(administrationData.getId());

        

        return fitIndicator(administrationData,areaData);
    }

    public List<Administration> administrationList(){
        return administrationRepository.findAll().stream()
        .sorted((a1, a2) ->  a2.getName().compareTo(a1.getName())) 
        .collect(Collectors.toList());
    }

    private IndicadoresGeraisDto fitIndicator(Administration administrationData,Organizer[] areaData){

        OverviewAreaDto[] areaDtos = areaService.treatAreaDtos(areaData);

        OverviewIndicadoresGeraisDto overview = OverviewIndicadoresGeraisDto.builder()
                                                    .areasEstrategicas(areaData.length)
                                                    .desafios(challengeService.challengesAmountByAdministration(administrationData.getId()))
                                                    .indicadores(indicatorService.indicatorAmountByAdministration(administrationData.getId()))
                                                    .build();
                                                    
        IndicadoresGeraisDto indicator = IndicadoresGeraisDto.builder()
                                            .name(administrationData.getName())
                                            .description(administrationData.getDescription())
                                            .active(administrationData.getActive())
                                            .startYear(administrationData.getStartYear())
                                            .endYear(administrationData.getEndYear())
                                            .adminId(administrationData.getAdminId())
                                            .overview(overview)
                                            .areas(areaDtos)
                                            .build();

        return indicator;
    }

    public OdsDto getOdsAndOdsGoal(Integer order){
        return odsRepository.getOdsAndOdsGoal(order);
    }
}
