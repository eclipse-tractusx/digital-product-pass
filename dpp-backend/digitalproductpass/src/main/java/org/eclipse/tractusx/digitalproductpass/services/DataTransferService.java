/*********************************************************************************
 *
 * Tractus-X - Digital Product Passport Application
 *
 * Copyright (c) 2022, 2024 BMW AG, Henkel AG & Co. KGaA
 * Copyright (c) 2023, 2024 CGI Deutschland B.V. & Co. KG
 * Copyright (c) 2022, 2024 Contributors to the Eclipse Foundation
 *
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Apache License, Version 2.0 which is available at
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the
 * License for the specific language govern in permissions and limitations
 * under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 ********************************************************************************/

package org.eclipse.tractusx.digitalproductpass.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import org.eclipse.tractusx.digitalproductpass.config.DtrConfig;
import org.eclipse.tractusx.digitalproductpass.config.PolicyCheckConfig;
import org.eclipse.tractusx.digitalproductpass.exceptions.ControllerException;
import org.eclipse.tractusx.digitalproductpass.exceptions.ServiceException;
import org.eclipse.tractusx.digitalproductpass.exceptions.ServiceInitializationException;
import org.eclipse.tractusx.digitalproductpass.managers.ProcessDataModel;
import org.eclipse.tractusx.digitalproductpass.managers.ProcessManager;
import org.eclipse.tractusx.digitalproductpass.models.catenax.Dtr;
import org.eclipse.tractusx.digitalproductpass.models.edc.CheckResult;
import org.eclipse.tractusx.digitalproductpass.models.http.requests.Search;
import org.eclipse.tractusx.digitalproductpass.models.http.responses.IdResponse;
import org.eclipse.tractusx.digitalproductpass.models.manager.History;
import org.eclipse.tractusx.digitalproductpass.models.manager.Status;
import org.eclipse.tractusx.digitalproductpass.models.negotiation.CallbackAddress;
import org.eclipse.tractusx.digitalproductpass.models.negotiation.catalog.Catalog;
import org.eclipse.tractusx.digitalproductpass.models.negotiation.catalog.CatalogRequest;
import org.eclipse.tractusx.digitalproductpass.models.negotiation.catalog.Dataset;
import org.eclipse.tractusx.digitalproductpass.models.negotiation.catalog.Policy;
import org.eclipse.tractusx.digitalproductpass.models.negotiation.policy.Set;
import org.eclipse.tractusx.digitalproductpass.models.negotiation.response.Negotiation;
import org.eclipse.tractusx.digitalproductpass.models.negotiation.response.NegotiationTransferResponse;
import org.eclipse.tractusx.digitalproductpass.models.negotiation.request.NegotiationRequest;
import org.eclipse.tractusx.digitalproductpass.models.negotiation.request.TransferRequest;
import org.eclipse.tractusx.digitalproductpass.models.negotiation.response.Transfer;
import org.eclipse.tractusx.digitalproductpass.models.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import utils.*;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

/**
 * This class consists exclusively of methods to operate on executing the Data Transfer operations.
 *
 * <p> The methods and inner classes defined here are intended to do every needed operations in order to be able to Transfer the Passport Data of a given request.
 *
 */
@Service
public class DataTransferService extends BaseService {

    /** ATTRIBUTES **/
    private final HttpUtil httpUtil;

    private final PolicyUtil policyUtil;
    private final JsonUtil jsonUtil;
    public String apiKey;
    public String bpnNumber;
    public String edcEndpoint;
    public String managementPath;
    public String catalogPath;
    public String negotiationPath;
    public String transferPath;


    public Environment env;
    public ProcessManager processManager;

    public EdcUtil edcUtil;
    public DtrConfig dtrConfig;

    // Success states of the EDC exchanges
    final public List<String> successStates = List.of(
            "CONFIRMED",
            "FINALIZED",
            "COMPLETED"
    );
    // Success states of the transfer exchange
    final public List<String> transferSuccessStates = List.of(
            "STARTED",
            "COMPLETED"
    );
    // Error states of the EDC exchanges
    final public List<String> errorStates = List.of(
            "TERMINATED",
            "TERMINATING",
            "ERROR"
    );

    /** CONSTRUCTOR(S) **/
    @Autowired
    public DataTransferService(Environment env, HttpUtil httpUtil, EdcUtil edcUtil, JsonUtil jsonUtil, PolicyUtil policyUtil, VaultService vaultService, ProcessManager processManager, DtrConfig dtrConfig) throws ServiceInitializationException {
        this.httpUtil = httpUtil;
        this.edcUtil = edcUtil;
        this.jsonUtil = jsonUtil;
        this.policyUtil =policyUtil;
        this.processManager = processManager;
        this.dtrConfig = dtrConfig;
        this.env = env;
        this.init(vaultService, env);
        this.checkEmptyVariables(List.of("apiKey")); // Add API Key as optional for initialization
    }

    /** METHODS **/

    /**
     * Initiates the main needed variables for Data Transfer Service by loading from the environment variables and Vault.
     **/
    public void init(VaultService vaultService, Environment env) {
        this.apiKey = (String) vaultService.getLocalSecret("edc.apiKey");
        this.bpnNumber = (String) vaultService.getLocalSecret("edc.participantId");
        this.edcEndpoint = env.getProperty("configuration.edc.endpoint", "");
        this.catalogPath = env.getProperty("configuration.edc.catalog", "");
        this.managementPath = env.getProperty("configuration.edc.management", "");
        this.negotiationPath = env.getProperty("configuration.edc.negotiation", "");
        this.transferPath = env.getProperty("configuration.edc.transfer", "");
    }

    /**
     * Creates a List of missing variables needed to proceed with the request.
     * <p>
     *
     * @return an {@code Arraylist} with the environment variables missing in the configuration for the request.
     *
     */
    @Override
    public List<String> getEmptyVariables() {
        List<String> missingVariables = new ArrayList<>();
        if (this.edcEndpoint == null || this.edcEndpoint.isEmpty()) {
            missingVariables.add("endpoint");
        }
        if (this.apiKey == null || this.apiKey.isEmpty()) {
            missingVariables.add("apiKey");
        }
        if (this.bpnNumber == null || this.bpnNumber.isEmpty()) {
            missingVariables.add("bpnNumber");
        }
        if (this.managementPath == null || this.managementPath.isEmpty()) {
            missingVariables.add("management");
        }
        if (this.catalogPath == null || this.catalogPath.isEmpty()) {
            missingVariables.add("catalog");
        }
        if (this.negotiationPath == null || this.negotiationPath.isEmpty()) {
            missingVariables.add("negotiation");
        }
        if (this.transferPath == null || this.transferPath.isEmpty()) {
            missingVariables.add("transfer");
        }

        return missingVariables;
    }

