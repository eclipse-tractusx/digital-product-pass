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
import org.eclipse.tractusx.productpass.exceptions.ControllerException;
import org.eclipse.tractusx.productpass.exceptions.ServiceException;
import org.eclipse.tractusx.productpass.exceptions.ServiceInitializationException;
import org.eclipse.tractusx.productpass.managers.DtrSearchManager;
import org.eclipse.tractusx.productpass.managers.ProcessManager;
import org.eclipse.tractusx.productpass.models.auth.JwtToken;
import org.eclipse.tractusx.productpass.models.catenax.Dtr;
import org.eclipse.tractusx.productpass.models.dtregistry.*;
import org.eclipse.tractusx.productpass.models.edc.AssetSearch;
import org.eclipse.tractusx.productpass.models.edc.DataPlaneEndpoint;
import org.eclipse.tractusx.productpass.models.http.requests.Search;
import org.eclipse.tractusx.productpass.models.manager.SearchStatus;
import org.eclipse.tractusx.productpass.models.manager.Status;
import org.eclipse.tractusx.productpass.models.service.BaseService;
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

    /** ATTRIBUTES **/
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

    /** CONSTRUCTOR(S) **/
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

    /** METHODS **/

    /**
     * Initiates the main needed variables for the AasService by loading from the application's configuration file.
     **/
    public void init(Environment env) {
        this.registryUrl = dtrConfig.getCentralUrl();
        this.central = dtrConfig.getCentral();
        Object decentralApis = dtrConfig.getDecentralApis();
        if (decentralApis == null) { // If the configuration is null use the default variables
            decentralApis = Map.of(
                    "search", "/lookup/shells",
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

    /**
     * Creates a List of missing variables needed to this service.
     * <p>
     *
     * @return an {@code Arraylist} with the environment variables missing in the service's configuration.
     *
     */
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

    /**
     * Searches a {@code Submodel} from a Digital Twin by a given position index.
     * <p>
     * @param   digitalTwin
     *          the {@code DigitalTwin} object with its submodels.
     * @param   position
     *          the {@code Integer} position index of the intended submodel.
     *
     * @return a {@code Submodel} object from the position index, if exists.
     *
     * @throws  ServiceException
     *           if unable to find a submodel for the specified position index.
     */
    @SuppressWarnings("Unused")
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

    /**
     * Searches a {@code DigitalTwin3} from a given position index containing the specified parameters.
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
     * @return a {@code DigitalTwin3} object from the position index, if exists.
     *
     * @throws  ServiceException
     *           if unable to find a {@code DigitalTwin3} for the specified position index.
     */
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

    /**
     * Builds the endpoint for a given registry URL and context key.
     * <p>
     * @param   registryUrl
     *          the {@code String} registry URL of the intended endpoint.
     * @param   key
     *          the {@code String} key with the intended URLs context (e.g: "digitaltwin", "search", "submodel", etc).
     *
     * @return a {@code String} object with the built endpoint.
     *
     */
    public String getPathEndpoint(String registryUrl, String key) {
        if (this.central || registryUrl == null || registryUrl.isEmpty()) {
            return (String) jsonUtil.getValue(this.apis, "central." + key, ".", null);
        }
        String path = (String) jsonUtil.getValue(this.apis, "decentral." + key, ".", null);
        return path;
    }

    /**
     * Searches a {@code Submodel3} from a Digital Twin by a given short identification.
     * <p>
     * @param   digitalTwin
     *          the {@code DigitalTwin3} object with its submodels.
     * @param   idShort
     *          the {@code String} short id of the intended submodel.
     *
     * @return a {@code Submodel3} object with the submodel found, if exists.
     *
     * @throws  ServiceException
     *           if unable to find a the {@code Submodel3} for the given id.
     */
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

    /**
     * Searches a {@code Submodel3} from a Digital Twin by a given semantic identification.
     * <p>
     * @param   digitalTwin
     *          the {@code DigitalTwin3} object with its submodels.
     * @param   semanticId
     *          the {@code String} semantic id of the intended submodel.
     *
     * @return a {@code Submodel3} object with the submodel found, if exists.
     *
     * @throws  ServiceException
     *           if unable to find a the {@code Submodel3} for the given semantic id.
     */
    public SubModel3 searchSubModel3BySemanticId(DigitalTwin3 digitalTwin, String semanticId) {
        try {
            SubModel3 subModel = this.getSubModel3BySemanticId(digitalTwin, semanticId);
            LogUtil.printWarning("SUBMODEL3:\n" + jsonUtil.toJson(subModel, true));
            if (subModel == null) {
                throw new ServiceException(this.getClass().getName() + "." + "searchSubModel3BySemanticId",
                        "It was not possible to get submodel in the selected position for the selected asset type and the the selected assetId");
            }
            return subModel;
        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName() + "." + "searchSubModel3ById",
                    e,
                    "It was not possible to search submodel!");
        }
    }

    /**
     * Searches a {@code Submodel} from a Digital Twin by a given short identification.
     * <p>
     * @param   digitalTwin
     *          the {@code DigitalTwin} object with its submodels.
     * @param   idShort
     *          the {@code String} short id of the intended submodel.
     *
     * @return a {@code Submodel} object with the submodel found, if exists.
     *
     * @throws  ServiceException
     *           if unable to find a the {@code Submodel} for the given id.
     */
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

    /**
     * Searches a {@code Submodel} from a Digital Twin from a given position index containing the specified parameters.
     * <p>
     * @param   digitalTwin
     *          the {@code DigitalTwin} object with its submodels.
     * @param   position
     *          the {@code Integer} position index of the intended submodel.
     * @param   registryUrl
     *          the {@code String} registry URL of the Digital Twin.
     * @param   edr
     *          the {@code DataPlaneEndpoint} object with the data plane needed parameters.
     *
     * @return a {@code Submodel} object from the position index, if exists.
     *
     * @throws  ServiceException
     *           if unable to find a {@code Submodel} for the specified position index.
     */
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

    /**
     * Gets the {@code DigitalTwin3} of a given id.
     * <p>
     * @param   digitalTwinId
     *          the {@code String} identification of the digital twin.
     * @param   registryUrl
     *          the {@code String} registry URL of the Digital Twin.
     * @param   edr
     *          the {@code DataPlaneEndpoint} object with the data plane needed parameters.
     *
     * @return a {@code DigitalTwin3} object with the given id, if exists.
     *
     * @throws  ServiceException
     *           if unable to find a {@code DigitalTwin3} for the specified id.
     */
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


    /**
     * Gets the {@code Submodel} from a Digital Twin from a given position index.
     * <p>
     * @param   digitalTwin
     *          the {@code DigitalTwin} object with its submodels.
     * @param   position
     *          the {@code Integer} position index of the intended submodel.
     *
     * @return a {@code Submodel} object from the position index, if exists.
     *
     * @throws  ServiceException
     *           if unable to find a {@code Submodel} for the specified position index.
     */
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

    /**
     * Gets the {@code Submodel} from a Digital Twin from a given position index containing the specified parameters.
     * <p>
     * @param   digitalTwin
     *          the {@code DigitalTwin} object with its submodels.
     * @param   position
     *          the {@code Integer} position index of the intended submodel.
     * @param   registryUrl
     *          the {@code String} registry URL of the Digital Twin.
     * @param   edr
     *          the {@code DataPlaneEndpoint} object with the data plane needed parameters.
     *
     * @return a {@code Submodel} object from the position index, if exists.
     *
     * @throws  ServiceException
     *           if unable to find a {@code Submodel} for the specified position index.
     */
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

    /**
     * Gets the {@code Submodel3} from a Digital Twin by a given short identification.
     * <p>
     * @param   digitalTwin
     *          the {@code DigitalTwin3} object with its submodels.
     * @param   idShort
     *          the {@code String} short id of the intended submodel.
     *
     * @return a {@code Submodel3} object with the submodel found, if exists.
     *
     * @throws  ServiceException
     *           if unable to find a the {@code Submodel3} for the given id.
     */
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

    /**
     * Gets the {@code Submodel3} from a Digital Twin by a given semantic identification.
     * <p>
     * @param   digitalTwin
     *          the {@code DigitalTwin3} object with its submodels.
     * @param   semanticId
     *          the {@code String} semantic id of the intended submodel.
     *
     * @return a {@code Submodel3} object with the submodel found, if exists.
     *
     * @throws  ServiceException
     *           if unable to find a the {@code Submodel3} for the given semantic id.
     */
    public SubModel3 getSubModel3BySemanticId(DigitalTwin3 digitalTwin, String semanticId) {
        try {
            ArrayList<SubModel3> subModels = digitalTwin.getSubmodelDescriptors();
            if (subModels.size() < 1) {
                throw new ServiceException(this.getClass().getName() + "." + "getSubModel3ById",
                        "No subModel found in digitalTwin!");
            }
            // Search for first subModel with matching idShort, if it fails gives null
            SubModel3 subModel = subModels.stream().filter(s -> s.getSemanticId().getKeys().get("Submodel").equalsIgnoreCase(semanticId)).findFirst().orElse(null);

            if (subModel == null) {
                // If the subModel idShort does not exist
                throw new ServiceException(this.getClass().getName() + "." + "getSubModel3ById",
                        "SubModel for SemanticId not found!");
            }
            // Return subModel if found
            return subModel;
        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName() + "." + "getSubModel3ById",
                    e,
                    "It was not possible to get subModel!");
        }
    }

    /**
     * Gets the {@code Submodel} from a Digital Twin by a given short identification.
     * <p>
     * @param   digitalTwin
     *          the {@code DigitalTwin} object with its submodels.
     * @param   idShort
     *          the {@code String} short id of the intended submodel.
     *
     * @return a {@code Submodel} object with the submodel found, if exists.
     *
     * @throws  ServiceException
     *           if unable to find a the {@code Submodel} for the given id.
     */
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
            return (url == null || this.central || url.isEmpty()) ? this.registryUrl : url;
    }

    /**
     * Searches for centralized Digital Twin Registries.
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
            LogUtil.printWarning("Decentral SearchStatus:\n" + jsonUtil.toJson(searchStatus, true));
            for (String endpointId : searchStatus.getDtrs().keySet()) {
                Dtr dtr = searchStatus.getDtr(endpointId);
                LogUtil.printWarning("EndpointId: " + endpointId);
                LogUtil.printWarning("Decentral DTR:\n" + jsonUtil.toJson(dtr, true));
                DataTransferService.DigitalTwinRegistryTransfer dtrTransfer = dataService.new DigitalTwinRegistryTransfer(
                        processId,
                        endpointId,
                        status,
                        searchBody,
                        dtr
                );
                LogUtil.printWarning("Decentral DTRTransfer:\n" + jsonUtil.toJson(dtrTransfer, true));
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
            LogUtil.printWarning("STATUS:\n" + jsonUtil.toJson(status, true));
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
    public ArrayList<String> queryDigitalTwin(String assetType, String assetId, String registryUrl, DataPlaneEndpoint edr) {
        try {
            String path = this.getPathEndpoint(registryUrl, "search");
            String url = this.getRegistryUrl(registryUrl) + path;
            Map<String, Object> params = httpUtil.getParams();
            ResponseEntity<?> response = null;
            if (!this.central && registryUrl != null && edr != null) {
                // Set request body as post if the central query is disabled
                // Query as GET if the central query is enabled
                Map<String, ?> assetIds = Map.of(
                        "name", assetType,
                        "value", assetId
                );

                String jsonString = jsonUtil.toJson(assetIds, false);
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

                String jsonString = jsonUtil.toJson(assetIds, false);
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
                if(status.getStatus().equals("FAILED")){
                    break;
                }
            }
        }

        public Status getStatus(){
            return this.processManager.getStatus(this.processId);
        }
    }

    public class DecentralDigitalTwinRegistryQueryById implements Runnable {

        /** ATTRIBUTES **/
        private DataPlaneEndpoint edr;
        private SubModel3 subModel;
        private DigitalTwin3 digitalTwin;
        private final String assetId;
        private final String idType;
        private final Integer dtIndex;
        private final String idShort;
        private final String semanticId;

        /** CONSTRUCTOR(S) **/
        public DecentralDigitalTwinRegistryQueryById(Search search, DataPlaneEndpoint edr) {
            this.assetId = search.getId();
            this.idType = search.getIdType();
            this.dtIndex = search.getDtIndex();
            this.idShort = search.getIdShort();
            this.edr = edr;
            this.semanticId = search.getSemanticId();
        }

        /** GETTERS AND SETTERS **/
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
        public String getSemanticId() { return semanticId; }

        /** METHODS **/

        /**
         * This method is exclusively to search for a digital twin and the submodel.
         *
         * <p> It's a Thread level method from Runnable interface and does the {@code DigitalTwin3} and {@code Submodel3} search, setting the results
         * to this class object.
         * <p> The submodel search it's done by the idShort or semanticId parameters depending on if it's a BatteryPass or DigitalProductPass search, respectively.
         *
         */
        @Override
        public void run() {
            this.setDigitalTwin(searchDigitalTwin3(this.getIdType(), this.getAssetId(), this.getDtIndex(),  this.getEdr().getEndpoint(), this.getEdr()));
            if (this.getIdShort().equalsIgnoreCase("digitalProductPass")) {
                this.setSubModel(searchSubModel3BySemanticId(this.getDigitalTwin(), this.getSemanticId()));
            } else {
                this.setSubModel(searchSubModel3ById(this.getDigitalTwin(), this.getIdShort()));
            }

        }
    }

    public class DigitalTwinRegistryQueryById implements Runnable {

        /** ATTRIBUTES **/
        private SubModel subModel;
        private DigitalTwin digitalTwin;
        private final String assetId;
        private final String idType;
        private final Integer dtIndex;
        private final String idShort;

        /** CONSTRUCTOR(S) **/
        public DigitalTwinRegistryQueryById(Search search) {
            this.assetId = search.getId();
            this.idType = search.getIdType();
            this.dtIndex = search.getDtIndex();
            this.idShort = search.getIdShort();
        }

        /** GETTERS AND SETTERS **/
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

        /** METHODS **/

        /**
         * This method is exclusively to search for a digital twin and the submodel.
         *
         * <p> It's a Thread level method from Runnable interface and does the {@code DigitalTwin} and {@code Submodel} search, setting the results
         * to this class object.
         * <p> The submodel search it's done by the idShort.
         *
         */
        @Override
        public void run() {
            this.digitalTwin = searchDigitalTwin(this.idType, this.assetId, this.dtIndex, null, null);
            this.subModel = searchSubModelById(this.digitalTwin, this.idShort);
        }

    }

    @SuppressWarnings("Unused")
    public class DigitalTwinRegistryQuery implements Runnable {

        /** ATTRIBUTES **/
        private SubModel subModel;
        private DigitalTwin digitalTwin;
        private final String assetId;
        private final String idType;
        private final Integer dtIndex;

        /** CONSTRUCTOR(S) **/
        @SuppressWarnings("Unused")
        public DigitalTwinRegistryQuery(String assetId, String idType, Integer dtIndex) {
            this.assetId = assetId;
            this.idType = idType;
            this.dtIndex = dtIndex;
        }

        /** GETTERS AND SETTERS **/
        @SuppressWarnings("Unused")
        public SubModel getSubModel() {
            return this.subModel;
        }
        @SuppressWarnings("Unused")
        public DigitalTwin getDigitalTwin() {
            return this.digitalTwin;
        }

        /** METHODS **/

        /**
         * This method is exclusively to search for a digital twin and the submodel.
         *
         * <p> It's a Thread level method from Runnable interface and does the {@code DigitalTwin} and {@code Submodel} search, setting the results
         * to this class object.
         * <p> The submodel search it's done by index position.
         *
         */
        @Override
        public void run() {
            this.digitalTwin = searchDigitalTwin(this.idType, this.assetId, this.dtIndex, null, null);
            this.subModel = searchSubModel(this.digitalTwin, this.dtIndex, null, null);
        }

    }

}
