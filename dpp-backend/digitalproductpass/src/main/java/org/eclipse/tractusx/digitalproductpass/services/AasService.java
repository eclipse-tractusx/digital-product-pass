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

import org.eclipse.tractusx.digitalproductpass.config.DtrConfig;
import org.eclipse.tractusx.digitalproductpass.config.PassportConfig;
import org.eclipse.tractusx.digitalproductpass.exceptions.ServiceException;
import org.eclipse.tractusx.digitalproductpass.exceptions.ServiceInitializationException;
import org.eclipse.tractusx.digitalproductpass.managers.DtrSearchManager;
import org.eclipse.tractusx.digitalproductpass.managers.ProcessManager;
import org.eclipse.tractusx.digitalproductpass.models.auth.JwtToken;
import org.eclipse.tractusx.digitalproductpass.models.catenax.Dtr;
import org.eclipse.tractusx.digitalproductpass.models.dtregistry.DigitalTwin;
import org.eclipse.tractusx.digitalproductpass.models.dtregistry.SubModel;
import org.eclipse.tractusx.digitalproductpass.models.edc.AssetSearch;
import org.eclipse.tractusx.digitalproductpass.models.edc.EndpointDataReference;
import org.eclipse.tractusx.digitalproductpass.models.http.requests.Search;
import org.eclipse.tractusx.digitalproductpass.models.manager.SearchStatus;
import org.eclipse.tractusx.digitalproductpass.models.manager.Status;
import org.eclipse.tractusx.digitalproductpass.models.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import utils.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class consists exclusively of methods to operate on getting Digital Twins and Submodels.
 *
 * <p> The methods defined here are intended to do every needed operations to be able to get Digital Twins and the Submodels.
 *
 */
@Service
public class AasService extends BaseService {
    /** CONSTANTS **/
    public static final String AUTHORIZATION_KEY = "Authorization";

    /** ATTRIBUTES **/
    public String registryUrl;

    public Boolean central;
    public String semanticIdTypeKey;
    private final HttpUtil httpUtil;
    private final JsonUtil jsonUtil;
    private final DtrConfig dtrConfig;

    public Environment env;
    private final AuthenticationService authService;
    Map<String, Object> apis;
    private DtrSearchManager dtrSearchManager;
    private final ProcessManager processManager;
    private DataTransferService dataService;
    private PassportConfig passportConfig;


    /** CONSTRUCTOR(S) **/
    @Autowired
    public AasService(Environment env, HttpUtil httpUtil, JsonUtil jsonUtil, AuthenticationService authService, DtrConfig dtrConfig, DtrSearchManager dtrSearchManager, ProcessManager processManager, DataTransferService dataService, PassportConfig passportConfig) throws ServiceInitializationException {
        this.httpUtil = httpUtil;
        this.jsonUtil = jsonUtil;
        this.authService = authService;
        this.dtrConfig = dtrConfig;
        this.dtrSearchManager = dtrSearchManager;
        this.processManager = processManager;
        this.dataService = dataService;
        this.init(env);
        this.checkEmptyVariables();
        this.passportConfig = passportConfig;
    }

    /** METHODS **/

    /**
     * Initiates the main needed variables for the AasService by loading from the application's configuration file.
     **/
    public void init(Environment env) {
        Object decentralApis = dtrConfig.getDecentralApis();
        if (decentralApis == null) { // If the configuration is null use the default variables
            decentralApis = Map.of(
                    "search", "/lookup/shells",
                    "digitalTwin", "/shell-descriptors",
                    "subModel", "/submodel-descriptors"
            );
        }
        this.semanticIdTypeKey = dtrConfig.getSemanticIdTypeKey();
        this.apis = Map.of(
                "decentral", decentralApis
        );
    }

    /**
     * Creates a List of missing variables needed to this service.
     * <p>
     *
     * @return an {@code Arraylist} with the environment variables missing in the service's configuration.
     *
     */
    @Override
    public List<String> getEmptyVariables() {
        return new ArrayList<>(); //Without central search there's no missing variables
    }

