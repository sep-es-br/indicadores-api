package br.gov.es.indicadores.model;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.neo4j.ogm.annotation.NodeEntity;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.schema.Relationship.Direction;

@NodeEntity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Challenge extends Entity implements Serializable {

    private String name;

    @Relationship(type = "CHALLENGES", direction = Direction.OUTGOING)
    private Organizer organizer;

    @Relationship(type = "MEASURES", direction = Direction.INCOMING)
    private List<MeasuresRelationship> measures;
    
}
