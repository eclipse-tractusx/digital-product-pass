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

package org.eclipse.tractusx.digitalproductpass.http.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.tractusx.digitalproductpass.config.DtrConfig;
import org.eclipse.tractusx.digitalproductpass.config.IrsConfig;
import org.eclipse.tractusx.digitalproductpass.config.PassportConfig;
import org.eclipse.tractusx.digitalproductpass.config.ProcessConfig;
import org.eclipse.tractusx.digitalproductpass.exceptions.ControllerException;
import org.eclipse.tractusx.digitalproductpass.managers.ProcessManager;
import org.eclipse.tractusx.digitalproductpass.managers.TreeManager;
import org.eclipse.tractusx.digitalproductpass.models.catenax.Dtr;
import org.eclipse.tractusx.digitalproductpass.models.dtregistry.DigitalTwin;
import org.eclipse.tractusx.digitalproductpass.models.dtregistry.EndPoint;
import org.eclipse.tractusx.digitalproductpass.models.dtregistry.SubModel;
import org.eclipse.tractusx.digitalproductpass.models.edc.EndpointDataReference;
import org.eclipse.tractusx.digitalproductpass.models.edc.Jwt;
import org.eclipse.tractusx.digitalproductpass.models.http.Response;
import org.eclipse.tractusx.digitalproductpass.models.http.requests.Search;
import org.eclipse.tractusx.digitalproductpass.models.manager.History;
import org.eclipse.tractusx.digitalproductpass.models.manager.Node;
import org.eclipse.tractusx.digitalproductpass.models.manager.SearchStatus;
import org.eclipse.tractusx.digitalproductpass.models.manager.Status;
import org.eclipse.tractusx.digitalproductpass.services.AasService;
import org.eclipse.tractusx.digitalproductpass.services.DataPlaneService;
import org.eclipse.tractusx.digitalproductpass.services.IrsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;
import utils.*;

import java.util.Map;
import java.util.Objects;

/**
 * This class consists exclusively to define the HTTP methods of the Application's controller.
 **/
@RestController
@Tag(name = "Public Controller")
public class AppController {

    /** ATTRIBUTES **/
    @SuppressWarnings("Unused")
    private @Autowired HttpServletRequest httpRequest;
    private @Autowired HttpServletResponse httpResponse;
    @Autowired
    HttpUtil httpUtil;
    @Autowired
    EdcUtil edcUtil;
    @SuppressWarnings("Unused")
    @Autowired
    JsonUtil jsonUtil;
    @SuppressWarnings("Unused")
    @Autowired
    Environment env;
    @SuppressWarnings("Unused")
    @Autowired
    PassportUtil passportUtil;
    @Autowired
    AasService aasService;

    @Autowired
    IrsService irsService;

    @Autowired
    TreeManager treeManager;
    @Autowired
    DataPlaneService dataPlaneService;
    @Autowired
    ProcessManager processManager;
    @Autowired
    IrsConfig irsConfig;
    @Autowired
    DtrConfig dtrConfig;

    @Autowired
    PassportConfig passportConfig;

    @SuppressWarnings("Unused")
    private @Autowired ProcessConfig processConfig;

    /** METHODS **/
    @GetMapping("/")
    @Hidden                     // hides this endpoint from api documentation - swagger-ui
    public Response index() {
        httpUtil.redirect(httpResponse, "/passport");
        return httpUtil.getResponse("Redirect to UI");
    }

