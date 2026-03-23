package br.gov.es.indicadores.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.schema.Relationship.Direction;

import java.util.List;
import java.util.Set;
@AllArgsConstructor
@NoArgsConstructor
@Node
@Data
@EqualsAndHashCode(callSuper = true)
public class Organizer extends Entity implements Serializable {

    private String name;
    private String description;
    private String icon;
    private String modelName;
    private String modelNameInPlural;
//    private Boolean active;
//    private Integer areaId;
//    private Integer code;

    @Relationship(type = "SEGMENTS", direction = Direction.OUTGOING)
    private Administration administration;

    @Relationship(type = "SEGMENTS", direction = Direction.OUTGOING)
    private Organizer parentOrganizer;

    @Relationship(type = "SEGMENTS", direction = Direction.INCOMING)
    private List<Organizer> children;


}
