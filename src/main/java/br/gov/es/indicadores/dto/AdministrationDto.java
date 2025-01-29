package br.gov.es.indicadores.dto;

public class AdministrationDto {

    private String name;
    private String description;
    private Integer startYear;
    private Integer endYear;

    
    public AdministrationDto() {
    }

    public AdministrationDto(String name, String description, Integer startYear, Integer endYear) {
        this.name = name;
        this.description = description;
        this.startYear = startYear;
        this.endYear = endYear;
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
}
