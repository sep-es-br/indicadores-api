package br.gov.es.indicadores.model;

import org.springframework.data.neo4j.core.schema.*;

public abstract class Entity {

    @Id
    @GeneratedValue
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
