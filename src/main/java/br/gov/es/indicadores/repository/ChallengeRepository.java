package br.gov.es.indicadores.repository;

import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import br.gov.es.indicadores.model.Challenge;

public interface ChallengeRepository extends  Neo4jRepository<Challenge,Long>{
    
    @Query(" MATCH (a:Area)<-[r:IS_GROUPED_BY]-(c:Challenge) "+
           " WHERE id(a) = $area "+
           " RETURN c")
    Challenge[] getChallengeByArea(@Param("area") Long area );
}
