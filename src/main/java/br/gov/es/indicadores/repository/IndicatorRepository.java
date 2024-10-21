package br.gov.es.indicadores.repository;

import java.util.List;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import br.gov.es.indicadores.dto.IndicatorDto;
import br.gov.es.indicadores.model.Indicator;

public interface IndicatorRepository extends Neo4jRepository<Indicator,String> {

    @Query(" MATCH (a:Administration {uuId: $administrationUuId })<-[:SEGMENTS]-(area:Organizer)<-[:CHALLENGES]-(c:Challenge)<-[:MEASURES]-(i:Indicator) "+
           " RETURN COUNT(i)")
    Integer indicatorAmountByAdministration(@Param("administrationUuId") String administrationUuId );

    @Query(" MATCH (i:Indicator)-[:MEASURES]->(c:Challenge)-[:CHALLENGES]->(a:Organizer {uuId: $areaUuId}) "+
           " RETURN COUNT(i)")
    Integer indicatorAmountByChallenge(@Param("areaUuId") String areaUuId );

    @Query(" MATCH (i:Indicator)-[:MEASURES]->(c:Challenge {uuId: $challengeUuId}) " +
           " OPTIONAL MATCH (i)-[t:TARGETS]->(odsG:OdsGoal)-[:COMPOPSES]->(ods:Ods) " +
           " OPTIONAL MATCH (i)-[r1:TARGETS_FOR]->(targetTime:Time) " +
           " OPTIONAL MATCH (i)-[r2:RESULTED_IN]->(resultTime:Time) " +
           " WITH i, collect(DISTINCT ods.order) AS ods, " +
           " collect(DISTINCT CASE WHEN targetTime.year IS NOT NULL AND r1.value IS NOT NULL AND r1.showValue IS NOT NULL " +
           " THEN { year: targetTime.year, value: r1.value, showValue: r1.showValue } ELSE NULL END) AS targetFor, " +
           " collect(DISTINCT CASE WHEN resultTime.year IS NOT NULL AND r2.value IS NOT NULL AND r2.showValue IS NOT NULL " +
           " THEN { year: resultTime.year, value: r2.value, showValue: r2.showValue }ELSE NULL END) AS resultedIn " +
           " RETURN collect(DISTINCT {id: i.uuId, " + 
                                    "name: i.name, "+ 
                                    "measureUnit: i.measureUnit, " + 
                                    "organizationAcronym: i.organizationAcronym, " + 
                                    "polarity: i.polarity, " +
                                    "ods: ods, " + 
                                    "targetFor: targetFor, " + 
                                    "resultedIn: resultedIn}) AS indicatorList") 
    List<IndicatorDto> indicatorByChallenge(@Param("challengeUuId") String challengeUuId);

    @Query("MATCH (i:Indicator)-[:MEASURES]->(c:Challenge {uuId: $challengeUuId}) " +
       "OPTIONAL MATCH (i)<-[]-(sg:StrategicGoal) " +
       "RETURN i.name AS name, i.measurementUnit AS measurementUnit, " +
       "i.organizationAcronym AS organizationAcronym, " +
       "i.organizationName AS organizationName, i.polarity AS polarity, COLLECT(sg) AS strategicGoalList")
    IndicatorDto[] getIndicatorsByChallenge(@Param("challengeUuId") String challengeUuId);
} 
