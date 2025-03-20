package br.gov.es.indicadores.model;

import java.io.Serializable;
import java.util.List;

import org.neo4j.ogm.annotation.NodeEntity;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.schema.Relationship.Direction;

@NodeEntity
public class Challenge extends Entity implements Serializable {

    private String name;

    @Relationship(type = "CHALLENGES", direction = Direction.OUTGOING)
    private Organizer organizer;

    @Relationship(type = "MEASURES", direction = Direction.INCOMING)
    private List<MeasuresRelationship> measures;

    public Challenge(){}

    public String getName() {
        return name;
    }

    public Organizer getOrganizer(){
        return organizer;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setOrganizer(Organizer organizer) {
        this.organizer = organizer;
    }

    public List<MeasuresRelationship> getMeasures() {
        return measures;
    }

    public void setMeasures(List<MeasuresRelationship> measures) {
        this.measures = measures;
    }
    
    
}
