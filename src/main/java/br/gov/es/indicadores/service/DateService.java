package br.gov.es.indicadores.service;

import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
public class DateService {
    public int getCurrentYear() {
        return LocalDate.now().getYear();
    }
}