package br.gov.es.indicadores.model;

import java.io.Serializable;

import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity
public class Time extends Entity implements Serializable{
    
    private Integer period;
    private char type;
    private int year;

    public Time(){

    }

    public Integer getPeriod() {
        return period;
    }

    public char getType() {
        return type;
    }

    public int getYear() {
        return year;
    }

    
}
