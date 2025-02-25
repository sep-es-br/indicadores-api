package br.gov.es.indicadores.repository;


import org.springframework.data.neo4j.repository.query.*;
import org.springframework.data.repository.query.Param;

import br.gov.es.indicadores.dto.OdsDto;
import br.gov.es.indicadores.model.ODS;

import java.util.List;

import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface OdsRepository extends Neo4jRepository<ODS,String>  {
    
    @Query("MATCH (ods:Ods {order:$orderOds})<-[:COMPOPSES]-(odsG:OdsGoal) " +
            "RETURN ods.name AS name, ods.description AS description, ods.order AS order, COLLECT(odsG{.order, .description}) AS odsGoals")
    OdsDto getOdsAndOdsGoal (@Param("orderOds") Integer orderOds);

    @Query("MATCH (ods:Ods)<-[:COMPOPSES]-(odsG:OdsGoal) " +
            "RETURN ods.name AS name, ods.description AS description, ods.order AS order, COLLECT(odsG{.order, .description}) AS odsGoals")
    List<OdsDto> getOdsAndOdsGoalList ();
}
