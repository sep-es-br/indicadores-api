package br.gov.es.indicadores.controller;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.gov.es.indicadores.dto.OrganizerAdminDto;
import br.gov.es.indicadores.dto.OrganizerDto;
import br.gov.es.indicadores.dto.OverviewOrganizerDto;
import br.gov.es.indicadores.model.Administration;
import br.gov.es.indicadores.service.OrganizerService;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = { "${frontend.painel}", "${frontend.admin}" })
@RestController
@RequestMapping("/organizer")
@RequiredArgsConstructor
public class OrganizerController {

    private final OrganizerService service;
    
    @GetMapping("/detail/{organizerUuId}")
    public OrganizerDto listarSelect(@PathVariable String organizerUuId) {
        return service.getOrganizerDto(organizerUuId);
    }

    @GetMapping("/{organizerUuId}")
    public Map<String, OverviewOrganizerDto[]> getAllOrganizerDtos(@PathVariable String organizerUuId){
        return service.getAll(organizerUuId);
    }

    @GetMapping("getAll")
    public Page<OrganizerAdminDto> getOrganizerList(@PageableDefault(size = 15, sort = "name") Pageable pageable, @RequestParam(required = false) String search) {
        return service.getOrganizerList(pageable, search);  
    }
}
