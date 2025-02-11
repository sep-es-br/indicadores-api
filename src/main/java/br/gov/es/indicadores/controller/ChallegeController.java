package br.gov.es.indicadores.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.gov.es.indicadores.dto.ChallengeDto;
import br.gov.es.indicadores.dto.OrganizerItemDto;
import br.gov.es.indicadores.exception.mensagens.MensagemErroRest;
import br.gov.es.indicadores.model.Challenge;
import br.gov.es.indicadores.service.ChallengeService;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = { "${frontend.painel}", "${frontend.admin}" })
@RestController
@RequestMapping("/challenge")
@RequiredArgsConstructor
public class ChallegeController {

    private final ChallengeService challengeService;
    
    @GetMapping("/detail/{ChallengeId}")
    public ChallengeDto listarSelect(@PathVariable String ChallengeId) {
        return challengeService.getChallengeDto(ChallengeId);
    }

    @GetMapping("/active")
    public ResponseEntity<List<Challenge>> findChallengesByActiveAdministration() {
        List<Challenge> challenges = challengeService.findChallengesByActiveAdministration();
        return ResponseEntity.ok(challenges);
    }

        @PostMapping("/{organizerId}")
        public ResponseEntity<?> createChallenge(@Validated @RequestBody List<Challenge> challengeList, @PathVariable String organizerId) {
        try{
            challengeService.createChallenge(challengeList, organizerId);
            return ResponseEntity.ok().build();
        } catch(Exception ex){
            MensagemErroRest error = new MensagemErroRest(
                HttpStatus.INTERNAL_SERVER_ERROR, "Ocorreu um erro ao criar a gestão", Collections.singletonList(ex.getLocalizedMessage()));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

}
