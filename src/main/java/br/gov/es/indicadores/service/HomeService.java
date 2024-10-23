package br.gov.es.indicadores.service;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
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
import br.gov.es.indicadores.repository.OrganizerRepository;
import br.gov.es.indicadores.repository.OdsRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HomeService {

    @Autowired
    private AdministrationRepository administrationRepository;

    @Autowired
    private OrganizerRepository organizerRepository;

    @Autowired
    private OrganizerService organizerService;

    @Autowired
    private final ChallengeService challengeService;

    @Autowired
    private IndicatorService indicatorService;

    @Autowired
    OdsRepository odsRepository;

    
    public GeneralIndicatorsDto getData(String administrationId){


        Administration administrationData = administrationRepository.getAdministrationByUuId(administrationId);
        Organizer[] organizerData = organizerRepository.getorganizersByAdministration(administrationData.getId());

        for (Organizer organizer : organizerData) {
            Organizer[] childrenOrganizers = organizerRepository.getChildrenOrganizers(organizer.getId());
            organizer.setChildren(Arrays.asList(childrenOrganizers));
        }
        
            return fitIndicator(administrationData, organizerData);
        }

    public List<Administration> administrationList(){
        return administrationRepository.findAll().stream()
        .sorted((a1, a2) ->  a2.getName().compareTo(a1.getName())) 
        .collect(Collectors.toList());
    }

    private GeneralIndicatorsDto fitIndicator(Administration administrationData,Organizer[] organizerData){


        Map<String, OverviewOrganizerDto[]> organizerDtos = organizerService.treatOrganizerDtos(organizerData);

        List<CountOrganizerDto> organizeResult = organizerRepository.organizerAmountByAdministration(administrationData.getId());

        List<CountOrganizerDto> parentOrganizer = new ArrayList<>();
        List<CountOrganizerDto> childOrganizer = new ArrayList<>();

        for (CountOrganizerDto organize : organizeResult) {

            if ("parent".equals(organize.relationshipType())) {
                parentOrganizer.add(organize);
            } else {
                childOrganizer.add(organize);
            }

        }

        CountOrganizerListDto countOrganizerListDto = CountOrganizerListDto.builder()
                                                            .parentOrganizer(parentOrganizer)
                                                            .childOrganizer(childOrganizer).build();

        OverviewDto overview = OverviewDto.builder()
                                    .organizer(countOrganizerListDto)
                                    .desafios(challengeService.challengesAmountByAdministration(administrationData.getId()))
                                    .indicadores(indicatorService.indicatorAmountByAdministration(administrationData.getId()))
                                    .build();
                                                    
        GeneralIndicatorsDto indicator = GeneralIndicatorsDto.builder()
                                            .name(administrationData.getName())
                                            .description(administrationData.getDescription())
                                            .active(administrationData.getActive())
                                            .startYear(administrationData.getStartYear())
                                            .endYear(administrationData.getEndYear())
                                            .adminId(administrationData.getAdminId())
                                            .overview(overview)
                                            .organizers(organizerDtos)
                                            .build();

        return indicator;
    }

    public OdsDto getOdsAndOdsGoal(Integer order){
        return odsRepository.getOdsAndOdsGoal(order);
    }
}
