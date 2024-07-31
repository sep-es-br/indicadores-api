package br.gov.es.indicadores.repository;

import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import br.gov.es.indicadores.model.Area;

public interface AreaRepository extends  Neo4jRepository<Area,Long> {
    @Query(" MATCH (admin:Administration)<-[r:IS_DEFINED_BY]-(area:Area) " +
           " WHERE id(admin) = $administration " +
           " WITH area " + 
           " RETURN area, [ [ (admin)<-[r:IS_DEFINED_BY]-(area) | [r,admin] ] ]")
    Area[] getAreasByAdministration(@Param("administration") Number administration);
}
