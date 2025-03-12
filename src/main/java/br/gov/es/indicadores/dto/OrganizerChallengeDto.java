package br.gov.es.indicadores.dto;

import java.util.List;

public class OrganizerChallengeDto {
    private String name;
    private List<ChallengeDto> challenges;

    public OrganizerChallengeDto(String name, List<ChallengeDto> challenges) {
        this.name = name;
        this.challenges = challenges;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ChallengeDto> getChallenges() {
        return challenges;
    }

    public void setChallenges(List<ChallengeDto> challenges) {
        this.challenges = challenges;
    }


}

