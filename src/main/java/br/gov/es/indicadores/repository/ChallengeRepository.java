package br.gov.es.indicadores.repository;

import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.neo4j.repository.Neo4jRepository;

import br.gov.es.indicadores.dto.ChallengeDto;
import br.gov.es.indicadores.model.Challenge;

public interface ChallengeRepository extends  Neo4jRepository<Challenge,String>{
    
    @Query(" MATCH (c:Challenge {uuId: $challengeUuId}) " +
           " RETURN c ")
    Optional<Challenge> findByUuId(@Param("challengeUuId") String challengeUuId);

    @Query("MATCH (c:Challenge {uuId: $uuId}) " +
       "OPTIONAL MATCH (i:Indicator)-[m:MEASURES]->(c) " +
       "RETURN c.uuId AS uuId, " +
       "c.name AS name, " +
       "COLLECT({uuId: i.uuId, name: i.name, organizationAcronym: m.organizationAcronym}) AS indicatorList")
   ChallengeDto findChallengeDtoById(String uuId);

    @Query(" MATCH (a:Organizer {uuId: $organizerUuId}) " +
       "<-[:CHALLENGES]-(c:Challenge) " +
       "RETURN c ORDER BY c.name asc")
    //    "MATCH (c)<-[m:MEASURES]-(i:Indicator) " +
    //    "OPTIONAL MATCH (i)-[r1:TARGETS_FOR]->(targetTime:Time) " +
    //    "OPTIONAL MATCH (i)-[r2:RESULTED_IN]->(resultTime:Time) " +
    //    "WITH c, i, collect(DISTINCT { value: r1.value, showValue: r1.showValue }) AS targetsFor, " +
    //    "collect(DISTINCT { value: r2.value, showValue: r2.showValue }) AS resultedIn " +
    //    "RETURN c.name as name, c.uuId as uuId , collect(DISTINCT {id: i.uuId," + 
                    // "            name: i.name, "+ 
                    // "           measureUnit: i.measureUnit," + 
                    // "           organizationAcronym: i.organizationAcronym, " + 
                    // "           polarity: i.polarity," + 
                    // "        targetsFor: targetsFor," + 
                    // "           resultedIn: resultedIn}) AS indicatorList") 
    List<Challenge> getChallengeByOrganizer(@Param("organizerUuId") String organizerUuId );

    @Query(" MATCH (a:Administration {uuId: $administrationUuId })<-[:SEGMENTS]-(org:Organizer) " +
       " <-[:SEGMENTS*0..]-(org2:Organizer)<-[:CHALLENGES]-(c:Challenge)" +
       " WITH apoc.text.clean(c.name) AS challengeName " +
       " RETURN COUNT(DISTINCT challengeName) ")
    Integer challengesAmountByAdministration(@Param("administrationUuId") String administrationUuId );

    @Query("MATCH (a:Administration) " +
       "WHERE a.active = true " +
       "MATCH (a)<-[:SEGMENTS]-(org:Organizer) " +
       "      <-[:SEGMENTS*0..]-(org2:Organizer) " +
       "      <-[:CHALLENGES]-(c:Challenge) " +
       "WITH apoc.text.clean(c.name) AS challengeName, apoc.agg.first(c) AS uniqueChallenge " +
       "RETURN uniqueChallenge ORDER BY challengeName")
    List<Challenge> findChallengesByActiveAdministration();

    @Query(" MATCH (c:Challenge {uuId: $challengeId})<-[:MEASURES]-(i) " +
    " RETURN COUNT(i) > 0")
    boolean existsIndicatorBychallengeId(@Param("challengeId") String challengeId);

      @Query(" MATCH (i:Indicator)-[r:MEASURES]->(c:Challenge {uuId: $challengeUuid}) " +
        "WHERE NOT i.uuId IN $indicatorUuids " +
        "DELETE r ")
    void removeIndicatorsNotInList(String challengeUuid, List<String> indicatorUuids);

    @Query("UNWIND $indicatorData AS data " +
        "MATCH (i:Indicator {uuId: data.uuId}) " +
        "MATCH (c:Challenge {uuId: $challengeUuid}) " +
        "MERGE (i)-[r:MEASURES]->(c) " +
        "SET r.organizationAcronym = data.organizationAcronym ")
    void upsertIndicatorsAndRelationships(String challengeUuid, List<Map<String, String>> indicatorData);

}