    /**
     * Checks the backend health status.
     * <p>
     *
     * @return  a {@code Response} HTTP response with the status.
     *
     */
    @GetMapping("/health")
    @Operation(summary = "Returns the backend health status", responses = {
            @ApiResponse(description = "Gets the application health", responseCode = "200", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Response.class)))
    })
    public Response health() {
        Response response = httpUtil.getResponse(
                "RUNNING",
                200
        );
        response.data = DateTimeUtil.getDateTimeFormatted(null);
        return response;
    }

    /**
     * HTTP POST method to get the Digital Twin for the given processId and endpointId in the URL.
     * <p>
     * @param   processId
     *          the {@code String} id of the application's process.
     * @param   endpointId
     *          the {@code String} id of the endpoint.
     *
     * @return this {@code Response} HTTP response with the status.
     *
     */
    @RequestMapping(value = "/endpoint/{processId}/{endpointId}", method = RequestMethod.POST)
    @Operation(summary = "Receives the EDR for the EDC Consumer and queries for the dDTR")
    public Response getDigitalTwin(@RequestBody Object body, @PathVariable String processId, @PathVariable String endpointId) {
        try {
            EndpointDataReference endpointData = null;
            try {
                endpointData = this.getEndpointData(body);
            } catch (Exception e) {
                return httpUtil.buildResponse(httpUtil.getBadRequest(e.getMessage()), httpResponse);
            }
            if (endpointData == null) {
                return httpUtil.buildResponse(httpUtil.getBadRequest("Failed to get data plane endpoint data"), httpResponse);
            }

            if (!processManager.checkProcess(processId)) {
                return httpUtil.buildResponse(httpUtil.getNotFound("Process not found!"), httpResponse);
            }

            Status status = processManager.getStatus(processId);
            if(status == null){
                return httpUtil.buildResponse(httpUtil.getNotFound("No status is created"), httpResponse);
            }

            SearchStatus searchStatus = processManager.getSearchStatus(processId);
            Search search = searchStatus.getSearch();
            if(search == null){
                return httpUtil.buildResponse(httpUtil.getNotFound("No search performed"), httpResponse);
            }
            Dtr dtr = searchStatus.getDtr(endpointId);
            if(dtr == null){
                return httpUtil.buildResponse(httpUtil.getNotFound("No dtr available for this endpointId"), httpResponse);
            }
            // Start Digital Twin Query
            AasService.DecentralDigitalTwinRegistryQueryById digitalTwinRegistry = aasService.new DecentralDigitalTwinRegistryQueryById(
                    search,
                    endpointData
            );
            Long dtRequestTime = DateTimeUtil.getTimestamp();
            Thread digitalTwinRegistryThread = ThreadUtil.runThread(digitalTwinRegistry);
            // Wait for digital twin query
            digitalTwinRegistryThread.join();
            DigitalTwin digitalTwin = null;
            SubModel subModel = null;
            String connectorId = null;
            String assetId = null;
            String connectorAddress = null;
            String semanticId = null;
            String dataPlaneUrl = null;
            try {
                digitalTwin = digitalTwinRegistry.getDigitalTwin();
                subModel = digitalTwinRegistry.getSubModel();
                semanticId = Objects.requireNonNull(subModel.getSemanticId().getKeys().stream().filter(k -> k.getType().equalsIgnoreCase(this.dtrConfig.getSemanticIdTypeKey())).findFirst().orElse(null)).getValue();
                connectorId = subModel.getIdShort();
                EndPoint endpoint = subModel.getEndpoints().stream().filter(obj -> obj.getInterfaceName().equals(dtrConfig.getEndpointInterface())).findFirst().orElse(null);
                if (endpoint == null) {
                    throw new ControllerException(this.getClass().getName(), "No EDC endpoint found in DTR SubModel!");
                }
                Map<String, String> subProtocolBody = endpoint.getProtocolInformation().parseSubProtocolBody();
                connectorAddress = subProtocolBody.get(dtrConfig.getDspEndpointKey()); // Get DSP endpoint address
                assetId = subProtocolBody.get("id"); // Get Asset Id
                dataPlaneUrl = endpoint.getProtocolInformation().getEndpointAddress(); // Get the HREF endpoint
            } catch (Exception e) {
                return httpUtil.buildResponse(httpUtil.getNotFound("No endpoint address found"), httpResponse);
            }
            if (connectorId.isEmpty() || connectorAddress.isEmpty()) {
                return httpUtil.buildResponse(httpUtil.getNotFound("ConnectorId and Connector Address may be empty"), httpResponse);
            }

            try {
                connectorAddress = CatenaXUtil.buildEndpoint(connectorAddress);
            } catch (Exception e) {
                return null;
            }
            if (connectorAddress.isEmpty() || assetId.isEmpty()) {
                LogUtil.printError("Failed to parse endpoint [" + connectorAddress + "] or the assetId is not found!");
            }
            String bpn =  dtr.getBpn();
            Boolean childrenCondition = search.getChildren();
            processManager.saveDtr(processId, endpointId);
            processManager.saveTransferInfo(processId, connectorAddress, semanticId, dataPlaneUrl, bpn, childrenCondition);
            processManager.saveDigitalTwin(processId, digitalTwin, dtRequestTime);

            // IRS FUNCTIONALITY START
            if(this.irsConfig.getEnabled() && search.getChildren()) {
                // Update tree
                String globalAssetId = digitalTwin.getGlobalAssetId();
                String actualPath = status.getTreeState() + "/" + globalAssetId;
                processManager.setTreeState(processId, actualPath);
                this.treeManager.setNodeByPath(processId, actualPath, new Node(digitalTwin, this.passportConfig.getSearchIdSchema()));

                // Get children from the node
                this.irsService.getChildren(processId, actualPath, globalAssetId, bpn);
            }
            LogUtil.printDebug("[PROCESS " + processId + "] Digital Twin [" + digitalTwin.getIdentification() + "] and Submodel [" + subModel.getIdentification() + "] with EDC endpoint [" + connectorAddress + "] retrieved from DTR");
            processManager.setStatus(processId, "digital-twin-found", new History(
                    assetId,
                    "DT-READY"
            ));

        } catch (Exception e) {
            LogUtil.printException(e, "This request is not allowed! It must contain the valid attributes from an EDC endpoint");
            return httpUtil.buildResponse(httpUtil.getForbiddenResponse(), httpResponse);
        }
        return httpUtil.buildResponse(httpUtil.getResponse("ok"), httpResponse);
    }

    /**
     * Gets the {@code DataPlaneEndpoint} data from the given body of the HTTP request.
     * <p>
     * @param   body
     *          the {@code Object} body from the HTTP request.
     *
     * @return the {@code DataPlaneEndpoint} object.
     *
     * @throws ControllerException
     *           if the unable to get the data plane endpoint.
     *
     */
    public EndpointDataReference getEndpointData(Object body) throws ControllerException {
        EndpointDataReference endpointData = edcUtil.parseDataPlaneEndpoint(body);
        if (endpointData == null) {
            throw new ControllerException(this.getClass().getName(), "[EDR] The endpoint data request is empty!");
        }
        EndpointDataReference.Properties properties =  endpointData.getPayload().getDataAddress().getProperties();
        if (properties.getEndpoint().isEmpty()) {
            throw new ControllerException(this.getClass().getName(), "[EDR] The data plane endpoint address is empty!");
        }
        if (endpointData.getPayload().getDataAddress().getProperties().getEndpoint().isEmpty()) {
            throw new ControllerException(this.getClass().getName(), "[EDR] The authorization code is empty!");
        }
        if (endpointData.getPayload().getContractId().isEmpty()) {
            throw new ControllerException(this.getClass().getName(), "[EDR] The contractId is empty!");
        }
        return endpointData;
    }

    /**
     * HTTP POST method to get the Passport for the given processId.
     * <p>
     * @param   processId
     *          the {@code String} id of the application's process.
     *
     * @return this {@code Response} HTTP response with the status.
     *
     */
    @RequestMapping(value = "/endpoint/{processId}", method = RequestMethod.POST)
    @Operation(summary = "Receives the EDR from the EDC Consumer and get the passport json")
    public Response endpoint(@RequestBody Object body, @PathVariable String processId) {
        try {
            EndpointDataReference endpointData = null;
            try {
                endpointData = this.getEndpointData(body);
            } catch (Exception e) {
                return httpUtil.buildResponse(httpUtil.getBadRequest(e.getMessage()), httpResponse);
            }
            if (endpointData == null) {
                return httpUtil.buildResponse(httpUtil.getBadRequest("Failed to get data plane endpoint data"), httpResponse);
            }

            if (!processManager.checkProcess(processId)) {
                return httpUtil.buildResponse(httpUtil.getNotFound("Process not found!"), httpResponse);
            }

            Status status = processManager.getStatus(processId);
            if(status == null){
                return httpUtil.buildResponse(httpUtil.getNotFound("No status is created"), httpResponse);
            }

            JsonNode passport = dataPlaneService.getPassportFromEndpoint(endpointData, status.getDataPlaneUrl());
            if (passport == null) {
                return httpUtil.buildResponse(httpUtil.getNotFound("Passport not found in data plane!"), httpResponse);
            }
            String passportPath = processManager.savePassport(processId, endpointData, passport);

            LogUtil.printMessage("[EDC] Passport Transfer Data [" + endpointData.getId() + "] Saved Successfully in [" + passportPath + "]!");
        } catch (Exception e) {
            LogUtil.printException(e, "This request is not allowed! It must contain the valid attributes from an EDC endpoint");
            return httpUtil.buildResponse(httpUtil.getForbiddenResponse(), httpResponse);
        }
        return httpUtil.buildResponse(httpUtil.getResponse("ok"), httpResponse);
    }


}
