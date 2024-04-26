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
import org.eclipse.tractusx.digitalproductpass.models.bpn.AddressInfo;
import org.eclipse.tractusx.digitalproductpass.models.bpn.BpnCompany;
import org.eclipse.tractusx.digitalproductpass.models.http.Response;
import org.eclipse.tractusx.digitalproductpass.models.http.requests.BpnRequest;
import org.eclipse.tractusx.digitalproductpass.models.http.requests.TokenRequest;
import org.eclipse.tractusx.digitalproductpass.models.http.responses.BpnResponse;
import org.eclipse.tractusx.digitalproductpass.models.passports.PassportResponse;
import org.eclipse.tractusx.digitalproductpass.services.AasService;
import org.eclipse.tractusx.digitalproductpass.services.AuthenticationService;
import org.eclipse.tractusx.digitalproductpass.services.BpdmService;
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
    private @Autowired BpdmService bpdmService;
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
     * HTTP POST method to retrieve information from the BPDM Service for all the BPNL, BPNS, BPNA
     * <p>
     * @param   bpnRequestBody
     *          the {@code BpnRequest} object with the list of bpns.
     *
     * @return this {@code Response} HTTP response with status.
     *
     */
    @RequestMapping(value = "/request", method = {RequestMethod.POST})
    @Operation(summary = "Requests information about a company information + address, which will be requested in the BPDM service for BPNL, BPNS and BPNS", responses = {
            @ApiResponse(description = "Default Response Structure", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Response.class))),
            @ApiResponse(description = "Content of Data Field in Response", responseCode = "200", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BpnResponse.class))),
    })
    public Response getCompanyInfo(@Valid @RequestBody BpnRequest bpnRequestBody) {
        Response response = httpUtil.getInternalError("It was not possible to get the company/site/address information with the selected BPNs!");
        try {
            // Check for authentication
            if (!authService.isAuthenticated(httpRequest)) {
                response = httpUtil.getNotAuthorizedResponse();
                return httpUtil.buildResponse(response, httpResponse);
            }
            // Check for the fields
            BpnResponse bpnResponse = new BpnResponse();
            List<String> bpnl = bpnRequestBody.getLegalEntities();
            List<String> bpns = bpnRequestBody.getSites();
            List<String> bpna = bpnRequestBody.getAddresses();

            // If all the fields are empty return an error
            if(bpnl == null && bpns == null && bpna == null){
                return httpUtil.buildResponse(httpUtil.getBadRequest("No parameters specified!"), httpResponse);
            }

            // If the bpnl field is not empty try to retrieve information
            if(bpnl != null && !bpnl.isEmpty()){
                // Request legal entity information from the bpdm service (with company info and address)
                Map<String, BpnCompany> companyInfo = bpdmService.requestLegalEntityInformation(bpnl);
                // If is not possible to get any info return an error
                if(companyInfo == null || companyInfo.isEmpty()){
                    return httpUtil.buildResponse(httpUtil.getInternalError("It was not possible to request legal entities information"), httpResponse);
                }
                // Set legal entity information to the response
                bpnResponse.setLegalEntities(companyInfo);
            }

            // If the bpns field is not empty try to retrieve information
            if(bpns != null && !bpns.isEmpty()){
                // Request site information from the bpdm service
                Map<String, AddressInfo> siteInfo = bpdmService.getBpnsInformation(bpns);
                // If is not possible to get any info return an error
                if(siteInfo == null || siteInfo.isEmpty()){
                    return httpUtil.buildResponse(httpUtil.getInternalError("It was not possible to request sites information"), httpResponse);
                }
                // Set sites information to the response
                bpnResponse.setSites(siteInfo);
            }

            // If the bpna field is not empty try to retrieve information
            if(bpna != null && !bpna.isEmpty()){
                // Request address information from the bpdm service
                Map<String, AddressInfo> addressInfo = bpdmService.getBpnaInformation(bpna);
                // If is not possible to get any info return an error
                if(addressInfo == null || addressInfo.isEmpty()){
                    return httpUtil.buildResponse(httpUtil.getInternalError("It was not possible to request addresses information"), httpResponse);
                }
                // Set address information to the response
                bpnResponse.setAddresses(addressInfo);
            }

            // If no information was found return an error!
            if(bpnResponse.isEmpty()){
                return httpUtil.buildResponse(httpUtil.getNotFound("No business partner information was found for the different BPN types!"), httpResponse);
            }

            // Add the bpn response to the data
            response.data = bpnResponse;
            return httpUtil.buildResponse(response, httpResponse);
        } catch (Exception e) {
            response.message = e.getMessage();
            return httpUtil.buildResponse(response, httpResponse);
        }
    }
}
