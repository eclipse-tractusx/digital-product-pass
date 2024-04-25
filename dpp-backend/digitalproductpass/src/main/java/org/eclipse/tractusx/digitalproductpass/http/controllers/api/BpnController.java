/*********************************************************************************
 *
 * Tractus-X - Digital Product Pass Application
 *
 * Copyright (c) 2022, 2024 BMW AG, Henkel AG & Co. KGaA
 * Copyright (c) 2022, 2024 CGI Deutschland B.V. & Co. KG
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
import org.eclipse.tractusx.digitalproductpass.models.bpn.BpnAddress;
import org.eclipse.tractusx.digitalproductpass.models.bpn.BpnCompany;
import org.eclipse.tractusx.digitalproductpass.models.http.Response;
import org.eclipse.tractusx.digitalproductpass.models.http.requests.BpnRequest;
import org.eclipse.tractusx.digitalproductpass.models.http.requests.TokenRequest;
import org.eclipse.tractusx.digitalproductpass.models.http.responses.BpnResponse;
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

import java.util.Map;


/**
 * This class consists exclusively to define the HTTP methods needed for the BPDM integration
 **/
@RestController
@RequestMapping("/api/bpn")
@Tag(name = "API Controller")
@SecurityRequirement(name = "BearerAuthentication")
public class BpnController {
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

    @RequestMapping(value = "/bpn/*", method = RequestMethod.GET)
    @Hidden
        // hide this endpoint from api documentation - swagger-ui
    Response index() throws Exception {
        httpUtil.redirect(httpResponse, "/passport");
        return httpUtil.getResponse("Redirect to UI");
    }
    /**
     * HTTP POST method to retrieve information from the BPDM Service
     * <p>
     * @param   bpnRequestBody
     *          the {@code BpnRequest} object with the list of bpns.
     *
     * @return this {@code Response} HTTP response with status.
     *
     */
    @RequestMapping(value = "/request", method = {RequestMethod.POST})
    @Operation(summary = "Requests information about a company information + address, which will be requested in the BPDM service", responses = {
            @ApiResponse(description = "Default Response Structure", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Response.class))),
            @ApiResponse(description = "Content of Data Field in Response", responseCode = "200", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BpnResponse.class))),
    })
    public Response getCompanyInfo(@Valid @RequestBody BpnRequest bpnRequestBody) {
        Response response = httpUtil.getResponse();
        // Do the search in the BPDM Service

        // Mock response
        BpnResponse bpnResponse = new BpnResponse(
                Map.of("BPNL000000000001",
                            new BpnCompany(
                                "Company-X Inc.",
                                    new BpnAddress(
                                            "Germany",
                                    "Ulm",
                                    "Beim Alten Fritz 2",
                                    "89075"
                                    )
                            )
                        )
        );
        response.setData(bpnResponse);
        return httpUtil.buildResponse(response, httpResponse);
    }
}
