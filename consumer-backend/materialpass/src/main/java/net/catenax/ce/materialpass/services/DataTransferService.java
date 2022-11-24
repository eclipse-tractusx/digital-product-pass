package net.catenax.ce.materialpass.services;

import com.fasterxml.jackson.databind.JsonNode;
import net.catenax.ce.materialpass.models.Catalog;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tools.configTools;
import tools.httpTools;
import tools.jsonTools;

import java.util.Map;

@Service
public class DataTransferService {
    public static final configTools configuration = new configTools();
    public Catalog getContractOfferCatalog(String providerUrl){
        String provider = providerUrl;
        if(providerUrl == null){
            provider = (String) configuration.getConfigurationParam("variables.providerUrl", ".", null);
        }
        if(provider == null){
            return null;
        }
        String path = "/consumer/data/catalog";
        String serverUrl = (String) configuration.getConfigurationParam("variables.serverUrl", ".", null);
        if(serverUrl == null){
            return null;
        }

        String APIKey = (String) configuration.getConfigurationParam("variables.apiKey", ".", null);
        if(APIKey == null){
            return null;
        }

        String url = serverUrl + path;
        Map<String, Object> params = httpTools.getParams();
        params.put("providerUrl", provider);
        HttpHeaders headers = httpTools.getHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("X-Api-Key", APIKey);
        ResponseEntity<Object> response = httpTools.doGet(url, String.class, headers, params, false, false);
        String body = (String) response.getBody();
        JsonNode json = jsonTools.toJsonNode(body);
        return (Catalog) jsonTools.bindJsonNode(json, Catalog.class);
    }
}
