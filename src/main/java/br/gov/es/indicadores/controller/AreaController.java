package br.gov.es.indicadores.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.gov.es.indicadores.dto.AreaDto;
import br.gov.es.indicadores.dto.OverviewAreaDto;
import br.gov.es.indicadores.service.AreaService;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = { "${frontend.painel}", "${frontend.admin}" })
@RestController
@RequestMapping("/area")
@RequiredArgsConstructor
public class AreaController {

    private final AreaService service;
    
    @GetMapping("/detail/{areaUuId}")
    public AreaDto listarSelect(@PathVariable String areaUuId) {
        return service.getAreaDto(areaUuId);
    }

    @GetMapping("/{areaUuId}")
    public OverviewAreaDto[] getAllAreaDtos(@PathVariable String areaUuId){
        return service.getAll(areaUuId);
    }
}
