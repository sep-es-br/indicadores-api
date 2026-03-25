package br.gov.es.indicadores.service;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import br.gov.es.indicadores.model.*;
import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import br.gov.es.indicadores.dto.IndicatorAdminDto;
import br.gov.es.indicadores.dto.IndicatorDto;
import br.gov.es.indicadores.dto.IndicatorFormDto;
import br.gov.es.indicadores.dto.ManagementOrganizerChallengeDto;
import br.gov.es.indicadores.dto.OdsDto;
import br.gov.es.indicadores.dto.OrganizerChallengeDto;
import br.gov.es.indicadores.dto.OrganizerItemDto;
import br.gov.es.indicadores.dto.TimeDto;
import br.gov.es.indicadores.dto.acessocidadaoapi.OrganizacoesACDto;
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

    @Autowired
    private IndicatorMapper indicatorMapper;

    @Value("${app.file.path}")
    private String uploadPathStr;

    public Integer indicatorAmountByAdministration(String administrationId) {
        return indicatorRepository.indicatorAmountByAdministration(administrationId);
    }

    public Integer indicatorAmountByChallenge(String organizerUuId) {
        return indicatorRepository.indicatorAmountByChallenge(organizerUuId);
    }

//    public List<IndicatorDto> getIndicatorByChallenge(String challengeUuId) {
//        List<Map<String, Object>> raw = indicatorRepository.indicatorByChallenge(challengeUuId);
//        return indicatorMapper.toIndicatorDtoList(raw);
//    }


    public List<IndicatorDto> getIndicatorByChallenge(String challengeUuId) {
        List<Map<String, Object>> raw = indicatorRepository.indicatorByChallenge(challengeUuId);

        List<Map<String, Object>> processed = raw.stream().map(res -> {
            List<Map<String, Object>> times = (List<Map<String, Object>>) res.get("times");

            if (times == null) return res;

            List<Map<String, Object>> expandedTimes = times.stream()
                    .flatMap(time -> {
                        String type = (String) time.get("type");
                        String year = String.valueOf(time.get("year"));

                        if ("BIANUAL".equalsIgnoreCase(type) && year.contains("-")) {
                            String[] years = year.split("-");

                            Map<String, Object> firstYear = new HashMap<>(time);
                            firstYear.put("year", years[0].trim());

                            Map<String, Object> secondYear = new HashMap<>(time);
                            secondYear.put("year", years[1].trim());
                            secondYear.put("type", "BIANUAL");
                            secondYear.put("valueGoal", null);
                            secondYear.put("valueResult", null);
                            secondYear.put("showValueGoal", null);
                            secondYear.put("showValueResult", null);
                            secondYear.put("justificationGoal", null);
                            secondYear.put("justificationResult", null);

                            return Stream.of(firstYear, secondYear);
                        }

                        return Stream.of(time);
                    })
                    .collect(Collectors.toList());

            Map<String, Object> updatedRes = new HashMap<>(res);
            updatedRes.put("times", expandedTimes);
            return updatedRes;
        }).collect(Collectors.toList());

        return indicatorMapper.toIndicatorDtoList(processed);
    }

    public Page<IndicatorAdminDto> indicatorPage(Pageable pageable, String search) throws Exception {
        Pageable unsorted = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
        return indicatorRepository.indicatorPage(search, unsorted);
    }

    public List<ManagementOrganizerChallengeDto> findManagementOrganizerChallenges() throws Exception {

        List<Administration> administrationList = administrationRepository.findAll();

        Collections.sort(administrationList, new Comparator<Administration>() {
            @Override
            public int compare(Administration a1, Administration a2) {
                return Boolean.compare(a2.getActive(), a1.getActive());
            }
        });

        List<ManagementOrganizerChallengeDto> returnList = new ArrayList<>();

        for (Administration administration : administrationList) {
            List<OrganizerChallengeDto> organizersChallenges = administrationRepository.findOrganizerChallengesByAdministration(administration.getId());

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

        Set<String> siglasApi = organizacoesApi.stream().map(OrganizacoesACDto::sigla).collect(Collectors.toSet());

        List<OrganizacoesACDto> organizacoesBanco = siglasBanco.stream().filter(sigla -> !siglasApi.contains(sigla)).map(sigla -> new OrganizacoesACDto(null, sigla)).toList();

        return Stream.concat(organizacoesApi.stream(), organizacoesBanco.stream()).collect(Collectors.toMap(OrganizacoesACDto::sigla, dto -> dto, (dto1, dto2) -> dto1)).values().stream().sorted(Comparator.comparing(OrganizacoesACDto::sigla, String.CASE_INSENSITIVE_ORDER)).collect(Collectors.toList());
    }

    public List<OdsDto> getOds() {
        return odsRepository.getOdsAndOdsGoalList();
    }

    public List<String> getAllYears() {
        return timeRepository.getAllYears();
    }


    public List<IndicatorAdminDto> getIndicators() throws Exception {
        return indicatorRepository.allIndicators();
    }

    public IndicatorAdminDto getIndicator(String indicatorId) {

        Indicator indicator = indicatorRepository.findById(indicatorId).orElseThrow(() -> new RuntimeException("Indicador não encontrado"));

        List<IndicatorAdminDto.ChallengeOrgan> measures = indicator.getMeasures() != null ? indicator.getMeasures().stream().map(measure -> {
            IndicatorAdminDto.ChallengeOrgan challengeOrgan = new IndicatorAdminDto.ChallengeOrgan();
            challengeOrgan.setChallengeId(measure.getChallenge() != null ? measure.getChallenge().getId() : null);
            challengeOrgan.setOrgan(measure.getOrganizationAcronym());
            return challengeOrgan;
        }).collect(Collectors.toList()) : Collections.emptyList();

        List<OdsGoal> odsList = indicator.getOdsgoal() != null ? indicator.getOdsgoal() : Collections.emptyList();

        String justificationBase = indicator.getJustificationBase() != null ? indicator.getJustificationBase() : "";

        String observations = indicator.getObservations() != null ? indicator.getObservations() : "";

        List<TimeDto> times = indicator.getTimes() != null ? indicator.getTimes().stream().map(t -> {
            TimeDto dto = new TimeDto();
            dto.setYear(t.getYear());
            dto.setType(t.getType());
            dto.setPeriod(t.getPeriod());
            dto.setValueGoal(t.getValueGoal());
            dto.setShowValueGoal(t.getShowValueGoal());
            dto.setValueResult(t.getValueResult());
            dto.setShowValueResult(t.getShowValueResult());
            dto.setJustificationGoal(t.getJustificationGoal());
            dto.setJustificationResult(t.getJustificationResult());
            return dto;
        }).collect(Collectors.toList()) : Collections.emptyList();

        return new IndicatorAdminDto(indicator.getId(), indicator.getName(), indicator.getMeasureUnit(), indicator.getPolarity(), justificationBase, observations, measures, odsList, times, indicator.getOriginalFileName());
    }

    public void createIndicator(IndicatorFormDto dto, MultipartFile file) throws Exception {
        Indicator indicator = buildIndicator(dto);
        handleFileUpload(file, indicator);
        indicator.setOdsgoal(resolveOdsGoals(dto));
        indicator.setMeasures(resolveMeasures(dto));
        indicator.setTimes(resolveTimes(dto));
        indicatorRepository.save(indicator);
    }

    @Nonnull
    private Indicator buildIndicator(IndicatorFormDto dto) {
        Indicator indicator = new Indicator();
        indicator.setName(dto.getName());
        indicator.setPolarity(dto.getPolarity());
        indicator.setMeasureUnit(dto.getMeasureUnit());

        if (StringUtils.hasText(dto.getJustificationBase())) {
            indicator.setJustificationBase(dto.getJustificationBase());
        }
        if (StringUtils.hasText(dto.getObservations())) {
            indicator.setObservations(dto.getObservations());
        }
        return indicator;
    }

    private void handleFileUpload(MultipartFile file, Indicator indicator) throws Exception {
        if (file == null || file.isEmpty()) return;

        Path uploadPath = Paths.get(uploadPathStr);
        Files.createDirectories(uploadPath);

        String originalFileName = Paths.get(file.getOriginalFilename()).getFileName().toString();
        String fileName = UUID.randomUUID().toString().replace("-", "") + "_" + originalFileName;

        Files.write(uploadPath.resolve(fileName), file.getBytes());

        indicator.setOriginalFileName(originalFileName);
        indicator.setFileName(fileName);
    }

    private List<OdsGoal> resolveOdsGoals(IndicatorFormDto dto) {
        if (dto.getOds() == null || dto.getOds().isEmpty()) return Collections.emptyList();
        return odsGoalRepository.findByOrderIn(dto.getOds());
    }

    private List<MeasuresRelationship> resolveMeasures(IndicatorFormDto dto) {
        List<String> challengeIds = dto.getOrganizationAcronym()
                .stream()
                .map(IndicatorFormDto.ChallengeOrgan::getChallengeId).collect(Collectors.toList());

        List<Challenge> challenges = challengeRepository.findAllById(challengeIds);

        return challenges.stream().flatMap(challenge -> dto.getOrganizationAcronym()
                .stream()
                .filter(org ->
                        org.getChallengeId()
                                .equals(challenge.getId()))
                .findFirst().map(org -> {
                    MeasuresRelationship measure = new MeasuresRelationship();
                    measure.setChallenge(challenge);
                    measure.setOrganizationAcronym(org.getOrgan());
                    return measure;
                }).stream()).collect(Collectors.toList());
    }

    private List<Time> resolveTimes(IndicatorFormDto dto) {
        if (dto.getTimes() == null) return Collections.emptyList();
        return dto.getTimes().stream().map(this::toTime).collect(Collectors.toList());
    }

    private Time toTime(TimeDto t) {
        Time time = new Time();
        time.setType(t.getType() != null ? t.getType() : "ANUAL");
        time.setYear(t.getYear());
        time.setPeriod(t.getPeriod());
        time.setValueGoal(t.getValueGoal());
        time.setShowValueGoal(t.getShowValueGoal());
        time.setValueResult(t.getValueResult());
        time.setShowValueResult(t.getShowValueResult());
        time.setJustificationGoal(t.getJustificationGoal());
        time.setJustificationResult(t.getJustificationResult());
        return time;
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

        // if (dto.getJustificationGoal() == null ||
        // dto.getJustificationGoal().isEmpty()) {
        // existingIndicator.setJustificationGoal(null);
        // } else {
        // existingIndicator.setJustificationGoal(dto.getJustificationGoal());
        // }

        if (dto.getObservations() == null || dto.getObservations().isEmpty()) {
            existingIndicator.setObservations(null);
        } else {
            existingIndicator.setObservations(dto.getObservations());
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

        List<OdsGoal> odsGoals = dto.getOds() == null || dto.getOds().isEmpty() ? Collections.emptyList() : odsGoalRepository.findByOrderIn(dto.getOds());

        List<String> challengeIds = dto.getOrganizationAcronym().stream().map(org -> org.getChallengeId()).collect(Collectors.toList());

        List<Challenge> challenges = challengeRepository.findAllById(challengeIds);

        List<MeasuresRelationship> measures = new ArrayList<>();
        for (Challenge challenge : challenges) {
            dto.getOrganizationAcronym().stream().filter(org -> org.getChallengeId().equals(challenge.getId())).findFirst().ifPresent(challengeOrgan -> {
                MeasuresRelationship measure = new MeasuresRelationship();
                measure.setChallenge(challenge);
                measure.setOrganizationAcronym(challengeOrgan.getOrgan());
                measures.add(measure);
            });
        }
        /*
         * List<TargetAndResultRelation> targetsFor =
         * createTargetAndResultRelations(dto.getTargetsFor());
         * List<TargetAndResultRelation> resultedIn =
         * createTargetAndResultRelations(dto.getResultedIn());
         */

        existingIndicator.setMeasures(measures);
        existingIndicator.setOdsgoal(odsGoals);

        List<Time> times = dto.getTimes() == null ? Collections.emptyList() : dto.getTimes().stream().map(t -> {
            Time time = new Time();
            time.setType(t.getType() != null ? t.getType() : "YEAR");
            time.setYear(t.getYear());
            time.setPeriod(t.getPeriod());
            time.setValueGoal(t.getValueGoal());
            time.setShowValueGoal(t.getShowValueGoal());
            time.setValueResult(t.getValueResult());
            time.setShowValueResult(t.getShowValueResult());
            time.setJustificationGoal(t.getJustificationGoal());
            time.setJustificationResult(t.getJustificationResult()); //add
            return time;
        }).collect(Collectors.toList());

        existingIndicator.setMeasures(measures);
        existingIndicator.setOdsgoal(odsGoals);
        existingIndicator.setTimes(times);

        indicatorRepository.save(existingIndicator);
    }

    /*
     * private List<TargetAndResultRelation>
     * createTargetAndResultRelations(List<TargetResultDto> values) {
     * if (values == null || values.isEmpty()) {
     * return Collections.emptyList();
     * }
     *
     * List<TargetAndResultRelation> relations = new ArrayList<>();
     *
     * for (TargetResultDto value : values) {
     * Time time = timeRepository.findByYear(value.year());
     *
     * TargetAndResultRelation relation = new TargetAndResultRelation();
     * relation.setTime(time);
     * relation.setValue(value.value());
     * relation.setShowValue(value.showValue());
     * // relation.setJustificationGoal(value.justificationGoal());
     *
     * relations.add(relation);
     * }
     * return relations;
     * } ---------> nao existe mais
     */

    public void deleteIndicator(String indicatorId) throws Exception {
        Indicator indicator = indicatorRepository.findById(indicatorId).orElseThrow(() -> new IllegalArgumentException("Administração com ID " + indicatorId + " não encontrada."));

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
