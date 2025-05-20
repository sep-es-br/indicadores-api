package br.gov.es.indicadores.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import java.net.http.HttpResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.json.JsonMapper;

import br.gov.es.indicadores.dto.acessocidadaoapi.OrganizacoesACDto;
import br.gov.es.indicadores.dto.acessocidadaoapi.TokenResponseDto;
import br.gov.es.indicadores.dto.acessocidadaoapi.UnidadesACResponseDto;

@Service
public class OrganogramaApiService {
    
    private static final String GUID_GOVES = "fe88eb2a-a1f3-4cb1-a684-87317baf5a57";

    @Value("${acessocidadao.tokenUrl}")
    private String ACTokenUrl;
  
    @Value("${acessocidadao.organogramaUrl}")
    private String organogramaUrl;
  
    @Value("${acessocidadao.clientId}")
    private String clientId;
  
    @Value("${acessocidadao.secret}")
    private String clientSecret;
  
    @Value("${acessocidadao.scope}")
    private String scopes;

    public String getClientToken() {
    String basicToken = clientId + ":" + clientSecret;
    HttpClient httpClient = HttpClient.newHttpClient();

    String urlParameters  = "grant_type=client_credentials&scope=" + scopes;

    HttpRequest request;
    try {
        request = HttpRequest.newBuilder()
                                .header("Content-type", "application/x-www-form-urlencoded")
                                .header("Authorization", "Basic " + Base64.getEncoder().encodeToString(basicToken.getBytes()))
                                .uri(new URI(ACTokenUrl))
                                .POST(BodyPublishers.ofString(urlParameters)).build();

                                
      HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString(Charset.forName("UTF-8")));

      
      if(response.statusCode() == HttpStatus.OK.value()) {
        TokenResponseDto tokenResponse = new JsonMapper().readValue(response.body(), TokenResponseDto.class);
        return tokenResponse.access_token();
      } else {
        Logger.getGlobal().severe(response.statusCode() + ": " + response.body());
      }

    } catch (Exception e) {
      Logger.getGlobal().info("ACTokenUrl: " + ACTokenUrl);
      e.printStackTrace();
    }

    return null;
  }

  public List<OrganizacoesACDto> getOrgaos() {
    String token = getClientToken();

    if (token == null) {
        Logger.getGlobal().severe("Falha ao obter o token de autenticação.");
        return Collections.emptyList();
    }

    HttpClient httpClient = HttpClient.newHttpClient();

    try {
        HttpRequest request = HttpRequest.newBuilder()
            .header("Content-type", "application/json")
            .header("Authorization", "Bearer " + token)
            .uri(new URI(this.organogramaUrl + "/organizacoes/" + GUID_GOVES + "/filhas/"))
            .GET()
            .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString(Charset.forName("UTF-8")));

        if (response.statusCode() == HttpStatus.OK.value()) {
            return new JsonMapper()
                .readValue(response.body(), new TypeReference<List<UnidadesACResponseDto>>() {})
                .stream()
                .map(u -> new OrganizacoesACDto(u.nomeFantasia(), u.sigla()))
                .toList();
        } else {
            Logger.getGlobal().severe("token: " + token);
            Logger.getGlobal().severe(response.statusCode() + ": " + response.body());
        }
    } catch (Exception e) {
        Logger.getGlobal().info("token: " + token);
        e.printStackTrace();
    }

    return Collections.emptyList(); 
}

}
