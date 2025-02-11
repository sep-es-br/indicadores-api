package br.gov.es.indicadores.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.gov.es.indicadores.dto.AdministrationDto;
import br.gov.es.indicadores.dto.CountOrganizerDto;
import br.gov.es.indicadores.dto.OrganizerAdminDto;
import br.gov.es.indicadores.dto.OrganizerDto;
import br.gov.es.indicadores.dto.OrganizerItemDto;
import br.gov.es.indicadores.dto.OrganizerItemStructureDto;
import br.gov.es.indicadores.dto.OverviewOrganizerDto;
import br.gov.es.indicadores.exception.mensagens.MensagemErroRest;
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
    public OrganizerDto selectList(@PathVariable String organizerUuId) {
        return service.getOrganizerDto(organizerUuId);
    }

    @GetMapping("/{organizerUuId}")
    public Map<String, OverviewOrganizerDto[]> getAllOrganizerDtos(@PathVariable String organizerUuId){
        return service.getAll(organizerUuId);
    }

    @GetMapping("/getOrganizerStructure/{organizerUuId}")
    public ResponseEntity<?> getOrganizer(@PathVariable String organizerUuId) {
        try {
            OrganizerItemDto organizerStructure = service.getOrganizer(organizerUuId);

            return ResponseEntity.ok(organizerStructure);
        } catch (Exception ex) {
            MensagemErroRest error = new MensagemErroRest(
                HttpStatus.INTERNAL_SERVER_ERROR, "Ocorreu um erro ao recuperar o organizador", 
                Collections.singletonList(ex.getLocalizedMessage())
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @PostMapping("/{administrationId}")
    public ResponseEntity<?> createOrganizers(@Validated @RequestBody List<OrganizerItemDto> organizerDto, @PathVariable String administrationId) {
        try{
            service.createOrganizers(organizerDto, administrationId);
            return ResponseEntity.ok().build();
        } catch(Exception ex){
            MensagemErroRest error = new MensagemErroRest(
                HttpStatus.INTERNAL_SERVER_ERROR, "Ocorreu um erro ao criar a gestão", Collections.singletonList(ex.getLocalizedMessage()));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @PostMapping("/{parentOrganizerId}/children")
    public ResponseEntity<?> createOrganizerChildren(@Validated @RequestBody List<OrganizerItemDto> organizerDto, @PathVariable String parentOrganizerId) {
        try{
            service.createOrganizerChildren(organizerDto, parentOrganizerId);
            return ResponseEntity.ok().build();
        } catch(Exception ex){
            MensagemErroRest error = new MensagemErroRest(
                HttpStatus.INTERNAL_SERVER_ERROR, "Ocorreu um erro ao criar a gestão", Collections.singletonList(ex.getLocalizedMessage()));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }


    @DeleteMapping("/{organizerId}")
    public ResponseEntity<?> deleteManagement(@PathVariable String organizerId) {
        try{
            service.deleteOrganizer(organizerId);
            return ResponseEntity.noContent().build(); 
        } catch(Exception ex){
            MensagemErroRest error = new MensagemErroRest(
                HttpStatus.INTERNAL_SERVER_ERROR, "Ocorreu um erro ao deletar a gestão", Collections.singletonList(ex.getLocalizedMessage()));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}
