package br.gov.es.indicadores.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.gov.es.indicadores.dto.AreaDto;
import br.gov.es.indicadores.dto.OverviewAreaDto;
import br.gov.es.indicadores.service.AreaService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/area")
@RequiredArgsConstructor
public class AreaController {

    private final AreaService service;
    
    @GetMapping("/detail/{idArea}")
    public AreaDto listarSelect(@PathVariable Long idArea) {
        return service.getAreaDto(idArea);
    }

    @GetMapping
    public OverviewAreaDto[] getAllAreaDtos(){
        return service.getAll();
    }
}
