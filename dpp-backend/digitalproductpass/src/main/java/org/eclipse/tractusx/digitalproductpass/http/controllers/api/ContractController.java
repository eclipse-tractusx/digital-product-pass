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

package org.eclipse.tractusx.digitalproductpass.http.controllers.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.eclipse.tractusx.digitalproductpass.config.DiscoveryConfig;
import org.eclipse.tractusx.digitalproductpass.config.DtrConfig;
import org.eclipse.tractusx.digitalproductpass.config.PassportConfig;
import org.eclipse.tractusx.digitalproductpass.config.ProcessConfig;
import org.eclipse.tractusx.digitalproductpass.managers.DtrSearchManager;
import org.eclipse.tractusx.digitalproductpass.managers.ProcessManager;
import org.eclipse.tractusx.digitalproductpass.models.catenax.BpnDiscovery;
import org.eclipse.tractusx.digitalproductpass.models.catenax.Dtr;
import org.eclipse.tractusx.digitalproductpass.models.edc.AssetSearch;
import org.eclipse.tractusx.digitalproductpass.models.http.Response;
import org.eclipse.tractusx.digitalproductpass.models.http.requests.DiscoverySearch;
import org.eclipse.tractusx.digitalproductpass.models.http.requests.Search;
import org.eclipse.tractusx.digitalproductpass.models.http.requests.TokenRequest;
import org.eclipse.tractusx.digitalproductpass.models.manager.History;
import org.eclipse.tractusx.digitalproductpass.models.manager.Process;
import org.eclipse.tractusx.digitalproductpass.models.manager.SearchStatus;
import org.eclipse.tractusx.digitalproductpass.models.manager.Status;
import org.eclipse.tractusx.digitalproductpass.models.negotiation.catalog.Catalog;
import org.eclipse.tractusx.digitalproductpass.models.negotiation.catalog.Dataset;
import org.eclipse.tractusx.digitalproductpass.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;
import utils.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class consists exclusively to define the HTTP methods needed for the Contract negotiation.
 **/
@RestController
@RequestMapping("/api/contract")
@Tag(name = "Contract Controller")
@SecurityRequirement(name = "BearerAuthentication")
public class ContractController {

    /** ATTRIBUTES **/
    private @Autowired HttpServletRequest httpRequest;
    private @Autowired HttpServletResponse httpResponse;
    private @Autowired DataTransferService dataService;
    private @Autowired ContractService contractService;
    private @Autowired VaultService vaultService;
    private @Autowired AasService aasService;
    private @Autowired AuthenticationService authService;
    private @Autowired PassportConfig passportConfig;
    private @Autowired DiscoveryConfig discoveryConfig;
    private @Autowired DtrConfig dtrConfig;

    private @Autowired EdcUtil edcUtil;
    private @Autowired Environment env;
    @Autowired
    ProcessManager processManager;
    @Autowired
    DtrSearchManager dtrSearchManager;
    private @Autowired ProcessConfig processConfig;
    @Autowired
    CatenaXService catenaXService;
    @Autowired
    HttpUtil httpUtil;
    @Autowired
    PolicyUtil policyUtil;
    private @Autowired JsonUtil jsonUtil;

    /** METHODS **/

