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

package org.eclipse.tractusx.productpass.http.controllers.api;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.bouncycastle.pqc.crypto.lms.LMOtsParameters;
import org.eclipse.tractusx.productpass.config.PassportConfig;
import org.eclipse.tractusx.productpass.exceptions.ControllerException;
import org.eclipse.tractusx.productpass.managers.ProcessManager;
import org.eclipse.tractusx.productpass.models.dtregistry.DigitalTwin;
import org.eclipse.tractusx.productpass.models.dtregistry.SubModel;
import org.eclipse.tractusx.productpass.models.http.Response;
import org.eclipse.tractusx.productpass.models.http.requests.TokenRequest;
import org.eclipse.tractusx.productpass.models.http.responses.IdResponse;
import org.eclipse.tractusx.productpass.models.manager.History;
import org.eclipse.tractusx.productpass.models.manager.Process;
import org.eclipse.tractusx.productpass.models.manager.Status;
import org.eclipse.tractusx.productpass.models.negotiation.*;
import org.eclipse.tractusx.productpass.models.passports.Passport;
import org.eclipse.tractusx.productpass.models.passports.PassportResponse;
import org.eclipse.tractusx.productpass.models.passports.PassportV3;
import org.eclipse.tractusx.productpass.services.AasService;
import org.eclipse.tractusx.productpass.services.AuthenticationService;
import org.eclipse.tractusx.productpass.services.DataTransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;
import utils.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Tag(name = "API Controller")
@SecurityRequirement(name = "BearerAuthentication")
public class ApiController {
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

    @RequestMapping(value="/api/*", method = RequestMethod.GET)
    @Hidden         // hide this endpoint from api documentation - swagger-ui
    Response index() throws Exception{
        httpUtil.redirect(httpResponse,"/passport");
        return httpUtil.getResponse("Redirect to UI");
    }

    @RequestMapping(value = "/passport", method = {RequestMethod.POST})
    @Operation(summary = "Returns versioned product passport by id", responses = {
            @ApiResponse(description = "Default Response Structure", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Response.class))),
            @ApiResponse(description = "Content of Data Field in Response", responseCode = "200", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = PassportResponse.class))),
            @ApiResponse(description = "Content of Passport Field in Data Field",useReturnTypeSchema = true, content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = PassportV3.class)))
    })
    public Response getPassport(@Valid @RequestBody TokenRequest tokenRequestBody) {
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
            History history = status.getHistory("contract-dataset");
            if (!history.getId().equals(contractId)) {
                response = httpUtil.getBadRequest("This contract id is incorrect!");
                return httpUtil.buildResponse(response, httpResponse);
            }

            // Check the validity of the token
            String expectedToken = processManager.generateToken(process, contractId);
            String token = tokenRequestBody.getToken();
            if (!expectedToken.equals(token)) {
                response = httpUtil.getForbiddenResponse("The token is invalid!");
                return httpUtil.buildResponse(response, httpResponse);
            }

            if (status.historyExists("contract-decline")) {
                response = httpUtil.getForbiddenResponse("The contract for this passport has been declined!");
                return httpUtil.buildResponse(response, httpResponse);
            }

            if (!status.historyExists("transfer-completed")) {
                response = httpUtil.getNotFound("The passport transfer was not completed!");
                return httpUtil.buildResponse(response, httpResponse);
            }

            if (!status.historyExists("passport-received")) {
                response = httpUtil.getNotFound("The passport is not available!");
                return httpUtil.buildResponse(response, httpResponse);
            }

            if (status.historyExists("passport-retrieved")) {
                response = httpUtil.getNotFound("The passport was already retrieved and is no longer available!");
                return httpUtil.buildResponse(response, httpResponse);
            }


            PassportV3 passport = processManager.loadPassport(processId);

            if(passport == null){
                response = httpUtil.getNotFound("Failed to load passport!");
                return httpUtil.buildResponse(response, httpResponse);
            }
            Dataset dataset = processManager.loadDataset(processId);
            Map<String, Object> negotiation = processManager.loadNegotiation(processId);
            Map<String, Object> transfer =processManager.loadTransfer(processId);
            response = httpUtil.getResponse();
            response.data = Map.of(
                    "contract", dataset,
                    "negotiation", negotiation,
                    "transfer", transfer,
                    "passport", passport
            );
            return httpUtil.buildResponse(response, httpResponse);
        } catch (Exception e) {
            response.message = e.getMessage();
            return httpUtil.buildResponse(response, httpResponse);
        }
    }

}
