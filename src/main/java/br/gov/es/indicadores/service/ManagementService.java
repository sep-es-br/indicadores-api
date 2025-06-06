package br.gov.es.indicadores.service;

import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.integration.IntegrationProperties.Management;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.gov.es.indicadores.dto.AdministrationDto;
import br.gov.es.indicadores.dto.OrganizerAdminDto;
import br.gov.es.indicadores.dto.OrganizerDto;
import br.gov.es.indicadores.model.Administration;
import br.gov.es.indicadores.model.Challenge;
import br.gov.es.indicadores.model.Organizer;
import br.gov.es.indicadores.repository.AdministrationRepository;
import br.gov.es.indicadores.repository.OrganizerRepository;

@Service
public class ManagementService {

    @Autowired
    private AdministrationRepository administrationRepository;

    @Autowired
    private OrganizerService organizerService;

    public AdministrationDto getAdministrationWithChallenges(String administrationId) throws Exception {

        Administration administration = administrationRepository.getAdministrationByUuId(administrationId);
    
        AdministrationDto administrations = new AdministrationDto(administration);
    
        List<OrganizerAdminDto> organizerAdmin = organizerService.getOrganizers(administrations);
    
        administrations.setOrganizerList(organizerAdmin);
    
        return administrations;
    }

    public List<Administration> administrationList(){
        return administrationRepository.getAllAdministration().stream()
        .sorted((a1, a2) ->  a2.getName().compareTo(a1.getName())) 
        .collect(Collectors.toList());
    }

    public Page<AdministrationDto> administrationPage(Pageable pageable, String search) throws Exception {

        Page<AdministrationDto> administrationPage = administrationRepository.findByNameContainingIgnoreCase("", pageable);

        List<AdministrationDto> validAdministrations = new ArrayList<>();

        for (AdministrationDto adminDto : administrationPage) {

            List<OrganizerAdminDto> organizerAdmin = organizerService.getOrganizers(adminDto);

            if (search.isEmpty()) {
                adminDto.setOrganizerList(organizerAdmin);
                validAdministrations.add(adminDto);
            } else {
                organizerAdmin = filterOrganizers(organizerAdmin, search);
    
                if (!organizerAdmin.isEmpty()) {
                    adminDto.setOrganizerList(organizerAdmin);
                    validAdministrations.add(adminDto);
                }
            }
        }

        return new PageImpl<>(validAdministrations, pageable, validAdministrations.size());
    }

    private boolean hasMatchingChildren(List<OrganizerAdminDto> children, String search) {
        if (children == null || children.isEmpty()) {
            return false;
        }
    
        boolean hasMatchingDescendant = false;
        for (OrganizerAdminDto child : children) {
            boolean matchesChild = (child.getNameOrganizer() != null && child.getNameOrganizer().toLowerCase().contains(search.toLowerCase()))
                    || (child.getTypeOrganizer() != null && child.getTypeOrganizer().toLowerCase().contains(search.toLowerCase()))
                    || hasMatchingChallenges(child.getChallengeList(), search); 
    
            if (matchesChild) {
                child.setChildren(filterOrganizers(child.getChildren(), search)); 
                child.setChallengeList(filterChallenges(child.getChallengeList(), search)); 
                hasMatchingDescendant = true;
            } else {
                boolean matchesChildren = hasMatchingChildren(child.getChildren(), search); 
                if (matchesChildren) {
                    child.setChildren(filterOrganizers(child.getChildren(), search)); 
                    child.setChallengeList(filterChallenges(child.getChallengeList(), search)); 
                    hasMatchingDescendant = true;
                } else {
                    child.setChildren(null);
                    child.setChallengeList(null);
                }
            }
        }
    
        return hasMatchingDescendant;
    }
    
    private boolean hasMatchingChallenges(List<Challenge> challenges, String search) {
        if (challenges == null || challenges.isEmpty()) {
            return false;
        }
    
        return challenges.stream()
                .anyMatch(challenge -> challenge.getName() != null && challenge.getName().toLowerCase().contains(search.toLowerCase()));
    }
    
    private List<Challenge> filterChallenges(List<Challenge> challenges, String search) {
        if (challenges == null || challenges.isEmpty()) {
            return new ArrayList<>();
        }
    
        return challenges.stream()
                .filter(challenge -> challenge.getName() != null && challenge.getName().toLowerCase().contains(search.toLowerCase()))
                .collect(Collectors.toList());
    }
    
