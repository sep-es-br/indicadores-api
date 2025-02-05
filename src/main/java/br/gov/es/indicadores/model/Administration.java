package br.gov.es.indicadores.model;

import java.io.Serializable;
import java.util.List;

import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity
public class Administration extends Entity implements Serializable {
    
    private String name;

    private Boolean active;

    private Integer startYear;

    private Integer endYear;

    private String description;

    private List<String> modelName;

    private List<String> modelNameInPlural;

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

        public List<String> getModelName() {
        return modelName;
    }

    public void setModelName(List<String> modelName) {
        this.modelName = modelName;
    }

    public List<String> getModelNameInPlural() {
        return modelNameInPlural;
    }

    public void setModelNameInPlural(List<String> modelNameInPlural) {
        this.modelNameInPlural = modelNameInPlural;
    }

}
