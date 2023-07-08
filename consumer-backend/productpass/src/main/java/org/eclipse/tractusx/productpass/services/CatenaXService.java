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

package org.eclipse.tractusx.productpass.services;


import com.fasterxml.jackson.databind.JsonNode;
import org.apache.juli.logging.Log;
import org.eclipse.tractusx.productpass.config.DiscoveryConfig;
import org.eclipse.tractusx.productpass.exceptions.ServiceException;
import org.eclipse.tractusx.productpass.exceptions.ServiceInitializationException;
import org.eclipse.tractusx.productpass.models.catenax.BPNDiscovery;
import org.eclipse.tractusx.productpass.models.catenax.Discovery;
import org.eclipse.tractusx.productpass.models.http.requests.Search;
import org.eclipse.tractusx.productpass.models.negotiation.Catalog;
import org.eclipse.tractusx.productpass.models.negotiation.CatalogRequest;
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
import utils.*;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CatenaXService extends BaseService {

    private final HttpUtil httpUtil;

    private final JsonUtil jsonUtil;
    private final FileUtil fileUtil;
    private final VaultService vaultService;


    private final AuthenticationService authService;

    private final DiscoveryConfig discoveryConfig;
    private String discoveryEndpoint;

    private List<String> mandatoryDiscoveryKeys;
    @Override
    public void checkEmptyVariables() throws ServiceInitializationException {
        super.checkEmptyVariables();
    }

    @Override
    public void checkEmptyVariables(List<String> initializationOptionalVariables) throws ServiceInitializationException {
        super.checkEmptyVariables(initializationOptionalVariables);
    }

    @Override
    public List<String> getEmptyVariables() {
        List<String> missingVariables = new ArrayList<>();
        if (this.discoveryEndpoint.isEmpty()) {
            missingVariables.add("discovery.endpoint");
        }
        if (this.discoveryConfig.getBpn().getKey().isEmpty()) {
            missingVariables.add("discovery.edcKey");
        }
        if (this.discoveryConfig.getBpn().getKey().isEmpty()) {
            missingVariables.add("discovery.bpnKey");
        }
        return missingVariables;
    }

    public void init(Environment env){
        this.discoveryEndpoint = this.discoveryConfig.getEndpoint();
        this.mandatoryDiscoveryKeys = List.of(
                this.discoveryConfig.getBpn().getKey(), this.discoveryConfig.getEdc().getKey()
        );
    }
    @Autowired
    public CatenaXService(Environment env, FileUtil fileUtil, HttpUtil httpUtil, JsonUtil jsonUtil, VaultService vaultService, AuthenticationService authService, DiscoveryConfig discoveryConfig) throws ServiceInitializationException {
        this.httpUtil = httpUtil;
        this.fileUtil = fileUtil;
        this.jsonUtil = jsonUtil;
        this.vaultService = vaultService;
        this.authService = authService;
        this.discoveryConfig = discoveryConfig;
        this.init(env);
        this.checkEmptyVariables();
    }
    public Discovery getDiscoveryEndpoints() {
        return this.getDiscoveryEndpoints(this.mandatoryDiscoveryKeys);
    }

    public Discovery start(){
        try {
            Discovery discovery = this.getDiscoveryEndpoints();
            LogUtil.printMessage(jsonUtil.toJson(discovery, true));
            Boolean rs = this.updateDiscovery(discovery);
            if (!rs) {
                throw new ServiceException(this.getClass().getName(), "Something went wrong when updating the discovery endpoints");
            }
            LogUtil.printMessage("[Catena-X Service] Retrieved and Stored the EDC & BPN Discovery endpoints!");
            return discovery;
        }catch(Exception e){
            throw new ServiceException(this.getClass().getName() + "." + "start",
                    e,
                    "It was not possible to get the discovery endpoints");
        }
    }

    public Boolean updateDiscovery(Discovery discovery){
        try{
            if(discovery.getEndpoints().isEmpty()){
                return false;
            }
            List<Discovery.Endpoint> endpoints = discovery.getEndpoints();

            Discovery.Endpoint bpnEndpoint = endpoints.stream().filter(endpoint -> endpoint.getType().equals(this.discoveryConfig.getBpn().getKey())).findAny().orElse(null);
            if(bpnEndpoint == null){
                throw new ServiceException(this.getClass().getName() + "." + "updateDiscovery",
                        "The bpn endpoint ["+this.discoveryConfig.getBpn().getKey()+"] is not available in the discovery endpoint ["+this.discoveryEndpoint +"]");
            }
            Discovery.Endpoint edcEndpoint = endpoints.stream().filter(endpoint -> endpoint.getType().equals(this.discoveryConfig.getEdc().getKey())).findAny().orElse(null);
            if(edcEndpoint == null){
                throw new ServiceException(this.getClass().getName() + "." + "updateDiscovery",
                        "The bpn endpoint ["+this.discoveryConfig.getEdc().getKey()+"] is not available in the discovery endpoint ["+this.discoveryEndpoint +"]");
            }
            return this.updateDiscoveryFile(bpnEndpoint, edcEndpoint);
        }catch(Exception e){
            throw new ServiceException(this.getClass().getName() + "." + "updateDiscovery",
                    e,
                    "Failed to update the discovery endpoints");
        }
    }

    public Boolean updateDiscoveryFile(Discovery.Endpoint bpnEndpoint, Discovery.Endpoint edcEndpoint){
        try {
            String bpnAddress = bpnEndpoint.getEndpointAddress();
            String edcAddress = edcEndpoint.getEndpointAddress();
            if(bpnAddress.isEmpty()) {
                LogUtil.printError("The bpn endpoint address is not defined!");
                return false;
            }
            if(edcAddress.isEmpty()){
                LogUtil.printError("The edc endpoint address is not defined!");
                return false;
            }

            if(!this.vaultService.setLocalSecret("discovery.bpn", bpnAddress)){
                LogUtil.printError("Failed to create/update discovery bpn endpoint!");
                return false;
            };
            if(!this.vaultService.setLocalSecret("discovery.edc", edcAddress)){
                LogUtil.printError("Failed to create/update discovery edc endpoint!");
                return false;
            };
            return true;
        }catch (Exception e){
            throw new ServiceException(this.getClass().getName(), e, "It was not possible to create/update discovery endpoints");
        }
    }
    public Discovery getDiscoveryEndpoints(List<String> endpoints) {
        try {
            this.checkEmptyVariables();

            Object body = Map.of(
              "types", endpoints
            );

            HttpHeaders headers = httpUtil.getHeadersWithToken(this.authService.getToken().getAccessToken());
            headers.add("Content-Type", "application/json");

            ResponseEntity<?> response = httpUtil.doPost(this.discoveryEndpoint, JsonNode.class, headers, httpUtil.getParams(), body, false, false);
            JsonNode result = (JsonNode) response.getBody();
            return (Discovery) jsonUtil.bindJsonNode(result, Discovery.class);
        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName() + "." + "getContractOfferCatalog",
                    e,
                    "It was not possible to retrieve the discovery finder!");
        }
    }

    public BPNDiscovery getBpnDiscovery(String key){
        try {
            this.checkEmptyVariables();
            String bpnEndpoint = null;
            try {
                bpnEndpoint = (String) this.vaultService.getLocalSecret("discovery.bpn");
            }catch (Exception e) {
                throw new ServiceException(this.getClass().getName() + ".getBpnDiscovery", e, "It was not possible to retrieve the bpn discovery endpoint from the vault");
            }
            if(bpnEndpoint == null){
                throw new ServiceException(this.getClass().getName() + ".getBpnDiscovery", "The bpn discovery endpoint is empty!");
            }

            String searchEndpoint = bpnEndpoint + this.discoveryConfig.getBpn().getSearchPath();


            // Set request body
            Object body = Map.of(
                    "searchFilter",List.of(
                            Map.of(
                                "type", this.discoveryConfig.getEdc().getKey(), "keys", List.of(key)
                            )
                    )
            );

            HttpHeaders headers = httpUtil.getHeadersWithToken(this.authService.getToken().getAccessToken());
            headers.add("Content-Type", "application/json");

            ResponseEntity<?> response = httpUtil.doPost(searchEndpoint, JsonNode.class, headers, httpUtil.getParams(), body, false, false);
            JsonNode result = (JsonNode) response.getBody();
            return (BPNDiscovery) jsonUtil.bindJsonNode(result, BPNDiscovery.class);
        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName() + "." + "getContractOfferCatalog",
                    e,
                    "It was not possible to retrieve the discovery finder!");
        }
    }



}