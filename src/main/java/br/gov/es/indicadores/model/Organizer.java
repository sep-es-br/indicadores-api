package br.gov.es.indicadores.model;

import java.io.Serializable;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;
import java.util.List;
import java.util.Arrays;

@NodeEntity
public class Organizer extends Entity implements Serializable {

    private String name;
    private String description;
    private String icon;
    private String modelName;
    private String modelNameInPlural;


    @Relationship(type = "SEGMENTS", direction = Relationship.OUTGOING)
    private Administration administration;

    @Relationship(type = "SEGMENTS", direction = Relationship.OUTGOING)
    private Organizer parentOrganizer; 

    private List<Organizer> children;

    @Relationship(type = "CHALLENGES", direction = Relationship.INCOMING)
    private Challenge challenge;

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

    public Organizer getParentOrganizer() {
        return parentOrganizer;
    }

    public void setParentOrganizer(Organizer parentOrganizer) {
        this.parentOrganizer = parentOrganizer;
    }

    public List<Organizer> getChildren() {
        return children;
    }

    public void setChildren(List<Organizer> children) {
        this.children = children;
    }

    public Challenge getChallenge() {
        return challenge;
    }

    public void setChallenge(Challenge challenge) {
        this.challenge = challenge;
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
}
