package br.gov.es.indicadores.model;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

import java.io.Serializable;

@RelationshipProperties
public class MeasuresRelationship implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @TargetNode
    private Challenge challenge;

    private String measureUnit;
    private String organizationAcronym;

    public MeasuresRelationship() {
    }

    public MeasuresRelationship(Challenge challenge, String measureUnit, String organizationAcronym) {
        this.challenge = challenge;
        this.measureUnit = measureUnit;
        this.organizationAcronym = organizationAcronym;
    }

    public Long getId() { 
        return id;
    }

    public void setId(Long id) { 
        this.id = id;
    }

    public Challenge getChallenge() {
        return challenge;
    }

    public void setChallenge(Challenge challenge) {
        this.challenge = challenge;
    }

    public String getMeasureUnit() {
        return measureUnit;
    }

    public void setMeasureUnit(String measureUnit) {
        this.measureUnit = measureUnit;
    }

    public String getOrganizationAcronym() {
        return organizationAcronym;
    }

    public void setOrganizationAcronym(String organizationAcronym) {
        this.organizationAcronym = organizationAcronym;
    }
}
