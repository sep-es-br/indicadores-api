package br.gov.es.indicadores.repository;


import org.springframework.data.neo4j.repository.query.*;
import org.springframework.data.repository.query.Param;

import br.gov.es.indicadores.model.Time;

import java.util.List;

import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface TimeRepository extends Neo4jRepository<Time,String>  {

   //retornar por ano 
    @Query("MATCH (t:Time) WHERE t.year = $year RETURN t")
    List<Time> findByYear(@Param("year") int year);

    //retornar bianual
    @Query("MATCH (t:Time) WHERE t.bianual = $bianual RETURN t")
    List<Time> findByBianual(@Param("bianual") boolean bianual);
}