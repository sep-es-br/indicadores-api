package br.gov.es.indicadores.model;

import java.io.Serializable;

import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity
public class Administration extends Entity implements Serializable {
    
    private String name;

    private Boolean active;

    private Integer adminId;

    private Integer startYear;

    private Integer endYear;

    private String description;

    public Administration(){
    }

    public Integer getEndYear() {
        return endYear;
    }
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public Integer getStartYear() {
        return startYear;
    }

    public Boolean getActive() {
        return active;
    }

    public Integer getAdminId() {
        return adminId;
    }
    

}
