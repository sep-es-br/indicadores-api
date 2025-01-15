package br.gov.es.indicadores.service;

import java.time.Year;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.integration.IntegrationProperties.Management;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.gov.es.indicadores.dto.AdministrationDto;
import br.gov.es.indicadores.model.Administration;
import br.gov.es.indicadores.repository.AdministrationRepository;

@Service
public class ManagementService {

    @Autowired
    private AdministrationRepository administrationRepository;

     public Page<Administration> administrationList(Pageable pageable, String search){
        return administrationRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(search, search, pageable);
    }

    public void createManagement(AdministrationDto administrationDto) {
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

    public void deleteManagement(String administrationId) {

        Administration administration = administrationRepository.getAdministrationByUuId(administrationId);
        
        if (administration == null) {
            throw new IllegalArgumentException("Administração com ID " + administrationId + " não encontrada.");
        }

        administrationRepository.delete(administration);
    }

}
