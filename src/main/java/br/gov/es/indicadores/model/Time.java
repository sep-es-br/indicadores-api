package br.gov.es.indicadores.model;

import java.io.Serializable;

import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity
public class Time extends Entity implements Serializable {

    public static final String ANNUAL = "ANNUAL";
    public static final String BIENNIAL = "BIENNIAL";


    private String type;
    private String year;
    private int period; 
    private Double valueGoal;
    private String showValueGoal;
    private Double valueResult;
    private String showValueResult;
    private String justificationGoal;
    private String justificationResult; //add

    public Time() {

    }

    public String getType() {
        return type;
    }

    public String getYear() {
        return year;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public Double getValueGoal() {
        return valueGoal;
    }

    public void setValueGoal(Double valueGoal) {
        this.valueGoal = valueGoal;
    }

    public String getShowValueGoal() {
        return showValueGoal;
    }

    public void setShowValueGoal(String showValueGoal) {
        this.showValueGoal = showValueGoal;
    }

    public Double getValueResult() {
        return valueResult;
    }

    public void setValueResult(Double valueResult) {
        this.valueResult = valueResult;
    }

    public String getShowValueResult() {
        return showValueResult;
    }

    public void setShowValueResult(String showValueResult) {
        this.showValueResult = showValueResult;
    }

    public String getJustificationGoal() {
        return justificationGoal;
    }

    public void setJustificationGoal(String justificationGoal) {
        this.justificationGoal = justificationGoal;
    }

    public String getJustificationResult() {
    return justificationResult;
    }

    public void setJustificationResult(String justificationResult) {
        this.justificationResult = justificationResult;
    }

}
