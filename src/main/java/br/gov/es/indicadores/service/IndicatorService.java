package br.gov.es.indicadores.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.gov.es.indicadores.dto.IndicatorAdminDto;
import br.gov.es.indicadores.dto.IndicatorDto;
import br.gov.es.indicadores.dto.IndicatorFormDto;
import br.gov.es.indicadores.dto.ManagementOrganizerChallengeDto;
import br.gov.es.indicadores.dto.OdsDto;
import br.gov.es.indicadores.dto.OrganizerChallengeDto;
import br.gov.es.indicadores.dto.OrganizerItemDto;
import br.gov.es.indicadores.dto.TargetResultDto;
import br.gov.es.indicadores.model.Administration;
import br.gov.es.indicadores.model.Challenge;
import br.gov.es.indicadores.model.Indicator;
import br.gov.es.indicadores.model.MeasuresRelationship;
import br.gov.es.indicadores.model.OdsGoal;
import br.gov.es.indicadores.model.Organizer;
import br.gov.es.indicadores.model.TargetAndResultRelation;
import br.gov.es.indicadores.model.Time;
import br.gov.es.indicadores.repository.AdministrationRepository;
import br.gov.es.indicadores.repository.ChallengeRepository;
import br.gov.es.indicadores.repository.IndicatorRepository;
import br.gov.es.indicadores.repository.OdsGoalRepository;
import br.gov.es.indicadores.repository.OdsRepository;
import br.gov.es.indicadores.repository.TimeRepository;

@Service
public class IndicatorService {

    @Autowired
    private TimeRepository timeRepository;

    @Autowired
    private OrganogramaApiService organogramaApiService;

    @Autowired
    private OdsRepository odsRepository;
    
    @Autowired
    private IndicatorRepository indicatorRepository;

    @Autowired
    private ChallengeRepository challengeRepository;

    @Autowired
    private OdsGoalRepository odsGoalRepository;

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
        List<String> OrganListOrganograma = organogramaApiService.getOrgaos();
        List<String> distinctAcronyms = indicatorRepository.findDistinctOrganizationAcronyms();
    
        distinctAcronyms.addAll(OrganListOrganograma);
    
