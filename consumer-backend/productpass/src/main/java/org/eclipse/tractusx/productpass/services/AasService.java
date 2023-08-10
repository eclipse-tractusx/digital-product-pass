/*********************************************************************************
 *
 * Catena-X - Product Passport Consumer Backend
 *
 * Copyright (c) 2022, 2023 BASF SE, BMW AG, Henkel AG & Co. KGaA
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

package org.eclipse.tractusx.productpass.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.juli.logging.Log;
import org.eclipse.tractusx.productpass.config.DtrConfig;
import org.eclipse.tractusx.productpass.exceptions.ControllerException;
import org.eclipse.tractusx.productpass.exceptions.ServiceException;
import org.eclipse.tractusx.productpass.exceptions.ServiceInitializationException;
import org.eclipse.tractusx.productpass.managers.DtrSearchManager;
import org.eclipse.tractusx.productpass.managers.ProcessDataModel;
import org.eclipse.tractusx.productpass.managers.ProcessManager;
import org.eclipse.tractusx.productpass.models.catenax.Dtr;
import org.eclipse.tractusx.productpass.models.dtregistry.*;
import org.eclipse.tractusx.productpass.models.edc.AssetSearch;
import org.eclipse.tractusx.productpass.models.edc.DataPlaneEndpoint;
import org.eclipse.tractusx.productpass.models.http.requests.Search;
import org.eclipse.tractusx.productpass.models.manager.SearchStatus;
import org.eclipse.tractusx.productpass.models.manager.Status;
import org.eclipse.tractusx.productpass.models.manager.Process;
import org.eclipse.tractusx.productpass.models.negotiation.Transfer;
import org.eclipse.tractusx.productpass.models.negotiation.TransferRequest;
import org.eclipse.tractusx.productpass.models.service.BaseService;
import org.eclipse.tractusx.productpass.models.auth.JwtToken;
import org.sonarsource.scanner.api.internal.shaded.minimaljson.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import utils.*;

import javax.xml.crypto.Data;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AasService extends BaseService {

    public String registryUrl;
    public Boolean central;

    private final HttpUtil httpUtil;

    private final JsonUtil jsonUtil;

    private final DtrConfig dtrConfig;
    private final AuthenticationService authService;

    Map<String, Object> apis;
    private DtrSearchManager dtrSearchManager;
    private ProcessManager processManager;

    private DataTransferService dataService;

    @Autowired
    public AasService(Environment env, HttpUtil httpUtil, JsonUtil jsonUtil, AuthenticationService authService, DtrConfig dtrConfig, DtrSearchManager dtrSearchManager, ProcessManager processManager, DataTransferService dataService) throws ServiceInitializationException {
        this.httpUtil = httpUtil;
        this.jsonUtil = jsonUtil;
        this.authService = authService;
        this.dtrConfig = dtrConfig;
        this.dtrSearchManager = dtrSearchManager;
        this.processManager = processManager;
        this.dataService = dataService;
        this.init(env);
        this.checkEmptyVariables();
    }

    public void init(Environment env) {
        this.registryUrl = dtrConfig.getCentralUrl();
        this.central = dtrConfig.getCentral();
        Object decentralApis = dtrConfig.getDecentralApis();
        if (decentralApis == null) { // If the configuration is null use the default variables
            decentralApis = Map.of(
                    "search", "/lookup/shells/query",
                    "digitalTwin", "/shell-descriptors",
                    "subModel", "/submodel-descriptors"
            );
        }
        this.apis = Map.of(
                "central", Map.of(
                        "search", "/lookup/shells",
                        "digitalTwin", "/registry/shell-descriptors",
                        "subModel", "/submodel-descriptors"
                ),
                "decentral", decentralApis
        );
    }

    @Override
    public List<String> getEmptyVariables() {
        List<String> missingVariables = new ArrayList<>();

        if (this.central == null) {
            missingVariables.add("dtr.central");
        } else {
            if (this.central) {
                if (this.registryUrl.isEmpty()) {
                    missingVariables.add("dtr.centralUrl");
                }
            }
        }
        return missingVariables;
    }

    public SubModel searchSubModelInDigitalTwinByIndex(DigitalTwin digitalTwin, Integer position) {
        try {
            SubModel subModel = this.getSubModelFromDigitalTwin(digitalTwin, position);
            if (subModel == null) {
                throw new ServiceException(this.getClass().getName() + "." + "searchSubModelInDigitalTwin",
                        "It was not possible to get digital twin in the selected position for the selected asset type and the the selected assetId");
            }
            return subModel;
        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName() + "." + "getIdShort",
                    e,
                    "It was not possible to get subModel!");
        }
    }
    public DigitalTwin3 searchDigitalTwin3(String assetType, String assetId, Integer position, String registryUrl, DataPlaneEndpoint edr) {
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
            DigitalTwin3 digitalTwin = this.getDigitalTwin3(digitalTwinId, registryUrl, edr);
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

    public DigitalTwin searchDigitalTwin(String assetType, String assetId, Integer position, String registryUrl, DataPlaneEndpoint edr) {
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


    public String getPathEndpoint(String registryUrl, String key) {
        if (this.central || registryUrl == null || registryUrl.isEmpty()) {
            return (String) jsonUtil.getValue(this.apis, "central." + key, ".", null);
        }
        String path = (String) jsonUtil.getValue(this.apis, "decentral." + key, ".", null);
        return path;
    }
    public SubModel3 searchSubModel3ById(DigitalTwin3 digitalTwin, String idShort) {
        try {
            SubModel3 subModel = this.getSubModel3ById(digitalTwin, idShort);
            if (subModel == null) {
                throw new ServiceException(this.getClass().getName() + "." + "searchSubModel3ById",
                        "It was not possible to get submodel in the selected position for the selected asset type and the the selected assetId");
            }
            return subModel;
        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName() + "." + "searchSubModel3ById",
                    e,
                    "It was not possible to search submodel!");
        }
    }
    public SubModel searchSubModelById(DigitalTwin digitalTwin, String idShort) {
        try {
            SubModel subModel = this.getSubModelById(digitalTwin, idShort);
            if (subModel == null) {
                throw new ServiceException(this.getClass().getName() + "." + "searchSubModelById",
                        "It was not possible to get submodel in the selected position for the selected asset type and the the selected assetId");
            }
            return subModel;
        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName() + "." + "searchSubModelById",
                    e,
                    "It was not possible to search submodel!");
        }
    }

    public SubModel searchSubModel(DigitalTwin digitalTwin, Integer position, String registryUrl, DataPlaneEndpoint edr) {
        try {
            SubModel subModel = this.getSubModel(digitalTwin, position, registryUrl, edr);
            if (subModel == null) {
                throw new ServiceException(this.getClass().getName() + "." + "searchSubModel",
                        "It was not possible to get submodel in the selected position for the selected asset type and the the selected assetId");
            }
            return subModel;
        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName() + "." + "searchSubModel",
                    e,
                    "It was not possible to search submodel!");
        }
    }

    public DigitalTwin3 getDigitalTwin3(String digitalTwinId, String registryUrl, DataPlaneEndpoint edr) {
        try {
            String path = this.getPathEndpoint(registryUrl, "digitalTwin");
            String url = this.getRegistryUrl(registryUrl) + path + "/" + CrypUtil.toBase64Url(digitalTwinId);
            Map<String, Object> params = httpUtil.getParams();
            HttpHeaders headers = this.getTokenHeader(edr);
            ResponseEntity<?> response = httpUtil.doGet(url, String.class, headers, params, true, false);
            String responseBody = (String) response.getBody();
            return (DigitalTwin3) jsonUtil.bindJsonNode(jsonUtil.toJsonNode(responseBody), DigitalTwin3.class);
        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName() + "." + "getDigitalTwin",
                    e,
                    "It was not possible to get digital twin!");
        }

    }
    public DigitalTwin getDigitalTwin(String digitalTwinId, String registryUrl, DataPlaneEndpoint edr) {
        try {
            String path = this.getPathEndpoint(registryUrl, "digitalTwin");
            String url = this.getRegistryUrl(registryUrl) + path + "/" + digitalTwinId;
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

    public SubModel getSubModelFromDigitalTwin(DigitalTwin digitalTwin, Integer position) {
        try {
            ArrayList<SubModel> subModels = digitalTwin.getSubmodelDescriptors();
            if (position > subModels.size()) {
                throw new ServiceException(this.getClass().getName() + "." + "getSubModelFromDigitalTwin",
                        "Position selected for subModel is out of range!");
            }
            return subModels.get(position);
        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName() + "." + "getSubModelFromDigitalTwin",
                    e,
                    "It was not possible to get subModel from digital twin!");
        }
    }

    public SubModel getSubModel(DigitalTwin digitalTwin, Integer position, String registryUrl, DataPlaneEndpoint edr) {
        try {
            String path = this.getPathEndpoint(registryUrl, "digitalTwin");
            SubModel subModel = this.getSubModelFromDigitalTwin(digitalTwin, position);
            String url = this.getRegistryUrl(registryUrl) + path + "/" + digitalTwin.getIdentification() + this.getPathEndpoint(registryUrl,"subModel") + "/" + subModel.getIdentification();
            Map<String, Object> params = httpUtil.getParams();
            HttpHeaders headers = this.getTokenHeader(edr);
            LogUtil.printMessage(jsonUtil.toJson(headers, true));
            ResponseEntity<?> response = httpUtil.doGet(url, String.class, headers, params, true, false);
            String responseBody = (String) response.getBody();
            return (SubModel) jsonUtil.bindJsonNode(jsonUtil.toJsonNode(responseBody), SubModel.class);
        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName() + "." + "getSubModel",
                    e,
                    "It was not possible to get subModel!");
        }
    }

    public SubModel3 getSubModel3ById(DigitalTwin3 digitalTwin, String idShort) {
        try {
            ArrayList<SubModel3> subModels = digitalTwin.getSubmodelDescriptors();
            if (subModels.size() < 1) {
                throw new ServiceException(this.getClass().getName() + "." + "getSubModel3ById",
                        "No subModel found in digitalTwin!");
            }
            // Search for first subModel with matching idShort, if it fails gives null
            SubModel3 subModel = subModels.stream().filter(s -> s.getIdShort().equalsIgnoreCase(idShort)).findFirst().orElse(null);

            if (subModel == null) {
                // If the subModel idShort does not exist
                throw new ServiceException(this.getClass().getName() + "." + "getSubModel3ById",
                        "SubModel for idShort not found!");
            }
            // Return subModel if found
            return subModel;
        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName() + "." + "getSubModel3ById",
                    e,
                    "It was not possible to get subModel!");
        }
    }

    public SubModel getSubModelById(DigitalTwin digitalTwin, String idShort) {
        try {
            ArrayList<SubModel> subModels = digitalTwin.getSubmodelDescriptors();
            if (subModels.size() < 1) {
                throw new ServiceException(this.getClass().getName() + "." + "getSubModelByIdShort",
                        "No subModel found in digitalTwin!");
            }
            // Search for first subModel with matching idShort, if it fails gives null
            SubModel subModel = subModels.stream().filter(s -> s.getIdShort().equalsIgnoreCase(idShort)).findFirst().orElse(null);

            if (subModel == null) {
                // If the subModel idShort does not exist
                throw new ServiceException(this.getClass().getName() + "." + "getSubModelByIdShort",
                        "SubModel for idShort not found!");
            }
            // Return subModel if found
            return subModel;
        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName() + "." + "getSubModelByIdShort",
                    e,
                    "It was not possible to get subModel!");
        }
    }

    public HttpHeaders getTokenHeader(DataPlaneEndpoint edr) {
        try {
            // If the dtr is central we just need the token
            if (this.central || edr == null) {
                // In case it fails we should throw get the token
                JwtToken token = authService.getToken();
                return this.httpUtil.getHeadersWithToken(token.getAccessToken());
            }

            // Get the normal headers based on the EDR
            HttpHeaders headers = this.httpUtil.getHeaders();
            headers.add(edr.getAuthKey(), ""+edr.getAuthCode());
            return headers;
        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName() + "." + "getTokenHeader",
                    "Failed to retrieve the token headers!");
        }
    }

    public String getRegistryUrl(String url) {
        try {
            return (url == null || this.central || url.isEmpty()) ? this.registryUrl : url;
        } catch (Exception e) {
            // Do nothing
        }
        return this.registryUrl;
    }


    public AssetSearch centralDtrSearch(String processId, Search searchBody){
        try {
            // Start Digital Twin Query
            AasService.DigitalTwinRegistryQueryById digitalTwinRegistry = this.new DigitalTwinRegistryQueryById(searchBody);
            Long dtRequestTime = DateTimeUtil.getTimestamp();
            Thread digitalTwinRegistryThread = ThreadUtil.runThread(digitalTwinRegistry);

            // Wait for digital twin query
            digitalTwinRegistryThread.join();
            DigitalTwin digitalTwin;
            SubModel subModel;
            String connectorId;
            String connectorAddress;
            try {
                digitalTwin = digitalTwinRegistry.getDigitalTwin();
                subModel = digitalTwinRegistry.getSubModel();
                connectorId = subModel.getIdShort();
                EndPoint endpoint = subModel.getEndpoints().stream().filter(obj -> obj.getInterfaceName().equals(dtrConfig.getEndpointInterface())).findFirst().orElse(null);
                if (endpoint == null) {
                    throw new ControllerException(this.getClass().getName(), "No EDC endpoint found in DTR SubModel!");
                }
                connectorAddress = endpoint.getProtocolInformation().getEndpointAddress();
            } catch (Exception e) {
                return null;
            }
            if (connectorId.isEmpty() || connectorAddress.isEmpty()) {
                return null;
            }


            try {
                connectorAddress = CatenaXUtil.buildEndpoint(connectorAddress);
            } catch (Exception e) {
                return null;
            }
            if (connectorAddress.isEmpty()) {
                return null;
            }
            processManager.saveDigitalTwin(processId, digitalTwin, dtRequestTime);
            LogUtil.printDebug("[PROCESS " + processId + "] Digital Twin [" + digitalTwin.getIdentification() + "] and Submodel [" + subModel.getIdentification() + "] with EDC endpoint [" + connectorAddress + "] retrieved from DTR");
            String assetId = String.join("-", digitalTwin.getIdentification(), subModel.getIdentification());
            return new AssetSearch(assetId, connectorAddress);
        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName() + "." + "centralDtrSearch",
                    e,
                    "It was not possible to search and find digital twin");
        }
    }
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
            // TODO: Wait until transfer is finished and retrieve digital twin ids
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
            if(status.historyExists("digital-twin-found")){
                return new AssetSearch(status.getHistory("digital-twin-found").getId(), status.getEndpoint());
            };
            return null;
        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName() + "." + "decentralDtrSearch",
                    e,
                    "It was not possible to search and find digital twin");
        }
    }

    public class DigitalTwinTimeout implements Runnable {
        private ProcessManager processManager;

        private String processId;

        public DigitalTwinTimeout(ProcessManager processManager, String processId) {
            this.processManager = processManager;
            this.processId = processId;
        }


        @Override
        public void run() {
            this.waitForDigitalTwin();
        }
        public void waitForDigitalTwin(){
            Status status = this.getStatus();
            while(!status.historyExists("digital-twin-found")){
                status = this.getStatus();
                if(status.getStatus().equals("FAILED")){
                    break;
                }
            }
        }
        public Status getStatus(){
            return this.processManager.getStatus(this.processId);
        }
    }



    public ArrayList<String> queryDigitalTwin(String assetType, String assetId, String registryUrl, DataPlaneEndpoint edr) {
        try {
            String path = this.getPathEndpoint(registryUrl, "search");
            String url = this.getRegistryUrl(registryUrl) + path;
            Map<String, Object> params = httpUtil.getParams();
            ResponseEntity<?> response = null;
            if (!this.central && registryUrl != null && edr != null) {
                // Set request body as post if the central query is disabled
                Object body = Map.of(
                        "query", Map.of(
                                "assetIds", List.of(
                                        Map.of(
                                                "name", assetType,
                                                "value", assetId
                                        )
                                )
                        )
                );
                HttpHeaders headers = this.getTokenHeader(edr);
                response = httpUtil.doPost(url, ArrayList.class, headers, httpUtil.getParams(), body, false, false);
            } else {
                // Query as GET if the central query is enabled
                Map<String, ?> assetIds = Map.of(
                        "key", assetType,
                        "value", assetId
                );

                String jsonString = jsonUtil.toJson(assetIds, false);
                HttpHeaders headers = httpUtil.getHeadersWithToken(this.authService.getToken().getAccessToken());;
                params.put("assetIds", jsonString);
                response = httpUtil.doGet(url, ArrayList.class, headers, params, true, false);
            }
            if(response == null){
                return null;
            }
            ArrayList<String> responseBody = (ArrayList<String>) response.getBody();
            return responseBody;

        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName() + "." + "queryDigitalTwin",
                    e,
                    "It was not possible to retrieve digital twin ");
        }
    }

    public class DecentralDigitalTwinRegistryQueryById implements Runnable {

        private DataPlaneEndpoint edr;
        private SubModel3 subModel;
        private DigitalTwin3 digitalTwin;

        private final String assetId;
        private final String idType;

        private final Integer dtIndex;


        private final String idShort;
        public DecentralDigitalTwinRegistryQueryById(Search search, DataPlaneEndpoint edr) {
            this.assetId = search.getId();
            this.idType = search.getIdType();
            this.dtIndex = search.getDtIndex();
            this.idShort = search.getIdShort();
            this.edr = edr;
        }

        @Override
        public void run() {
            this.setDigitalTwin(searchDigitalTwin3(this.getIdType(), this.getAssetId(), this.getDtIndex(),  this.getEdr().getEndpoint(), this.getEdr()));
            this.setSubModel(searchSubModel3ById(this.getDigitalTwin(), this.getIdShort()));
        }

        public DataPlaneEndpoint getEdr() {
            return edr;
        }

        public void setEdr(DataPlaneEndpoint edr) {
            this.edr = edr;
        }

        public SubModel3 getSubModel() {
            return subModel;
        }

        public void setSubModel(SubModel3 subModel) {
            this.subModel = subModel;
        }

        public DigitalTwin3 getDigitalTwin() {
            return digitalTwin;
        }

        public void setDigitalTwin(DigitalTwin3 digitalTwin) {
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

        public String getIdShort() {
            return idShort;
        }
    }





    public class DigitalTwinRegistryQueryById implements Runnable {
        private SubModel subModel;
        private DigitalTwin digitalTwin;

        private final String assetId;
        private final String idType;

        private final Integer dtIndex;


        private final String idShort;

        public DigitalTwinRegistryQueryById(Search search) {
            this.assetId = search.getId();
            this.idType = search.getIdType();
            this.dtIndex = search.getDtIndex();
            this.idShort = search.getIdShort();
        }

        @Override
        public void run() {
            this.digitalTwin = searchDigitalTwin(this.idType, this.assetId, this.dtIndex, null, null);
            this.subModel = searchSubModelById(this.digitalTwin, this.idShort);
        }

        public SubModel getSubModel() {
            return this.subModel;
        }

        public DigitalTwin getDigitalTwin() {
            return this.digitalTwin;
        }

        public void setSubModel(SubModel subModel) {
            this.subModel = subModel;
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

        public String getIdShort() {
            return idShort;
        }
    }

    public class DigitalTwinRegistryQuery implements Runnable {
        private SubModel subModel;
        private DigitalTwin digitalTwin;
        private final String assetId;
        private final String idType;

        private final Integer dtIndex;

        public DigitalTwinRegistryQuery(String assetId, String idType, Integer dtIndex) {
            this.assetId = assetId;
            this.idType = idType;
            this.dtIndex = dtIndex;

        }

        @Override
        public void run() {
            this.digitalTwin = searchDigitalTwin(this.idType, this.assetId, this.dtIndex, null, null);
            this.subModel = searchSubModel(this.digitalTwin, this.dtIndex, null, null);
        }

        public SubModel getSubModel() {
            return this.subModel;
        }

        public DigitalTwin getDigitalTwin() {
            return this.digitalTwin;
        }

    }

}
