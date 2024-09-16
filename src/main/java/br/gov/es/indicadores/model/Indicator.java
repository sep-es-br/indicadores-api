package br.gov.es.indicadores.model;

import java.io.Serializable;
import java.util.List;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity
public class Indicator extends Entity implements Serializable {
    
    private String name;
    private String measureUnit;
    private String organizationAcronym;
    private String polarity;

    @Relationship(type = "MEASURES", direction = Relationship.OUTGOING)
    private Challenge challenge;

    @Relationship(type = "COMPOSES", direction = Relationship.OUTGOING)
    private ODSGoal odsgoal;

    @Relationship(type = "TARGETS_FOR", direction = Relationship.OUTGOING)
    private List<TargetsForRelationship> targetsFor;

    @Relationship(type = "RESULTED_IN", direction = Relationship.OUTGOING)
    private List<ResultedInRelationship> ResultedIn;


    public Indicator(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getmeasureUnitUnit() {
        return measureUnit;
    }

    public void setmeasureUnit(String measureUnit) {
        this.measureUnit = measureUnit;
    }

    public String getOrganizationAcronym() {
        return organizationAcronym;
    }

    public void setOrganizationAcronym(String organizationAcronym) {
        this.organizationAcronym = organizationAcronym;
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

    public ODSGoal getOdsgoal() {
        return odsgoal;
    }

    public void setOdsgoal(ODSGoal odsgoal) {
        this.odsgoal = odsgoal;
    }

    public List<TargetsForRelationship> getTargetsFor() {
        return targetsFor;
    }

    public void setTargetsFor(List<TargetsForRelationship> targetsFor) {
        this.targetsFor = targetsFor;
    }

    public List<ResultedInRelationship> getResultedIn() {
        return ResultedIn;
    }

    public void setResultedIn(List<ResultedInRelationship> resultedIn) {
        ResultedIn = resultedIn;
    }

    

    

    
    
}
