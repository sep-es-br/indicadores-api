package br.gov.es.indicadores.model;

import java.io.Serializable;
import java.util.List;

import org.neo4j.ogm.annotation.NodeEntity;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.schema.Relationship.Direction;

@NodeEntity
public class Indicator extends Entity implements Serializable {
    
    private String name;
    private String measureUnit;
    private String polarity;
    private String justificationBase;
    private String justificationGoal;
    private String observations;
    private String fileName;
    private String originalFileName;

    @Relationship(type = "MEASURES", direction = Direction.OUTGOING)
    private List<MeasuresRelationship> measures;

    @Relationship(type = "TARGETS", direction = Direction.OUTGOING)
    private List<OdsGoal> odsgoal;

    @Relationship(type = "TARGETS_FOR", direction = Direction.OUTGOING)
    private List<TargetAndResultRelation> targetsFor;

    @Relationship(type = "RESULTED_IN", direction = Direction.OUTGOING)
    private List<TargetAndResultRelation> resultedIn;


    public Indicator(){}

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

    public List<TargetAndResultRelation> getTargetsFor() {
        return targetsFor;
    }

    public void setTargetsFor(List<TargetAndResultRelation> targetsFor) {
        this.targetsFor = targetsFor;
    }

    public List<TargetAndResultRelation> getResultedIn() {
        return resultedIn;
    }

    public void setResultedIn(List<TargetAndResultRelation> resultedIn) {
        this.resultedIn = resultedIn;
    }

    public String getJustificationBase() {
        return justificationBase;
    }

    public void setJustificationBase(String justificationBase) {
        this.justificationBase = justificationBase;
    }

    public String getJustificationGoal() {
        return justificationGoal;
    }

    public void setJustificationGoal(String justificationGoal) {
        this.justificationGoal = justificationGoal;
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
    
}
