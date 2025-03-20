package br.gov.es.indicadores.model;

import java.io.Serializable;

import org.neo4j.ogm.annotation.NodeEntity;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.schema.Relationship.Direction;

@NodeEntity
public class Ods extends Entity implements Serializable {

	private String nome;

    private String description;

    private Integer order;

    @Relationship(type = "COMPOSES", direction = Direction.INCOMING)
    private OdsGoal odsGoal;

    
    public Ods(){
    }
    
	public String getDescription() {
        return description;
    }
    
    public OdsGoal getOdsGoal() {
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
