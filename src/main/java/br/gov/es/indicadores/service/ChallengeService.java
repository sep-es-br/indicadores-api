package br.gov.es.indicadores.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.gov.es.indicadores.dto.ChallengeDto;
import br.gov.es.indicadores.dto.IndicatorDto;
import br.gov.es.indicadores.dto.OrganizerItemDto;
import br.gov.es.indicadores.model.Challenge;
import br.gov.es.indicadores.model.Organizer;
import br.gov.es.indicadores.repository.ChallengeRepository;
import br.gov.es.indicadores.repository.OrganizerRepository;

@Service
public class ChallengeService {

    @Autowired
    private ChallengeRepository challengeRepository;

    @Autowired
    private OrganizerRepository organizerRepository;

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
                .name(challengeData.get().getName().toUpperCase())
                .indicatorList(indicators)
                .build();
        return challengeDto;
    }

    public List<Challenge> findChallengesByActiveAdministration() {
        return challengeRepository.findChallengesByActiveAdministration();
    }
    
    public void createChallenge(List<Challenge> challengeList, String parentOrganizerId) throws Exception {
        Organizer parentOrganizer = organizerRepository.findById(parentOrganizerId)
            .orElseThrow(() -> new RuntimeException("Organizador não encontrado"));
    
        for (Challenge dto : challengeList) {
    
            dto.setOrganizer(parentOrganizer); 
    
            challengeRepository.save(dto);
    
        }
    }

    public void deleteChallenge(String uuId) throws Exception {
        Challenge challenge = challengeRepository.findByUuId(uuId)
        .orElseThrow(() -> new RuntimeException("Desafio não encontrado"));

        boolean hasAssociatedIndicators  = challengeRepository.existsIndicatorBychallengeId(uuId);

        if (hasAssociatedIndicators) {
            throw new IllegalStateException("O desafio não pode ser excluído porque ainda possui dependências.");
        }
    
        challengeRepository.delete(challenge);
    }

    public Challenge getChallenge(String challengeId) throws Exception {
    
        Challenge challenge = challengeRepository.findByUuId(challengeId)
        .orElseThrow(() -> new RuntimeException("Desafio não encontrado"));
    
        return challenge;
    }

    public void updateChallenge(Challenge challengeDto) throws Exception {
        Challenge challenge = challengeRepository.findById(challengeDto.getId())
            .orElseThrow(() -> new RuntimeException("Desafio não encontrado"));
    
        if (challengeDto.getName() != null) {
            challenge.setName(challengeDto.getName());
        }
    
        challengeRepository.save(challenge);
    }
    
}
