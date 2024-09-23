package br.gov.es.indicadores.repository;

import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

import org.springframework.data.neo4j.repository.Neo4jRepository;

import br.gov.es.indicadores.model.Area;

public interface AreaRepository extends  Neo4jRepository<Area,String> {
    @Query(" MATCH (a:Area {uuId: $areaUuId}) " +
           " RETURN a ")
    Optional<Area> findByUuId(@Param("areaUuId") String areaUuId);

    @Query(" MATCH (admin:Administration {uuId: $administrationUuId})<-[r:SEGMENTS]-(area:Area) " +
           " WITH area " + 
           " RETURN area, [ [ (admin)<-[r:SEGMENTS]-(area) | [r,admin] ] ]")
    Area[] getAreasByAdministration(@Param("administrationUuId") String administrationUuId);

}
