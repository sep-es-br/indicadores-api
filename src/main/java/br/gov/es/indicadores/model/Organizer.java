package br.gov.es.indicadores.model;

import java.io.Serializable;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity
public class Organizer extends Entity implements Serializable {

    private String name;
    private String description;
    private Boolean status;
    private String icon;
    private String modelName;
    private String modelNameInPlural;


    @Relationship(type = "SEGMENTS", direction = Relationship.OUTGOING)
    private Administration administration;

    @Relationship(type = "SEGMENTS", direction = Relationship.OUTGOING)
    private Organizer parentOrganizer;

    @Relationship(type = "CHALLENGES", direction = Relationship.INCOMING)
    private Challenge challenge;

    public Organizer(){}

    public String getName() {
        return name;
    }

    public String getDescription(){
        return description;
    }

    public Boolean getStatus(){
        return status;
    }

    public String getIcon(){
        return icon;
    }

    public Administration getAdministration(){
        return administration;
    }

    public void setAdministration(Administration administration) {
        this.administration = administration;
    }

    public Challenge getChallenge(){
        return challenge;
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
