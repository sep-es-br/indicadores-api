package br.gov.es.indicadores.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.es.indicadores.dto.AreaDto;
import br.gov.es.indicadores.dto.ChallengeDto;
import br.gov.es.indicadores.model.Area;
import br.gov.es.indicadores.model.Challenge;
import br.gov.es.indicadores.repository.ChallengeRepository;

@Service
public class ChallengeService {

    @Autowired
    private ChallengeRepository repository;

    public Integer challengesAmountByAdministration(String idAdministarion){
        return repository.challengesAmountByAdministration(idAdministarion);
    }

    public List<Challenge> getChallengeByArea(Area area){
        return repository.getChallengeByArea(area.getId());
    }

    public ChallengeDto getChallengeDto(String idChallenge){
        Optional<Challenge> challengeData = repository.findById(idChallenge);
        ChallengeDto challengeDto = ChallengeDto.builder()
                                 .id(challengeData.get().getId())
                                 .name(challengeData.get().getName())
                                 .build();
        return challengeDto;
    }
    
}
