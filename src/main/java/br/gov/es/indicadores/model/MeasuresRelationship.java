package br.gov.es.indicadores.model;

import java.io.Serializable;

import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

@RelationshipEntity(type = "MEASURES")
public class MeasuresRelationship  extends Entity implements Serializable {

    @StartNode
    private Indicator indicator;

    @EndNode
    private Challenge challenge;

    private Double value; 

    public MeasuresRelationship() {}

    public MeasuresRelationship(Indicator indicator, Challenge challenge, Double value) {
        this.indicator = indicator;
        this.challenge = challenge;
        this.value = value;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Indicator getIndicator() {
        return indicator;
    }

    public void setIndicator(Indicator indicator) {
        this.indicator = indicator;
    }

    public Challenge getChallenge() {
        return challenge;
    }

    public void setChallenge(Challenge challenge) {
        this.challenge = challenge;
    }
}
