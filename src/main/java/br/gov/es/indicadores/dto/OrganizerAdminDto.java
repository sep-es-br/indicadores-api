package br.gov.es.indicadores.dto;

import java.util.List;

public class OrganizerAdminDto {

    private String nameAdministration;
    private String nameOrganizer;
    private String typeOrganizer;
    private String typeOrganizerPlural;
    private String idOrganizer;
    private List<OrganizerAdminDto> children; 

    public String getNameAdministration() {
        return nameAdministration;
    }

    public void setNameAdministration(String nameAdministration) {
        this.nameAdministration = nameAdministration;
    }

    public String getNameOrganizer() {
        return nameOrganizer;
    }

    public void setNameOrganizer(String nameOrganizer) {
        this.nameOrganizer = nameOrganizer;
    }

    public String getTypeOrganizer() {
        return typeOrganizer;
    }

    public void setTypeOrganizer(String typeOrganizer) {
        this.typeOrganizer = typeOrganizer;
    }

    public String getIdOrganizer() {
        return idOrganizer;
    }

    public void setIdOrganizer(String idOrganizer) {
        this.idOrganizer = idOrganizer;
    }

    public List<OrganizerAdminDto> getChildren() {
        return children;
    }

    public void setChildren(List<OrganizerAdminDto> children) {
        this.children = children;
    }

    public String getTypeOrganizerPlural() {
        return typeOrganizerPlural;
    }

    public void setTypeOrganizerPlural(String typeOrganizerPlural) {
        this.typeOrganizerPlural = typeOrganizerPlural;
    }
}

