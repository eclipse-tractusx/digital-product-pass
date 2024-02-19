/*********************************************************************************
 *
 * Tractus-X - Digital Product Passport Application
 *
 * Copyright (c) 2022, 2024 BASF SE, BMW AG, Henkel AG & Co. KGaA
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

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.eclipse.tractusx.digitalproductpass.config.PassportConfig;
import org.eclipse.tractusx.digitalproductpass.managers.ProcessManager;
import org.eclipse.tractusx.digitalproductpass.models.http.Response;
import org.eclipse.tractusx.digitalproductpass.models.http.requests.TokenRequest;
import org.eclipse.tractusx.digitalproductpass.models.manager.History;
import org.eclipse.tractusx.digitalproductpass.models.manager.Process;
import org.eclipse.tractusx.digitalproductpass.models.manager.Status;
import org.eclipse.tractusx.digitalproductpass.models.negotiation.Dataset;
import org.eclipse.tractusx.digitalproductpass.models.passports.PassportResponse;
import org.eclipse.tractusx.digitalproductpass.services.AasService;
import org.eclipse.tractusx.digitalproductpass.services.AuthenticationService;
import org.eclipse.tractusx.digitalproductpass.services.DataTransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import utils.HttpUtil;
import utils.JsonUtil;

import java.util.List;
import java.util.Map;


/**
 * This class consists exclusively to define the HTTP methods needed for the API controller.
 **/
@RestController
@RequestMapping("/api")
@Tag(name = "API Controller")
@SecurityRequirement(name = "BearerAuthentication")
public class ApiController {

    /** ATTRIBUTES **/
    private @Autowired HttpServletRequest httpRequest;
    private @Autowired HttpServletResponse httpResponse;
    private @Autowired DataTransferService dataService;
    private @Autowired AasService aasService;
    private @Autowired Environment env;
    private @Autowired AuthenticationService authService;
    private @Autowired PassportConfig passportConfig;
    private @Autowired HttpUtil httpUtil;
    private @Autowired JsonUtil jsonUtil;
    private @Autowired ProcessManager processManager;

    /** METHODS **/

    @RequestMapping(value = "/api/*", method = RequestMethod.GET)
    @Hidden
        // hide this endpoint from api documentation - swagger-ui
    Response index() throws Exception {
        httpUtil.redirect(httpResponse, "/passport");
        return httpUtil.getResponse("Redirect to UI");
    }

    /**
     * HTTP POST method to retrieve the Passport.
     * <p>
     * @param   tokenRequestBody
     *          the {@code TokenRequest} object with the processId, contractId and the authentication token.
     *
     * @return this {@code Response} HTTP response with status.
     *
     */
    @RequestMapping(value = "/data", method = {RequestMethod.POST})
    @Operation(summary = "Returns the data negotiated and transferred", responses = {
            @ApiResponse(description = "Default Response Structure", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Response.class))),
            @ApiResponse(description = "Content of Data Field in Response", responseCode = "200", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = PassportResponse.class))),
    })
    public Response getData(@Valid @RequestBody TokenRequest tokenRequestBody) {
        Response response = httpUtil.getInternalError();

        // Check for authentication
        if (!authService.isAuthenticated(httpRequest)) {
            response = httpUtil.getNotAuthorizedResponse();
            return httpUtil.buildResponse(response, httpResponse);
        }
        try {
            // Check for the mandatory fields
            List<String> mandatoryParams = List.of("processId", "contractId", "token");
            if (!jsonUtil.checkJsonKeys(tokenRequestBody, mandatoryParams, ".", false)) {
                response = httpUtil.getBadRequest("One or all the mandatory parameters " + mandatoryParams + " are missing");
                return httpUtil.buildResponse(response, httpResponse);
            }

            // Check for processId
            String processId = tokenRequestBody.getProcessId();
            if (!processManager.checkProcess(httpRequest, processId)) {
                response = httpUtil.getBadRequest("The process id does not exists!");
                return httpUtil.buildResponse(response, httpResponse);
            }


            Process process = processManager.getProcess(httpRequest, processId);
            if (process == null) {
                response = httpUtil.getBadRequest("The process id does not exists!");
                return httpUtil.buildResponse(response, httpResponse);
            }

            // Get status to check for contract id
            String contractId = tokenRequestBody.getContractId();
            Status status = processManager.getStatus(processId);

            if (status.historyExists("contract-decline")) {
                response = httpUtil.getForbiddenResponse("The contract for this passport has been declined!");
                return httpUtil.buildResponse(response, httpResponse);
            }
            if (status.historyExists("negotiation-canceled")) {
                response = httpUtil.getForbiddenResponse("This negotiation has been canceled! Please request a new one");
                return httpUtil.buildResponse(response, httpResponse);
            }

            // Check if the contract id is correct
            Map<String, Dataset> availableContracts = processManager.loadDatasets(processId);
            String seedId = String.join("|",availableContracts.keySet()); // Generate Seed
            // Check the validity of the token
            String expectedToken = processManager.generateToken(process, seedId);
            String token = tokenRequestBody.getToken();
            if (!expectedToken.equals(token)) {
                response = httpUtil.getForbiddenResponse("The token is invalid!");
                return httpUtil.buildResponse(response, httpResponse);
            }
            Dataset dataset = availableContracts.get(contractId);

            if (dataset == null) {
                response.message = "The Contract Selected was not found!";
                return httpUtil.buildResponse(response, httpResponse);
            }

            if (!status.historyExists("data-received")) {
                status = processManager.getStatus(processId); // Retry to get the status before giving an error
                if(!status.historyExists("data-received")) {
                    response = httpUtil.getNotFound("The data is not available!");
                    return httpUtil.buildResponse(response, httpResponse);
                }
            }

            if (status.historyExists("data-retrieved")) {
                response = httpUtil.getNotFound("The data was already retrieved and is no longer available!");
                return httpUtil.buildResponse(response, httpResponse);
            }
            String semanticId = status.getSemanticId();
            JsonNode passport = processManager.loadPassport(processId);
            if (passport == null) {
                response = httpUtil.getNotFound("Failed to load passport!");
                return httpUtil.buildResponse(response, httpResponse);
            }
            Map<String, Object> negotiation = processManager.loadNegotiation(processId);
            Map<String, Object> transfer = processManager.loadTransfer(processId);
            response = httpUtil.getResponse();
            response.data = Map.of(
                    "metadata", Map.of(
                            "contract", dataset,
                            "negotiation", negotiation,
                            "transfer", transfer
                    ),
                    "aspect", passport,
                    "semanticId", semanticId
            );
            return httpUtil.buildResponse(response, httpResponse);
        } catch (Exception e) {
            response.message = e.getMessage();
            return httpUtil.buildResponse(response, httpResponse);
        }
    }

}
