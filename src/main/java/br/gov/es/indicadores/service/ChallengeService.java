package br.gov.es.indicadores.service;

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

    public Challenge[] getChallengeByArea(Area area){
        return repository.getChallengeByArea(area.getId());
    }

    public ChallengeDto getChallengeDto(Long idChallenge){
        Optional<Challenge> challengeData = repository.findById(idChallenge);
        ChallengeDto challengeDto = ChallengeDto.builder()
                                 .id(challengeData.get().getId())
                                 .name(challengeData.get().getName())
                                 .year(challengeData.get().getYear())
                                 .score(challengeData.get().getScore())
                                 .build();
        return challengeDto;
    }
    
}
