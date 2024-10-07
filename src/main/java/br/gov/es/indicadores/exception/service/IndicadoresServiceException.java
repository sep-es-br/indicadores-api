package br.gov.es.indicadores.exception.service;

import lombok.Getter;

import java.util.List;

@Getter
public class IndicadoresServiceException extends RuntimeException {

    private final List<String> errors;

    public IndicadoresServiceException(List<String> errors) {
        this.errors = errors;
    }
}
