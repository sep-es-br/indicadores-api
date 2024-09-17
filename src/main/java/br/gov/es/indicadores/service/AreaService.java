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
import br.gov.es.indicadores.model.Area;
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
    public OverviewAreaDto[] getAll(){

        Administration administrationData = administrationRepository.getAdministrationByActive();
        Area[] areaData = areaRepository.getAreasByAdministration(administrationData.getId());
        OverviewAreaDto[] areaDtos = this.treatAreaDtos(areaData);
        return areaDtos;
    }

    public AreaDto getAreaDto(String AreaUuId){
        Optional<Area> areaData = areaRepository.findById(AreaUuId);
        List<ChallengeDto> challengeData = challengeRepository.getChallengeByArea(AreaUuId);
        List<ChallengeDto> challengesWithIndicators = new ArrayList<>();

        for (ChallengeDto challengeDto : challengeData){
            List<IndicatorDto> indicators = indicatorService.getIndicatorByChallenge(challengeDto.uuId());
            ChallengeDto updatedChallenge = new ChallengeDto(
                challengeDto.uuId(), 
                challengeDto.name(), 
                indicators
                );
            challengesWithIndicators.add(updatedChallenge);
        }

        AreaDto areaDto = AreaDto.builder()
                                 .id(areaData.get().getId())
                                 .indicator(null)
                                 .challenge(challengesWithIndicators)
                                 .icon(areaData.get().getIcon())
                                 .name(areaData.get().getName())
                                 .description(areaData.get().getDescription())
                                 .build();
        return areaDto;
    }

    public OverviewAreaDto[] treatAreaDtos(Area[] areas){
        OverviewAreaDto[] areaDtos = Arrays.stream(areas)
                                   .map(this::convertToOverviewAreaDto)
                                   .toArray(OverviewAreaDto[]::new);
        return areaDtos;
    }

    private OverviewAreaDto convertToOverviewAreaDto(Area area) {
        var adm = area.getAdministration();
        List<ChallengeDto> challenge = challengeRepository.getChallengeByArea(area.getId());
        return OverviewAreaDto.builder()
            .id(area.getId())
            .icon(area.getIcon())
            .name(area.getName())
            .challenge(challenge.size())
            .indicator(indicatorService.indicatorAmountByChallenge(area.getId()))
            .build();
    }
}
