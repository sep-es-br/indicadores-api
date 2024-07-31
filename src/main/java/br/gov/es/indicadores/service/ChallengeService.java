package br.gov.es.indicadores.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.es.indicadores.model.Area;
import br.gov.es.indicadores.model.Challenge;
import br.gov.es.indicadores.repository.ChallengeRepository;

@Service
public class ChallengeService {

    @Autowired
    private ChallengeRepository repository;

    public Challenge[] getChallengeByArea(Area area){
        return repository.getChallengeByArea(area.getId());
    }
    
}
