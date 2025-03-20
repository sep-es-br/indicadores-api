package br.gov.es.indicadores.model;

import java.io.Serializable;

import org.neo4j.ogm.annotation.NodeEntity;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.schema.Relationship.Direction;

import com.fasterxml.jackson.annotation.JsonIgnore;

@NodeEntity
public class OdsGoal extends Entity implements Serializable {

    private String description;

    private String order;

    @JsonIgnore
    @Relationship(type = "COMPOSES", direction = Direction.OUTGOING)
    private Ods ods;

    @Relationship(type = "TARGETS", direction = Direction.INCOMING)
    private Indicator indicator;

    public OdsGoal(){}

    public String getDescription(){
        return description;
    }

    public Ods getOds(){
        return ods;
    }

    public String getOrder() {
        return order;
    }
    
}
