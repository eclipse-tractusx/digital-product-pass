package net.catenax.ce.productpass.services;

import com.fasterxml.jackson.databind.JsonNode;
import net.catenax.ce.productpass.exceptions.ServiceException;
import net.catenax.ce.productpass.exceptions.ServiceInitializationException;
import net.catenax.ce.productpass.models.negotiation.*;
import net.catenax.ce.productpass.models.passports.PassportV1;
import net.catenax.ce.productpass.models.service.BaseService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tools.*;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class DataTransferService extends BaseService {

    private final VaultService vaultService = new VaultService();
    public static final configTools configuration = new configTools();
    public final String serverUrl = (String) configuration.getConfigurationParam("variables.serverUrl", ".", null);
    public final String APIKey = (String) vaultService.getLocalSecret("apiKey");
    public final String providerUrl = (String) configuration.getConfigurationParam("variables.providerUrl", ".", null);

    public DataTransferService() throws ServiceInitializationException {
        this.checkEmptyVariables(List.of("APIKey")); // Add API Key as optional for initialization
    }

    @Override
    public List<String> getEmptyVariables() {
        List<String> missingVariables = new ArrayList<>();
        if (serverUrl == null || serverUrl.isEmpty()) {
            missingVariables.add("serverUrl");
        }
        if (APIKey == null || APIKey.isEmpty()) {
            missingVariables.add("APIKey");
        }
        if (providerUrl == null || providerUrl.isEmpty()) {
            missingVariables.add("providerUrl");
        }
        return missingVariables;
    }

    public Catalog getContractOfferCatalog(String providerUrl) {
        try {
            this.checkEmptyVariables();
            String provider = providerUrl;
            String path = "/consumer/data/catalog";
            if (providerUrl == null) {
                provider = (String) configuration.getConfigurationParam("variables.providerUrl", ".", null);
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
        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName() + "." + "getContractOfferCatalog",
                    e,
                    "It was not possible to retrieve the catalog!");
        }
    }

    public Negotiation doContractNegotiations(Offer contractOffer) {
        try {
            this.checkEmptyVariables();
            contractOffer.open();
            logTools.printMessage("["+contractOffer.getId()+"] ===== [INITIALIZING CONTRACT NEGOTIATION] ===========================================");
            HttpHeaders headers = httpTools.getHeaders();
            String path = "/consumer/data/contractnegotiations";
            // Get variables from configuration
            if (serverUrl == null || APIKey == null || providerUrl == null) {
                return null;
            }
            String url = serverUrl + path;
            headers.add("Content-Type", "application/json");
            headers.add("X-Api-Key", APIKey);
            Object body = new NegotiationOffer(contractOffer.getConnectorId(), providerUrl, contractOffer);
            ResponseEntity<Object> response = httpTools.doPost(url, JsonNode.class, headers, httpTools.getParams(), body, false, false);
            JsonNode result = (JsonNode) response.getBody();
            return (Negotiation) jsonTools.bindJsonNode(result, Negotiation.class);
        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName() + "." + "doContractNegotiations",
                    e,
                    "It was not possible to retrieve the catalog!");
        }
    }

    public Negotiation getNegotiation(String Id) {
        try {
            this.checkEmptyVariables();
            HttpHeaders headers = httpTools.getHeaders();
            String path = "/consumer/data/contractnegotiations";
            // Get variables from configuration
            if (serverUrl == null || APIKey == null) {
                return null;
            }
            String url = serverUrl + path + "/" + Id;
            headers.add("Content-Type", "application/json");
            headers.add("X-Api-Key", APIKey);
            Map<String, Object> params = httpTools.getParams();
            JsonNode result = null;
            String actualState = "";
            boolean sw = true;
            Instant start = Instant.now();
            Instant end = start;
            logTools.printMessage("["+Id+"] ===== [STARTING CHECKING STATUS FOR CONTRACT NEGOTIATION]  ===========================================");
            while (sw) {
                ResponseEntity<Object> response = httpTools.doGet(url, JsonNode.class, headers, params, false, false);
                result = (JsonNode) response.getBody();
                if (!result.has("state") || result.get("state") == null) {
                    logTools.printMessage("["+Id+"] ===== [ERROR CONTRACT NEGOTIATION] ===========================================");
                    throw new ServiceException(this.getClass().getName() + "." + "doContractNegotiations",
                            "It was not possible to do contract negotiations!");
                }
                String state = result.get("state").asText();
                if (state.equals("CONFIRMED") || state.equals("ERROR")) {
                    sw = false;
                    logTools.printMessage("["+Id+"]===== [FINISHED CONTRACT NEGOTIATION] ===========================================");
                }
                if (!state.equals(actualState)) {
                    actualState = state; // Update current state
                    end = Instant.now();
                    Duration timeElapsed = Duration.between(start, end);
                    logTools.printMessage("["+Id+"] The contract negotiation status changed: [" + state + "] - TIME->[" + timeElapsed + "]s");
                    start = Instant.now();
                }
            }
            return (Negotiation) jsonTools.bindJsonNode(result, Negotiation.class);
        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName() + "." + "getNegotiation",
                    e,
                    "It was not possible to retrieve the catalog!");
        }
    }


    public Transfer initiateTransfer(TransferRequest transferRequest) {
        try {
            this.checkEmptyVariables();
            HttpHeaders headers = httpTools.getHeaders();
            String path = "/consumer/data/transferprocess";
            // Get variables from configuration
            String url = serverUrl + path;
            headers.add("Content-Type", "application/json");
            headers.add("X-Api-Key", APIKey);
            Object body = transferRequest;
            ResponseEntity<Object> response = httpTools.doPost(url, String.class, headers, httpTools.getParams(), body, false, false);
            String responseBody = (String) response.getBody();
            return (Transfer) jsonTools.bindJsonNode(jsonTools.toJsonNode(responseBody), Transfer.class);
        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName() + "." + "doTransferProcess",
                    e,
                    "It was not possible to initiate transfer process!");
        }
    }

    public Transfer getTransfer(String Id) {
        try {
            this.checkEmptyVariables();
            HttpHeaders headers = httpTools.getHeaders();
            String path = "/consumer/data/transferprocess";
            String url = serverUrl + path + "/" + Id;
            headers.add("Content-Type", "application/json");
            headers.add("X-Api-Key", APIKey);
            Map<String, Object> params = httpTools.getParams();
            JsonNode result = null;
            String actualState = "";
            boolean sw = true;
            Instant start = Instant.now();
            Instant end = start;
            logTools.printMessage("["+Id+"] ===== [STARTING CONTRACT TRANSFER] ===========================================");
            while (sw) {
                ResponseEntity<Object> response = httpTools.doGet(url, JsonNode.class, headers, params, false, false);
                result = (JsonNode) response.getBody();
                if (!result.has("state") || result.get("state") == null) {
                    logTools.printMessage("["+Id+"] ===== [ERROR CONTRACT TRANSFER]===========================================");
                    throw new ServiceException(this.getClass().getName() + "." + "getTransfer",
                            "It was not possible to do the transfer process!");
                }
                String state = result.get("state").asText();
                if (state.equals("COMPLETED") || state.equals("ERROR")) {
                    logTools.printMessage("["+Id+"] ===== [FINISHED CONTRACT TRANSFER] ["+Id+"]===========================================");
                    sw = false;
                }
                if (!state.equals(actualState)) {
                    actualState = state; // Update current state
                    end = Instant.now();
                    Duration timeElapsed = Duration.between(start, end);
                    logTools.printMessage("["+Id+"] The data transfer status changed: [" + state + "] - TIME->[" + timeElapsed + "]s");
                    start = Instant.now();
                }
            }
            return (Transfer) jsonTools.bindJsonNode(result, Transfer.class);
        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName() + "." + "getTransfer",
                    e,
                    "It was not possible to transfer the contract! " + Id);
        }
    }


    public PassportV1 getPassportV1(String transferProcessId) {
        try {
            this.checkEmptyVariables();
            String path = "/consumer_backend";
            String url = serverUrl + path + "/" + transferProcessId;
            Map<String, Object> params = httpTools.getParams();
            HttpHeaders headers = httpTools.getHeaders();
            headers.add("Accept", "application/octet-stream");
            boolean retry = false;
            ResponseEntity<Object> response = null;
            try {
                response = httpTools.doGet(url, String.class, headers, params, false, false);
            }catch (Exception e){
                throw new ServiceException(this.getClass().getName() + ".getPassportV1", "It was not possible to get passport with id " + transferProcessId);
            }
            String responseBody = (String) response.getBody();
            return (PassportV1) jsonTools.bindJsonNode(jsonTools.toJsonNode(responseBody), PassportV1.class);
        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName() + "." + "getPassportV1",
                    e,
                    "It was not possible to retrieve the getPassport V1 for transferProcessId ["+transferProcessId+"]!");
        }
    }

    /*
    STATIC FUNCTIONS
     */
    public static String generateTransferId(Negotiation negotiation, String connectorId, String connectorAddress) {
        return crypTools.sha256(dateTimeTools.getDateTimeFormatted("yyyyMMddHHmmssSSS") + negotiation.getId() + connectorId + connectorAddress);
    }

}
