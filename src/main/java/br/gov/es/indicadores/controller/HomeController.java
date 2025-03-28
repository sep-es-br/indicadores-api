package br.gov.es.indicadores.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.gov.es.indicadores.dto.GeneralIndicatorsDto;
import br.gov.es.indicadores.dto.OdsDto;
import br.gov.es.indicadores.model.Administration;
import br.gov.es.indicadores.service.HomeService;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = { "${frontend.painel}", "${frontend.admin}" })
@RestController
@RequestMapping("/home-info")
@RequiredArgsConstructor
public class HomeController {

    private final HomeService service;

     @GetMapping("/general/{administrationId}")
    public GeneralIndicatorsDto listarSelect(@PathVariable String administrationId) {
        return service.getData(administrationId);
    }

    @GetMapping("/administrations")
    public List<Administration> administrations() {
        return service.administrationList(); 
    }

    @GetMapping("/ods")
    public OdsDto ods(@RequestParam Integer order) {
        return service.getOdsAndOdsGoal(order);
    }

    
}
