package br.gov.es.indicadores.dto;

import java.util.List;

public class OrganizerItemStructureDto {
    
    private List<CountOrganizerDto> structureList;

    private OrganizerItemDto organizer;


    public OrganizerItemStructureDto() {
    }

    public List<CountOrganizerDto> getStructureList() {
        return structureList;
    }

    public void setStructureList(List<CountOrganizerDto> structureList) {
        this.structureList = structureList;
    }

    public OrganizerItemDto getOrganizer() {
        return organizer;
    }

    public void setOrganizer(OrganizerItemDto organizer) {
        this.organizer = organizer;
    }
}
