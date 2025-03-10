package br.gov.es.indicadores.repository;


import org.springframework.data.neo4j.repository.query.*;
import org.springframework.data.repository.query.Param;

import br.gov.es.indicadores.dto.OdsDto;
import br.gov.es.indicadores.model.Ods;

import java.util.List;

import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface OdsRepository extends Neo4jRepository<Ods,String>  {
    
    @Query("MATCH (ods:Ods {order:$orderOds})<-[:COMPOSES]-(odsG:OdsGoal) " +
            "RETURN ods.name AS name, ods.description AS description, ods.order AS order, COLLECT(odsG{.order, .description}) AS odsGoals")
    OdsDto getOdsAndOdsGoal (@Param("orderOds") Integer orderOds);

    @Query("MATCH (ods:Ods)<-[:COMPOSES]-(odsG:OdsGoal) " +
            "RETURN ods.name AS name, ods.description AS description, ods.order AS order, COLLECT(odsG{.order, .description}) AS odsGoals")
    List<OdsDto> getOdsAndOdsGoalList ();
}
