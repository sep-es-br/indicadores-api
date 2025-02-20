package br.gov.es.indicadores.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.gov.es.indicadores.dto.IndicatorAdminDto;
import br.gov.es.indicadores.dto.IndicatorDto;
import br.gov.es.indicadores.dto.ManagementOrganizerChallengeDto;
import br.gov.es.indicadores.exception.mensagens.MensagemErroRest;
import br.gov.es.indicadores.service.IndicatorService;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = { "${frontend.painel}", "${frontend.admin}" })
@RestController
@RequestMapping("/indicator")
@RequiredArgsConstructor
public class IndicatorController {

    private final IndicatorService indicatorService;

    @GetMapping()
    public ResponseEntity<?> indicatorPage(@PageableDefault(size = 15, sort = "name") Pageable pageable, @RequestParam(required = false) String search) {
        try{
            Page<IndicatorAdminDto> indicatorPage = indicatorService.indicatorPage(pageable, search);
            return ResponseEntity.ok(indicatorPage);
        } catch(Exception ex){
        MensagemErroRest error = new MensagemErroRest(
            HttpStatus.INTERNAL_SERVER_ERROR, "Ocorreu um erro ao listar os indicadores", Collections.singletonList(ex.getLocalizedMessage()));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    
    
    @GetMapping("/detail/{idChallenge}")
    public IndicatorDto[] listIndicatorsByChallengeId(@PathVariable String idChallenge) {
        return indicatorService.getListIndicatorsByChallengeId(idChallenge);
    }

    @GetMapping("/getManagementOrganizerChallenges")
    public ResponseEntity<?> findManagementOrganizerChallenges() {
        try{
            List<ManagementOrganizerChallengeDto> managementOrganizerChallenges = indicatorService.findManagementOrganizerChallenges();
            return ResponseEntity.ok(managementOrganizerChallenges);
        } catch(Exception ex){
        MensagemErroRest error = new MensagemErroRest(
            HttpStatus.INTERNAL_SERVER_ERROR, "Ocorreu um erro ao listar os desafios", Collections.singletonList(ex.getLocalizedMessage()));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/measure-units")
    public List<String> getDistinctMeasureUnits() {
        return indicatorService.getDistinctMeasureUnits();
    }

    @GetMapping("/organization-acronym")
    public List<String> getDistinctOrganizationAcronyms() {
        return indicatorService.getDistinctOrganizationAcronyms();
    }

}