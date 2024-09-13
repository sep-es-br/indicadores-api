package br.gov.es.indicadores.model;

import java.io.Serializable;

import org.neo4j.ogm.annotation.RelationshipEntity;

@RelationshipEntity(type = "TARGETS_FOR")
public class TargetsForRelationship extends Entity implements Serializable {
    
    private Double value;
    private String showValue;

    private Time time;

    public TargetsForRelationship(){}

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getShowValue() {
        return showValue;
    }

    public void setShowValue(String showValue) {
        this.showValue = showValue;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }
    
}