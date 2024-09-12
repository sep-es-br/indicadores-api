package br.gov.es.indicadores.model;

import java.io.Serializable;
import java.util.List;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity
public class Indicator extends Entity implements Serializable {
    
    private String name;
    private String measurementUnit;
    private String organizationAcronym;
    private String organizationName;
    private String polarity;

    @Relationship(type = "MEASURES", direction = Relationship.OUTGOING)
    private Challenge challenge;

    @Relationship(type = "RELATES_TO", direction = Relationship.OUTGOING)
    private Subject subject;

    @Relationship(type = "COMPOSES", direction = Relationship.OUTGOING)
    private ODSGoal odsgoal;

    @Relationship(type = "COMPOSES", direction = Relationship.INCOMING)
    private StrategicGoal strategicGoal;

    public Indicator(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMeasurementUnit() {
        return measurementUnit;
    }

    public void setMeasurementUnit(String measurementUnit) {
        this.measurementUnit = measurementUnit;
    }

    public String getOrganizationAcronym() {
        return organizationAcronym;
    }

    public void setOrganizationAcronym(String organizationAcronym) {
        this.organizationAcronym = organizationAcronym;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getPolarity() {
        return polarity;
    }

    public void setPolarity(String polarity) {
        this.polarity = polarity;
    }

    public Challenge getChallenge() {
        return challenge;
    }

    public void setChallenge(Challenge challenge) {
        this.challenge = challenge;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public ODSGoal getOdsgoal() {
        return odsgoal;
    }

    public void setOdsgoal(ODSGoal odsgoal) {
        this.odsgoal = odsgoal;
    }

    
    
}
