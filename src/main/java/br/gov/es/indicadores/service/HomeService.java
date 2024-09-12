package br.gov.es.indicadores.service;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.es.indicadores.dto.*;
import br.gov.es.indicadores.model.Administration;
import br.gov.es.indicadores.model.Area;
import br.gov.es.indicadores.model.Challenge;
import br.gov.es.indicadores.model.ODS;
import br.gov.es.indicadores.repository.AdministrationRepository;
import br.gov.es.indicadores.repository.AreaRepository;
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

    
    public IndicadoresGeraisDto getData(){


        Administration administrationData = administrationRepository.getAdministrationByActive();
        Area[] areaData = areaRepository.getAreasByAdministration(administrationData.getId());

        

        return fitIndicator(administrationData,areaData);
    }

    private IndicadoresGeraisDto fitIndicator(Administration administrationData,Area[] areaData){

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
}
