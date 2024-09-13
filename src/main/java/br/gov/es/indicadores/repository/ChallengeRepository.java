package br.gov.es.indicadores.repository;

import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import org.springframework.data.neo4j.repository.Neo4jRepository;

import br.gov.es.indicadores.model.Challenge;

public interface ChallengeRepository extends  Neo4jRepository<Challenge,String>{
    
    @Query(" MATCH (a:Area {uuId: $areaUuId}) " +
       "<-[:CHALLENGES]-(c:Challenge) " +
       "<-[:MEASURES]-(i:Indicator) " +
       // "OPTIONAL MATCH (i)-[r1:TARGETS_FOR]->(targetTime:Time) " +
       // "OPTIONAL MATCH (i)-[r2:RESULTED_IN]->(resultTime:Time) " +
       "RETURN i") 
    List<Challenge> getChallengeByArea(@Param("areaUuId") String areaUuId );

    @Query(" MATCH (a:Administration {uuId: $administrationUuId})<-[:SEGMENTS]-(area:Area)<-[:CHALLENGES]-(c:Challenge) "+
           " RETURN COUNT(c)")
    Integer challengesAmountByAdministration(@Param("administrationUuId") String administrationUuId );
}
