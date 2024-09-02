package br.gov.es.indicadores.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import br.gov.es.indicadores.model.Challenge;
import br.gov.es.indicadores.model.Indicator;

public interface IndicatorRepository extends Neo4jRepository<Indicator,Long> {

    @Query(" MATCH (i:Indicator)-[:MEASURES]->(c:Challenge) "+
           " WHERE id(c) = $challengeId "+
           " RETURN i")
    Indicator[] getIndicatorsByChallenge(@Param("challengeId") Long challengeId );
} 
