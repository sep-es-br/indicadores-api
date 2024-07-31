package br.gov.es.indicadores.model;

import java.io.Serializable;

import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity
public class Subject extends Entity implements Serializable {

    private String name;
    private Boolean status;
    
}