        return distinctAcronyms.stream()
                           .distinct() 
                           .sorted()  
                           .collect(Collectors.toList());
    }

    public List<OdsDto> getOds() {
        return odsRepository.getOdsAndOdsGoalList();
    }

    public List<Integer> getAllYears() {
        return timeRepository.getAllYears();
    }

    public IndicatorAdminDto getOIndicator(String indicatorId) throws Exception {
    
        Indicator indicator = indicatorRepository.findById(indicatorId)
            .orElseThrow(() -> new RuntimeException("Indicador não encontrado"));
    
        List<IndicatorAdminDto.ChallengeOrgan> measures = indicator.getMeasures().stream()
            .map(measure -> {
                IndicatorAdminDto.ChallengeOrgan challengeOrgan = new IndicatorAdminDto.ChallengeOrgan();
                challengeOrgan.setChallengeId(measure.getChallenge().getId());
                challengeOrgan.setOrgan(measure.getOrganizationAcronym());
                return challengeOrgan;
            })
            .collect(Collectors.toList());

        List<OdsGoal> odsList = indicator.getOdsgoal();

    
        List<TargetResultDto> targetsFor = indicator.getTargetsFor().stream()
            .map(this::convertToTargetResultDto)
            .collect(Collectors.toList());
    
        List<TargetResultDto> resultedIn = indicator.getResultedIn().stream()
            .map(this::convertToTargetResultDto)
            .collect(Collectors.toList());
    
        return new IndicatorAdminDto(
            indicator.getId(),
            indicator.getName(),
            indicator.getMeasureUnit(),
            indicator.getPolarity(),
            measures,
            odsList,
            targetsFor,
            resultedIn
        );
    }
    
    private TargetResultDto convertToTargetResultDto(TargetAndResultRelation relation) {
        return new TargetResultDto(
            relation.getValue(),
            relation.getShowValue(),
            relation.getTime() != null ? relation.getTime().getYear() : 0
        );
    }
    
    

    public void createIndicator(IndicatorFormDto dto) throws Exception{
        Indicator indicator = new Indicator();
        indicator.setName(dto.getName());
        indicator.setPolarity(dto.getPolarity());
        indicator.setMeasureUnit(dto.getMeasureUnit());

        List<OdsGoal> odsGoals = dto.getOds() == null || dto.getOds().isEmpty()
        ? Collections.emptyList()
        : odsGoalRepository.findByOrderIn(dto.getOds());

        List<String> challengeIds = dto.getOrganizationAcronym().stream()
            .map(org -> org.getChallengeId())
            .collect(Collectors.toList());

        List<Challenge> challenges = challengeRepository.findAllById(challengeIds);

        List<MeasuresRelationship> measures = new ArrayList<>();

        for (Challenge challenge : challenges) {
            dto.getOrganizationAcronym().stream()
                .filter(org -> org.getChallengeId().equals(challenge.getId()))
                .findFirst()
                .ifPresent(challengeOrgan -> {
                    MeasuresRelationship measure = new MeasuresRelationship();
                    measure.setChallenge(challenge);
                    measure.setOrganizationAcronym(challengeOrgan.getOrgan());
                    
                    measures.add(measure);
                });
        }

        List<TargetAndResultRelation> targetsFor = createTargetAndResultRelations(dto.getTargetsFor());
        List<TargetAndResultRelation> resultedIn = createTargetAndResultRelations(dto.getResultedIn());
    
        indicator.setMeasures(measures);
        indicator.setOdsgoal(odsGoals);
        indicator.setTargetsFor(targetsFor);
        indicator.setResultedIn(resultedIn);

        indicatorRepository.save(indicator);
    }

    public void updateIndicator(IndicatorFormDto dto) throws Exception {
        Optional<Indicator> existingIndicatorOpt = indicatorRepository.findById(dto.getId());
        if (!existingIndicatorOpt.isPresent()) {
            throw new Exception("Indicador não encontrado.");
        }
        
        Indicator existingIndicator = existingIndicatorOpt.get();
        
        existingIndicator.setName(dto.getName());
        existingIndicator.setPolarity(dto.getPolarity());
        existingIndicator.setMeasureUnit(dto.getMeasureUnit());
    
        List<OdsGoal> odsGoals = dto.getOds() == null || dto.getOds().isEmpty()
            ? Collections.emptyList()
            : odsGoalRepository.findByOrderIn(dto.getOds());
    
        List<String> challengeIds = dto.getOrganizationAcronym().stream()
            .map(org -> org.getChallengeId())
            .collect(Collectors.toList());
    
        List<Challenge> challenges = challengeRepository.findAllById(challengeIds);
    
        List<MeasuresRelationship> measures = new ArrayList<>();
        for (Challenge challenge : challenges) {
            dto.getOrganizationAcronym().stream()
                .filter(org -> org.getChallengeId().equals(challenge.getId()))
                .findFirst()
                .ifPresent(challengeOrgan -> {
                    MeasuresRelationship measure = new MeasuresRelationship();
                    measure.setChallenge(challenge);
                    measure.setOrganizationAcronym(challengeOrgan.getOrgan());
                    measures.add(measure);
                });
        }
    
        List<TargetAndResultRelation> targetsFor = createTargetAndResultRelations(dto.getTargetsFor());
        List<TargetAndResultRelation> resultedIn = createTargetAndResultRelations(dto.getResultedIn());
    
        existingIndicator.setMeasures(measures);
        existingIndicator.setOdsgoal(odsGoals);
        existingIndicator.setTargetsFor(targetsFor);
        existingIndicator.setResultedIn(resultedIn);
    
        indicatorRepository.save(existingIndicator);
    }
    

    private List<TargetAndResultRelation> createTargetAndResultRelations(List<TargetResultDto> values) {
    if (values == null || values.isEmpty()) {
        return Collections.emptyList();
    }

    List<TargetAndResultRelation> relations = new ArrayList<>();
    for (TargetResultDto value : values) {
        Time time = timeRepository.findByYear(value.year());

        TargetAndResultRelation relation = new TargetAndResultRelation();
        relation.setTime(time);
        relation.setValue(value.value());
        relation.setShowValue(value.showValue());
        
        relations.add(relation);
    }
    return relations;
    }

    public void deleteIndicator(String indicatorId) throws Exception {
        Indicator indicator = indicatorRepository.findById(indicatorId)
            .orElseThrow(() -> new IllegalArgumentException("Administração com ID " + indicatorId + " não encontrada."));
    
        indicatorRepository.delete(indicator);
    }

}
