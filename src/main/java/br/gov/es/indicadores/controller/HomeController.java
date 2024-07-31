package br.gov.es.indicadores.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.gov.es.indicadores.dto.IndicadoresGeraisDto;
import br.gov.es.indicadores.service.HomeService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/home-info")
@RequiredArgsConstructor
public class HomeController {

    private final HomeService service;

     @GetMapping("/general")
    public IndicadoresGeraisDto listarSelect() {
        return service.getData();
    }
}
