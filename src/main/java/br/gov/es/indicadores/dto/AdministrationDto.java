package br.gov.es.indicadores.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Transient;

import br.gov.es.indicadores.model.Administration;

public class AdministrationDto {

    private String uuId;

    private String name;

    private Boolean active;

    private Integer startYear;

    private Integer endYear;

    private String description;

    private List<String> modelName;

    private List<String> modelNameInPlural;

    @Transient
    private List<OrganizerAdminDto> organizerList;
    
    public AdministrationDto() {
    }

    public AdministrationDto(Administration administration) {
    this.uuId = administration.getId();  
    this.name = administration.getName();
    this.active = administration.getActive();
    this.startYear = administration.getStartYear();
    this.endYear = administration.getEndYear();
    this.description = administration.getDescription();
    this.modelName = administration.getModelName();
    this.modelNameInPlural = administration.getModelNameInPlural();
    }

    public AdministrationDto(String id, String name, Boolean active, Integer startYear, Integer endYear,
            String description, List<String> modelName, List<String> modelNameInPlural,
            List<OrganizerAdminDto> organizerList) {
        this.uuId = id;
        this.name = name;
        this.active = active;
        this.startYear = startYear;
        this.endYear = endYear;
        this.description = description;
        this.modelName = modelName;
        this.modelNameInPlural = modelNameInPlural;
        this.organizerList = organizerList;
    }



    public Boolean getActive() {
        return active;
    }
    public void setActive(Boolean active) {
        this.active = active;
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
    public List<OrganizerAdminDto> getOrganizerList() {
        return organizerList;
    }
    public void setOrganizerList(List<OrganizerAdminDto> organizerList) {
        this.organizerList = organizerList;
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



    public String getId() {
        return uuId;
    }



    public void setId(String id) {
        this.uuId = id;
    }
}
