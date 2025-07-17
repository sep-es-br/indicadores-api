package br.gov.es.indicadores.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import br.gov.es.indicadores.dto.IndicatorAdminDto;
import br.gov.es.indicadores.dto.IndicatorDto;
import br.gov.es.indicadores.model.Indicator;

public interface IndicatorRepository extends Neo4jRepository<Indicator,String> {

    Page<IndicatorDto> findByNameContainingIgnoreCase(String name, Pageable pageable);

    @Query(
    value = "MATCH (n:Indicator) WHERE apoc.text.clean(n.name) CONTAINS apoc.text.clean($name) " +
            "RETURN n.uuId AS uuId, toUpper(n.name) AS name " +
            "ORDER BY apoc.text.clean(n.name) ASC " +
            "SKIP $skip LIMIT $limit",
    countQuery = "MATCH (n:Indicator) WHERE apoc.text.clean(n.name) CONTAINS apoc.text.clean($name) RETURN count(n)"
       )
    Page<IndicatorAdminDto> indicatorPage(String name, Pageable pageable);

    @Query("MATCH (i:Indicator) RETURN i.uuId AS uuId, toUpper(i.name) AS name ORDER BY apoc.text.clean(i.name) ASC")
    List<IndicatorAdminDto> allIndicators();

    @Query(" MATCH (a:Administration {uuId: $administrationUuId })<-[:SEGMENTS]-(org:Organizer) " +
           " <-[:SEGMENTS*0..]-(org2:Organizer)<-[:CHALLENGES]-(c:Challenge)<-[:MEASURES]-(i:Indicator) "+
           " RETURN COUNT(distinct i)")
    Integer indicatorAmountByAdministration(@Param("administrationUuId") String administrationUuId );

    @Query(" MATCH (i:Indicator)-[:MEASURES]->(c:Challenge)-[:CHALLENGES]->(org:Organizer {uuId: $organizerUuId}) "+
           " RETURN COUNT(distinct i)")
    Integer indicatorAmountByChallenge(@Param("organizerUuId") String organizerUuId );

    @Query(" MATCH (i:Indicator)-[rm:MEASURES]->(c:Challenge {uuId: $challengeUuId}) " +
           " OPTIONAL MATCH (i)-[t:TARGETS]->(odsG:OdsGoal)-[:COMPOSES]->(ods:Ods) " +
           " OPTIONAL MATCH (i)-[r1:TARGETS_FOR]->(targetTime:Time) " +
           " OPTIONAL MATCH (i)-[r2:RESULTED_IN]->(resultTime:Time) " +
           " WITH i, rm, apoc.coll.sort(collect(DISTINCT ods.order)) AS ods, " +
           " collect(DISTINCT CASE WHEN targetTime.year IS NOT NULL AND r1.value IS NOT NULL AND r1.showValue IS NOT NULL " +
           " THEN { year: targetTime.year, value: r1.value, showValue: r1.showValue } ELSE NULL END) AS targetFor, " +
           " collect(DISTINCT CASE WHEN resultTime.year IS NOT NULL AND r2.value IS NOT NULL AND r2.showValue IS NOT NULL " +
           " THEN { year: resultTime.year, value: r2.value, showValue: r2.showValue }ELSE NULL END) AS resultedIn " +
           " ORDER BY i.name ASC " +
           " RETURN collect(DISTINCT {uuId: i.uuId, " + 
                                    "name: toUpper(i.name), "+ 
                                    "measureUnit: i.measureUnit, " + 
                                    "organizationAcronym: rm.organizationAcronym, " + 
                                    "polarity: i.polarity, " +
                                    "justificationBase: i.justificationBase, " +
                                    "justificationGoal: i.justificationGoal, " +
                                    "ods: ods, " + 
                                    "fileName: i.fileName, " + 
                                    "originalFileName: i.originalFileName, " + 
                                    "targetFor: targetFor, " + 
                                    "resulted: resultedIn}) AS indicatorList") 
    List<IndicatorDto> indicatorByChallenge(@Param("challengeUuId") String challengeUuId);

    @Query("MATCH (i:Indicator)-[:MEASURES]->(c:Challenge {uuId: $challengeUuId}) " +
       "OPTIONAL MATCH (i)<-[]-(sg:StrategicGoal) " +
       "RETURN i.name AS name, i.measurementUnit AS measurementUnit, " +
       "i.organizationAcronym AS organizationAcronym, " +
       "i.organizationName AS organizationName, i.polarity AS polarity, COLLECT(sg) AS strategicGoalList")
    IndicatorDto[] getIndicatorsByChallenge(@Param("challengeUuId") String challengeUuId);


    @Query("MATCH (n:Challenge)<-[r:MEASURES]-(i:Indicator) " +
           "WITH i.measureUnit AS originalMeasureUnit, apoc.text.clean(i.measureUnit) AS cleanedMeasureUnit " +
           "WITH DISTINCT cleanedMeasureUnit, COLLECT(originalMeasureUnit)[0] AS measureUnit " +
           "RETURN measureUnit " +
           "ORDER BY apoc.text.clean(measureUnit) ")
    List<String> findDistinctMeasureUnits();

    @Query("MATCH (n:Challenge)<-[r:MEASURES]-(i:Indicator) " +
       "WITH r.organizationAcronym AS originalOrganizationAcronym, apoc.text.clean(r.organizationAcronym) AS cleanedOrganizationAcronym " +
       "WHERE cleanedOrganizationAcronym <> '' " +
       "WITH DISTINCT cleanedOrganizationAcronym, COLLECT(originalOrganizationAcronym)[0] AS organizationAcronym " +
       "RETURN organizationAcronym")
    List<String> findDistinctOrganizationAcronyms();

} 