    /**
     * Checks the EDC consumer connection by trying to establish a connection and retrieve an empty catalog.
     * <p>
     *
     * @return a {@code String} participantId of the retrieved catalog.
     *
     * @throws  ControllerException
     *           if unable to check the EDC consumer connection.
     */
    public Boolean checkEdcConsumerConnection() throws ServiceException {
        try {
            return this.getReadinessStatus().getSystemHealthy();
        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName()+".checkEdcConsumerConnection", e, "It was not possible to establish connection with the EDC consumer endpoint [" + this.edcEndpoint+"]");
        }
    }
    /**
     * Checks the EDC consumer connection by trying to establish a connection and retrieve an empty catalog.
     * <p>
     *
     * @return a {@code Boolean} true if bpn number is the same as the application one
     *
     * @throws  ControllerException
     *           if unable to check the EDC consumer connection.
     */
    public Boolean isApplicationEdc(String applicationBpn) throws ServiceException {
        try {
            String edcConsumerDsp = this.edcEndpoint + CatenaXUtil.edcDataEndpoint;
            Catalog catalog = this.getContractOfferCatalog(edcConsumerDsp, applicationBpn, ""); // Get empty catalog
            if (catalog == null || catalog.getParticipantId() == null || catalog.getParticipantId().isEmpty()) {
                return false;
            }
            return catalog.getParticipantId().equals(applicationBpn); // Return true if the bpns matches
        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName()+".isApplicationEdc", e, "It was not possible to establish connection with the EDC consumer endpoint [" + this.edcEndpoint+"]");
        }
    }
    /**
     * A wrapper function to filter the contract offers and give the valid ones according to the configuration
     * <p>
     * @param   catalog
     *          the {@code Catalog} catalog with the complete offers
     * @param   policyConfig
     *          the {@code PolicyCheckConfig} configuration for the policies contained in the contracts
     * @return  a {@code Map<String, Dataset>} object with the contract offers mapped by id
     *
     * @throws  ServiceException
     *           if unable to get the contract offer for the assetId.
     */
    public Map<String, Dataset> getValidContractOffers(Catalog catalog, PolicyCheckConfig policyConfig) throws ServiceException {
        /*
         *   This method receives the assetId and looks up for targets with the same name.
         */
        try {
            // Get contracts with the default method
            Map<String, Dataset> contracts = this.getContractOffers(catalog);
            if(contracts == null || contracts.keySet().size() == 0){
                return null;
            }
            // If policy check is not enabled return all the contract offers
            if(!policyConfig.getEnabled()){
                return contracts;
            }
            // If is enabled filter for the valid contract offers
            return edcUtil.filterValidContracts(contracts, policyConfig);
        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName(), e, "It was not possible to get the valid contract Contract Offers");
        }
    }

    /**
     * Gets the Contract Offers mapped by contract id
     * <p>
     * @param   catalog
     *          the {@code Catalog} catalog with the complete offers
     *
     * @return  a {@code Map<String, Dataset>} object with the contract offers mapped by id
     *
     * @throws  ServiceException
     *           if unable to get the contract offer for the assetId.
     */
    public Map<String, Dataset> getContractOffers(Catalog catalog) throws ServiceException {
        /*
         *   This method receives the assetId and looks up for targets with the same name.
         */
        try {
            if(catalog==null){
                return null;
            }
            Object offers = catalog.getContractOffers();
            if(offers == null){
                return null;
            }
            if(catalog.getContractOffers() instanceof LinkedHashMap){
                Dataset retDataset = (Dataset) jsonUtil.bindObject(offers, Dataset.class);
                return new HashMap<>(){{put(retDataset.getId(),retDataset);}};
            }

            List<Dataset> contractOffers = (List<Dataset>) jsonUtil.bindObject(offers, List.class);
            if(contractOffers.size() == 0){
                return null;
            }

            return edcUtil.mapDatasetsById(contractOffers);
        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName(), e, "It was not possible to get Contract Offers for assetId");
        }
    }

    /**
     * Gets the Contract Offer from the given AssetId in the given provider URL.
     * <p>
     * @param   assetId
     *          the {@code String} identification of the EDC's asset to lookup for.
     * @param   counterPartyAddress
     *          the {@code String} provider URL of the asset.
     *
     * @return  a {@code Dataset} object with the contract offer information.
     *
     * @throws  ServiceException
     *           if unable to get the contract offer for the assetId.
     */
    public Dataset getContractOfferByAssetId(String assetId, String counterPartyAddress, String counterPartId) throws ServiceException {
        /*
         *   This method receives the assetId and looks up for targets with the same name.
         */
        try {
            Catalog catalog = this.getContractOfferCatalog(counterPartyAddress, counterPartId, assetId);
            if(catalog == null){
                return null;
            }
            Object offers = catalog.getContractOffers();
            if(offers == null){
                return null;
            }
            if(catalog.getContractOffers() instanceof LinkedHashMap){
                return (Dataset) jsonUtil.bindObject(offers, Dataset.class);
            }

            List<Dataset> contractOffers = (List<Dataset>) jsonUtil.bindObject(offers, List.class);
            if(contractOffers.size() == 0){
                return null;
            }
            int i = 0;
            Map<String, Integer> contractOffersMap = new HashMap<>();
            for(Dataset offer: contractOffers){
                contractOffersMap.put(offer.getAssetId(),i);
                i++;
            }
            if(!contractOffersMap.containsKey(assetId))
            {
                return null;
            }
            Integer index = contractOffersMap.get(assetId);
            return contractOffers.get(index);
        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName(), e, "It was not possible to get Contract Offer for assetId [" + assetId + "]");
        }
    }

    /**
     * Builds a negotiation request with the given data.
     * <p>
     * @param   dataset
     *          the {@code Dataset} data for the contract offer.
     * @param   endpoint
     *          the {@code String} url from the edc
     * @param   providerBpn
     *          the {@code String} BPN number from provider of the catalog
     *
     * @return  a {@code NegotiationRequest} object with the given data.
     *
     */
    public NegotiationRequest buildRequestFirstPolicy(Dataset dataset, String endpoint, String providerBpn) {
        return new NegotiationRequest(
                jsonUtil.toJsonNode(Map.of("odrl", "http://www.w3.org/ns/odrl/2/")),
                endpoint,
                "dataspace-protocol-http",
                this.buildOffer(dataset, 0, providerBpn)
        );
    }

    /**
     * Builds a negotiation request with the given data by id.
     * <p>
     * @param   dataset
     *          the {@code Dataset} data for the contract offer.
     * @param   endpoint
     *          the {@code String} sedc endpoint
     * @param   providerBpn
     *          the {@code String} BPN number from provider of the catalog
     * @param   policyId
     *          the {@code policyId} id from the policy
     * @return  a {@code NegotiationRequest} object with the given data.
     *
     */
    public NegotiationRequest buildRequestById(Dataset dataset, String endpoint, String providerBpn, String policyId) {
        Policy policy = this.buildOfferById(dataset, policyId, providerBpn);
        return this.buildRequest(dataset, endpoint, providerBpn, policy);
    }

    /**
     * Builds a negotiation request with the given policy
     * <p>
     * @param   endpoint
     *          the {@code String} edc endpoint
     * @param   policyOffer
     *          the {@code Policy} policy offer to be negotiated
     *
     *
     * @return  a {@code NegotiationRequest} object with the given data.
     *
     */
    public NegotiationRequest buildRequest(String endpoint, Policy policyOffer) {
        return new NegotiationRequest(
                jsonUtil.toJsonNode(Map.of("odrl", "http://www.w3.org/ns/odrl/2/")),
                "odrl:ContractRequest",
                endpoint,
                "dataspace-protocol-http",
                policyOffer
        );
    }
    /**
     * Builds a negotiation request with the given policy
     * <p>
     * @param   dataset
     *          the {@code Dataset} data for the contract offer.
     * @param   endpoint
     *          the {@code String} edc endpoint
     * @param   providerBpn
     *          the {@code String} BPN number from provider of the catalog
     * @param   policy
     *          the {@code Sete} policy to be negotiated
     *
     *
     * @return  a {@code NegotiationRequest} object with the given data.
     *
     */
    public NegotiationRequest buildRequest(Dataset dataset, String endpoint, String providerBpn, Set policy) {
        return this.buildRequest(
                endpoint,
                this.buildOffer(dataset, policy, providerBpn)
        );
    }

    /**
     * Gets a policy by index from a dataset dynamic policy data
     * <p>
     * @param   policies
     *          the {@code Object} one or more policies from a dataset
     * @param   defaultIndex
     *          the {@code Integer} default index for the policy.
     *
     * @return  a {@code Offer} object with the given data built offer.
     *
     */
    public Set selectPolicyByIndex(Object policies, Integer defaultIndex){
        if(policies instanceof LinkedHashMap){
            return Set.build(policies);
        }
        List<LinkedHashMap> policyList = (List<LinkedHashMap>) jsonUtil.bindObject(policies, List.class);
        return Set.build(policyList.get(defaultIndex)); // Get fist policy from the list to resolve the conflict
    }

    /**
     * Builds a negotiation request with the given data.
     * <p>
     * @param   dataset
     *          the {@code Dataset} data for the offer.
     * @param   defaultIndex
     *          the {@code Integer} default index for the policy.
     *
     * @return  a {@code Offer} object with the given data built offer.
     *
     */
    public Policy buildOffer(Dataset dataset, Integer defaultIndex, String bpn) {
        Object rawPolicy = dataset.getPolicy();
        Set policy = null;
        if(rawPolicy instanceof LinkedHashMap){
            policy =  Set.build(rawPolicy);
        }else{
            List<LinkedHashMap> policyList = (List<LinkedHashMap>) jsonUtil.bindObject(rawPolicy, List.class);
            policy = Set.build(policyList.get(defaultIndex)); // Get fist policy from the list to resolve the conflict
        }
        return this.buildOffer(dataset, policy, bpn);
    }
    /**
     * Builds a negotiation request with the given data.
     * <p>
     * @param   dataset
     *          the {@code Dataset} data for the offer.
     * @param   policyId
     *          the {@code String} id of the selected policy to build the offer
     *
     * @return  a {@code Offer} object with the given data built offer.
     *
     */
    public Policy buildOfferById(Dataset dataset, String policyId, String bpn) {
        try {
            Object rawPolicy = dataset.getPolicy();
            Set policy = null;
            if (rawPolicy instanceof LinkedHashMap) {
                policy =  Set.build(rawPolicy);
            } else {
                List<Set> policyList = policyUtil.parsePolicies(rawPolicy);
                policy = policyList.stream().filter(p -> p.getId().equals(policyId)).findAny().orElse(null);
            }
            if(policy == null) {
                throw new ServiceException("DataTransferService.buildOfferById()", "Failed to build offer by id! ["+policyId+"] Because policy does not exists!");
            }
            return this.buildOffer(dataset, policy, bpn);
        }catch (Exception e) {
            throw new ServiceException("DataTransferService.buildOfferById()", e, "Failed to build offer by id! ["+policyId+"]");
        }
    }
    /**
     * Builds a negotiation request with the given data.
     * <p>
     * @param   dataset
     *          the {@code Dataset} data for the offer.
     * @param   policy
     *          the {@code Set} policy to be negotiated
     *
     * @return  a {@code Offer} object with the given data built offer.
     *
     */
    public Policy buildOffer(Dataset dataset, Set policy, String bpn) {
        Policy policyOffer = jsonUtil.bind(policy, new TypeReference<>() {});
        return policyOffer.setup(dataset.getAssetId(), bpn, "odrl:Offer");
    }
    /**
     * Gets the Contract Offer's Catalog from the provider.
     * <p>
     * @param   counterPartyAddress
     *          the {@code String} URL from the provider.
     * @param   assetId
     *          the {@code String} identification of the EDC's asset.
     *
     * @return  a {@code Catalog} object for the given AssetId .
     *
     * @throws  ServiceException
     *           if unable to retrieve the catalog.
     */
    public Catalog getContractOfferCatalog(String counterPartyAddress, String counterPartyId, String assetId) {
        try {
            this.checkEmptyVariables();

            String url = CatenaXUtil.buildManagementEndpoint(env, this.catalogPath);
            // Simple catalog request query with no limitation.
            CatalogRequest.QuerySpec querySpec = new CatalogRequest.QuerySpec();
            CatalogRequest.QuerySpec.FilterExpression filterExpression = new CatalogRequest.QuerySpec.FilterExpression(
                    "https://w3id.org/edc/v0.0.1/ns/id",
                    "=",
                    assetId
            ); // Filter by asset id
            querySpec.setFilterExpression(List.of(filterExpression));
            Object body = new CatalogRequest(
                    jsonUtil.toJsonNode(Map.of(
                        "edc", "https://w3id.org/edc/v0.0.1/ns/",
                        "odrl", "http://www.w3.org/ns/odrl/2/",
                            "dct","https://purl.org/dc/terms/"
                    )),
                    "dataspace-protocol-http",
                    counterPartyAddress,
                    counterPartyId,
                    querySpec,
                    "edc:CatalogRequest"
            );
            HttpHeaders headers = httpUtil.getHeaders();
            headers.add("Content-Type", "application/json");
            headers.add("X-Api-Key", this.apiKey);
            ResponseEntity<?> response = httpUtil.doPost(url, JsonNode.class, headers, httpUtil.getParams(), body, false, false);
            JsonNode result = (JsonNode) response.getBody();
            return (Catalog) jsonUtil.bindJsonNode(result, Catalog.class);
        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName() + "." + "getContractOfferCatalog",
                    e,
                    "It was not possible to retrieve the catalog!");
        }
    }

    /**
     * Searches for the Digital Twin's Catalog from the provider.
     * <p>
     * @param   counterPartyAddress
     *          the {@code String} URL from the provider.
     *
     * @return  a {@code Catalog} object of the given provider, if exists.
     *
     * @throws  ServiceException
     *           if unable to retrieve the catalog.
     */
    public Catalog searchDigitalTwinCatalog(String counterPartyAddress, String counterPartId) throws ServiceException {
        try {
            this.checkEmptyVariables();

            String url = CatenaXUtil.buildManagementEndpoint(env, this.catalogPath);
            // Simple catalog request query with no limitation.
            CatalogRequest.QuerySpec querySpec = new CatalogRequest.QuerySpec();
            CatalogRequest.QuerySpec.FilterExpression filterExpression = new CatalogRequest.QuerySpec.FilterExpression(
                    this.dtrConfig.getAssetPropType(),
                    "=",
                    this.dtrConfig.getAssetType()
            ); // Filter by asset id
            querySpec.setFilterExpression(List.of(filterExpression));
            Object body = new CatalogRequest(
                    jsonUtil.toJsonNode(Map.of(
                            "edc", "https://w3id.org/edc/v0.0.1/ns/",
                            "odrl", "http://www.w3.org/ns/odrl/2/",
                            "dct","https://purl.org/dc/terms/"
                    )),
                    "dataspace-protocol-http",
                    CatenaXUtil.buildDataEndpoint(counterPartyAddress),
                    counterPartId,
                    querySpec,
                    "edc:CatalogRequest"
            );

            HttpHeaders headers = httpUtil.getHeaders();
            headers.add("Content-Type", "application/json");
            headers.add("X-Api-Key", this.apiKey);
            ResponseEntity<?> response = httpUtil.doPost(url, JsonNode.class, headers, httpUtil.getParams(), body, false, false);
            if(response == null){
                return null;
            }
            JsonNode result = (JsonNode) response.getBody();
            return (Catalog) jsonUtil.bindJsonNode(result, Catalog.class);
        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName() + "." + "getContractOfferCatalog",
                    e,
                    "It was not possible to retrieve the catalog!");
        }
    }

    /**
     * Initiates the Contract Negotiation in order to retrieve it.
     * <p>
     * @param   negotiationRequest
     *          the {@code NegotiationRequest} object with negotiation request data.
     *
     * @return  a {@code IdResponse} object with the contract negotiation response.
     *
     * @throws  ServiceException
     *           if unable to retrieve the contract negotiation.
     */
    public IdResponse doContractNegotiation(NegotiationRequest negotiationRequest) {
        try {
            this.checkEmptyVariables();
            LogUtil.printDebug("[" + negotiationRequest.getPolicy().getId() + "] ===== [INITIALIZING CONTRACT NEGOTIATION] ===========================================");
            String url = CatenaXUtil.buildManagementEndpoint(env, this.negotiationPath);
            HttpHeaders headers = httpUtil.getHeaders();
            headers.add("Content-Type", "application/json");
            headers.add("X-Api-Key", this.apiKey);
            ResponseEntity<?> response = httpUtil.doPost(url, JsonNode.class, headers, httpUtil.getParams(), negotiationRequest, false, false);
            JsonNode result = (JsonNode) response.getBody();
            return (IdResponse) jsonUtil.bindJsonNode(result, IdResponse.class);
        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName() + "." + "doContractNegotiations",
                    e,
                    "It was not possible to retrieve the contract negotiation!");
        }
    }

    /**
     * Initiates the Contract Negotiation in order to retrieve it.
     * <p>
     * @param   policy
     *          the {@code Offer} object with contract offer data.
     * @param   counterPartyAddress
     *          the {@code String} URL from the provider.
     *
     * @return  a {@code IdResponse} object with the contract negotiation response.
     *
     * @throws  ServiceException
     *           if unable to retrieve the contract negotiation.
     */
    public IdResponse doContractNegotiation(Policy policy, String counterPartyAddress) {
        try {
            this.checkEmptyVariables();
            return this.doContractNegotiation(this.buildRequest(counterPartyAddress, policy));
        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName() + "." + "doContractNegotiations",
                    e,
                    "It was not possible to execute the contract negotiation!");
        }
    }
    /**
     * Retrieves the contract negotiation/transfer state success status
     * <p>
     * @param   state
     *          the {@code String} contains the state of the contract negotiation
     * @param   completedStates
     *          the {@code List<String>} contains the list of "success" states, if {@code mull} the default edc
     *          states will be used
     *
     * @return  a {@code Boolean} with indicating if the negotiation/transfer is in a success state
     *
     * @throws  ServiceException
     *           there was an error with the processing or the negotiation response was an error
     */
    public Boolean isStateSuccess(String state, List<String> completedStates){
        if(completedStates == null){
            completedStates = successStates;
        }
        if (state == null || state.isEmpty() || errorStates.contains(state)) {
            throw new ServiceException(this.getClass().getName() + "." + "isStateSuccess",
                    "It was not possible to do the information exchange with the EDC!");
        }                // If the state is success
        return completedStates.contains(state);
    }

    /**
     * Gets the Negotiation/Transfer and waits for state to be success
     * <p>
     * @param   url
     *          the {@code String} url of the status exchange api
     * @param   id
     *          the {@code String} id of the negotiation response.
     * @param   processId
     *          the {@code String} id of the application's process.
     * @param   dataModel
     *          the {@code ProcessDataModel} object of the process's data model.
     *
     * @return  a {@code JsonNode} object with the negotiation/transfer data. If null there was an error in retrieving the exchange status.
     *
     * @throws  ServiceException
     *           if unable to process the exchange of the negotiation or the transfer
     */
    public JsonNode processExchange(String url, String id, String processId, ProcessDataModel dataModel, List<String> completedStates) throws ServiceException {
        // Initialize variables
        String actualState = "", state = "";
        boolean success;
        Instant start = Instant.now(), end = start;
        NegotiationTransferResponse body = null;

        // Initialize headers for the request
        HttpHeaders headers = httpUtil.getHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("X-Api-Key", this.apiKey);

        LogUtil.printDebug("[" + id + "] ===== [STARTING CHECKING STATUS FOR CONTRACT NEGOTIATION]  ===========================================");
        // Request for the negotiation status always
        do {
            // Get the negotiation/transfer status
            body = (NegotiationTransferResponse) httpUtil.doGet(url, NegotiationTransferResponse.class, headers, httpUtil.getParams(), false, false).getBody();
            if (body == null) {
                throw new ServiceException(this.getClass().getName() + "." + "processExchange",
                        "No response was received from the EDC!");
            }

            // Get the current state
            state = body.getState();
            try {
                // Check if the state is already successful
                success = this.isStateSuccess(state, completedStates);
            }catch(Exception e){
                LogUtil.printDebug("[" + id + "] ===== [ERROR CONTRACT NEGOTIATION] ===========================================");
                return null;
            }
            end = Instant.now();
            Duration timeElapsed = Duration.between(start, end);
            // If the state has changed we will print the negotiation/state for the debug
            if (!state.equals(actualState)) {
                actualState = state;
                LogUtil.printDebug("[" + id + "] The contract exchange status changed: [" + state + "] - TIME->[" + timeElapsed + "]s");
                start = Instant.now();
            }
            // If the process has not changed and the timeout has arrived it will break the process exchange
            if(timeElapsed.getSeconds() >= env.getProperty("configuration.edc.timeout.exchange", Integer.class, 20)){
                LogUtil.printError("TIMEOUT! [" + id + "] The contract exchange status took too long in state  ["+ state + "] - Duration [" + timeElapsed + "]s");
                throw new ServiceException(this.getClass().getName() + "." + "processExchange",
                        "Timeout achieved while requesting the contract exchange for transfer or negotiation!");
            }

            // If the user calls the cancel api the negotiation/transfer will be terminated
            if (dataModel != null && processId != null && dataModel.getState(processId).equals("TERMINATED")) {
                LogUtil.printStatus("[" + id + "] The contract exchange was cancelled!");
                return null;
            }
            // If is not success we will wait for the next request to be done
            if(!success){
                ThreadUtil.sleep(this.env.getProperty("configuration.edc.delay", Integer.class, 200)); // Wait some milliseconds
            }
        } while (!success);
        // Get the latest status from the contract exchange
        JsonNode response = (JsonNode) httpUtil.doGet(url, JsonNode.class, headers, httpUtil.getParams(), false, false).getBody();
        if (response == null) {
            throw new ServiceException(this.getClass().getName() + "." + "processExchange",
                    "No response was received in the last status request from the EDC!");
        }
        return response;
    }

    /**
     * Gets the Negotiation data object from a Negotiation Response related to a Process.
     * <p>
     * @param   id
     *          the {@code String} id of the negotiation response.
     * @param   processId
     *          the {@code String} id of the application's process.
     * @param   dataModel
     *          the {@code ProcessDataModel} object of the process's data model.
     *
     * @return  a {@code Negotiation} object with the negotiation data.
     *
     * @throws  ServiceException
     *           if unable to see the negotiation.
     */
    public Negotiation seeNegotiation(String id, String processId, ProcessDataModel dataModel) {
        try {
            this.checkEmptyVariables();

            String endpoint = CatenaXUtil.buildManagementEndpoint(env, this.negotiationPath);
            // Get variables from configuration
            String url = endpoint + "/" + id;
            // Do the process exchange
            JsonNode response = this.processExchange(url, id, processId, dataModel, this.successStates);
            if(response == null) {
                return null;
            }
            return (Negotiation) jsonUtil.bindReferenceType(response, new TypeReference<Negotiation>() {});
        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName() + "." + "getNegotiation",
                    e,
                    "It was not possible to see the contract negotiation!");
        }
    }

    /**
     * Gets the Negotiation data object from a Negotiation Response.
     * <p>
     * @param   id
     *          the {@code String} id of the negotiation response.
     *
     * @return  a {@code Negotiation} object with the negotiation data.
     *
     * @throws  ServiceException
     *           if unable to see the negotiation.
     */
    public Negotiation seeNegotiation(String id) {
        try {
            return this.seeNegotiation(id, null, null);
        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName() + "." + "getNegotiation",
                    e,
                    "It was not possible to see the contract negotiation!");
        }
    }

    /**
     * Initiates a transfer regarding a transfer request.
     * <p>
     * @param   transferRequest
     *          the {@code TransferRequest} object with transfer request data.
     *
     * @return  a {@code IdResponse} object with the transfer response.
     *
     * @throws  ServiceException
     *           if unable to see the negotiation.
     */
    public IdResponse initiateTransfer(TransferRequest transferRequest) {
        try {
            this.checkEmptyVariables();
            HttpHeaders headers = httpUtil.getHeaders();
            // Get variables from configuration
            String url = CatenaXUtil.buildManagementEndpoint(env, this.transferPath);

            headers.add("Content-Type", "application/json");
            headers.add("X-Api-Key", this.apiKey);
            ResponseEntity<?> response = httpUtil.doPost(url, String.class, headers, httpUtil.getParams(), transferRequest, false, false);
            String responseBody = (String) response.getBody();
            return (IdResponse) jsonUtil.bindJsonNode(jsonUtil.toJsonNode(responseBody), IdResponse.class);
        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName() + "." + "doTransferProcess",
                    e,
                    "It was not possible to initiate transfer process!");
        }
    }

    /**
     * Gets the Transfer data object from a Transfer Response.
     * <p>
     * @param   id
     *          the {@code String} id of the transfer response.
     *
     * @return  a {@code Negotiation} object with the negotiation data.
     *
     * @throws  ServiceException
     *           if unable to get the transfer data.
     */
    public Transfer seeTransfer(String id) {
        try {
            return this.seeTransfer(id, null, null);
        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName() + "." + "getTransfer",
                    e,
                    "It was not possible to transfer the contract! " + id);
        }
    }

    /**
     * Gets the Transfer data object from a Transfer Response related to a Process.
     * <p>
     * @param   id
     *          the {@code String} id of the transfer response.
     * @param   processId
     *          the {@code String} id of the application's process.
     * @param   dataModel
     *          the {@code ProcessDataModel} object of the process's data model.
     *
     * @return  a {@code Negotiation} object with the negotiation data.
     *
     * @throws  ServiceException
     *           if unable to get the transfer data.
     */
    public Transfer seeTransfer(String id, String processId, ProcessDataModel dataModel) {
        try {
            this.checkEmptyVariables();
            String endpoint = CatenaXUtil.buildManagementEndpoint(env, this.transferPath);
            String url = endpoint + "/" + id;
            // Do the process exchange
            JsonNode response = this.processExchange(url, id, processId, dataModel, this.transferSuccessStates);
            if(response == null) {
                return null;
            }
            return (Transfer) jsonUtil.bindReferenceType(response, new TypeReference<Transfer>() {});
        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName() + "." + "getTransfer",
                    e,
                    "It was not possible to transfer the contract! " + id);
        }
    }

    /**
     * Gets the Health Readiness Status of the EDC
     * <p>
     *
     * @return  a {@code CheckResult} object with the health status readiness
     *
     * @throws  ServiceException
     *           if unable to get readiness status
     */
    public CheckResult getReadinessStatus() {
        try {
            this.checkEmptyVariables();
            String endpoint = CatenaXUtil.buildReadinessApi(env);
            Map<String, Object> params = httpUtil.getParams();
            HttpHeaders headers = httpUtil.getHeaders();
            ResponseEntity<?> response = null;
            try {
                response = httpUtil.doGet(endpoint, String.class, headers, params, false, false);
            } catch (Exception e) {
                throw new ServiceException(this.getClass().getName() + ".getReadinessStatus", "It was not possible to get readiness status from the edc endpoint ["+endpoint+"]!");
            }
            String responseBody = (String) response.getBody();
            return (CheckResult) jsonUtil.bindJsonNode(jsonUtil.toJsonNode(responseBody), CheckResult.class);
        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName() + "." + "getReadinessStatus",
                    e,
                    "It was not possible to get readiness status from the edc consumer!");
        }
    }

    /** STATIC METHODS **/

    /**
     * Gets the Passport version 3 from the Process.
     * <p>
     * @param   negotiation
     *          the {@code Negotiation} object for the request.
     * @param   connectorId
     *          the {@code String} identification of the connector.
     * @param   connectorAddress
     *          the {@code String} URL address the of the connector.
     *
     * @return  a {@code PassportV3} object with the passport data.
     *
     * @throws  ServiceException
     *           if unable to get the passport.
     */
    @SuppressWarnings("Unused")
    public static String generateTransferId(Negotiation negotiation, String connectorId, String connectorAddress) {
        return CrypUtil.sha256(DateTimeUtil.getDateTimeFormatted("yyyyMMddHHmmssSSS") + negotiation.getId() + connectorId + connectorAddress);
    }

    /** INNER CLASSES **/

    /**
     * This inner class consists exclusively of methods to operate on executing the Contract Negotiation  .
     *
     * <p> The methods defined here are intended to do every needed operation in order to be able to Negotiate the Contract.
     *
     */
    public class NegotiateContract implements Runnable {

        /** ATTRIBUTES **/
        private NegotiationRequest negotiationRequest;
        private ProcessDataModel dataModel;
        private Dataset dataset;
        private Negotiation negotiation;
        private Transfer transfer;
        private TransferRequest transferRequest;
        private IdResponse negotiationResponse;
        private IdResponse tranferResponse;
        private Integer negotiationAttempts;
        private Integer transferAttempts;
        private String endpoint;
        private String processId;

        private String providerBpn;

        /** CONSTRUCTOR(S) **/
        public NegotiateContract() {};
        // Negotiate contract with policy
        public NegotiateContract(ProcessDataModel dataModel, String processId, String providerBpn, Dataset dataset, String endpoint, Set policy) {
            this.dataModel = dataModel;
            this.processId = processId;
            this.dataset = dataset;
            this.endpoint = endpoint;
            this.providerBpn = providerBpn;
            this.negotiationRequest = buildRequest(dataset, endpoint, providerBpn, policy);
        }
        // Start the negotiation and build contract by policy id
        public NegotiateContract(ProcessDataModel dataModel, String processId,String providerBpn, Dataset dataset, String endpoint, String policyId) {
            this.dataModel = dataModel;
            this.processId = processId;
            this.dataset = dataset;
            this.endpoint = endpoint;
            this.providerBpn = providerBpn;
            this.negotiationRequest = buildRequestById(dataset, endpoint, providerBpn, policyId);
        }

        public NegotiateContract(NegotiationRequest negotiationRequest, ProcessDataModel dataModel, Dataset dataset, Negotiation negotiation, Transfer transfer, TransferRequest transferRequest, IdResponse negotiationResponse, IdResponse tranferResponse, Integer negotiationAttempts, Integer transferAttempts, String endpoint, String processId, String providerBpn) {
            this.negotiationRequest = negotiationRequest;
            this.dataModel = dataModel;
            this.dataset = dataset;
            this.negotiation = negotiation;
            this.transfer = transfer;
            this.transferRequest = transferRequest;
            this.negotiationResponse = negotiationResponse;
            this.tranferResponse = tranferResponse;
            this.negotiationAttempts = negotiationAttempts;
            this.transferAttempts = transferAttempts;
            this.endpoint = endpoint;
            this.processId = processId;
            this.providerBpn = providerBpn;
        }

        /** GETTERS AND SETTERS **/
        @SuppressWarnings("Unused")
        public void setNegotiationRequest(NegotiationRequest negotiationRequest) {
            this.negotiationRequest = negotiationRequest;
        }

        public Dataset getDataset() {
            return dataset;
        }

        public void setDataset(Dataset dataset) {
            this.dataset = dataset;
        }

        public Negotiation getNegotiation() {
            return negotiation;
        }

        public void setNegotiation(Negotiation negotiation) {
            this.negotiation = negotiation;
        }

        @SuppressWarnings("Unused")
        public NegotiationRequest getNegotiationRequest() {
            return negotiationRequest;
        }

        @SuppressWarnings("Unused")
        public ProcessDataModel getDataModel() {
            return dataModel;
        }

        @SuppressWarnings("Unused")
        public void setDataModel(ProcessDataModel dataModel) {
            this.dataModel = dataModel;
        }

        @SuppressWarnings("Unused")
        public Integer getNegotiationAttempts() {
            return negotiationAttempts;
        }

        @SuppressWarnings("Unused")
        public void setNegotiationAttempts(Integer negotiationAttempts) {
            this.negotiationAttempts = negotiationAttempts;
        }

        @SuppressWarnings("Unused")
        public Integer getTransferAttempts() {
            return transferAttempts;
        }

        @SuppressWarnings("Unused")
        public void setTransferAttempts(Integer transferAttempts) {
            this.transferAttempts = transferAttempts;
        }

        public String getEndpoint() {
            return endpoint;
        }

        public void setEndpoint(String endpoint) {
            this.endpoint = endpoint;
        }

        public String getProcessId() {
            return processId;
        }

        public void setProcessId(String processId) {
            this.processId = processId;
        }

        public Transfer getTransfer() {
            return transfer;
        }

        public void setTransfer(Transfer transfer) {
            this.transfer = transfer;
        }

        @SuppressWarnings("Unused")
        public TransferRequest getTransferRequest() {
            return transferRequest;
        }

        @SuppressWarnings("Unused")
        public void setTransferRequest(TransferRequest transferRequest) {
            this.transferRequest = transferRequest;
        }

        @SuppressWarnings("Unused")
        public IdResponse getNegotiationResponse() {
            return negotiationResponse;
        }

        @SuppressWarnings("Unused")
        public void setNegotiationResponse(IdResponse negotiationResponse) {
            this.negotiationResponse = negotiationResponse;
        }

        @SuppressWarnings("Unused")
        public IdResponse getTranferResponse() {
            return tranferResponse;
        }

        @SuppressWarnings("Unused")
        public void setTranferResponse(IdResponse tranferResponse) {
            this.tranferResponse = tranferResponse;
        }

        /** METHODS **/

        /**
         * Builds a transfer request with the given data.
         * <p>
         * @param   dataset
         *          the {@code Dataset} data for the contract offer.
         * @param   endpoint
         *          the {@code String} edc endpoint
         * @param   negotiation
         *          the {@code Negotiation} object for the request.
         *
         * @return  a {@code TransferRequest} object with the given data.
         *
         * @throws  ServiceException
         *           if unable to build the transfer request.
         */
        public TransferRequest buildTransferRequest(Dataset dataset, String endpoint, Negotiation negotiation) {
            try {
                String receiverEndpoint = env.getProperty("configuration.edc.receiverEndpoint") + "/" + this.processId; // Send process Id to identification the session.


                TransferRequest.DataDestination dataDestination = new TransferRequest.DataDestination();
                dataDestination.setType("HttpProxy");


                List<CallbackAddress> callbackAddresses = List.of(CallbackAddress.builder().transactional(false).uri(receiverEndpoint).events(List.of("transfer.process.started")).build());

                return new TransferRequest(
                        jsonUtil.toJsonNode(Map.of("odrl", "http://www.w3.org/ns/odrl/2/","@vocab", "https://w3id.org/edc/v0.0.1/ns/")),
                        dataset.getAssetId(),
                        endpoint,
                        negotiation.getContractAgreementId(),
                        dataDestination,
                        false,
                        "dataspace-protocol-http",
                        env.getProperty("configuration.edc.transferType"),
                        callbackAddresses
                );
            } catch (Exception e) {
                throw new ServiceException(this.getClass().getName(), e, "Failed to build the transfer request!");
            }
        }

        /**
         * This method is exclusively for the Negotiation Process.
         *
         * <p> It's a Thread level method from Runnable interface and does the Negotiation Request, gets the Negotiation Response and saves in the Process.
         * Also builds Transfer Request, and gets the data from the Transfer Response and save it in the Process.
         *
         * @throws  ServiceException
         *           if unable to do the negotiation and/or transferring the data.
         */
        @Override
        public void run() {
            // NEGOTIATION PROCESS
            try {
                processManager.saveNegotiationRequest(processId, negotiationRequest, new IdResponse(processId, null), false);
                this.negotiationResponse = this.requestNegotiation(this.negotiationRequest);
                processManager.saveNegotiationRequest(processId, negotiationRequest, negotiationResponse, false);
                this.negotiation = this.getNegotiationData(negotiationResponse);
                if (this.negotiation == null) {
                    return;
                }
                processManager.saveNegotiation(this.processId, this.negotiation, false);
                String state = this.negotiation.getState();
                if (!successStates.contains(state)) {
                    throw new ServiceException(this.getClass().getName(), "Contract Negotiation Process Failed [" + this.negotiation.getId() + "]");
                }
            } catch (Exception e) {
                processManager.setStatus(this.processId, "negotiation-failed", new History(
                        this.processId,
                        "FAILED"
                ));
                this.dataModel.setState(processId, "FAILED");
                throw new ServiceException(this.getClass().getName(), e, "Failed to do the contract negotiation!");
            }

            if (this.dataModel.getState(processId).equals("TERMINATED")) {
                LogUtil.printMessage("Terminated process " + processId + " transfer terminated!");
                return;
            }
            ;
            this.dataModel.setState(processId, "NEGOTIATED");
            LogUtil.printStatus("[PROCESS " + this.processId + "] Negotiation Finished with status [" + negotiation.getState() + "]!");
            // TRANSFER PROCESS
            try {
                this.transferRequest = buildTransferRequest(this.dataset, this.endpoint, this.negotiation);
                processManager.saveTransferRequest(this.processId, transferRequest, new IdResponse(processId, null), false);
                this.tranferResponse = this.requestTransfer(transferRequest);
                processManager.saveTransferRequest(this.processId, transferRequest, this.tranferResponse, false);
                this.transfer = this.getTransferData(this.tranferResponse);
                if (this.transfer == null) {
                    return;
                }
                processManager.saveTransfer(this.processId, transfer, false);
                if (!transferSuccessStates.contains(transfer.getState())) {
                    throw new ServiceException(this.getClass().getName(), "Transfer Process Failed [" + this.tranferResponse.getId() + "]");
                }
            } catch (Exception e) {
                processManager.setStatus(processId, "transfer-failed", new History(
                        processId,
                        "FAILED"
                ));
                this.dataModel.setState(processId, "FAILED");
                throw new ServiceException(this.getClass().getName(), e, "Failed to do the contract transfer");
            }
            this.dataModel.setState(processId, "COMPLETED");
            LogUtil.printStatus("[PROCESS " + this.processId + "] Negotiation and Transfer Completed!");
        }

        /**
         * Gets the Negotiation data from the Negotiation Response.
         * <p>
         * @param   negotiationResponse
         *          the {@code IdResponse} object with negotiation response.
         *
         * @return  a {@code Negotiation} object with the negotiation data.
         *
         * @throws  ServiceException
         *           if unable to get negotiation data.
         */
        public Negotiation getNegotiationData(IdResponse negotiationResponse) {
            Negotiation negotiation = null;
            try {
                negotiation = seeNegotiation(negotiationResponse.getId(), this.processId, this.dataModel);
            } catch (Exception e) {
                throw new ServiceException(this.getClass().getName(), e, "Failed to get the negotiation [" + negotiationResponse.getId() + "]");
            }
            return negotiation;
        }

        /**
         * Starts the negotiation by requesting it.
         * <p>
         * @param   negotiationRequest
         *          the {@code NegotiationRequest} object with negotiation request data.
         *
         * @return  a {@code IdResponse} object with the negotiation response.
         *
         * @throws  ServiceException
         *           if unable to request the negotiation.
         */
        public IdResponse requestNegotiation(NegotiationRequest negotiationRequest) {
            IdResponse negotiationResponse = null;
            try {
                negotiationResponse = doContractNegotiation(negotiationRequest);
            } catch (Exception e) {
                throw new ServiceException(this.getClass().getName(), e, "Failed to start the negotiation for offer [" + negotiationRequest.getPolicy().getId() + "]");
            }

            if (negotiationResponse.getId() == null) {
                throw new ServiceException(this.getClass().getName(), "The ID from the Offer is null [" + negotiationRequest.getPolicy().getId() + "]");
            }
            LogUtil.printMessage("[PROCESS " + this.processId + "] Negotiation Requested [" + negotiationResponse.getId() + "]");
            return negotiationResponse;
        }

        /**
         * Starts the transfer by requesting it.
         * <p>
         * @param   transferRequest
         *          the {@code TransferRequest} object with transfer request data.
         *
         * @return  a {@code IdResponse} object with the transfer response.
         *
         * @throws  ServiceException
         *           if unable to request the transfer.
         */
        public IdResponse requestTransfer(TransferRequest transferRequest) {
            IdResponse transferResponse = null;
            try {
                transferResponse = initiateTransfer(transferRequest);
            } catch (Exception e) {
                throw new ServiceException(this.getClass().getName(), e, "Failed to start the transfer for contract  [" + transferRequest.getContractId() + "]");
            }
            if (transferResponse.getId() == null) {
                throw new ServiceException(this.getClass().getName(), "The ID from the transfer is null for contract  [" + transferRequest.getContractId() + "]");
            }
            LogUtil.printStatus("[PROCESS " + this.processId + "] Transfer Requested [" + transferResponse.getId() + "]");
            return transferResponse;
        }

        /**
         * Gets the Transfer data from the response.
         * <p>
         * @param   transferResponse
         *          the {@code IdResponse} object with transfer response data.
         *
         * @return  a {@code Transfer} object with the transfer data.
         *
         * @throws  ServiceException
         *           if unable to get the transfer data.
         */
        public Transfer getTransferData(IdResponse transferResponse) {
            /*[8]=========================================*/
            // Check for transfer updates and the status
            Transfer transfer = null;
            try {
                transfer = seeTransfer(transferResponse.getId(), this.processId, this.dataModel);
            } catch (Exception e) {
                throw new ServiceException(this.getClass().getName(), e, "Failed to get the transfer data [" + transferResponse.getId() + "]");
            }
            return transfer;
        }
    }

    public class DigitalTwinRegistryTransfer implements Runnable{

        /** ATTRIBUTES **/
        Dtr dtr;
        TransferRequest dtrRequest;
        String processId;
        String endpointId;
        Search search;
        Status status;
        Transfer transfer;
        IdResponse transferResponse;

        /** CONSTRUCTOR(S) **/
        public DigitalTwinRegistryTransfer(String processId, String endpointId, Status status, Search search, Dtr dtr) {
            this.dtr = dtr;
            this.endpointId = endpointId;
            this.processId = processId;
            this.status = status;
            this.search = search;
        }

        /** METHODS **/

        /**
         * This method is exclusively for the Digital Twin Registry (DTR) transfer.
         *
         * <p> It's a Thread level method from Runnable interface and builds the Transfer Request, requests the Transfer for the DTR, gets the Transfer data
         *  and saves in the Process.
         *
         * @throws  ServiceException
         *           if unable to do the contract transfer for the DTR.
         */
        @Override
        public void run() {
            try {
                this.dtrRequest = this.buildTransferRequest(this.processId,this.dtr, this.endpointId);
                processManager.saveTransferRequest(this.processId, dtrRequest, new IdResponse(processId, null), true);
                this.transferResponse = this.requestTransfer(dtrRequest);
                processManager.saveTransferRequest(this.processId, dtrRequest, this.transferResponse, true);
                this.transfer = this.getTransferData(this.transferResponse);
                if (this.transfer == null) {
                    return;
                }
                if(this.transfer.getState().equals("TERMINATED")){
                    processManager.setStatus(processId, "dtr-"+this.endpointId+"-transfer-incomplete", new History(
                            endpointId,
                            "INCOMPLETE"
                    ));
                }

                processManager.saveTransfer(this.processId, transfer, true);
            } catch (Exception e) {
                processManager.setStatus(processId, "dtr-transfer-failed", new History(
                        processId,
                        "FAILED"
                ));

                throw new ServiceException(this.getClass().getName(), e, "Failed to do the contract transfer for Digital Twin Registry");
            }
        }

        /**
         * Builds a transfer request for a given DTR.
         * <p>
         * @param   processId
         *          the {@code String} identification of the Process.
         * @param   dtr
         *          the {@code DTR} object data of the Digital Twin Registry.
         * @param   endpointId
         *          the {@code String} identification of the receiver endpoint.
         *
         * @return  a {@code TransferRequest} object with transfer request data.
         *
         * @throws  ServiceException
         *           if unable to build the DTR's transfer request.
         */
        public TransferRequest buildTransferRequest(String processId, Dtr dtr, String endpointId) {
            try {
                // Build transfer request to make the Digital Twin Query
                String receiverEndpoint = env.getProperty("configuration.edc.receiverEndpoint") + "/" + processId +  "/" + endpointId;

                TransferRequest.DataDestination dataDestination = new TransferRequest.DataDestination();
                dataDestination.setType("HttpProxy");
                List<CallbackAddress> callbackAddresses = List.of(CallbackAddress.builder().transactional(false).uri(receiverEndpoint).events(List.of("transfer.process.started")).build());
                return new TransferRequest(
                        jsonUtil.toJsonNode(Map.of("odrl", "http://www.w3.org/ns/odrl/2/","@vocab", "https://w3id.org/edc/v0.0.1/ns/")),
                        dtr.getAssetId(),
                        CatenaXUtil.buildDataEndpoint(dtr.getEndpoint()),
                        dtr.getContractId(),
                        dataDestination,
                        false,
                        "dataspace-protocol-http",
                        env.getProperty("configuration.edc.transferType"),
                        callbackAddresses
                );
            } catch (Exception e) {
                throw new ServiceException(this.getClass().getName(), e, "Failed to build the transfer request!");
            }
        }

        /**
         * Gets the Transfer response of a given DTR's Transfer request.
         * <p>
         * @param   transferRequest
         *          the {@code TransferRequest} object with transfer request data.
         *
         * @return  a {@code IdResponse} object with transfer response data.
         *
         * @throws  ServiceException
         *           if unable to transfer the DTR's transfer response.
         */
        public IdResponse requestTransfer(TransferRequest transferRequest) {
            IdResponse transferResponse = null;
            try {
                transferResponse = initiateTransfer(transferRequest);
            } catch (Exception e) {
                throw new ServiceException(this.getClass().getName(), e, "Failed to start the transfer for contract  [" + transferRequest.getContractId() + "]");
            }
            if (transferResponse.getId() == null) {
                throw new ServiceException(this.getClass().getName(), "The ID from the transfer is null for contract  [" + transferRequest.getContractId() + "]");
            }
            LogUtil.printStatus("[PROCESS " + this.processId + "] Transfer Requested [" + transferResponse.getId() + "] for Digital Twin");
            return transferResponse;
        }

        /**
         * Gets the Transfer data from the response.
         * <p>
         * @param   transferResponse
         *          the {@code IdResponse} object with transfer response data.
         *
         * @return  a {@code Transfer} object with the transfer data.
         *
         * @throws  ServiceException
         *           if unable to get the transfer data.
         */
        public Transfer getTransferData(IdResponse transferResponse) {
            /*[8]=========================================*/
            // Check for transfer updates and the status
            Transfer transfer = null;
            try {
                transfer = seeTransfer(transferResponse.getId());
            } catch (Exception e) {
                throw new ServiceException(this.getClass().getName(), e, "Failed to get the transfer data [" + transferResponse.getId() + "]");
            }
            return transfer;
        }

    }

}
