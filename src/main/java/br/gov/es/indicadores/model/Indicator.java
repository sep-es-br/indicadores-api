package br.gov.es.indicadores.model;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.schema.Relationship.Direction;

@Node
public class Indicator extends Entity implements Serializable {

    private String name;
    private String measureUnit;
    private String polarity;
    private String justificationBase;
    private String observations;
    private String fileName;
    private String originalFileName;

    @Relationship(type = "MEASURES", direction = Direction.OUTGOING)
    private List<MeasuresRelationship> measures;

    @Relationship(type = "TARGETS", direction = Direction.OUTGOING)
    private List<OdsGoal> odsgoal;

    @Relationship(type = "IS_DEFINED_FOR", direction = Direction.INCOMING)
    private List<Time> times;

    public Indicator() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPolarity() {
        return polarity;
    }

    public void setPolarity(String polarity) {
        this.polarity = polarity;
    }

    public String getMeasureUnit() {
        return measureUnit;
    }

    public void setMeasureUnit(String measureUnit) {
        this.measureUnit = measureUnit;
    }

    public List<MeasuresRelationship> getMeasures() {
        return measures;
    }

    public void setMeasures(List<MeasuresRelationship> measures) {
        this.measures = measures;
    }

    public List<OdsGoal> getOdsgoal() {
        return odsgoal;
    }

    public void setOdsgoal(List<OdsGoal> odsgoal) {
        this.odsgoal = odsgoal;
    }

    public String getJustificationBase() {
        return justificationBase;
    }

    public void setJustificationBase(String justificationBase) {
        this.justificationBase = justificationBase;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public List<Time> getTimes() {
        return times;
    }

    public void setTimes(List<Time> times) {
        this.times = times;
    }

}
