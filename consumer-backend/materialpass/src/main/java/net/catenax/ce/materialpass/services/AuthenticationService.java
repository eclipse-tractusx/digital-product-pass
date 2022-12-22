package net.catenax.ce.materialpass.services;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tools.configTools;
import tools.httpTools;
import tools.jsonTools;

@Service
public class AuthenticationService {
    @Autowired
    private VaultService vaultService;
    public static final configTools configuration = new configTools();
    public final String tokenUri = (String) configuration.getConfigurationParam("keycloak.tokenUri", ".", null);
    public String getToken(){
        String clientId = (String) vaultService.getLocalSecret("client.id");
        String clientSecret = (String) vaultService.getLocalSecret("client.secret");

        HttpHeaders headers = httpTools.getHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("clientId", clientId);
        headers.add("clientSecret", clientSecret);
        ResponseEntity<Object> response = httpTools.doPost(tokenUri, String.class, headers, httpTools.getParams(), false, false);
        String body = (String) response.getBody();
        JsonNode json = jsonTools.toJsonNode(body);
        return body;
    }



}
