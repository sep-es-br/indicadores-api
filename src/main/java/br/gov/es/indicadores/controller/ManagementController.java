package br.gov.es.indicadores.controller;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import br.gov.es.indicadores.dto.AdministrationDto;
import br.gov.es.indicadores.exception.mensagens.MensagemErroRest;
import br.gov.es.indicadores.model.Administration;
import br.gov.es.indicadores.service.ManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;



@CrossOrigin(origins = { "${frontend.painel}", "${frontend.admin}" })
@RestController
@RequestMapping("/management")
@RequiredArgsConstructor
public class ManagementController {

    private final ManagementService managementService;

    @GetMapping("/{administrationId}")
    public AdministrationDto getAdministrationWithChallenges(@PathVariable String administrationId) throws Exception {
        return managementService.getAdministrationWithChallenges(administrationId);
    }

    @GetMapping("/list")
    public List<Administration> administrationList() {
        return managementService.administrationList();
    }
    
    @GetMapping()
    public ResponseEntity<?> administrationPage(@PageableDefault(size = 15, sort = "name") Pageable pageable, @RequestParam(required = false) String search) {
        try{
            Page<AdministrationDto> administrationPage = managementService.administrationPage(pageable, search);
            return ResponseEntity.ok(administrationPage);
        } catch(Exception ex){
        MensagemErroRest error = new MensagemErroRest(
            HttpStatus.INTERNAL_SERVER_ERROR, "Ocorreu um erro ao criar a gestão", Collections.singletonList(ex.getLocalizedMessage()));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @PostMapping
    public ResponseEntity<?> createManagement(@Validated @RequestBody Administration management) {
        try{
            managementService.createManagement(management);
            return ResponseEntity.ok().build();
        } catch(Exception ex){
            MensagemErroRest error = new MensagemErroRest(
                HttpStatus.INTERNAL_SERVER_ERROR, "Ocorreu um erro ao criar a gestão", Collections.singletonList(ex.getLocalizedMessage()));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @DeleteMapping("/{administrationId}")
    public ResponseEntity<?> deleteManagement(@PathVariable String administrationId) {
        try{
            managementService.deleteManagement(administrationId);
            return ResponseEntity.noContent().build(); 
        } catch(Exception ex){
            MensagemErroRest error = new MensagemErroRest(
                HttpStatus.INTERNAL_SERVER_ERROR, "Ocorreu um erro ao deletar a gestão", Collections.singletonList(ex.getLocalizedMessage()));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @PutMapping
    public ResponseEntity<?> updateManagement(@Validated @RequestBody Administration managementDto) {
        try{
            managementService.updateManagement(managementDto);
            return ResponseEntity.ok().build();
        } catch(Exception ex){
            MensagemErroRest error = new MensagemErroRest(
                HttpStatus.INTERNAL_SERVER_ERROR, "Ocorreu um erro ao atualizar a gestão", Collections.singletonList(ex.getLocalizedMessage()));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

}