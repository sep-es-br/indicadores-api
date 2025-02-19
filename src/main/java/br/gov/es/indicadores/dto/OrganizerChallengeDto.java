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

    public static class ChallengeDto {
        private String name;
        private String id;

        public ChallengeDto(String name, String id) {
            this.name = name;
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}