    /**
     * HTTP POST method to create a new Process.
     * <p>
     * @param   searchBody
     *          the {@code DiscoverySearch} body from the HTTP request with the partInstanceId.
     *
     * @return this {@code Response} HTTP response with the processId of the new Process created.
     *
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @Operation(summary = "Creates a process and checks for the viability of the data retrieval")
    public Response create(@Valid @RequestBody DiscoverySearch searchBody) {
        Response response = httpUtil.getInternalError();
        try {
            if (!authService.isAuthenticated(httpRequest)) {
                response = httpUtil.getNotAuthorizedResponse();
                return httpUtil.buildResponse(response, httpResponse);
            }

            return contractService.createCall(httpResponse, searchBody);
        } catch (Exception e) {
            assert response != null;
            response.message = "It was not possible to create the process and search for the decentral digital twin registries";
            LogUtil.printException(e, response.message);
            return httpUtil.buildResponse(response, httpResponse);
        }
    }

    /**
     * HTTP POST method to search the passport of an asset.
     * <p>
     * @param   searchBody
     *          the {@code DiscoverySearch} body from the HTTP request with the partInstanceId, passport version and the processId.
     *
     * @return this {@code Response} HTTP response with the status.
     *
     */
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    @Operation(summary = "Searches for a passport with the following id", responses = {
            @ApiResponse(description = "Default Response Structure", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Response.class))),
            @ApiResponse(description = "Content of Data Field in Response", responseCode = "200", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Dataset.class)))
    })
    public Response search(@Valid @RequestBody Search searchBody) {
        Response response = httpUtil.getInternalError();
        if (!authService.isAuthenticated(httpRequest)) {
            response = httpUtil.getNotAuthorizedResponse();
            return httpUtil.buildResponse(response, httpResponse);
        }
        try {
           return contractService.searchCall(httpRequest, httpResponse, searchBody);  
        } catch (Exception e) {
            assert response != null;
            response.message = "It was not possible to search for the serialized id";
            LogUtil.printException(e, response.message);
            return httpUtil.buildResponse(response, httpResponse);
        }
    }

    /**
     * HTTP GET method to get the Process status for the given processId.
     * <p>
     * @param   processId
     *          the {@code String} id of the application's process.
     *
     * @return this {@code Response} HTTP response with the status.
     *
     */
    @RequestMapping(value = "/status/{processId}", method = RequestMethod.GET)
    @Operation(summary = "Get status from process")
    public Response status(@PathVariable String processId) {
        Response response = httpUtil.getInternalError();
        // Check for authentication
        if (!authService.isAuthenticated(httpRequest)) {
            response = httpUtil.getNotAuthorizedResponse();
            return httpUtil.buildResponse(response, httpResponse);
        }
        try {
            // Check for processId
            if (!processManager.checkProcess(httpRequest, processId)) {
                response = httpUtil.getBadRequest("The process id does not exists!");
                return httpUtil.buildResponse(response, httpResponse);
            }
            return contractService.statusCall(httpResponse, processId);
        } catch (Exception e) {
            response.message = e.getMessage();
            return httpUtil.buildResponse(response, httpResponse);
        }
    }

    /**
     * HTTP POST method to cancel a Process.
     * <p>
     * @param   tokenRequestBody
     *          the {@code TokenRequest} object with the processId, contractId and the authentication token.
     *
     * @return this {@code Response} HTTP response with the status.
     *
     */
    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
    @Operation(summary = "Cancel the negotiation")
    public Response cancel(@Valid @RequestBody TokenRequest tokenRequestBody) {
        Response response = httpUtil.getInternalError();

        // Check for authentication
        if (!authService.isAuthenticated(httpRequest)) {
            response = httpUtil.getNotAuthorizedResponse();
            return httpUtil.buildResponse(response, httpResponse);
        }
        try {
            return contractService.cancelCall(httpRequest, httpResponse, tokenRequestBody);
        } catch (Exception e) {
            response.message = e.getMessage();
            return httpUtil.buildResponse(response, httpResponse);
        }
    }


    /**
     * HTTP POST method to sign a Contract retrieved from provider and start the negotiation.
     * <p>
     *
     * @param tokenRequestBody the {@code TokenRequest} object with the processId, contractId, policyId the authentication token.
     * @return this {@code Response} HTTP response with the status.
     */
    @RequestMapping(value = "/agree", method = RequestMethod.POST)
    @Operation(summary = "Start the negotiation for an specific asset, contract agreement and policy agreement")
    public Response negotiate(@Valid @RequestBody TokenRequest tokenRequestBody) {
        Response response = httpUtil.getInternalError();
        try {
            // Check for authentication
            if (!authService.isAuthenticated(httpRequest)) {
                response = httpUtil.getNotAuthorizedResponse();
                return httpUtil.buildResponse(response, httpResponse);
            }
           return contractService.doContractAgreement(httpRequest, httpResponse, tokenRequestBody);
        } catch (Exception e) {
            response.message = e.getMessage();
            return httpUtil.buildResponse(response, httpResponse);
        }
    }


    /**
     * HTTP POST method to decline a Passport negotiation.
     * <p>
     *
     * @param tokenRequestBody the {@code TokenRequest} object with the processId, contractId and the authentication token.
     * @return this {@code Response} HTTP response with the status.
     */
    @RequestMapping(value = "/decline", method = RequestMethod.POST)
    @Operation(summary = "Decline passport negotiation")
    public Response decline(@Valid @RequestBody TokenRequest tokenRequestBody) {
        Response response = httpUtil.getInternalError();

        // Check for authentication
        if (!authService.isAuthenticated(httpRequest)) {
            response = httpUtil.getNotAuthorizedResponse();
            return httpUtil.buildResponse(response, httpResponse);
        }
        try {
            return contractService.declineContractAgreement(httpRequest, httpResponse, tokenRequestBody);
        } catch (Exception e) {
            response.message = e.getMessage();
            return httpUtil.buildResponse(response, httpResponse);
        }

    }

}
