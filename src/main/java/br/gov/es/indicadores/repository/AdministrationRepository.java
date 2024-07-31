package br.gov.es.indicadores.repository;

import org.springframework.data.neo4j.repository.query.*;
import org.springframework.data.repository.query.Param;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import br.gov.es.indicadores.model.Administration;

public interface AdministrationRepository extends Neo4jRepository<Administration,Long> {
    
    @Query(" MATCH (a:Administration) " +
           " WHERE a.startYear <= $year " +
           " AND a.endYear >= $year " + 
           " RETURN a")
    Administration getAdministrationByYear(@Param("year") Number year);
}
