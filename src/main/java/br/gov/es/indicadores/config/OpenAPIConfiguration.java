package br.gov.es.indicadores.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfiguration {

    @Value("${openapi.server.url}")
    private String absoluteUrl;


    @Bean
    public OpenAPI defineOpenApi() {
        return new OpenAPI()
                .info(info())
                .servers(List.of(server()))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new Components().addSecuritySchemes("Bearer Authentication", createAPIKeyScheme()))
                .externalDocs(new ExternalDocumentation()
                        .description("Indicadores Painel")
                        .url("https://hom.indicadores.es.gov.br/home"))
                .tags(
                        List.of(
                                new Tag().name("Autenticação").description("Endpoints para login e informações do usuário"),
                                new Tag().name("Organizer").description("Endpoints para construir o Organizer"),
                                new Tag().name("Management").description("Endpoints da Gestão Administrativa"),
                                new Tag().name("Indicator").description("Enpoints para construir o Indicador"),
                                new Tag().name("Challege").description("Enpoints para cosntruir o challege"),
                                new Tag().name("Home").description("Enpoints Home")
                        )
                );
    }

    @Bean
    public OpenApiCustomizer customerGlobalHeaderOpenApiCustomiser() {
        return openApi -> openApi.getPaths().values()
                .forEach(pathItem -> pathItem.readOperations().forEach(operation -> {
                    ApiResponses responses = operation.getResponses();
                    responses.addApiResponse("401",
                            new ApiResponse().description("Token JWT inválido, expirado ou ausente"));
                    responses.addApiResponse("403",
                            new ApiResponse().description("Usuário não possui permissão para acessar este recurso"));
                    responses.addApiResponse("500",
                            new ApiResponse().description("Erro interno inesperado no servidor"));
                }));
    }

    private Server server() {
        Server server = new Server();
        server.url(absoluteUrl);
        server.description("Indicadoes API");
        return server;
    }

    private Contact contact() {
        Contact contact = new Contact();
        contact.name("Indicadores Administração - SEP");
        contact.url("https://indicadores.sep.es.gov.br");
        return contact;
    }

    private Info info() {
        return new Info()
                .title("Indicadores API")
                .version("1.0.0")
                .description("Indicadores SEP")
                .contact(contact());
    }

    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("bearer");
    }
}
