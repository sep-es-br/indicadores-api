package br.gov.es.indicadores.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.gov.es.indicadores.dto.ChallengeDto;
import br.gov.es.indicadores.dto.IndicatorDto;
import br.gov.es.indicadores.model.Challenge;
import br.gov.es.indicadores.repository.ChallengeRepository;

@Service
public class ChallengeService {

    @Autowired
    private ChallengeRepository challengeRepository;

    @Autowired
    private IndicatorService indicatorService;

    public Integer challengesAmountByAdministration(String idAdministarion){
        return challengeRepository.challengesAmountByAdministration(idAdministarion);
    }

    public ChallengeDto getChallengeDto(String idChallenge){
        Optional<Challenge> challengeData = challengeRepository.findByUuId(idChallenge);
        List<IndicatorDto> indicators = indicatorService.getIndicatorByChallenge(idChallenge);
            ChallengeDto challengeDto = ChallengeDto.builder()
                .uuId(challengeData.get().getId())
                .name(challengeData.get().getName())
                .indicatorList(indicators)
                .build();
        return challengeDto;
    }
    
}
