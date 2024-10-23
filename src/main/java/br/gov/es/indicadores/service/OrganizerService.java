package br.gov.es.indicadores.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.es.indicadores.dto.OrganizerDto;
import br.gov.es.indicadores.dto.ChallengeDto;
import br.gov.es.indicadores.dto.IndicatorDto;
import br.gov.es.indicadores.dto.OverviewOrganizerDto;
import br.gov.es.indicadores.model.Administration;
import br.gov.es.indicadores.model.Organizer;
import br.gov.es.indicadores.model.Challenge;
import br.gov.es.indicadores.model.Indicator;
import br.gov.es.indicadores.repository.AdministrationRepository;
import br.gov.es.indicadores.repository.OrganizerRepository;
import br.gov.es.indicadores.repository.ChallengeRepository;

@Service
public class OrganizerService {

    @Autowired
    private OrganizerRepository organizerRepository;

    @Autowired
    private DateService dateService;

    @Autowired
    private AdministrationRepository administrationRepository;

    @Autowired
    private ChallengeRepository challengeRepository;

    @Autowired
    private IndicatorService indicatorService;
/**
 *   Number id,
    String name,
    String icon,
    Integer indicator,
    Integer challenge
 * @param idOrganizer
 * @return
 */
    public Map<String, OverviewOrganizerDto[]> getAll(String organizerId){

        Administration administrationData = administrationRepository.getAdministrationByOrganizer(organizerId);
        Organizer[] organizerData = organizerRepository.getorganizersByAdministration(administrationData.getId());
        for (Organizer organizer : organizerData) {
            Organizer[] childrenOrganizers = organizerRepository.getChildrenOrganizers(organizer.getId());
            organizer.setChildren(Arrays.asList(childrenOrganizers));
        }
        Map<String, OverviewOrganizerDto[]> organizerDtos = this.treatOrganizerDtos(organizerData);
        return organizerDtos;
    }

    public OrganizerDto getOrganizerDto(String OrganizerUuId){
        Administration administrationData = administrationRepository.getAdministrationByOrganizer(OrganizerUuId);
        Optional<Organizer> organizerData = organizerRepository.findByUuId(OrganizerUuId);
        List<Challenge> challengeData = challengeRepository.getChallengeByOrganizer(OrganizerUuId);
        List<ChallengeDto> challengesWithIndicators = new ArrayList<>();

        for (Challenge challenge : challengeData){
            List<IndicatorDto> indicators = indicatorService.getIndicatorByChallenge(challenge.getId());
            ChallengeDto updatedChallenge = new ChallengeDto(
                challenge.getId(), 
                challenge.getName(), 
                indicators
                );
            challengesWithIndicators.add(updatedChallenge);
        }

        OrganizerDto organizerDto = OrganizerDto.builder()
                                 .endOfAdministrationYear(administrationData.getEndYear())
                                 .startOfAdministrationYear(administrationData.getStartYear())
                                 .administrationName(administrationData.getName())
                                 .id(organizerData.get().getId())
                                 .modelName(organizerData.get().getModelName())
                                 .modelNameInPlural(organizerData.get().getModelNameInPlural())
                                 .indicator(null)
                                 .challenge(challengesWithIndicators)
                                 .icon(organizerData.get().getIcon())
                                 .name(organizerData.get().getName())
                                 .description(organizerData.get().getDescription())
                                 .build();
        return organizerDto;
    }

    public Map<String, OverviewOrganizerDto[]> treatOrganizerDtos(Organizer[] organizer){

        Map<String, OverviewOrganizerDto[]> organizerMap  = new HashMap<>();

        for (Organizer parentOrganizer : organizer) {    
            if(parentOrganizer.getChildren() != null && !parentOrganizer.getChildren().isEmpty()){
                OverviewOrganizerDto[] childDtos = parentOrganizer.getChildren().stream()
                    .map(this::convertToOverviewOrganizerDto)
                    .toArray(OverviewOrganizerDto[]::new);
    
                    organizerMap.put(parentOrganizer.getName(), childDtos);
            } 
        }
        if(organizerMap.isEmpty()){
            OverviewOrganizerDto[] parentDto  = Arrays.stream(organizer)
                    .map(this::convertToOverviewOrganizerDto)
                    .toArray(OverviewOrganizerDto[]::new);

            organizerMap.put("", parentDto );
        }


        return organizerMap;
    }

    private OverviewOrganizerDto convertToOverviewOrganizerDto(Organizer organizer) {

        List<Challenge> challenge = challengeRepository.getChallengeByOrganizer(organizer.getId());

        return OverviewOrganizerDto.builder()
            .id(organizer.getId())
            .icon(organizer.getIcon())
            .name(organizer.getName())
            .challenge(challenge.size())
            .indicator(indicatorService.indicatorAmountByChallenge(organizer.getId()))
            .build();
    }
}
