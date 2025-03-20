package br.gov.es.indicadores.dto;

import java.util.List;

public class ManagementOrganizerChallengeDto {

    private String managementName;  
    private List<OrganizerChallengeDto> organizers;  

    public ManagementOrganizerChallengeDto(String managementName, List<OrganizerChallengeDto> organizers) {
        this.managementName = managementName;
        this.organizers = organizers;
    }

    public String getManagementName() {
        return managementName;
    }

    public void setManagementName(String managementName) {
        this.managementName = managementName;
    }

    public List<OrganizerChallengeDto> getOrganizers() {
        return organizers;
    }

    public void setOrganizers(List<OrganizerChallengeDto> organizers) {
        this.organizers = organizers;
    }

}
