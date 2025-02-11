package br.gov.es.indicadores.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import br.gov.es.indicadores.dto.OrganizerDto;
import br.gov.es.indicadores.dto.OrganizerItemDto;
import br.gov.es.indicadores.dto.OrganizerItemStructureDto;
import br.gov.es.indicadores.dto.AdministrationDto;
import br.gov.es.indicadores.dto.ChallengeDto;
import br.gov.es.indicadores.dto.CountOrganizerDto;
import br.gov.es.indicadores.dto.CountOrganizerListDto;
import br.gov.es.indicadores.dto.IndicatorDto;
import br.gov.es.indicadores.dto.OrganizerAdminDto;
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
            List<Organizer> childrenOrganizers = organizerRepository.getChildrenOrganizers(organizer.getId());
            organizer.setChildren(childrenOrganizers);
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

    public List<OrganizerAdminDto> getOrganizers(AdministrationDto administration) throws Exception {
        
        List<OrganizerAdminDto> allOrganizerDtos = new ArrayList<>();

        List<Organizer> organizerData = organizerRepository.getorganizersByAdministrationList(administration.getId());
        if (organizerData.isEmpty()) {
            return allOrganizerDtos; 
        }

        allOrganizerDtos.addAll(buildChildrenTree(administration.getModelName(), administration.getModelNameInPlural(), organizerData));

        return allOrganizerDtos;

    }


    private List<OrganizerAdminDto> buildChildrenTree(List<String> modelNames, List<String> modelNamesInPlural, List<Organizer> parentList) {
        List<OrganizerAdminDto> childrenDtos = new ArrayList<>();

        List<String> childModelNames = new ArrayList<>();
        List<String> childModelNamesPlural = new ArrayList<>();

        if(modelNames != null && modelNamesInPlural != null){
            childModelNames = new ArrayList<>(modelNames);
            childModelNamesPlural = new ArrayList<>(modelNamesInPlural);
            if(!modelNames.isEmpty() && !modelNamesInPlural.isEmpty()){
                childModelNames.remove(0);
                childModelNamesPlural.remove(0);
            }
        }


            for (Organizer parent : parentList) {

                List<Organizer> childrenOrganizers = organizerRepository.getChildrenOrganizers(parent.getId());

                OrganizerAdminDto childDto = new OrganizerAdminDto();
                childDto.setNameOrganizer(parent.getName());
                childDto.setIdOrganizer(parent.getId()); 
                childDto.setTypeOrganizer(
                    (modelNames != null && !modelNames.isEmpty() && modelNames.get(0) != null) 
                        ? modelNames.get(0) 
                        : parent.getModelName()
                );
                childDto.setTypeOrganizerPlural(
                    (modelNames != null && !modelNames.isEmpty() && modelNames.get(0) != null) && 
                    (modelNamesInPlural != null && !modelNamesInPlural.isEmpty() && modelNamesInPlural.get(0) != null)  
                        ? modelNamesInPlural.get(0) 
                        : parent.getModelNameInPlural()
                );

                if(!childrenOrganizers.isEmpty()){
                    childDto.setChildren(buildChildrenTree(childModelNames, childModelNamesPlural, childrenOrganizers));
                }else{
                    List<Challenge> challengeList = challengeRepository.getChallengeByOrganizer(parent.getId());
                    childDto.setChallengeList(challengeList);
                }
                
                childrenDtos.add(childDto);
            }
            return childrenDtos;
    }
    
    
    public void createOrganizers(List<OrganizerItemDto> organizerDtoList, String administrationId) throws Exception {
        Administration administration = administrationRepository.findById(administrationId)
            .orElseThrow(() -> new RuntimeException("Administração não encontrada"));
    
        for (OrganizerItemDto dto : organizerDtoList) {
            Organizer organizer = new Organizer();
    
            organizer.setName(dto.getName());
            organizer.setDescription(dto.getDescription());
            organizer.setIcon(dto.getIcon().isEmpty() ? null : dto.getIcon());
            organizer.setAdministration(administration);
    
            organizerRepository.save(organizer); 
    
        }
    }

    public void createOrganizerChildren(List<OrganizerItemDto> organizerDtoList, String parentOrganizerId) throws Exception {
        Organizer parentOrganizer = organizerRepository.findById(parentOrganizerId)
            .orElseThrow(() -> new RuntimeException("Organizador pai não encontrado"));
    
        for (OrganizerItemDto dto : organizerDtoList) {
            Organizer childOrganizer = new Organizer();
    
            childOrganizer.setName(dto.getName());
            childOrganizer.setDescription(dto.getDescription());
            childOrganizer.setIcon(dto.getIcon().isEmpty() ? null : dto.getIcon());
            childOrganizer.setParentOrganizer(parentOrganizer); 
    
            organizerRepository.save(childOrganizer);
    
        }
    }
    
    public void deleteOrganizer(String uuId) throws Exception {
        Organizer organizer = organizerRepository.findByUuId(uuId)
        .orElseThrow(() -> new RuntimeException("Organizador não encontrado"));

        boolean hasChildren = organizerRepository.existsChildrenByOrganizerId(uuId);

        if (hasChildren) {
            throw new IllegalStateException("O organizador não pode ser excluído porque ainda possui dependências.");
        }
    
        organizerRepository.delete(organizer);
    }
    

    public OrganizerItemDto getOrganizer(String organizerId) throws Exception {
    
        Organizer organizer = organizerRepository.findByUuId(organizerId)
            .orElseThrow(() -> new RuntimeException("Organizador não encontrado"));

        OrganizerItemDto organizerItemDto = new OrganizerItemDto();
    
        organizerItemDto.setName(organizer.getName());
        organizerItemDto.setDescription(organizer.getDescription());
        organizerItemDto.setIcon(organizer.getIcon());
    
    
        return organizerItemDto;
    }
    
}
