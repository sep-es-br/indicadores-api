package br.gov.es.indicadores.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.gov.es.indicadores.dto.IndicatorAdminDto;
import br.gov.es.indicadores.dto.IndicatorDto;
import br.gov.es.indicadores.dto.IndicatorFormDto;
import br.gov.es.indicadores.dto.ManagementOrganizerChallengeDto;
import br.gov.es.indicadores.dto.OdsDto;
import br.gov.es.indicadores.dto.OrganizerItemDto;
import br.gov.es.indicadores.exception.mensagens.MensagemErroRest;
import br.gov.es.indicadores.model.Administration;
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

    @GetMapping("/ods-list")
    public List<OdsDto> getOds() {
        return indicatorService.getOds();
    }

    @GetMapping("/year-list")
    public List<Integer> getAllYears() {
        return indicatorService.getAllYears();
    }

    @GetMapping("/getIndicator/{indicatorId}")
    public ResponseEntity<?> getIndicator(@PathVariable String indicatorId) {
        try {
            IndicatorAdminDto indicator = indicatorService.getOIndicator(indicatorId);

            return ResponseEntity.ok(indicator);
        } catch (Exception ex) {
            MensagemErroRest error = new MensagemErroRest(
                HttpStatus.INTERNAL_SERVER_ERROR, "Ocorreu um erro ao recuperar o indicador", 
                Collections.singletonList(ex.getLocalizedMessage())
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/getIndicators")
    public ResponseEntity<?> getIndicators() {
        try {
            List<IndicatorAdminDto> indicator = indicatorService.getIndicators();

            return ResponseEntity.ok(indicator);
        } catch (Exception ex) {
            MensagemErroRest error = new MensagemErroRest(
                HttpStatus.INTERNAL_SERVER_ERROR, "Ocorreu um erro ao recuperar o indicador", 
                Collections.singletonList(ex.getLocalizedMessage())
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    
    @PostMapping
    public ResponseEntity<?> createIndicator(@Validated @RequestBody IndicatorFormDto indicador) {
        try{
            indicatorService.createIndicator(indicador);
            return ResponseEntity.ok().build();
        } catch(Exception ex){
            MensagemErroRest error = new MensagemErroRest(
                HttpStatus.INTERNAL_SERVER_ERROR, "Ocorreu um erro ao criar o indicador", Collections.singletonList(ex.getLocalizedMessage()));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @PutMapping
    public ResponseEntity<?> updateIndicator(@Validated @RequestBody IndicatorFormDto indicador) {
        try{
            indicatorService.updateIndicator(indicador);
            return ResponseEntity.ok().build();
        } catch(Exception ex){
            MensagemErroRest error = new MensagemErroRest(
                HttpStatus.INTERNAL_SERVER_ERROR, "Ocorreu um erro ao atualizar o indicador", Collections.singletonList(ex.getLocalizedMessage()));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @DeleteMapping("/{indicatorId}")
    public ResponseEntity<?> deleteIndicator(@PathVariable String indicatorId) {
        try{
            indicatorService.deleteIndicator(indicatorId);
            return ResponseEntity.noContent().build(); 
        } catch(Exception ex){
            MensagemErroRest error = new MensagemErroRest(
                HttpStatus.INTERNAL_SERVER_ERROR, "Ocorreu um erro ao deletar o indicador", Collections.singletonList(ex.getLocalizedMessage()));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }


}