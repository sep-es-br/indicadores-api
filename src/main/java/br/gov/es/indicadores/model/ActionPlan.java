package br.gov.es.indicadores.model;

import org.springframework.data.neo4j.core.schema.Node;

@Node
public class ActionPlan extends Entity {
    
    private String name;

    private String description;

    private Boolean status;

    public ActionPlan() {

    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

}
