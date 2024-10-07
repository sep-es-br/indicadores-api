package br.gov.es.indicadores.model;

import java.io.Serializable;

import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity
public abstract class ODS extends Entity implements Serializable {

	private String nome;

    private String description;

    private Integer order;

    public ODS(){
    }

	public String getDescription() {
        return description;
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
