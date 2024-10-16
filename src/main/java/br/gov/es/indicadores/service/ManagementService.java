package br.gov.es.indicadores.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.gov.es.indicadores.model.Administration;
import br.gov.es.indicadores.repository.AdministrationRepository;

@Service
public class ManagementService {

    @Autowired
    private AdministrationRepository administrationRepository;

     public Page<Administration> administrationList(Pageable pageable, String search){
        return administrationRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(search, search, pageable);
    }
}
