package br.gov.es.indicadores.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.es.indicadores.dto.AreaDto;
import br.gov.es.indicadores.dto.ChallengeDto;
import br.gov.es.indicadores.dto.IndicatorDto;
import br.gov.es.indicadores.dto.OverviewAreaDto;
import br.gov.es.indicadores.model.Administration;
import br.gov.es.indicadores.model.Organizer;
import br.gov.es.indicadores.model.Challenge;
import br.gov.es.indicadores.model.Indicator;
import br.gov.es.indicadores.repository.AdministrationRepository;
import br.gov.es.indicadores.repository.AreaRepository;
import br.gov.es.indicadores.repository.ChallengeRepository;

@Service
public class AreaService {

    @Autowired
    private AreaRepository areaRepository;

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
 * @param idArea
 * @return
 */
    public OverviewAreaDto[] getAll(String areaId){

        Administration administrationData = administrationRepository.getAdministrationByArea(areaId);
        Organizer[] areaData = areaRepository.getAreasByAdministration(administrationData.getId());
        OverviewAreaDto[] areaDtos = this.treatAreaDtos(areaData);
        return areaDtos;
    }

    public AreaDto getAreaDto(String AreaUuId){
        Administration administrationData = administrationRepository.getAdministrationByArea(AreaUuId);
        Optional<Organizer> areaData = areaRepository.findByUuId(AreaUuId);
        List<Challenge> challengeData = challengeRepository.getChallengeByArea(AreaUuId);
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

        AreaDto areaDto = AreaDto.builder()
                                 .endOfAdministrationYear(administrationData.getEndYear())
                                 .startOfAdministrationYear(administrationData.getStartYear())
                                 .administrationName(administrationData.getName())
                                 .id(areaData.get().getId())
                                 .indicator(null)
                                 .challenge(challengesWithIndicators)
                                 .icon(areaData.get().getIcon())
                                 .name(areaData.get().getName())
                                 .description(areaData.get().getDescription())
                                 .build();
        return areaDto;
    }

    public OverviewAreaDto[] treatAreaDtos(Organizer[] areas){
        OverviewAreaDto[] areaDtos = Arrays.stream(areas)
                                   .map(this::convertToOverviewAreaDto)
                                   .toArray(OverviewAreaDto[]::new);
        return areaDtos;
    }

    private OverviewAreaDto convertToOverviewAreaDto(Organizer area) {
        List<Challenge> challenge = challengeRepository.getChallengeByArea(area.getId());
        return OverviewAreaDto.builder()
            .id(area.getId())
            .icon(area.getIcon())
            .name(area.getName())
            .challenge(challenge.size())
            .indicator(indicatorService.indicatorAmountByChallenge(area.getId()))
            .build();
    }
}
