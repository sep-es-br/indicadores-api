package br.gov.es.indicadores.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.gov.es.indicadores.dto.AreaDto;
import br.gov.es.indicadores.dto.ChallengeDto;
import br.gov.es.indicadores.service.ChallengeService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/challenge")
@RequiredArgsConstructor
public class ChallegeController {

    private final ChallengeService challengeService;
    
    @GetMapping("/detail/{idChallenge}")
    public ChallengeDto listarSelect(@PathVariable String challengeUuId) {
        return challengeService.getChallengeDto(challengeUuId);
    }
}