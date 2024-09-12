package br.gov.es.indicadores.model;

import java.io.Serializable;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity
public class StrategicGoal extends Entity implements Serializable{
    
    private String name;
    private String description;
    private int refYear;
    private float value;
    private float result;
    private Boolean active;

    @Relationship(type = "COMPOSES", direction = Relationship.OUTGOING)
    private Indicator indicator;

    public StrategicGoal(){

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

    public int getRefYear() {
        return refYear;
    }

    public void setRefYear(int refYear) {
        this.refYear = refYear;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public float getResult() {
        return result;
    }

    public void setResult(float result) {
        this.result = result;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Indicator getIndicator() {
        return indicator;
    }

    public void setIndicator(Indicator indicator) {
        this.indicator = indicator;
    }

    
}
