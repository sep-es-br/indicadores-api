package br.gov.es.indicadores.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.gov.es.indicadores.dto.ChallengeDto;
import br.gov.es.indicadores.dto.IndicatorDto;
import br.gov.es.indicadores.dto.OverviewAreaDto;
import br.gov.es.indicadores.service.ChallengeService;
import br.gov.es.indicadores.service.IndicatorService;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = { "${frontend.painel}", "${frontend.admin}" })
@RestController
@RequestMapping("/indicator")
@RequiredArgsConstructor
public class IndicatorController {

    private final IndicatorService indicatorService;
    
    @GetMapping("/detail/{idChallenge}")
    public IndicatorDto[] listIndicatorsByChallengeId(@PathVariable String idChallenge) {
        return indicatorService.getListIndicatorsByChallengeId(idChallenge);
    }
}