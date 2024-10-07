package br.gov.es.indicadores.model;

import java.io.Serializable;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity
public class ODSGoal extends Entity implements Serializable {

    private String description;

    private String order;

    @Relationship(type = "COMPOSES", direction = Relationship.OUTGOING)
    private ODS ods;

    @Relationship(type = "TARGETS", direction = Relationship.INCOMING)
    private Indicator indicator;

    public ODSGoal(){}

    public String getDescription(){
        return description;
    }

    public ODS getOds(){
        return ods;
    }

    public String getOrder() {
        return order;
    }
    
}
