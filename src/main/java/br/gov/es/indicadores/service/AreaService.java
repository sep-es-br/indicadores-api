package br.gov.es.indicadores.service;

import java.util.Arrays;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.es.indicadores.dto.AreaDto;
import br.gov.es.indicadores.dto.OverviewAreaDto;
import br.gov.es.indicadores.model.Administration;
import br.gov.es.indicadores.model.Area;
import br.gov.es.indicadores.model.Challenge;
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

        Number year = dateService.getCurrentYear();

        Administration administrationData = administrationRepository.getAdministrationByYear(year);
        Area[] areaData = areaRepository.getAreasByAdministration(administrationData.getId());
        OverviewAreaDto[] areaDtos = this.treatAreaDtos(areaData);
        return areaDtos;
    }

    public AreaDto getAreaDto(Long idArea){
        Optional<Area> areaData = areaRepository.findById(idArea);
        AreaDto areaDto = AreaDto.builder()
                                 .id(areaData.get().getId())
                                 .indicator(null)
                                 .challenge(null)
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
        Challenge[] challenge = challengeRepository.getChallengeByArea(area.getId());
        return OverviewAreaDto.builder()
            .id(area.getId())
            .icon(area.getIcon())
            .name(area.getName())
            .challenge(challenge.length)
            .indicator(0)
            .build();
    }
}
