package br.gov.es.indicadores.model;

import java.io.Serializable;

import org.neo4j.ogm.annotation.NodeEntity;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.schema.Relationship.Direction;


import java.util.List;
import java.util.Set;

@NodeEntity
public class Organizer extends Entity implements Serializable {

    private String name;
    private String description;
    private String icon;
    private String modelName;
    private String modelNameInPlural;

    @Relationship(type = "SEGMENTS", direction = Direction.OUTGOING)
    private Administration administration;

    @Relationship(type = "SEGMENTS", direction = Direction.OUTGOING)
    private Organizer parentOrganizer; 

    @Relationship(type = "SEGMENTS", direction = Direction.INCOMING)
    private List<Organizer> children;

    public Organizer() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Administration getAdministration() {
        return administration;
    }

    public void setAdministration(Administration administration) {
        this.administration = administration;
    }


    public List<Organizer> getChildren() {
        return children;
    }

    public void setChildren(List<Organizer> children) {
        this.children = children;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getModelNameInPlural() {
        return modelNameInPlural;
    }

    public void setModelNameInPlural(String modelNameInPlural) {
        this.modelNameInPlural = modelNameInPlural;
    }

    
    public Organizer getParentOrganizer() {
        return parentOrganizer;
    }

    public void setParentOrganizer(Organizer parentOrganizer) {
        this.parentOrganizer = parentOrganizer;
    }
}
