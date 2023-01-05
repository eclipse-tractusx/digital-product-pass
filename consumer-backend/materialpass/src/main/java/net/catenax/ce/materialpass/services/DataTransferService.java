package net.catenax.ce.materialpass.services;

import com.fasterxml.jackson.databind.JsonNode;
import net.catenax.ce.materialpass.exceptions.ServiceException;
import net.catenax.ce.materialpass.models.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tools.*;

import java.util.Map;

@Service
public class DataTransferService {

    private final VaultService vaultService = new VaultService();
    public static final configTools configuration = new configTools();
    public final String serverUrl = (String) configuration.getConfigurationParam("variables.serverUrl", ".", null);
    public final String APIKey = (String) vaultService.getLocalSecret("apiKey");
    public final String providerUrl = (String) configuration.getConfigurationParam("variables.providerUrl", ".", null);

    public Catalog getContractOfferCatalog(String providerUrl){
        try {
            String provider = providerUrl;
            String path = "/consumer/data/catalog";
            if (providerUrl == null) {
                provider = (String) configuration.getConfigurationParam("variables.providerUrl", ".", null);
            }
            if(serverUrl == null || APIKey==null || provider==null){
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
        }catch (Exception e){
            throw new ServiceException(this.getClass().getName()+"."+"getContractOfferCatalog",
                    e,
                    "It was not possible to retrieve the catalog!");
        }
    }

    public Negotiation doContractNegotiations(Offer contractOffer){
        try{

            contractOffer.open();
            HttpHeaders headers = httpTools.getHeaders();
            String path = "/consumer/data/contractnegotiations";
            // Get variables from configuration
            if(serverUrl == null || APIKey==null || providerUrl==null){
                return null;
            }
            String url = serverUrl + path;
            headers.add("Content-Type", "application/json");
            headers.add("X-Api-Key", APIKey);
            Object body = new NegotiationOffer(contractOffer.getConnectorId(),providerUrl,contractOffer);
            System.out.println(jsonTools.toJson(body));
            ResponseEntity<Object> response = httpTools.doPost(url, String.class, headers, httpTools.getParams(), body, false, false);
            String responseBody = (String) response.getBody();
            return (Negotiation) jsonTools.bindJsonNode(jsonTools.toJsonNode(responseBody), Negotiation.class);
        }catch (Exception e){
            throw new ServiceException(this.getClass().getName()+"."+"doContractNegotiations",
                    e,
                    "It was not possible to retrieve the catalog!");
        }
    }
    public Negotiation getNegotiation(String Id){
        try {
            HttpHeaders headers = httpTools.getHeaders();
            String path = "/consumer/data/contractnegotiations";
            // Get variables from configuration
            if(serverUrl == null || APIKey==null){
                return null;
            }
            String url = serverUrl + path + "/" + Id;
            headers.add("Content-Type", "application/json");
            headers.add("X-Api-Key", APIKey);
            Map<String, Object> params = httpTools.getParams();
            ResponseEntity<Object> response = httpTools.doGet(url, String.class, headers, params, false, false);
            String body = (String) response.getBody();
            return (Negotiation) jsonTools.bindJsonNode(jsonTools.toJsonNode(body), Negotiation.class);
        }catch (Exception e){
            throw new ServiceException(this.getClass().getName()+"."+"getNegotiation",
                    e,
                    "It was not possible to retrieve the catalog!");
        }
    }


    public Transfer doTransferProcess(Negotiation negotiation, String connectorId, String connectorAddress, Offer offer, Boolean managedResources){
        try{
            HttpHeaders headers = httpTools.getHeaders();
            String path = "/consumer/data/transferprocess";
            // Get variables from configuration
            String url = serverUrl + path;
            headers.add("Content-Type", "application/json");
            headers.add("X-Api-Key", APIKey);
            Object body = new TransferRequest(
                    DataTransferService.generateTransferId(negotiation, connectorId, connectorAddress),
                    connectorId,
                    connectorAddress,
                    //negotiation.getContractAgreementId(), -> At the moment null
                    negotiation.getId(),
                    offer.getAssetId(),
                    managedResources,
                    "HttpProxy"
            );
            System.out.println(jsonTools.toJson(body));
            ResponseEntity<Object> response = httpTools.doPost(url, String.class, headers, httpTools.getParams(), body, false, false);
            String responseBody = (String) response.getBody();
            return (Transfer) jsonTools.bindJsonNode(jsonTools.toJsonNode(responseBody), Transfer.class);
        }catch (Exception e){
            throw new ServiceException(this.getClass().getName()+"."+"doTransferProcess",
                    e,
                    "It was not possible to do transfer process!");
        }
    }

    public Object getTransfer(String Id){
        try {
            HttpHeaders headers = httpTools.getHeaders();
            String path = "/consumer/data/transferprocess";
            // Get variables from configuration
            if(serverUrl == null || APIKey==null){
                return null;
            }
            String url = serverUrl + path + "/" + Id;
            headers.add("Content-Type", "application/json");
            headers.add("X-Api-Key", APIKey);
            Map<String, Object> params = httpTools.getParams();
            String state = "INITIAL";
            JsonNode result = null;
            boolean sw = true;
            while(sw) {
                ResponseEntity<Object> response = httpTools.doGet(url, JsonNode.class, headers, params, false, false);
                result = (JsonNode) response.getBody();
                if(!result.has("state") || result.get("state") == null){
                    throw new ServiceException(this.getClass().getName()+"."+"getTransfer",
                            "It was not possible to retrieve the transfer!");
                }
                state = String.valueOf(result.get("state"));
                if(state.equals("COMPLETED") || state.equals("ERROR")){
                    sw = false;
                }
            }
            //return (Transfer) jsonTools.bindJsonNode(jsonTools.toJsonNode(body), Transfer.class);
            return result;
        }catch (Exception e){
            throw new ServiceException(this.getClass().getName()+"."+"getNegotiation",
                    e,
                    "It was not possible to retrieve the transfer!");
        }
    }
    /*
    STATIC FUNCTIONS
     */
    public static String generateTransferId(Negotiation negotiation, String connectorId, String connectorAddress){
        return crypTools.sha256(dateTimeTools.getDateTimeFormatted("yyyyMMddHHmmssSSS") + negotiation.getId() + connectorId + connectorAddress);
    }
}
