package br.gov.es.indicadores.model;

import java.io.Serializable;
import java.util.List;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity
public class Challenge extends Entity implements Serializable {

    String name;

    Integer challengeId;
    Area area;

    @Relationship(type = "MEASURES", direction = Relationship.INCOMING)
    List<Indicator> indicator;

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

    public void setName(String name) {
        this.name = name;
    }

    public void setChallengeId(Integer challengeId) {
        this.challengeId = challengeId;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public List<Indicator> getIndicator() {
        return indicator;
    }

    public void setIndicator(List<Indicator> indicator) {
        this.indicator = indicator;
    }


    
    
}
