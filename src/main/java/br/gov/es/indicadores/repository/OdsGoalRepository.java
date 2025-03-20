package br.gov.es.indicadores.repository;

import java.util.List;

import org.springframework.data.neo4j.repository.Neo4jRepository;

import br.gov.es.indicadores.model.OdsGoal;

public interface OdsGoalRepository extends Neo4jRepository<OdsGoal,String>  {

    List<OdsGoal> findByOrderIn(List<String> orders);

}