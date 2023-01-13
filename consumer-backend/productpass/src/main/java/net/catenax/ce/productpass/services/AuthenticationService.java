package net.catenax.ce.productpass.services;

import com.fasterxml.jackson.databind.JsonNode;
import net.catenax.ce.productpass.exceptions.ServiceException;
import net.catenax.ce.productpass.exceptions.ServiceInitializationException;
import net.catenax.ce.productpass.models.auth.JwtToken;
import net.catenax.ce.productpass.models.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tools.configTools;
import tools.httpTools;
import tools.jsonTools;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AuthenticationService extends BaseService {
    @Autowired
    private VaultService vaultService;

    public static final configTools configuration = new configTools();
    public final String tokenUri = (String) configuration.getConfigurationParam("keycloak.tokenUri", ".", null);
    public String clientId;
    public String clientSecret;

    public AuthenticationService() throws ServiceInitializationException {
        this.checkEmptyVariables(List.of("clientId", "clientSecret"));
    }

    @Override
    public List<String> getEmptyVariables() {
        List<String> missingVariables = new ArrayList<>();
        if (tokenUri == null || tokenUri.isEmpty()) {
            missingVariables.add("tokenUri");
        }
        if (clientId == null || clientId.isEmpty()) {
            missingVariables.add("clientId");
        }
        if (clientSecret == null || clientSecret.isEmpty()) {
            missingVariables.add("clientSecret");
        }
        return missingVariables;
    }

    public JwtToken getToken(){
        try{
            this.clientId = (String) vaultService.getLocalSecret("client.id");
            this.clientSecret = (String) vaultService.getLocalSecret("client.secret");
            this.checkEmptyVariables();
            HttpHeaders headers = httpTools.getHeaders();
            headers.add("Content-Type", "application/x-www-form-urlencoded");
            Map<String,?> body = Map.of(
                    "grant_type", "client_credentials",
                    "client_id", clientId,
                    "client_secret", clientSecret,
                    "scope", "openid profile email"
            );

            String encodedBody = httpTools.mapToParams(body, false);
            ResponseEntity<?> response = httpTools.doPost(tokenUri, String.class, headers, httpTools.getParams(), encodedBody, false, false);
            String responseBody = (String) response.getBody();
            JsonNode json = jsonTools.toJsonNode(responseBody);
            JwtToken token = (JwtToken) jsonTools.bindJsonNode(json, JwtToken.class);
            return token;
        }catch (Exception e){
            throw new ServiceException(this.getClass().getName()+"."+"getToken",
                    e,
                    "It was not possible to retrieve the token!");
        }
    }



}
