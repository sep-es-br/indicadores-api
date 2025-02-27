package br.gov.es.indicadores.service;

import java.util.Arrays;
import java.util.Comparator;
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
            List<Organizer> childrenOrganizers = organizerRepository.getChildrenOrganizers(organizer.getId());
            organizer.setChildren(childrenOrganizers);
        }
        
            return fitIndicator(administrationData, organizerData);
        }

    public List<Administration> administrationList() {
        return administrationRepository.getAllAdministration().stream()
            .sorted(Comparator
                .comparing(Administration::getActive, Comparator.reverseOrder()) 
                .thenComparing(a -> a.getName().toLowerCase()) 
            )
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
                                            .overview(overview)
                                            .organizers(organizerDtos)
                                            .build();

        return indicator;
    }


    public OdsDto getOdsAndOdsGoal(Integer order) {
        OdsDto odsDto = odsRepository.getOdsAndOdsGoal(order);
        
        if (odsDto != null && odsDto.odsGoals() != null) {
            odsDto.odsGoals().sort((odsGoal1, odsGoal2) -> compareOrder(odsGoal1.order(), odsGoal2.order()));
        }
        
        return odsDto;
    }

    private int compareOrder(String order1, String order2) {
        String[] parts1 = order1.split("\\.");
        String[] parts2 = order2.split("\\.");
        
        int maxLength = Math.max(parts1.length, parts2.length);
        for (int i = 0; i < maxLength; i++) {
            String part1 = i < parts1.length ? parts1[i] : "";
            String part2 = i < parts2.length ? parts2[i] : "";
            
            if (isNumeric(part1) && isNumeric(part2)) {
                int num1 = Integer.parseInt(part1);
                int num2 = Integer.parseInt(part2);
                if (num1 != num2) {
                    return num1 - num2;
                }
            } else {
                int cmp = part1.compareTo(part2);
                if (cmp != 0) {
                    return cmp;
                }
            }
        }
        return 0; 
    }

    private boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
