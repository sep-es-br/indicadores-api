package br.gov.es.indicadores.model;

import org.springframework.data.neo4j.core.schema.*;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import java.util.Objects;

public abstract class Entity {

    @Id
    @GeneratedValue(UUIDStringGenerator.class)
    private String uuId;

    protected Entity() {

    }

    public String getId() {
        return uuId;
    }

    public void setId(String id) {
        this.uuId = id;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Entity entity = (Entity) o;
        return Objects.equals(uuId, entity.uuId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(uuId);
    }
}
