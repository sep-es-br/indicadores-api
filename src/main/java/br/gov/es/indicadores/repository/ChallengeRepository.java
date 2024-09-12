package br.gov.es.indicadores.repository;

import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import br.gov.es.indicadores.model.Challenge;

public interface ChallengeRepository extends  Neo4jRepository<Challenge,Long>{
    
    @Query(" MATCH (a:Area)<-[r:CHALLENGES]-(c:Challenge) "+
           " WHERE id(a) = $area "+
           " RETURN c")
    Challenge[] getChallengeByArea(@Param("area") Long area );

    @Query(" MATCH (a:Administration)<-[:SEGMENTS]-(area:Area)<-[:CHALLENGES]-(c:Challenge) "+
           " WHERE id(a) = $idAdministration "+
           " RETURN COUNT(c)")
    Integer challengesAmountByAdministration(@Param("idAdministration") Long area );
}
