package br.gov.es.indicadores.model;

import org.springframework.data.neo4j.core.schema.RelationshipId;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

import java.io.Serializable;

@RelationshipProperties
public class MeasuresRelationship implements Serializable {

    @RelationshipId
    private Long id;

    @TargetNode
    private Challenge challenge;

    private String organizationAcronym;

    public MeasuresRelationship() {
    }

    public MeasuresRelationship(Challenge challenge, String organizationAcronym) {
        this.challenge = challenge;
        this.organizationAcronym = organizationAcronym;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Challenge getChallenge() {
        return challenge;
    }

    public void setChallenge(Challenge challenge) {
        this.challenge = challenge;
    }

    public String getOrganizationAcronym() {
        return organizationAcronym;
    }

    public void setOrganizationAcronym(String organizationAcronym) {
        this.organizationAcronym = organizationAcronym;
    }
}