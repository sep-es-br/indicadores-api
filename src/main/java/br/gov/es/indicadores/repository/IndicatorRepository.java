package br.gov.es.indicadores.repository;

import java.util.List;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import br.gov.es.indicadores.dto.IndicatorDto;
import br.gov.es.indicadores.model.Indicator;

public interface IndicatorRepository extends Neo4jRepository<Indicator,Long> {

    @Query(" MATCH (a:Administration)<-[:SEGMENTS]-(area:Area)<-[:CHALLENGES]-(c:Challenge)<-[:MEASURES]-(i:Indicator) "+
           " WHERE id(a) = $idAdministration "+
           " RETURN COUNT(i)")
    Integer indicatorAmountByAdministration(@Param("idAdministration") Long area );

    @Query(" MATCH (i:Indicator)-[:MEASURES]->(c:Challenge)-[:CHALLENGES]->(a:Area) "+
           " WHERE id(a) = $areaId "+
           " RETURN COUNT(i)")
    Integer indicatorAmountByChallenge(@Param("areaId") Long areaId );

    @Query("MATCH (i:Indicator)-[:MEASURES]->(c:Challenge) " +
       "WHERE id(c) = $challengeId " +
       "OPTIONAL MATCH (i)<-[]-(sg:StrategicGoal) " +
       "RETURN i.name AS name, i.measurementUnit AS measurementUnit, " +
       "i.organizationAcronym AS organizationAcronym, " +
       "i.organizationName AS organizationName, i.polarity AS polarity, COLLECT(sg) AS strategicGoalList")
    IndicatorDto[] getIndicatorsByChallenge(@Param("challengeId") Long challengeId);
} 
