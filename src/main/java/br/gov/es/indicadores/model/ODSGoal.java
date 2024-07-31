package br.gov.es.indicadores.model;

import java.io.Serializable;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity
public class ODSGoal extends Entity implements Serializable {

    private String name;
    private String description;

    @Relationship(type = "TARGETS", direction = Relationship.OUTGOING)
    private ODS ods;

    @Relationship(type = "TARGETS", direction = Relationship.OUTGOING)
    private ODSGoal odsGoal;

    @Relationship(type = "COMPOSES", direction = Relationship.INCOMING)
    private Indicator indicator;

    public ODSGoal(){}

    public String getName(){
        return name;
    }

    public String getDescription(){
        return description;
    }

    public ODS getOds(){
        return ods;
    }
    
}
