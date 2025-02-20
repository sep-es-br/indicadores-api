package br.gov.es.indicadores.repository;

import org.springframework.data.neo4j.repository.query.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import br.gov.es.indicadores.dto.AdministrationDto;
import br.gov.es.indicadores.dto.ManagementOrganizerChallengeDto;
import br.gov.es.indicadores.dto.OrganizerChallengeDto;
import br.gov.es.indicadores.model.Administration;

public interface AdministrationRepository extends Neo4jRepository<Administration,String> {

    Page<AdministrationDto> findByNameContainingIgnoreCase(String name, Pageable pageable);
    
    @Query(" MATCH (a:Administration) " +
           " RETURN a")
    List<Administration> getAllAdministration();

    @Query(" MATCH (a:Administration {uuId: $administrationId}) " +
           " RETURN a")
    Administration getAdministrationByUuId(@Param("administrationId") String administrationId);

    @Query(" MATCH (a:Administration)<-[]-(:Organizer)<-[:SEGMENTS*0..]-(:Organizer {uuId: $organizerUuId}) " +
           " RETURN a")
    Administration getAdministrationByOrganizer(@Param("organizerUuId") String organizerUuId);

    @Query(" MATCH (a:Administration) WHERE a.active = true RETURN a ")
    Administration findByActiveTrue();

    @Query(" MATCH (a:Administration {uuId: $administrationId})<-[:SEGMENTS]-(o:Organizer) " +
           " RETURN COUNT(o) > 0 ")
    boolean existsOrganizerByAdministrationId(@Param("administrationId") String administrationId);

    @Query("MATCH (a:Administration)<-[:SEGMENTS*0..]-(o:Organizer)<-[:CHALLENGES]-(c:Challenge) " +
           "WHERE a.uuId = $administrationId " + 
           "WITH a.name + ' - ' + o.name AS name, c.name AS challengeName, c.uuId AS challengeId " +
           "WITH name, COLLECT(DISTINCT { name: challengeName, id: challengeId }) AS challenges " +
           "RETURN name, challenges " +
           "ORDER BY name")
    List<OrganizerChallengeDto> findOrganizerChallengesByAdministration(@Param("administrationId") String administrationId);


}
