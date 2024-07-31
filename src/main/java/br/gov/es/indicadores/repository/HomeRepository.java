package br.gov.es.indicadores.repository;


import org.springframework.data.neo4j.repository.query.*;

import br.gov.es.indicadores.model.ODS;

import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface HomeRepository extends Neo4jRepository<ODS,Long>  {
    
    @Query("\" MATCH (a:Administration) \" +\r\n" + //
                "           \" WHERE a.startYear <= 2024 \" +\r\n" + //
                "           \" AND a.endYear >= 2024 \" + \r\n" + //
                "           \" RETURN a\"")
    ODS findDataHome();
}
