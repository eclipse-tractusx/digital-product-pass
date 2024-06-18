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

package org.eclipse.tractusx.digitalproductpass.verification.http.controllers.api;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperties;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.eclipse.tractusx.digitalproductpass.core.models.http.Response;
import org.eclipse.tractusx.digitalproductpass.core.services.AuthenticationService;
import org.eclipse.tractusx.digitalproductpass.verification.config.VerificationConfig;
import org.eclipse.tractusx.digitalproductpass.verification.services.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import utils.HttpUtil;

import java.awt.print.Book;

/**
 * This class consists exclusively to define the HTTP methods needed for the verification controller used in the verification add-on.
 **/
@RestController
@RequestMapping("/api/verification")
@Tag(name = "Verification Add-on Controller",
        description = "This controller contains all the verification methods used by the frontend to verify the Digital Product Passports, " +
                "using the guidelines from the Digital Product Pass Verification Add-on using a simple-wallet component.")
@SecurityRequirement(name = "BearerAuthentication")
public class VerificationController {

    private @Autowired HttpServletRequest httpRequest;
    private @Autowired HttpServletResponse httpResponse;
    private @Autowired Environment env;
    private @Autowired AuthenticationService authService;
    private @Autowired VerificationConfig verificationConfig;
    private @Autowired WalletService walletService;
    private @Autowired HttpUtil httpUtil;

    /**
     * HTTP POST method to retrieve verified passport from the dpp-wallet with an API Key authentication.
     * <p>
     *
     * @param credential the {@code credential} object with the needed and optional parameters to retrieve the data.
     * @return this {@code Response} HTTP response with status.
     */
    @RequestMapping(value = "/verify", method = {RequestMethod.POST})
    @Operation(summary = "Verifies a Catena-X Digital Product Pass Verifiable Credential against the `simple-wallet` component from the digital product verification add-on", responses = {
            @ApiResponse(description = "The content of data will true if is verified or false if not", responseCode = "200", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Response.class))),
            @ApiResponse(description = "If the verification is not enabled or it was not possible to verify", responseCode = "403", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Response.class))),
            @ApiResponse(description = "If there was an error or an exception happened", responseCode = "500", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Response.class))),
            @ApiResponse(description = "If the user is not authorized", responseCode = "401", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Response.class)))
    })
    public Response verify(@RequestBody(description="Verifiable Credential with JsonWebSignature2020 proof type",content=@Content(mediaType="application/vc+ld+json", schema=@Schema(ref = "#/components/schemas/CertifiedDataCredential")), required = true) JsonNode credential) {
        Response response = httpUtil.getInternalError();
        if (!authService.isAuthenticated(httpRequest)) {
            response = httpUtil.getNotAuthorizedResponse();
            return httpUtil.buildResponse(response, httpResponse);
        }
        try {
            if (!this.verificationConfig.getEnabled()) {
                response = httpUtil.getForbiddenResponse("The verification service is disabled or not available!");
                response.data = false;
                return httpUtil.buildResponse(response, httpResponse);
            }
            if (credential == null) {
                response = httpUtil.getBadRequest();
                return httpUtil.buildResponse(response, httpResponse);
            }
            JsonNode verifiedResponse = null;
            try {
                verifiedResponse = walletService.startVerification(credential);
            } catch (Exception e) {
                response = httpUtil.getForbiddenResponse("Verifiable Credential was not able to be verified!");
                response.data = false;
                return httpUtil.buildResponse(response, httpResponse);
            }

            if (verifiedResponse == null) {
                response = httpUtil.getInternalError();
                return httpUtil.buildResponse(response, httpResponse);
            }

            if (!verifiedResponse.has("verified")) {
                response = httpUtil.getForbiddenResponse("Verifiable Credential was not able to be verified!");
                response.data = false;
                return httpUtil.buildResponse(response, httpResponse);
            }
            response = httpUtil.getResponse();
            if (verifiedResponse.has("message"))
                response.setMessage(verifiedResponse.get("message").asText());

            response.setData(verifiedResponse.get("verified"));

            return response;
        } catch (Exception e) {
            response.message = e.getMessage();
            return httpUtil.buildResponse(response, httpResponse);
        }

    }
}