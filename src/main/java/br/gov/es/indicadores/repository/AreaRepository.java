package br.gov.es.indicadores.repository;

import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import br.gov.es.indicadores.model.Area;

public interface AreaRepository extends  Neo4jRepository<Area,String> {
    @Query(" MATCH (admin:Administration {uuId: $administrationUuId})<-[r:SEGMENTS]-(area:Area) " +
           " WITH area " + 
           " RETURN area, [ [ (admin)<-[r:SEGMENTS]-(area) | [r,admin] ] ]")
    Area[] getAreasByAdministration(@Param("administrationUuId") String administrationUuId);
}
