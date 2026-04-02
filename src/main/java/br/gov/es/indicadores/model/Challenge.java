package br.gov.es.indicadores.model;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.schema.Relationship.Direction;

@Node
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Challenge extends Entity implements Serializable {

    private String name;

    @Relationship(type = "CHALLENGES", direction = Direction.OUTGOING)
    private Organizer organizer;

    @Relationship(type = "MEASURES", direction = Direction.INCOMING)
    private List<MeasuresRelationship> measures;


    
}
