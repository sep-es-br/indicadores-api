package br.gov.es.indicadores.model;

import java.io.Serializable;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity
public class Challenge extends Entity implements Serializable {

    String name;

    Integer challengeId;
    Area area;

    public Challenge(){}

    public String getName() {
        return name;
    }

    public Area getArea(){
        return area;
    }

    public Integer getChallengeId() {
        return challengeId;
    }
}
