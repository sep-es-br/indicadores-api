package br.gov.es.indicadores.model;

import org.springframework.data.neo4j.core.schema.*;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

public abstract class Entity {

    @Id
    @GeneratedValue(UUIDStringGenerator.class)
    private String uuId;

    public String getId() {
        return uuId;
    }

    public void setId(String id) {
        this.uuId = id;
    }
}
