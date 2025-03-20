package br.gov.es.indicadores.dto;

import java.util.List;

public class OrganizerItemDto {
    private String name;
    private String description;
    private String icon;

    public OrganizerItemDto(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

}
