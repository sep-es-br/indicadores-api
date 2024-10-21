package br.gov.es.indicadores.repository;

import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

import org.springframework.data.neo4j.repository.Neo4jRepository;

import br.gov.es.indicadores.model.Administration;
import br.gov.es.indicadores.model.Organizer;

public interface AreaRepository extends  Neo4jRepository<Organizer,String> {
    @Query(" MATCH (a:Organizer {uuId: $areaUuId}) " +
           " RETURN a ")
    Optional<Organizer> findByUuId(@Param("areaUuId") String areaUuId);

    @Query(" MATCH (admin:Administration {uuId: $administrationUuId})<-[r:SEGMENTS]-(area:Organizer) " +
           " WITH area " + 
           " RETURN area, [ [ (admin)<-[r:SEGMENTS]-(area) | [r,admin] ] ]")
    Organizer[] getAreasByAdministration(@Param("administrationUuId") String administrationUuId);

}
