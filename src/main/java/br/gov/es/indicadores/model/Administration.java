package br.gov.es.indicadores.model;

import java.io.Serializable;
import java.util.Set;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity
public class Administration extends Entity implements Serializable {
    
    private String name;

    private Boolean active;

    private Integer adminId;

    private Integer startYear;

    private Integer endYear;

    private String description;

    @Relationship(type = "SEGMENTS", direction = Relationship.INCOMING)
    private Set<Organizer> organizer;

    public Administration(){
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public Integer getStartYear() {
        return startYear;
    }

    public void setStartYear(Integer startYear) {
        this.startYear = startYear;
    }

    public Integer getEndYear() {
        return endYear;
    }

    public void setEndYear(Integer endYear) {
        this.endYear = endYear;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Organizer> getOrganizer() {
        return organizer;
    }

    public void setOrganizer(Set<Organizer> organizer) {
        this.organizer = organizer;
    }
    

}
