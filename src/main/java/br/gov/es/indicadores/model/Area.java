package br.gov.es.indicadores.model;

import java.io.Serializable;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity
public class Area extends Entity implements Serializable {

    private String name;
    private String description;
    private Boolean status;
    private String icon;

    @Relationship(type = "IS_DEFINED_BY", direction = Relationship.OUTGOING)
    private Administration administration;

    @Relationship(type = "IS_GROUPED_BY", direction = Relationship.INCOMING)
    private Challenge challenge;

    public Area(){}

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




}
