package br.gov.es.indicadores;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.neo4j.config.EnableNeo4jAuditing;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode;

@SpringBootApplication()
@EnableSpringDataWebSupport(pageSerializationMode = PageSerializationMode.VIA_DTO)
@EnableFeignClients
@EnableNeo4jRepositories(basePackages = "br.gov.es.indicadores.repository")
@EnableNeo4jAuditing
public class IndicadoresApplication {
    public static void main(String[] args) {
		SpringApplication.run(IndicadoresApplication.class, args);
	}
}
