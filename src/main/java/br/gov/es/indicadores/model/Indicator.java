package br.gov.es.indicadores.model;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity
public class Indicator extends Entity {
    
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

    public Indicator(){}

    
    // private String 
}
