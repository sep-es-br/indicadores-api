package br.gov.es.indicadores.repository;


import org.springframework.data.neo4j.repository.query.*;

import br.gov.es.indicadores.model.Time;

import java.util.List;

import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface TimeRepository extends Neo4jRepository<Time,String>  {

    @Query("MATCH (t:Time) RETURN t.year AS year ORDER BY t.year")
    List<Integer> getAllYears();

}
