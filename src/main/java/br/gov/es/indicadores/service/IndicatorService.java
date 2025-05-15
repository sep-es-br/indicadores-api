package br.gov.es.indicadores.service;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.gov.es.indicadores.dto.IndicatorAdminDto;
import br.gov.es.indicadores.dto.IndicatorDto;
import br.gov.es.indicadores.dto.IndicatorFormDto;
import br.gov.es.indicadores.dto.ManagementOrganizerChallengeDto;
import br.gov.es.indicadores.dto.OdsDto;
import br.gov.es.indicadores.dto.OrganizerChallengeDto;
import br.gov.es.indicadores.dto.OrganizerItemDto;
import br.gov.es.indicadores.dto.TargetResultDto;
import br.gov.es.indicadores.dto.acessocidadaoapi.OrganizacoesACDto;
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
import org.springframework.core.io.Resource;

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

    @Value("${app.file.path}")
    private String uploadPathStr;

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

    public List<OrganizacoesACDto> getDistinctOrganizationAcronyms() {

    List<OrganizacoesACDto> organizacoesApi = organogramaApiService.getOrgaos();

    List<String> siglasBanco = indicatorRepository.findDistinctOrganizationAcronyms();

    Set<String> siglasApi = organizacoesApi.stream()
                                           .map(OrganizacoesACDto::sigla)
                                           .collect(Collectors.toSet());

    List<OrganizacoesACDto> organizacoesBanco = siglasBanco.stream()
        .filter(sigla -> !siglasApi.contains(sigla))
        .map(sigla -> new OrganizacoesACDto(null, sigla))
        .toList();

    return Stream.concat(organizacoesApi.stream(), organizacoesBanco.stream())
        .collect(Collectors.toMap(
            OrganizacoesACDto::sigla,      
            dto -> dto,                    
            (dto1, dto2) -> dto1           
        ))
        .values().stream()
        .sorted(Comparator.comparing(OrganizacoesACDto::sigla, String.CASE_INSENSITIVE_ORDER))
        .collect(Collectors.toList());
}


    public List<OdsDto> getOds() {
        return odsRepository.getOdsAndOdsGoalList();
    }

    public List<Integer> getAllYears() {
        return timeRepository.getAllYears();
    }

    public List<IndicatorAdminDto> getIndicators() throws Exception {
        return indicatorRepository.allIndicators();
    }

    public IndicatorAdminDto getIndicator(String indicatorId) throws Exception {
    
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

        String justificationBase = indicator.getJustificationBase() != null && !indicator.getJustificationBase().isEmpty()
            ? indicator.getJustificationBase()
            : "";

        String justificationGoal = indicator.getJustificationGoal() != null && !indicator.getJustificationGoal().isEmpty()
            ? indicator.getJustificationGoal()
            : "";

        String observations = indicator.getObservations() != null && !indicator.getObservations().isEmpty()
            ? indicator.getObservations()
            : "";
    
        return new IndicatorAdminDto(
            indicator.getId(),
            indicator.getName(),
            indicator.getMeasureUnit(),
            indicator.getPolarity(),
            justificationBase,
            justificationGoal,
            observations,
            measures,
            odsList,
            targetsFor,
            resultedIn,
            indicator.getOriginalFileName()
        );
    }
    
    private TargetResultDto convertToTargetResultDto(TargetAndResultRelation relation) {
        return new TargetResultDto(
            relation.getValue(),
            relation.getShowValue(),
            relation.getTime() != null ? relation.getTime().getYear() : 0
        );
    }
    
    

    public void createIndicator(IndicatorFormDto dto, MultipartFile file) throws Exception{
        Indicator indicator = new Indicator();
        indicator.setName(dto.getName());
        indicator.setPolarity(dto.getPolarity());
        indicator.setMeasureUnit(dto.getMeasureUnit());
        if (dto.getJustificationBase() != null && !dto.getJustificationBase().isEmpty()) {
            indicator.setJustificationBase(dto.getJustificationBase()); 
        }
    
        if (dto.getJustificationGoal() != null && !dto.getJustificationGoal().isEmpty()) {
            indicator.setJustificationGoal(dto.getJustificationGoal());
        }

        if (dto.getObservations() != null && !dto.getObservations().isEmpty()) {
            indicator.setObservations(dto.getObservations());
        }

        if (file != null && !file.isEmpty()) {
            Path uploadPath = Paths.get(uploadPathStr);
            Files.createDirectories(uploadPath);
        
            String originalFileName = Paths.get(file.getOriginalFilename()).getFileName().toString();
            String uuid = UUID.randomUUID().toString().replace("-", "");
            
            String fileName = uuid + "_" + originalFileName;
            Path filePath = uploadPath.resolve(fileName);
            Files.write(filePath, file.getBytes());
        
            indicator.setOriginalFileName(originalFileName);
            indicator.setFileName(fileName);
        }

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

    public void updateIndicator(IndicatorFormDto dto, MultipartFile file) throws Exception {
        Optional<Indicator> existingIndicatorOpt = indicatorRepository.findById(dto.getId());
        if (!existingIndicatorOpt.isPresent()) {
            throw new Exception("Indicador não encontrado.");
        }
        
        Indicator existingIndicator = existingIndicatorOpt.get();
        
        existingIndicator.setName(dto.getName());
        existingIndicator.setPolarity(dto.getPolarity());
        existingIndicator.setMeasureUnit(dto.getMeasureUnit());


        if (dto.getJustificationBase() == null || dto.getJustificationBase().isEmpty()) {
            existingIndicator.setJustificationBase(null); 
        } else {
            existingIndicator.setJustificationBase(dto.getJustificationBase());
        }

        if (dto.getJustificationGoal() == null || dto.getJustificationGoal().isEmpty()) {
            existingIndicator.setJustificationGoal(null); 
        } else {
            existingIndicator.setJustificationGoal(dto.getJustificationGoal());
        }

        if (dto.getObservations() == null || dto.getObservations().isEmpty()) {
            existingIndicator.setObservations(null); 
        } else {
            existingIndicator.setObservations(dto.getJustificationGoal());
        }
        
        if (dto.isRemovePdf()) {
            if (existingIndicator.getFileName() != null) {
                Path oldFile = Paths.get(uploadPathStr).resolve(existingIndicator.getFileName());
                Files.deleteIfExists(oldFile);
            }
            existingIndicator.setFileName(null);
            existingIndicator.setOriginalFileName(null);
        }

        if (file != null && !file.isEmpty()) {
            Path uploadPath = Paths.get(uploadPathStr);
            Files.createDirectories(uploadPath);
    
            String originalFileName = Paths.get(file.getOriginalFilename()).getFileName().toString();
            String uuid = UUID.randomUUID().toString().replace("-", "");
            
            String fileName = uuid + "_" + originalFileName;
            Path filePath = uploadPath.resolve(fileName);
            Files.write(filePath, file.getBytes());
    
            existingIndicator.setFileName(fileName);
            existingIndicator.setOriginalFileName(originalFileName);
        }
    
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

    public Resource getPdfFile(String filename) throws MalformedURLException, FileNotFoundException {
        Path filePath = Paths.get(uploadPathStr).resolve(filename).normalize();

        if (!Files.exists(filePath)) {
            throw new FileNotFoundException("Arquivo não encontrado: " + filename);
        }

        return new UrlResource(filePath.toUri());
    }

}
