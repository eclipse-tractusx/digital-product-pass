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

import org.eclipse.tractusx.productpass.config.DtrConfig;
import org.eclipse.tractusx.productpass.exceptions.ServiceException;
import org.eclipse.tractusx.productpass.exceptions.ServiceInitializationException;
import org.eclipse.tractusx.productpass.models.edc.DataPlaneEndpoint;
import org.eclipse.tractusx.productpass.models.http.requests.Search;
import org.eclipse.tractusx.productpass.models.service.BaseService;
import org.eclipse.tractusx.productpass.models.dtregistry.DigitalTwin;
import org.eclipse.tractusx.productpass.models.auth.JwtToken;
import org.eclipse.tractusx.productpass.models.dtregistry.SubModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import utils.HttpUtil;
import utils.JsonUtil;
import utils.LogUtil;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AasService extends BaseService {

    public String registryUrl;
    public Boolean central;

    private final HttpUtil httpUtil;

    private final JsonUtil jsonUtil;

    private final DtrConfig dtrConfig;
    private final AuthenticationService authService;

    @Autowired
    public AasService(Environment env, HttpUtil httpUtil, JsonUtil jsonUtil, AuthenticationService authService, DtrConfig dtrConfig) throws ServiceInitializationException {
        this.httpUtil = httpUtil;
        this.jsonUtil = jsonUtil;
        this.authService = authService;
        this.dtrConfig = dtrConfig;
        this.init(env);
        this.checkEmptyVariables();
    }

    public void init(Environment env){
        this.registryUrl = dtrConfig.getCentralUrl();
        this.central = dtrConfig.getCentral();
    }
    @Override
    public List<String> getEmptyVariables() {
        List<String> missingVariables = new ArrayList<>();

        if (this.central == null) {
            missingVariables.add("dtr.central");
        }else{
            if(this.central) {
                if (this.registryUrl.isEmpty()) {
                    missingVariables.add("dtr.centralUrl");
                }
            }
        }
        return missingVariables;
    }
    public SubModel searchSubModelInDigitalTwinByIndex(DigitalTwin digitalTwin, Integer position){
        try {
            SubModel subModel = this.getSubModelFromDigitalTwin(digitalTwin, position);
            if(subModel == null){
                throw new ServiceException(this.getClass().getName() + "." + "searchSubModelInDigitalTwin",
                        "It was not possible to get digital twin in the selected position for the selected asset type and the the selected assetId");
            }
            return subModel;
        }
        catch (Exception e) {
            throw new ServiceException(this.getClass().getName() + "." + "getIdShort",
                    e,
                    "It was not possible to get subModel!");
        }
    }

    public DigitalTwin searchDigitalTwin(String assetType, String assetId, Integer position, String registryUrl, DataPlaneEndpoint edr){
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
        }catch (Exception e){
            throw new ServiceException(this.getClass().getName() + "." + "searchDigitalTwin",
                    e,
                    "It was not possible to search digital twin!");
        }
    }
    public SubModel searchSubModelById(DigitalTwin digitalTwin, String idShort){
        try {
            SubModel subModel = this.getSubModelById(digitalTwin, idShort);
            if(subModel == null){
                throw new ServiceException(this.getClass().getName() + "." + "searchSubModelById",
                        "It was not possible to get submodel in the selected position for the selected asset type and the the selected assetId");
            }
            return subModel;
        }
        catch (Exception e) {
            throw new ServiceException(this.getClass().getName() + "." + "searchSubModelById",
                    e,
                    "It was not possible to search submodel!");
        }
    }
    public SubModel searchSubModel(DigitalTwin digitalTwin, Integer position, String registryUrl, DataPlaneEndpoint edr){
        try {
            SubModel subModel = this.getSubModel(digitalTwin, position, registryUrl, edr);
            if(subModel == null){
                throw new ServiceException(this.getClass().getName() + "." + "searchSubModel",
                        "It was not possible to get submodel in the selected position for the selected asset type and the the selected assetId");
            }
            return subModel;
        }
        catch (Exception e) {
                throw new ServiceException(this.getClass().getName() + "." + "searchSubModel",
                        e,
                        "It was not possible to search submodel!");
            }
    }


    public DigitalTwin getDigitalTwin(String digitalTwinId, String registryUrl, DataPlaneEndpoint edr) {
            try {
                String path = "/registry/registry/shell-descriptors";
                String url =this.getRegistryUrl(registryUrl) + path + "/" + digitalTwinId;
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
        }
        catch (Exception e) {
                throw new ServiceException(this.getClass().getName() + "." + "getSubModelFromDigitalTwin",
                        e,
                        "It was not possible to get subModel from digital twin!");
            }
    }

    public SubModel getSubModel(DigitalTwin digitalTwin, Integer position, String registryUrl, DataPlaneEndpoint edr) {
        try {
            String path = "/registry/registry/shell-descriptors";
            SubModel subModel = this.getSubModelFromDigitalTwin(digitalTwin, position);
            String url = this.getRegistryUrl(registryUrl) + path + "/" + digitalTwin.getIdentification() + "/submodel-descriptors/" + subModel.getIdentification();
            Map<String, Object> params = httpUtil.getParams();
            HttpHeaders headers = this.getTokenHeader(edr);
            ResponseEntity<?> response = httpUtil.doGet(url, String.class, headers, params, true, false);
            String responseBody = (String) response.getBody();
            return (SubModel) jsonUtil.bindJsonNode(jsonUtil.toJsonNode(responseBody), SubModel.class);
        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName() + "." + "getSubModel",
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

            if(subModel == null){
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
    public SubModel getSubModel(String digitalTwinId, String subModelId,  String registryUrl, DataPlaneEndpoint edr) {
        try {
            String path = "/registry/registry/shell-descriptors";
            String url = this.getRegistryUrl(registryUrl)  + path + "/" + digitalTwinId + "/submodel-descriptors/" + subModelId;
            Map<String, Object> params = httpUtil.getParams();
            HttpHeaders headers = this.getTokenHeader(edr);
            ResponseEntity<?> response = httpUtil.doGet(url, String.class, headers, params, true, false);
            String responseBody = (String) response.getBody();
            return (SubModel) jsonUtil.bindJsonNode(jsonUtil.toJsonNode(responseBody), SubModel.class);
        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName() + "." + "getSubModel",
                    e,
                    "It was not possible to get subModel!");
        }
    }

    public HttpHeaders getTokenHeader(DataPlaneEndpoint edr){
        try {
            // If the dtr is central we just need the token
            if(this.central || edr == null) {
                // In case it fails we should throw get the token
                JwtToken token = authService.getToken();
                return this.httpUtil.getHeadersWithToken(token.getAccessToken());
            }

            // Get the normal headers based on the EDR
            HttpHeaders headers = this.httpUtil.getHeaders();
            headers.add(edr.getAuthKey(), edr.getAuthKey());
            return headers;
        }catch (Exception e){
            throw new ServiceException(this.getClass().getName() + "." + "getTokenHeader",
                    "Failed to retrieve the token headers!");
        }
    }

    public String getRegistryUrl(String registerUrl){
        try {
            return (!this.central && !registerUrl.isEmpty())?registerUrl:this.registryUrl;
        }catch (Exception e){
            // Do nothing
        }
        return this.registryUrl;
    }

    public ArrayList<String> queryDigitalTwin(String assetType, String assetId, String registryUrl, DataPlaneEndpoint edr) {
        try {
            String path = "/registry/lookup/shells";
            String url = this.getRegistryUrl(registryUrl) + path;
            Map<String, Object> params = httpUtil.getParams();
            Map<String, ?> assetIds = Map.of(
                    "key", assetType,
                    "value", assetId
            );
            HttpHeaders headers = this.getTokenHeader(edr);
            String jsonString = jsonUtil.toJson(assetIds,false);
            params.put("assetIds", jsonString);
            ResponseEntity<?> response = httpUtil.doGet(url, ArrayList.class, headers, params, true, false);
            ArrayList<String> responseBody = (ArrayList<String>) response.getBody();
            return responseBody;

        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName() + "." + "queryDigitalTwin",
                    e,
                    "It was not possible to retrieve digital twin ");
        }
    }

    public class DecentralDigitalTwinRegistryQueryById extends DigitalTwinRegistryQueryById{

        private String registryUrl;
        private DataPlaneEndpoint edr;
        public DecentralDigitalTwinRegistryQueryById(Search search, String registryUrl, DataPlaneEndpoint edr){
            super(search);
            this.registryUrl = registryUrl;
            this.edr = edr;
        }

        @Override
        public void run() {
            this.setDigitalTwin(searchDigitalTwin(this.getIdType(), this.getAssetId(), this.getDtIndex(), this.getRegistryUrl(), this.getEdr()));
            this.setSubModel(searchSubModelById(this.getDigitalTwin(), this.getIdShort()));
        }
        public String getRegistryUrl() {
            return registryUrl;
        }

        public void setRegistryUrl(String registryUrl) {
            this.registryUrl = registryUrl;
        }

        public DataPlaneEndpoint getEdr() {
            return edr;
        }

        public void setEdr(DataPlaneEndpoint edr) {
            this.edr = edr;
        }
    }


    public class DigitalTwinRegistryQueryById implements Runnable{
        private SubModel subModel;
        private DigitalTwin digitalTwin;

        private final String assetId;
        private final String idType;

        private final Integer dtIndex;


        private final String idShort;
        public DigitalTwinRegistryQueryById(Search search){
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

    public class DigitalTwinRegistryQuery implements Runnable{
        private SubModel subModel;
        private DigitalTwin digitalTwin;
        private final String assetId;
        private final String idType;

        private final Integer dtIndex;

        public DigitalTwinRegistryQuery(String assetId, String idType, Integer dtIndex){
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
