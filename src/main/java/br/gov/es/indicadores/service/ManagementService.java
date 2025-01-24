package br.gov.es.indicadores.service;

import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.integration.IntegrationProperties.Management;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.gov.es.indicadores.dto.AdministrationDto;
import br.gov.es.indicadores.dto.OrganizerAdminDto;
import br.gov.es.indicadores.dto.OrganizerDto;
import br.gov.es.indicadores.model.Administration;
import br.gov.es.indicadores.model.Organizer;
import br.gov.es.indicadores.repository.AdministrationRepository;
import br.gov.es.indicadores.repository.OrganizerRepository;

@Service
public class ManagementService {

    @Autowired
    private AdministrationRepository administrationRepository;

     public Page<Administration> administrationList(Pageable pageable, String search){
        return administrationRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(search, search, pageable);
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
        if (administration.getAdminId() != null) {
            existingAdministration.setAdminId(administration.getAdminId());
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

        administrationRepository.save(existingAdministration);

    }

    public void createManagement(AdministrationDto administrationDto) throws Exception {
    if (administrationDto.getName() == null || 
        administrationDto.getDescription() == null || 
        administrationDto.getStartYear() == null || 
        administrationDto.getEndYear() == null) {
        throw new IllegalArgumentException("Os campos obrigatórios não podem ser nulos.");
    }

    int currentYear = Year.now().getValue();

    boolean isActive = currentYear >= administrationDto.getStartYear() && currentYear <= administrationDto.getEndYear();

    if (isActive) {
        Administration activeAdministration = administrationRepository.findByActiveTrue();
        if (activeAdministration != null) {
            activeAdministration.setActive(false);
            administrationRepository.save(activeAdministration);
        }
    }

    Administration administration = new Administration();
    administration.setName(administrationDto.getName());
    administration.setDescription(administrationDto.getDescription());
    administration.setStartYear(administrationDto.getStartYear());
    administration.setEndYear(administrationDto.getEndYear());
    administration.setActive(isActive);

    administrationRepository.save(administration);

    }

    public void deleteManagement(String administrationId) throws Exception {
        Administration administration = administrationRepository.findById(administrationId)
            .orElseThrow(() -> new IllegalArgumentException("Administração com ID " + administrationId + " não encontrada."));
    
        administrationRepository.delete(administration);
    }

}
