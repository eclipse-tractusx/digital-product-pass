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
import org.eclipse.tractusx.productpass.config.DtrConfig;
import org.eclipse.tractusx.productpass.exceptions.ServiceException;
import org.eclipse.tractusx.productpass.exceptions.ServiceInitializationException;
import org.eclipse.tractusx.productpass.managers.DtrSearchManager;
import org.eclipse.tractusx.productpass.models.catenax.BpnDiscovery;
import org.eclipse.tractusx.productpass.models.catenax.Discovery;
import org.eclipse.tractusx.productpass.models.catenax.Dtr;
import org.eclipse.tractusx.productpass.models.catenax.EdcDiscoveryEndpoint;
import org.eclipse.tractusx.productpass.models.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import utils.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

@Service
public class CatenaXService extends BaseService {

    private final HttpUtil httpUtil;

    private final JsonUtil jsonUtil;
    private final FileUtil fileUtil;
    private final VaultService vaultService;
    private final DtrSearchManager dtrSearchManager;
    private final DataTransferService dataTransferService;

    private final AuthenticationService authService;

    private final DiscoveryConfig discoveryConfig;

    private DtrConfig dtrConfig;
    private String discoveryEndpoint;

    private List<String> defaultDiscoveryKeys;
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
        this.defaultDiscoveryKeys = List.of(
                this.discoveryConfig.getBpn().getKey(), this.discoveryConfig.getEdc().getKey()
        );
    }
    @Autowired
    public CatenaXService(Environment env, FileUtil fileUtil, HttpUtil httpUtil, JsonUtil jsonUtil, VaultService vaultService, DtrSearchManager dtrSearchManager, AuthenticationService authService, DiscoveryConfig discoveryConfig, DataTransferService dataTransferService, DtrConfig dtrConfig) throws ServiceInitializationException {
        this.httpUtil = httpUtil;
        this.fileUtil = fileUtil;
        this.jsonUtil = jsonUtil;
        this.dtrConfig = dtrConfig;
        this.vaultService = vaultService;
        this.dtrSearchManager = dtrSearchManager;
        this.authService = authService;
        this.discoveryConfig = discoveryConfig;
        this.dataTransferService = dataTransferService;
        this.init(env);
        this.checkEmptyVariables();
    }
    public Discovery getDiscoveryEndpoints() {
        return this.getDiscoveryEndpoints(this.defaultDiscoveryKeys); // Get default discovery endpoints
    }

    public Discovery start(){
        try {
            Discovery discovery = this.getDiscoveryEndpoints();
            Boolean rs = this.updateDefaultDiscovery(discovery);
            if (!rs) {
                LogUtil.printError("["+this.getClass().getName() + "." + "start] Something went wrong when updating the discovery endpoints");
            }
            LogUtil.printMessage("[Catena-X Service] Retrieved and Stored the EDC & BPN Discovery endpoints!");
            return discovery;
        }catch(Exception e){
            LogUtil.printError("["+this.getClass().getName() + "." + "start] It was not possible to get the discovery endpoints");
        }
        return null;
    }
    public Discovery addEndpoint(String key){
        try {
            Discovery discovery = this.getDiscoveryEndpoint(key);
            Boolean rs = this.updateDefaultDiscovery(discovery);
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

    public List<Discovery.Endpoint> getDiscoveryEndpoints(Discovery discovery, String key) {
        List<Discovery.Endpoint> endpoints = discovery.getEndpoints();
        List<Discovery.Endpoint> filteredEndpoints = endpoints.stream().filter(endpoint -> endpoint.getType().equals(key)).toList();
        if(filteredEndpoints.isEmpty()){
            throw new ServiceException(this.getClass().getName() + "." + "updateDiscovery",
                    "The endpoint ["+key+"] is not available in the discovery endpoint ["+this.discoveryEndpoint +"]");
        }
        return filteredEndpoints;
    }


    public Discovery.Endpoint getDiscoveryEndpoint(Discovery discovery, String key) {
        List<Discovery.Endpoint> endpoints = discovery.getEndpoints();
        Discovery.Endpoint responseEndpoint = endpoints.stream().filter(endpoint -> endpoint.getType().equals(key)).findAny().orElse(null);
        if(responseEndpoint == null){
            throw new ServiceException(this.getClass().getName() + "." + "updateDiscovery",
                    "The endpoint ["+key+"] is not available in the discovery endpoint ["+this.discoveryEndpoint +"]");
        }
        return responseEndpoint;
    }

    public Boolean updateDiscovery(Discovery discovery, String key){
        try{
            if(discovery.getEndpoints().isEmpty()){
                return false;
            }
            // Get discovery endpoints by key
            Discovery.Endpoint endpoint = this.getDiscoveryEndpoint(discovery, key);
            return this.updateEndpointFile(endpoint, key); // Create default discovery file
        }catch(Exception e){
            throw new ServiceException(this.getClass().getName() + "." + "updateDiscovery",
                    e,
                    "Failed to update the discovery endpoints");
        }
    }

    public Boolean updateDefaultDiscovery(Discovery discovery){
        try{
            if(discovery.getEndpoints().isEmpty()){
                return false;
            }
            // Get discovery endpoints by key
            List<Discovery.Endpoint> bpnEndpoints = this.getDiscoveryEndpoints(discovery, this.discoveryConfig.getBpn().getKey());
            List<Discovery.Endpoint> edcEndpoints = this.getDiscoveryEndpoints(discovery, this.discoveryConfig.getEdc().getKey());
            return this.updateDefaultDiscoveryFile(bpnEndpoints, edcEndpoints); // Create default discovery file
        }catch(Exception e){
            throw new ServiceException(this.getClass().getName() + "." + "updateDiscovery",
                    e,
                    "Failed to update the discovery endpoints");
        }
    }
    public Boolean updateEndpointFile(List<Discovery.Endpoint> endpoints, String key){
        try {
            boolean returnState;
            for(Discovery.Endpoint endpoint: endpoints) {
                returnState = this.updateEndpointFile(endpoint, key);
                if(!returnState){
                    return false;
                }
            }
            return true;
        }catch (Exception e){
            throw new ServiceException(this.getClass().getName(), e, "It was not possible to create/update discovery endpoints for key [" + key + "]");
        }
    }
    public Boolean updateEndpointFile(Discovery.Endpoint endpoint, String key){
        try {
            String address = endpoint.getEndpointAddress();
            if(address.isEmpty()) {
                LogUtil.printError("The endpoint for key ["+key+"] address is not defined!");
                return false;
            }
            List<String> endpoints = new ArrayList<String>();
            if(this.vaultService.secretExists("discovery."+key)){
                endpoints = (List<String>) this.vaultService.getLocalSecret("discovery."+key);
            }
            if(!endpoints.contains(address)){
                endpoints.add(address);
                if(!this.vaultService.setLocalSecret("discovery."+key, endpoints)){
                    LogUtil.printError("Failed to create/update discovery key ["+key+"] endpoint!");
                    return false;
                };
            }
            return true;
        }catch (Exception e){
            throw new ServiceException(this.getClass().getName(), e, "It was not possible to create/update discovery endpoints for key [" + key + "]");
        }
    }
    public Boolean updateDefaultDiscoveryFile(List<Discovery.Endpoint> bpnEndpoints, List<Discovery.Endpoint> edcEndpoints){
        try {
            Boolean bpnResponse = this.updateEndpointFile(bpnEndpoints, this.discoveryConfig.getBpn().getKey());
            Boolean edcResponse = this.updateEndpointFile(edcEndpoints, "edc");
            if(!bpnResponse){
                LogUtil.printError("Something went wrong when getting the bpn endpoint");
                return false;
            }
            if(!edcResponse){
                LogUtil.printError("Something went wrong when getting the edc endpoint");
                return false;
            }
            return true;
        }catch (Exception e){
            throw new ServiceException(this.getClass().getName(), e, "It was not possible to create/update main discovery endpoints");
        }
    }
    public Boolean updateDefaultDiscoveryFile(Discovery.Endpoint bpnEndpoint, Discovery.Endpoint edcEndpoint){
        try {
            Boolean bpnResponse = this.updateEndpointFile(bpnEndpoint, this.discoveryConfig.getBpn().getKey());
            Boolean edcResponse = this.updateEndpointFile(edcEndpoint, "edc");
            if(!bpnResponse){
                LogUtil.printError("Something went wrong when getting the bpn endpoint");
                return false;
            }
            if(!edcResponse){
                LogUtil.printError("Something went wrong when getting the edc endpoint");
                return false;
            }
            return true;
        }catch (Exception e){
            throw new ServiceException(this.getClass().getName(), e, "It was not possible to create/update main discovery endpoints");
        }
    }

    public Discovery getDiscoveryEndpoint(String key) {
        try {
            return this.getDiscoveryEndpoints(List.of(key));
        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName() + "." + "getDiscoveryEndpoint",
                    e,
                    "It was not possible to retrieve the discovery finder!");
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
            throw new ServiceException(this.getClass().getName() + "." + "getDiscoveryEndpoints",
                    e,
                    "It was not possible to retrieve the discovery finder!");
        }
    }

    public List<EdcDiscoveryEndpoint> getEdcDiscovery(List<String> bpns) {
        try {
            this.checkEmptyVariables();
            List<String> edcEndpoints = null;
            // Check if the variable edc endpoint is correct
            try {
                edcEndpoints = (List<String>) this.vaultService.getLocalSecret("discovery.edc");
            } catch (Exception e) {
                throw new ServiceException(this.getClass().getName() + ".getEdcDiscovery", e, "It was not possible to retrieve the edc discovery endpoint from the vault");
            }
            if (edcEndpoints == null) {
                throw new ServiceException(this.getClass().getName() + ".getEdcDiscovery", "The edc discovery endpoint is empty!");
            }
            List<EdcDiscoveryEndpoint> edcDiscoveryResponses = new ArrayList<>();
            for(String edcEndpoint : edcEndpoints) {

                // Add the technical token
                HttpHeaders headers = httpUtil.getHeadersWithToken(this.authService.getToken().getAccessToken());
                headers.add("Content-Type", "application/json");
                try{
                    ResponseEntity<?> response = httpUtil.doPost(edcEndpoint, JsonNode.class, headers, httpUtil.getParams(), bpns, false, false);
                    JsonNode result = (JsonNode) response.getBody();
                    List<EdcDiscoveryEndpoint> edcDiscoveryResponse = (List<EdcDiscoveryEndpoint>) jsonUtil.bindJsonNode(result, List.class);
                    if(edcDiscoveryResponse.isEmpty()) {
                        LogUtil.printWarning("List of EDCs not found in EDC Discovery Url: ["+edcEndpoint+"]");
                        continue;
                    }
                    edcDiscoveryResponses.addAll(edcDiscoveryResponse);
                }catch (Exception e){
                    LogUtil.printException(e, "It was not possible to get the edc endpoints from the EDC Discovery Service endpoint: ["+edcEndpoint+"]");
                }

            }
            if(edcDiscoveryResponses.isEmpty()){
                return null;
            }
            return edcDiscoveryResponses;
        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName() + "." + "getEdcDiscovery",
                    e,
                    "It was not possible to get the edc endpoints from the EDC Discovery Service");
        }
    }

    public List<BpnDiscovery> getBpnDiscovery(String id, String type){
        try {
            this.checkEmptyVariables();
            // Check if the discovery type id exists in the endpoint from the edc discovery
            if(!this.vaultService.secretExists("discovery."+type)){
                Discovery discovery = this.addEndpoint(type);
                if(discovery == null){
                    throw new ServiceException(this.getClass().getName() + ".getBpnDiscovery", "The discovery finder service failed");
                }
            }
            List<String> bpnEndpoints = new ArrayList<>();
            try {
                bpnEndpoints = (List<String>) this.vaultService.getLocalSecret("discovery."+type);
            }catch (Exception e) {
                throw new ServiceException(this.getClass().getName() + ".getBpnDiscovery", e, "It was not possible to retrieve the bpn discovery endpoint from the vault");
            }
            if(bpnEndpoints == null){
                throw new ServiceException(this.getClass().getName() + ".getBpnDiscovery", "The bpn discovery endpoint is empty!");
            }
            List<BpnDiscovery> bpnDiscoveryResponse = new ArrayList<>();
            for(String bpnEndpoint : bpnEndpoints) {

                String searchEndpoint = bpnEndpoint + this.discoveryConfig.getBpn().getSearchPath();


                // Set request body
                Object body = Map.of(
                        "searchFilter", List.of(
                                Map.of(
                                        "type", type, "keys", List.of(id)
                                )
                        )
                );

                HttpHeaders headers = httpUtil.getHeadersWithToken(this.authService.getToken().getAccessToken());
                headers.add("Content-Type", "application/json");
                try{
                    ResponseEntity<?> response = httpUtil.doPost(searchEndpoint, JsonNode.class, headers, httpUtil.getParams(), body, false, false);
                    JsonNode result = (JsonNode) response.getBody();
                    BpnDiscovery bpnDiscovery = (BpnDiscovery) jsonUtil.bindJsonNode(result, BpnDiscovery.class);
                    if(bpnDiscovery == null){
                        LogUtil.printWarning("Response from search endpoint: [" + searchEndpoint + "]");
                        continue;
                    }
                    bpnDiscoveryResponse.add(bpnDiscovery);
                }catch (Exception e){
                    LogUtil.printException(e, "BPN Number not found for ["+searchEndpoint+"] and keyType ["+type+"]!");
                }
            }
            if(bpnDiscoveryResponse.isEmpty()){
                return null;
            }
            return bpnDiscoveryResponse;
        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName() + "." + "getBpnDiscovery",
                    e,
                    "It was not possible to retrieve the bpn at the BPN discovery service");
        }
    }

    public void searchDTRs (List<EdcDiscoveryEndpoint> edcEndpoints, String processId) {
        try {
            Thread thread = ThreadUtil.runThread(dtrSearchManager.startProcess(edcEndpoints, processId), "ProcessDtrDataModel");
            thread.join();
        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName() + "." + "searchDtrs",
                    e,
                    "It was not possible to search the DTRs.");
        }
    }

}
