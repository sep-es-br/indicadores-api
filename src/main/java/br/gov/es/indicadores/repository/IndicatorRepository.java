package br.gov.es.indicadores.repository;

import java.util.List;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import br.gov.es.indicadores.dto.IndicatorDto;
import br.gov.es.indicadores.model.Indicator;

public interface IndicatorRepository extends Neo4jRepository<Indicator,String> {

    @Query(" MATCH (a:Administration {uuId: $administrationUuId })<-[:SEGMENTS]-(area:Area)<-[:CHALLENGES]-(c:Challenge)<-[:MEASURES]-(i:Indicator) "+
           " RETURN COUNT(i)")
    Integer indicatorAmountByAdministration(@Param("administrationUuId") String administrationUuId );

    @Query(" MATCH (i:Indicator)-[:MEASURES]->(c:Challenge)-[:CHALLENGES]->(a:Area {uuId: $areaUuId}) "+
           " RETURN COUNT(i)")
    Integer indicatorAmountByChallenge(@Param("areaUuId") String areaUuId );

    @Query(" MATCH (i:Indicator)-[:MEASURES]->(c:Challenge {uuId: '4:2cf4f70e-78bf-4f23-b92c-9b6e161cafee:104'}) "+
           " RETURN i")
    List<Indicator> indicatorByChallenge(@Param("challengeUuId") String challengeUuId);

    @Query("MATCH (i:Indicator)-[:MEASURES]->(c:Challenge {uuId: $challengeUuId}) " +
       "OPTIONAL MATCH (i)<-[]-(sg:StrategicGoal) " +
       "RETURN i.name AS name, i.measurementUnit AS measurementUnit, " +
       "i.organizationAcronym AS organizationAcronym, " +
       "i.organizationName AS organizationName, i.polarity AS polarity, COLLECT(sg) AS strategicGoalList")
    IndicatorDto[] getIndicatorsByChallenge(@Param("challengeUuId") String challengeUuId);
} 