    /**
     * Searches a {@code DigitalTwin} from a given position index containing the specified parameters.
     * <p>
     * @param   assetType
     *          the {@code String} type of the asset.
     * @param   assetId
     *          the {@code String} identification of the asset.
     * @param   position
     *          the {@code Integer} position index of the intended digital twin.
     * @param   registryUrl
     *          the {@code String} registry URL of the Digital Twin.
     * @param   edr
     *          the {@code DataPlaneEndpoint} object with the data plane needed parameters.
     *
     * @return a {@code DigitalTwin} object from the position index, if exists.
     *
     * @throws  ServiceException
     *           if unable to find a {@code DigitalTwin} for the specified position index.
     */
    public DigitalTwin searchDigitalTwin(String assetType, String assetId, Integer position, String registryUrl, EndpointDataReference edr) {
        try {
            ArrayList<String> digitalTwinIds = this.queryDigitalTwin(assetType, assetId, registryUrl, edr);
            if (digitalTwinIds == null || digitalTwinIds.size() == 0) {
                throw new ServiceException(this.getClass().getName() + "." + "searchDigitalTwin",
                        "It was not possible to get digital twin for the selected asset type and the the selected assetId");
            }
            if (position > digitalTwinIds.size()) {
                throw new ServiceException(this.getClass().getName() + "." + "searchDigitalTwin",
                        "It was not possible to get digital twin in the selected position for the selected asset type and the the selected assetId");
            }

            String digitalTwinId = digitalTwinIds.get(position);
            DigitalTwin digitalTwin = this.getDigitalTwin(digitalTwinId, registryUrl, edr);
            if (digitalTwin == null) {
                throw new ServiceException(this.getClass().getName() + "." + "searchDigitalTwin",
                        "It was not possible to get digital twin in the selected position for the selected asset type and the the selected assetId");
            }
            return digitalTwin;
        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName() + "." + "searchDigitalTwin",
                    e,
                    "It was not possible to search digital twin!");
        }
    }

    /**
     * Builds the endpoint for a given registry URL and context key.
     * <p>
     * @param   key
     *          the {@code String} key with the intended URLs context (e.g: "digitaltwin", "search", "submodel", etc).
     *
     * @return a {@code String} object with the built endpoint.
     *
     */
    public String getPathEndpoint(String key) {
        String path = (String) jsonUtil.getValue(this.apis, "decentral." + key, ".", null);
        return path;
    }

    /**
     * Searches a {@code Submodel} from a Digital Twin by a given semantic identification.
     * <p>
     * @param   digitalTwin
     *          the {@code DigitalTwin} object with its submodels.
     * @param   semanticId
     *          the {@code String} semantic id of the intended submodel.
     *
     * @return a {@code Submodel} object with the submodel found, if exists.
     *
     * @throws  ServiceException
     *           if unable to find a the {@code Submodel} for the given semantic id.
     */
    public SubModel searchSubModelBySemanticId(DigitalTwin digitalTwin, String semanticId) {
        try {
            SubModel subModel = this.getSubModelBySemanticId(digitalTwin, semanticId);
            if (subModel == null) {
                throw new ServiceException(this.getClass().getName() + "." + "searchSubModelBySemanticId",
                        "It was not possible to get submodel in the selected position for the selected asset type and the the selected assetId");
            }
            return subModel;
        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName() + "." + "searchSubModelById",
                    e,
                    "It was not possible to search submodel!");
        }
    }
    /**
     * Searches a {@code Submodel} from a Digital Twin by the configured semanticIds
     * <p>
     * @param   digitalTwin
     *          the {@code DigitalTwin} object with its submodels.
     *
     * @return a {@code Submodel} object with the submodel found, if exists.
     *
     * @throws  ServiceException
     *           if unable to find a the {@code Submodel} for the given semantic id.
     */
    public SubModel searchSubModelBySemanticId(DigitalTwin digitalTwin) {
        try {
            SubModel subModel = this.getSubModelBySemanticId(digitalTwin);
            if (subModel == null) {
                throw new ServiceException(this.getClass().getName() + "." + "searchSubModelBySemanticId",
                        "It was not possible to get submodel in the selected position for the selected asset type and the the selected assetId");
            }
            return subModel;
        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName() + "." + "searchSubModelById",
                    e,
                    "It was not possible to search submodel!");
        }
    }

    /**
     * Gets the {@code DigitalTwin} of a given id.
     * <p>
     * @param   digitalTwinId
     *          the {@code String} identification of the digital twin.
     * @param   registryUrl
     *          the {@code String} registry URL of the Digital Twin.
     * @param   edr
     *          the {@code DataPlaneEndpoint} object with the data plane needed parameters.
     *
     * @return a {@code DigitalTwin} object with the given id, if exists.
     *
     * @throws  ServiceException
     *           if unable to find a {@code DigitalTwin} for the specified id.
     */
    public DigitalTwin getDigitalTwin(String digitalTwinId, String registryUrl, EndpointDataReference edr) {
        try {
            String path = this.getPathEndpoint("digitalTwin");
            String url = this.getRegistryUrl(registryUrl) + path + "/" + CrypUtil.toBase64Url(digitalTwinId);
            Map<String, Object> params = httpUtil.getParams();
            HttpHeaders headers = this.getTokenHeader(edr);
            ResponseEntity<?> response = httpUtil.doGet(url, String.class, headers, params, true, false);
            String responseBody = (String) response.getBody();
            return (DigitalTwin) jsonUtil.bindJsonNode(jsonUtil.toJsonNode(responseBody), DigitalTwin.class);
        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName() + "." + "getDigitalTwin",
                    e,
                    "It was not possible to get digital twin!");
        }

    }

    /**
     * Gets the {@code Submodel} from a Digital Twin by the configured semantic aspects
     * <p>
     * @param   digitalTwin
     *          the {@code DigitalTwin} object with its submodels.
     *
     * @return a {@code Submodel} object with the submodel found, if exists.
     *
     * @throws  ServiceException
     *           if unable to find a the {@code Submodel} for the given semantic id.
     */

    public SubModel getSubModelBySemanticId(DigitalTwin digitalTwin) {
        try {
            ArrayList<SubModel> subModels = digitalTwin.getSubmodelDescriptors();
            if (subModels.size() < 1) {
                throw new ServiceException(this.getClass().getName() + "." + "getSubModelBySemanticId",
                        "No subModel found in digitalTwin!");
            }
            SubModel subModel = null;
            // Search for first subModel with matching semanticId, if it fails gives null
            for (String semanticId: passportConfig.getAspects()) {
                subModel = subModels.stream().filter(s -> s.getSemanticId().getKeys().stream().anyMatch(
                        k -> (k.getType().equalsIgnoreCase(this.semanticIdTypeKey) && k.getValue().equalsIgnoreCase(semanticId))
                )).findFirst().orElse(null);
                if (subModel != null) {
                    return subModel; // Return subModel if found
                }
            }
            // If the subModel semanticId does not exist
            throw new ServiceException(this.getClass().getName() + "." + "getSubModelBySemanticId",
                    "SubModel for SemanticId not found!");
        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName() + "." + "getSubModelBySemanticId",
                    e,
                    "It was not possible to get subModel!");
        }
    }
    /**
     * Gets the {@code Submodel} from a Digital Twin by a given semantic identification.
     * <p>
     * @param   digitalTwin
     *          the {@code DigitalTwin} object with its submodels.
     * @param   aspectSemanticId
     *          the {@code String} semantic id of the intended submodel.
     *
     * @return a {@code Submodel} object with the submodel found, if exists.
     *
     * @throws  ServiceException
     *           if unable to find a the {@code Submodel} for the given semantic id.
     */
    public SubModel getSubModelBySemanticId(DigitalTwin digitalTwin, String aspectSemanticId) {
        try {
            ArrayList<SubModel> subModels = digitalTwin.getSubmodelDescriptors();
            if (subModels.size() < 1) {
                throw new ServiceException(this.getClass().getName() + "." + "getSubModelBySemanticId",
                        "No subModel found in digitalTwin!");
            }
            SubModel subModel = null;
            // Search for first subModel with matching semanticId, if it fails gives null
            subModel = subModels.stream().filter(
                    s -> s.getSemanticId().getKeys().stream().anyMatch(
                            k -> (k.getType().equalsIgnoreCase(this.semanticIdTypeKey) && k.getValue().equalsIgnoreCase(aspectSemanticId))
                    )).findFirst().orElse(null);
            if (subModel != null) {
                return subModel; // Return subModel if found
            }
            // If the subModel semanticId does not exist
            throw new ServiceException(this.getClass().getName() + "." + "getSubModelBySemanticId",
                    "SubModel for SemanticId not found!");
        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName() + "." + "getSubModelBySemanticId",
                    e,
                    "It was not possible to get subModel!");
        }
    }

    /**
     * Builds an HTTP header with the authentication key and code from the EDR's token.
     * <p>
     * @param   edr
     *          the {@code DataPlaneEndpoint} object with the data plane information.
     *
     * @return an {@code HTTPHeaders} object set with authentication key and code.
     *
     * @throws  ServiceException
     *           if unable to retrieve the token headers.
     */
    public HttpHeaders getTokenHeader(EndpointDataReference edr) {
        try {
            if (edr == null) {
                // In case it fails we should throw get the token
                JwtToken token = authService.getToken();
                return this.httpUtil.getHeadersWithToken(token.getAccessToken());
            }

            // Get the normal headers based on the EDR
            HttpHeaders headers = this.httpUtil.getHeaders();
            String authKey = AUTHORIZATION_KEY;
            if(env != null){
                authKey =  env.getProperty("configuration.edc.authorizationKey", AUTHORIZATION_KEY);
            }
            headers.add(authKey, edr.getPayload().getDataAddress().getProperties().getAuthorization());
            return headers;
        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName() + "." + "getTokenHeader",
                    "Failed to retrieve the token headers!");
        }
    }

    /**
     * Gets the registry URL.
     * <p>
     * @param   url
     *          the {@code String} URL for the registry.
     *
     * @return a {@code String} object with the registry URL.
     *
     */
    public String getRegistryUrl(String url) {
            return url;
    }

    /**
     * Searches for decentralized Digital Twin Registries.
     * <p>
     * @param   processId
     *          the {@code String} identification of the process.
     * @param   searchBody
     *          the {@code Search} object with the Request Body of the HTTP request.
     *
     * @return an {@code AssetSearch} object with the connector address and the asset id of the DTR.
     *
     * @throws  ServiceException
     *           if unable to search and/or find the DTR.
     */
    public AssetSearch decentralDtrSearch(String processId, Search searchBody){
        try {
            Status status = this.processManager.getStatus(processId);
            SearchStatus searchStatus = this.processManager.setSearch(processId, searchBody);
            for (String endpointId : searchStatus.getDtrs().keySet()) {
                Dtr dtr = searchStatus.getDtr(endpointId);
                DataTransferService.DigitalTwinRegistryTransfer dtrTransfer = dataService.new DigitalTwinRegistryTransfer(
                        processId,
                        endpointId,
                        status,
                        searchBody,
                        dtr
                );
                Thread thread =  ThreadUtil.runThread(dtrTransfer, dtr.getEndpoint());
                thread.join(Duration.ofSeconds(this.dtrConfig.getTimeouts().getTransfer()));
            }
            Thread blockThread = ThreadUtil.runThread(new DigitalTwinTimeout(this.processManager, processId));
            try {
                if(!blockThread.join(Duration.ofSeconds(this.dtrConfig.getTimeouts().getDigitalTwin()))){
                    LogUtil.printError("Timeout reached while waiting for receiving digital twin!");
                    return null;
                };
            } catch (InterruptedException e) {
                return null;
            }
            status = this.processManager.getStatus(processId);
            if (status.historyExists("digital-twin-found")) {
                return new AssetSearch(status.getHistory("digital-twin-found").getId(), status.getBpn(), status.getEndpoint());
            }
            return null;
        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName() + "." + "decentralDtrSearch",
                    e,
                    "It was not possible to search and find digital twin");
        }
    }

    /**
     * Gets the Digital Twin's ids list for a given asset type and id.
     * <p>
     * @param   assetType
     *          the {@code String} type of the asset.
     * @param   assetId
     *          the {@code String} identification of the asset.
     * @param   registryUrl
     *          the {@code String} registry URL of the Digital Twin.
     * @param   edr
     *          the {@code DataPlaneEndpoint} object with the data plane needed parameters.
     *
     * @return a {@code ArrayList<String>} object list with the digital twin ids found.
     *
     * @throws  ServiceException
     *           if unable to find any digital twin.
     */
    public ArrayList<String> queryDigitalTwin(String assetType, String assetId, String registryUrl, EndpointDataReference edr) {
        try {
            String path = this.getPathEndpoint("search");
            String url = this.getRegistryUrl(registryUrl) + path;
            Map<String, Object> params = httpUtil.getParams();
            ResponseEntity<?> response = null;
            if (registryUrl != null && edr != null) {
                // Set request body as post if the central query is disabled
                // Query as GET if the central query is enabled
                Map<String, ?> assetIds = Map.of(
                        "name", assetType,
                        "value", assetId
                );

                String jsonString = CrypUtil.toBase64(jsonUtil.toJson(assetIds, false));
                HttpHeaders headers = this.getTokenHeader(edr);
                headers.remove("Content-Type");         //  This should be fixed by the dtr team to allow content-type as application/json
                params.put("assetIds", jsonString);
                response = httpUtil.doGet(url, Map.class, headers, params, true, false);
                if(response == null){
                    return null;
                }
                Map<String,Object> responseBody = (Map<String,Object>) response.getBody();
                return (ArrayList<String>) responseBody.get("result");

            } else {
                // Query as GET if the central query is enabled
                Map<String, ?> assetIds = Map.of(
                        "name", assetType,
                        "value", assetId
                );

                String jsonString = CrypUtil.toBase64(jsonUtil.toJson(assetIds, false));
                HttpHeaders headers = httpUtil.getHeadersWithToken(this.authService.getToken().getAccessToken());
                params.put("assetIds", jsonString);
                response = httpUtil.doGet(url, ArrayList.class, headers, params, true, false);
                if(response == null){
                    return null;
                }
                ArrayList<String> responseBody = (ArrayList<String>) response.getBody();
                return responseBody;
            }
        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName() + "." + "queryDigitalTwin",
                    e,
                    "It was not possible to retrieve digital twin ");
        }
    }

    /** INNER CLASSES **/

    public class DigitalTwinTimeout implements Runnable {

        /** ATTRIBUTES **/
        private ProcessManager processManager;
        private String processId;

        /** CONSTRUCTOR(S) **/
        public DigitalTwinTimeout(ProcessManager processManager, String processId) {
            this.processManager = processManager;
            this.processId = processId;
        }

        /** METHODS **/

        /**
         * This method is exclusively for the waiting for the response of a Digital Twin request.
         *
         * <p> It's a Thread level method from Runnable interface used to wait for the response of a Digital Twin request by the timeout period defined
         * in the calling thread join method. This timeout is configured on the application's configuration variables.
         *
         */
        @Override
        public void run() {
            this.waitForDigitalTwin();
        }

        /**
         * Method used to wait for the digital twin for a timeout period defined in a thread join.
         **/
        public void waitForDigitalTwin(){
            Status status = this.getStatus();
            while(!status.historyExists("digital-twin-found")){
                status = this.getStatus();
                if (status.getStatus().equals("FAILED")) {
                    break;
                }
            }
        }

        /**
         * Method used to get the status information from the current executing process.
         *
         * @return a {@code Status} object with process's status information.
         *
         **/
        public Status getStatus(){
            try {
                return this.processManager.getStatus(this.processId);
            } catch (Exception e) {
                LogUtil.printWarning("["+this.getClass().getName()+".getStatus()] Status file for process ["+ this.processId + "] is not available!");
                return new Status(Map.of());
            }
        }
    }
    public class DecentralDigitalTwinRegistryQueryById implements Runnable {

        /** ATTRIBUTES **/
        private EndpointDataReference edr;
        private SubModel subModel;
        private DigitalTwin digitalTwin;
        private final String assetId;
        private final String idType;
        private final Integer dtIndex;
        private final String semanticId;

        /** CONSTRUCTOR(S) **/
        public DecentralDigitalTwinRegistryQueryById(Search search, EndpointDataReference edr) {
            this.assetId = search.getId();
            this.idType = search.getIdType();
            this.dtIndex = search.getDtIndex();
            this.edr = edr;
            this.semanticId = search.getSemanticId();
        }

        /** GETTERS AND SETTERS **/
        public EndpointDataReference getEdr() {
            return edr;
        }
        public void setEdr(EndpointDataReference edr) {
            this.edr = edr;
        }
        public SubModel getSubModel() {
            return subModel;
        }
        public void setSubModel(SubModel subModel) {
            this.subModel = subModel;
        }
        public DigitalTwin getDigitalTwin() {
            return digitalTwin;
        }
        public void setDigitalTwin(DigitalTwin digitalTwin) {
            this.digitalTwin = digitalTwin;
        }
        public String getAssetId() {
            return assetId;
        }
        public String getIdType() {
            return idType;
        }
        public Integer getDtIndex() {
            return dtIndex;
        }
        public String getSemanticId() { return semanticId; }

        /** METHODS **/
        /**
         * This method is exclusively to search for a digital twin and the submodel.
         *
         * <p> It's a Thread level method from Runnable interface and does the {@code DigitalTwin} and {@code Submodel} search, setting the results
         * to this class object.
         * <p> The submodel search it's done by semanticId parameter depending on if the semanticId is available or not, if not does the search by a semanticId default value.
         *
         */
        @Override
        public void run() {
            this.setDigitalTwin(searchDigitalTwin(this.getIdType(), this.getAssetId(), this.getDtIndex(),  this.getEdr().getPayload().getDataAddress().getProperties().getEndpoint(), this.getEdr()));
            if(this.semanticId == null || this.semanticId.isEmpty()){
                this.setSubModel(searchSubModelBySemanticId(this.getDigitalTwin()));
            }else {
                this.setSubModel(searchSubModelBySemanticId(this.getDigitalTwin(), semanticId));
            }
        }
    }
}
