/*********************************************************************************
 *
 * Catena-X - Product Passport Consumer Backend
 *
 * Copyright (c) 2022, 2023 BASF SE, BMW AG, Henkel AG & Co. KGaA
 * Copyright (c) 2022, 2023 Contributors to the CatenaX (ng) GitHub Organisation.
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

package net.catenax.ce.productpass.services;

import net.catenax.ce.productpass.exceptions.ServiceException;
import net.catenax.ce.productpass.exceptions.ServiceInitializationException;
import net.catenax.ce.productpass.models.service.BaseService;
import net.catenax.ce.productpass.models.dtregistry.DigitalTwin;
import net.catenax.ce.productpass.models.auth.JwtToken;
import net.catenax.ce.productpass.models.dtregistry.SubModel;
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
public class AasService extends BaseService {
    public static final configTools configuration = new configTools();
    public final String registryUrl = (String) configuration.getConfigurationParam("variables.default.registryUrl", ".", null);
    @Autowired
    private AuthenticationService authService;

    public AasService() throws ServiceInitializationException {
        this.checkEmptyVariables();
    }

    @Override
    public List<String> getEmptyVariables() {
        List<String> missingVariables = new ArrayList<>();
        if (registryUrl == null || registryUrl.isEmpty()) {
            missingVariables.add("registryUrl");
        }
        return missingVariables;
    }
    public SubModel searchSubModelInDigitalTwin(String assetType,String assetId, Integer position){
        try {
            ArrayList<String> digitalTwinIds = this.queryDigitalTwin(assetType, assetId);
            if(digitalTwinIds==null || digitalTwinIds.size()==0){
                throw new ServiceException(this.getClass().getName() + "." + "searchSubModelInDigitalTwin",
                        "It was not possible to get digital twin for the selected asset type and the the selected assetId");
            }
            if(position > digitalTwinIds.size()){
                throw new ServiceException(this.getClass().getName() + "." + "searchSubModelInDigitalTwin",
                        "It was not possible to get digital twin in the selected position for the selected asset type and the the selected assetId");
            }

            String digitalTwinId = digitalTwinIds.get(position);
            DigitalTwin digitalTwin = this.getDigitalTwin(digitalTwinId);
            if(digitalTwin == null){
                throw new ServiceException(this.getClass().getName() + "." + "searchSubModelInDigitalTwin",
                        "It was not possible to get digital twin in the selected position for the selected asset type and the the selected assetId");
            }
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

    public SubModel searchSubModel(String assetType,String assetId, Integer position){
        try {
            ArrayList<String> digitalTwinIds = this.queryDigitalTwin(assetType, assetId);
            if(digitalTwinIds==null || digitalTwinIds.size()==0){
                throw new ServiceException(this.getClass().getName() + "." + "searchSubModel",
                        "It was not possible to get digital twin for the selected asset type and the the selected assetId");
            }
            if(position > digitalTwinIds.size()){
                throw new ServiceException(this.getClass().getName() + "." + "searchSubModel",
                        "It was not possible to get digital twin in the selected position for the selected asset type and the the selected assetId");
            }


            String digitalTwinId = digitalTwinIds.get(position);
            DigitalTwin digitalTwin = this.getDigitalTwin(digitalTwinId);
            if(digitalTwin == null){
                throw new ServiceException(this.getClass().getName() + "." + "searchSubModel",
                        "It was not possible to get submodel in the selected position for the selected asset type and the the selected assetId");
            }
            SubModel subModel = this.getSubModel(digitalTwin, position);
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


    public DigitalTwin getDigitalTwin(String digitalTwinId) {
            try {
                String path = "/registry/registry/shell-descriptors";
                String url = registryUrl + path + "/" + digitalTwinId;
                Map<String, Object> params = httpTools.getParams();
                JwtToken token = authService.getToken();
                HttpHeaders headers = httpTools.getHeadersWithToken(token.getAccessToken());
                ResponseEntity<?> response = httpTools.doGet(url, String.class, headers, params, true, false);
                String responseBody = (String) response.getBody();
                return (DigitalTwin) jsonTools.bindJsonNode(jsonTools.toJsonNode(responseBody), DigitalTwin.class);
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

    public SubModel getSubModel(DigitalTwin digitalTwin, Integer position) {
        try {
            String path = "/registry/registry/shell-descriptors";
            SubModel subModel = this.getSubModelFromDigitalTwin(digitalTwin, position);
            String url = registryUrl + path + "/" + digitalTwin.getIdentification() + "/submodel-descriptors/" + subModel.getIdentification();
            Map<String, Object> params = httpTools.getParams();
            JwtToken token = authService.getToken();
            HttpHeaders headers = httpTools.getHeadersWithToken(token.getAccessToken());
            ResponseEntity<?> response = httpTools.doGet(url, String.class, headers, params, true, false);
            String responseBody = (String) response.getBody();
            return (SubModel) jsonTools.bindJsonNode(jsonTools.toJsonNode(responseBody), SubModel.class);
        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName() + "." + "getSubModel",
                    e,
                    "It was not possible to get subModel!");
        }
    }
    public SubModel getSubModel(String digitalTwinId, String subModelId) {
        try {
            String path = "/registry/registry/shell-descriptors";
            String url = registryUrl + path + "/" + digitalTwinId + "/submodel-descriptors/" + subModelId;
            Map<String, Object> params = httpTools.getParams();
            JwtToken token = authService.getToken();
            HttpHeaders headers = httpTools.getHeadersWithToken(token.getAccessToken());
            ResponseEntity<?> response = httpTools.doGet(url, String.class, headers, params, true, false);
            String responseBody = (String) response.getBody();
            return (SubModel) jsonTools.bindJsonNode(jsonTools.toJsonNode(responseBody), SubModel.class);
        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName() + "." + "getSubModel",
                    e,
                    "It was not possible to get subModel!");
        }
    }
    public ArrayList<String> queryDigitalTwin(String assetType,String assetId) {
        try {
            String path = "/registry/lookup/shells";
            String url = registryUrl + path;
            Map<String, Object> params = httpTools.getParams();
            Map<String, ?> assetIds = Map.of(
                    "key", assetType,
                    "value", assetId
            );
            JwtToken token = authService.getToken();
            HttpHeaders headers = httpTools.getHeadersWithToken(token.getAccessToken());
            String jsonString = jsonTools.toJson(assetIds,false);
            params.put("assetIds", jsonString);
            ResponseEntity<?> response = httpTools.doGet(url, ArrayList.class, headers, params, true, false);
            ArrayList<String> responseBody = (ArrayList<String>) response.getBody();
            return responseBody;

        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName() + "." + "queryDigitalTwin",
                    e,
                    "It was not possible to retrieve digital twin ");
        }
    }

    public class DigitalTwinRegistryQuery implements Runnable{
        private SubModel subModel;
        private final String assetId;
        private final String idType;

        private final Integer index;

        public DigitalTwinRegistryQuery(String assetId,String idTyp, Integer index){
            this.assetId = assetId;
            this.idType = idTyp;
            this.index = index;
        }

        @Override
        public void run() {
            this.subModel = searchSubModel(this.idType, this.assetId, this.index);
        }

        public SubModel getSubModel() {
            return this.subModel;
        }

    }

}
