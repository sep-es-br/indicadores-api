package br.gov.es.indicadores.repository;

import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.List;
import java.util.Map;

import org.springframework.data.neo4j.repository.Neo4jRepository;

import br.gov.es.indicadores.dto.CountOrganizerDto;
import br.gov.es.indicadores.dto.OrganizerAdminDto;
import br.gov.es.indicadores.dto.OrganizerItemDto;
import br.gov.es.indicadores.model.Administration;
import br.gov.es.indicadores.model.Organizer;

public interface OrganizerRepository extends  Neo4jRepository<Organizer,String> {
    @Query(" MATCH (a:Organizer {uuId: $organizerUuId}) " +
           " RETURN a ")
    Optional<Organizer> findByUuId(@Param("organizerUuId") String organizerUuId);

    @Query(" MATCH (org:Organizer)-[:SEGMENTS]->(admin:Administration {uuId: $administrationUuId})" +
           " RETURN org")
    Organizer[] getorganizersByAdministration(@Param("administrationUuId") String administrationUuId);

    @Query(" MATCH (org:Organizer)-[:SEGMENTS]->(admin:Administration {uuId: $administrationUuId})" +
    " RETURN org")
    List<Organizer> getorganizersByAdministrationList(@Param("administrationUuId") String administrationUuId);

    @Query(" MATCH (org:Organizer)-[:SEGMENTS]->(org2:Organizer {uuId: $organizerUuId})" +
    " RETURN org")
    List<Organizer> getChildrenOrganizers(@Param("organizerUuId") String organizerUuId);
    
    @Query(" MATCH (a:Administration {uuId: $administrationUuId})<-[:SEGMENTS]-(org)" +
       " WITH org" +
       " OPTIONAL MATCH (org)<-[:SEGMENTS*0..]-(org2:Organizer) " +
       " RETURN " +
       " org2.modelName AS name," +
       " org2.modelNameInPlural AS nameInPlural, " +
       " COUNT(org2) AS countOrganizer," +
       " CASE " +
       " WHEN org.modelName = org2.modelName THEN 'parent'" +
       " ELSE 'child'" +
       " END AS relationshipType")
       List<CountOrganizerDto> organizerAmountByAdministration(@Param("administrationUuId") String administrationUuId );
    

}
