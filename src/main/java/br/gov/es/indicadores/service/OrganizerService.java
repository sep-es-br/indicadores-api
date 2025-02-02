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

    public Page<OrganizerAdminDto> getOrganizerList(Pageable pageable, String search) throws Exception {

        List<Administration> administrationList = administrationRepository.getAllAdministration();
    
        List<OrganizerAdminDto> allOrganizerDtos = new ArrayList<>();
    
        for (Administration administration : administrationList) {
            Organizer[] organizerData = organizerRepository.getorganizersByAdministration(administration.getId());
        
                for (Organizer organizer : organizerData) {
                    OrganizerAdminDto dto = new OrganizerAdminDto();
                    dto.setNameAdministration(administration.getName());
                    dto.setNameOrganizer(organizer.getName());
                    dto.setTypeOrganizer(organizer.getModelName());
                    dto.setIdOrganizer(organizer.getId());
        
                    dto.setChildren(buildChildrenTree(organizer, false));
        
                    allOrganizerDtos.add(dto);
                }
            }
    
        List<OrganizerAdminDto> filteredDtos = allOrganizerDtos.stream()
                .filter(dto -> {
        boolean matchesParent = dto.getNameAdministration().toLowerCase().contains(search.toLowerCase())
                || dto.getNameOrganizer().toLowerCase().contains(search.toLowerCase());

        boolean matchesChildren = dto.getChildren().stream()
                .anyMatch(child -> child.getNameOrganizer().toLowerCase().contains(search.toLowerCase())
                        || child.getTypeOrganizer().toLowerCase().contains(search.toLowerCase()));

        return matchesParent || matchesChildren;
        })
        .collect(Collectors.toList());
        
            int start = (int) pageable.getOffset();
            int end = Math.min((start + pageable.getPageSize()), filteredDtos.size());
            List<OrganizerAdminDto> paginatedDtos = filteredDtos.subList(start, end);
        
            return new PageImpl<>(paginatedDtos, pageable, filteredDtos.size());
    }

    private List<OrganizerAdminDto> buildChildrenTree(Organizer parent, boolean isStructure) {
        List<OrganizerAdminDto> childrenDtos = new ArrayList<>();
        List<Organizer> childrenOrganizers = organizerRepository.getChildrenOrganizers(parent.getId());
        
        if(!isStructure){
            for (Organizer child : childrenOrganizers) {
                OrganizerAdminDto childDto = new OrganizerAdminDto();
                childDto.setNameOrganizer(child.getName());
                childDto.setTypeOrganizer(child.getModelName());
                childDto.setTypeOrganizerPlural(child.getModelNameInPlural());
                childDto.setIdOrganizer(child.getId());
    
                childDto.setChildren(buildChildrenTree(child, false));
        
                childrenDtos.add(childDto);
            }
            return childrenDtos;
        }else{
            for (Organizer child : childrenOrganizers) {
                OrganizerAdminDto childDto = new OrganizerAdminDto();
                childDto.setTypeOrganizer(child.getModelName());
                childDto.setTypeOrganizerPlural(child.getModelNameInPlural());
    
                childDto.setChildren(buildChildrenTree(child, true));
        
                childrenDtos.add(childDto);
            }
            return childrenDtos;
        }
    }


    public OrganizerAdminDto getOrganizerListWithChildren(String administrationUuId) {
        Organizer[] organizerData = organizerRepository.getorganizersByAdministration(administrationUuId);
    
        OrganizerAdminDto selectedOrganizerDto = null;
        int maxDescendantsCount = 0;
    
        for (Organizer organizer : organizerData) {
            OrganizerAdminDto dto = new OrganizerAdminDto();
            dto.setTypeOrganizer(organizer.getModelName());
            dto.setTypeOrganizerPlural(organizer.getModelNameInPlural());
    
            List<OrganizerAdminDto> childrenDtos = buildChildrenTree(organizer,true);
            dto.setChildren(childrenDtos);
    
            int descendantsCount = countDescendants(childrenDtos);
    
            if (descendantsCount >= maxDescendantsCount) {
                maxDescendantsCount = descendantsCount;
                selectedOrganizerDto = dto;
            }
        }
    
        return selectedOrganizerDto;
    }

    private int countDescendants(List<OrganizerAdminDto> children) {
        int count = 0;
    
        for (OrganizerAdminDto child : children) {
            count++; 
            count += countDescendants(child.getChildren()); 
        }
    
        return count;
    }
    
    
    public void createOrganizers(List<OrganizerItemDto> organizerDtoList, String administrationId) throws Exception {
        Administration administration = administrationRepository.findById(administrationId)
            .orElseThrow(() -> new RuntimeException("Administração não encontrada"));
    
        for (OrganizerItemDto dto : organizerDtoList) {
            Organizer organizer = new Organizer();
    
            organizer.setName(dto.getName());
            organizer.setDescription(dto.getDescription());
            organizer.setIcon(dto.getIcon().isEmpty() ? null : dto.getIcon());
            organizer.setModelName(dto.getStructureName());
            organizer.setModelNameInPlural(dto.getStructureNamePlural());
            organizer.setAdministration(administration);
    
            organizerRepository.save(organizer); 
    
            if (dto.getChildren() != null && !dto.getChildren().isEmpty()) {
                organizer.setChildren(createChildren(dto.getChildren(), organizer)); 
                organizerRepository.save(organizer); 
            }
        }
    }
    
    private List<Organizer> createChildren(List<OrganizerItemDto> childrenDtoList, Organizer parentOrganizer) throws Exception {
        List<Organizer> children = new ArrayList<>();
    
        for (OrganizerItemDto childDto : childrenDtoList) {
            Organizer child = new Organizer();
            child.setName(childDto.getName());
            child.setDescription(childDto.getDescription());
            child.setIcon(childDto.getIcon().isEmpty() ? null : childDto.getIcon());
            child.setModelName(childDto.getStructureName());
            child.setModelNameInPlural(childDto.getStructureNamePlural());
    
            child.setParentOrganizer(parentOrganizer);
    
            organizerRepository.save(child); 
    
            if (childDto.getChildren() != null && !childDto.getChildren().isEmpty()) {
                child.setChildren(createChildren(childDto.getChildren(), child));
                organizerRepository.save(child); 
            }
    
            children.add(child);
        }
    
        return children;
    }
    
    //ajustar
    public void deleteOrganizerWithChildren(String uuId) throws Exception {
        Organizer organizer = organizerRepository.findByUuId(uuId)
        .orElseThrow(() -> new RuntimeException("Organizador não encontrado"));
    
        List<Organizer> childrenList = this.processOrganizerChildren(organizer);

        organizer.setChildren(childrenList);
    
        if (!childrenList.isEmpty()) {
            organizerRepository.deleteAll(childrenList);
        }
    
        organizerRepository.delete(organizer);
    }

    public OrganizerItemDto getOrganizerStructure(String organizerId) throws Exception {
    
        Organizer organizer = organizerRepository.findByUuId(organizerId)
            .orElseThrow(() -> new RuntimeException("Organizador não encontrado"));
    
        List<Organizer> childrenList = this.processOrganizerChildren(organizer);

        organizer.setChildren(childrenList);

        OrganizerItemDto organizerItemDtoList = mapOrganizerToDto(organizer);
    
    
        return organizerItemDtoList;
    }

    private List<Organizer> processOrganizerChildren(Organizer organizer) {
        List<Organizer> childrenList = organizerRepository.getChildrenOrganizers(organizer.getId());
    
        for (Organizer child : childrenList) {
            if (!organizerRepository.getChildrenOrganizers(child.getId()).isEmpty()) {
                processOrganizerChildren(child);
            }
        }
        organizer.setChildren(childrenList);
        return childrenList;
    }
    
    private OrganizerItemDto mapOrganizerToDto(Organizer organizer) {
        OrganizerItemDto organizerItemDto = new OrganizerItemDto();
        
        organizerItemDto.setName(organizer.getName());
        organizerItemDto.setDescription(organizer.getDescription());
        organizerItemDto.setStructureName(organizer.getModelName());
        organizerItemDto.setStructureNamePlural(organizer.getModelNameInPlural());
        organizerItemDto.setIcon(organizer.getIcon());
    
        if (organizer.getChildren() != null && !organizer.getChildren().isEmpty()) {
            List<OrganizerItemDto> childrenDtos = new ArrayList<>();
            for (Organizer child : organizer.getChildren()) {
                childrenDtos.add(mapOrganizerToDto(child));
            }
            organizerItemDto.setChildren(childrenDtos);
        }
    
        return organizerItemDto;
    }
    
}
