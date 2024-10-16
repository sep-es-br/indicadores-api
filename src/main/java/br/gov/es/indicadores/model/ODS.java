package br.gov.es.indicadores.model;

import java.io.Serializable;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity
public class ODS extends Entity implements Serializable {

	private String nome;

    private String description;

    private Integer order;

    @Relationship(type = "COMPOSES", direction = Relationship.INCOMING)
    private ODSGoal odsGoal;

    
    public ODS(){
    }
    
	public String getDescription() {
        return description;
    }
    
    public ODSGoal getOdsGoal() {
        return odsGoal;
    }

    public Integer getOrder() {
        return order;
    }

    
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