    private List<OrganizerAdminDto> filterOrganizers(List<OrganizerAdminDto> allOrganizerDtos, String search) {
        if (allOrganizerDtos == null || allOrganizerDtos.isEmpty()) {
            return new ArrayList<>();
        }
    
        return allOrganizerDtos.stream()
                .filter(dto -> {
                    boolean matchesParent = (dto.getNameAdministration() != null && dto.getNameAdministration().toLowerCase().contains(search.toLowerCase()))
                            || (dto.getNameOrganizer() != null && dto.getNameOrganizer().toLowerCase().contains(search.toLowerCase()))
                            || hasMatchingChallenges(dto.getChallengeList(), search); // Verifica os challenges do pai
    
                    boolean matchesChildren = hasMatchingChildren(dto.getChildren(), search); // Verifica os filhos
    
                    return matchesParent || matchesChildren;
                })
                .map(dto -> {
                    if (dto.getChildren() != null) {
                        if (!dto.getNameOrganizer().toLowerCase().contains(search.toLowerCase())) {
                            dto.setChildren(filterOrganizers(dto.getChildren(), search)); // Filtra os filhos recursivamente
                        }
                    }
                    if (dto.getChallengeList() != null) {
                        dto.setChallengeList(filterChallenges(dto.getChallengeList(), search)); // Filtra os challenges
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }
    
    public void updateManagement(Administration administration) throws Exception{

        Administration existingAdministration = administrationRepository.findById(administration.getId())
        .orElseThrow(() -> new IllegalArgumentException("Administração com ID " + administration.getId() + " não encontrada."));
        

        if (administration.getName() != null) {
            existingAdministration.setName(administration.getName());
        }
        if (administration.getActive() != null) {
            if (administration.getActive()) {
                Administration activeAdministration = administrationRepository.findByActiveTrue();
                if (activeAdministration != null) {
                    activeAdministration.setActive(false);
                    administrationRepository.save(activeAdministration);
                }
            }
            existingAdministration.setActive(administration.getActive());
        }

        if (administration.getStartYear() != null) {
            existingAdministration.setStartYear(administration.getStartYear());
        }
        if (administration.getEndYear() != null) {
            existingAdministration.setEndYear(administration.getEndYear());
        }
        if (administration.getDescription() != null) {
            existingAdministration.setDescription(administration.getDescription());
        }
        if(administration.getModelName() != null || !administration.getModelName().isEmpty()){
            existingAdministration.setModelName(administration.getModelName());
        }
        if(administration.getModelNameInPlural() != null || !administration.getModelNameInPlural().isEmpty()){
            existingAdministration.setModelNameInPlural(administration.getModelNameInPlural());
        }

        administrationRepository.save(existingAdministration);

    }

    public void createManagement(Administration administration) throws Exception {
    if (administration.getName() == null || 
        administration.getDescription() == null || 
        administration.getStartYear() == null || 
        administration.getEndYear() == null || 
        administration.getModelName().isEmpty() || 
        administration.getModelNameInPlural().isEmpty()) {
        throw new IllegalArgumentException("Os campos obrigatórios não podem ser nulos.");
    }

    int currentYear = Year.now().getValue();

    boolean isActive = currentYear >= administration.getStartYear() && currentYear <= administration.getEndYear();

    administration.setActive(isActive);

    if (isActive) {
        Administration activeAdministration = administrationRepository.findByActiveTrue();
        if (activeAdministration != null) {
            activeAdministration.setActive(false);
            administrationRepository.save(activeAdministration);
        }
    }

    administrationRepository.save(administration);

    }

    public void deleteManagement(String administrationId) throws Exception {
        Administration administration = administrationRepository.findById(administrationId)
            .orElseThrow(() -> new IllegalArgumentException("Administração com ID " + administrationId + " não encontrada."));
    
        boolean hasOrganizer = administrationRepository.existsOrganizerByAdministrationId(administration.getId());
    
        if (hasOrganizer) {
            throw new IllegalStateException("A administração não pode ser excluída porque ainda está associada a um organizador.");
        }
    
        administrationRepository.delete(administration);
    }
    

}
