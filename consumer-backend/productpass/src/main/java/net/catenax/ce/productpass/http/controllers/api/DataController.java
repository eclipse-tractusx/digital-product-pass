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

package net.catenax.ce.productpass.http.controllers.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import net.catenax.ce.productpass.models.dtregistry.SubModel;
import net.catenax.ce.productpass.models.http.Response;
import net.catenax.ce.productpass.models.negotiation.Catalog;
import net.catenax.ce.productpass.models.negotiation.ContractOffer;
import net.catenax.ce.productpass.models.passports.PassportV1;
import net.catenax.ce.productpass.services.AasService;
import net.catenax.ce.productpass.services.DataTransferService;
import net.catenax.ce.productpass.services.VaultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tools.httpTools;
import tools.logTools;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/data")
@SecurityRequirement(name = "API-Key")
public class DataController {
    private @Autowired HttpServletRequest httpRequest;
    private @Autowired HttpServletResponse httpResponse;
    private @Autowired DataTransferService dataService;
    private @Autowired VaultService vaultService;
    private @Autowired AasService aasService;

    @RequestMapping(value = "/catalog", method = {RequestMethod.GET})
    @Operation(summary = "Returns contract offers catalog", responses = {
            @ApiResponse(description = "Gets contract offer catalog from provider", responseCode = "200", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Catalog.class)))
    })
    public Response getCatalog(@RequestParam(value = "providerUrl") String providerUrl) {
        Response response = httpTools.getResponse();
        response.data = dataService.getContractOfferCatalog(providerUrl);
        return httpTools.buildResponse(response, httpResponse);
    }

    @RequestMapping(value = "/submodel/{assetId}", method = {RequestMethod.GET})
    @Operation(summary = "Returns asset submodel by asset Id", responses = {
            @ApiResponse(description = "Gets submodel for specific asset", responseCode = "200", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = SubModel.class)))
    })
    public Response getSubmodel(@PathVariable("assetId") String assetId,
                                @RequestParam(value = "idType", required = false, defaultValue = "Battery_ID_DMC_Code") String idType,
                                @RequestParam(value = "index", required = false, defaultValue = "0") Integer index) {
        Response response = httpTools.getResponse();
        SubModel subModel;
        String connectorId;
        String connectorAddress;
        try {
            subModel = aasService.searchSubModel(idType, assetId, index);
            connectorId = subModel.getIdShort();
            connectorAddress = subModel.getEndpoints().get(index).getProtocolInformation().getEndpointAddress();
        } catch (Exception e) {
            response.message = "Failed to get subModel from digital twin registry" + " [" + e.getMessage() + "]";
            response.status = 404;
            return httpTools.buildResponse(response, httpResponse);
        }
        if (connectorId.isEmpty() || connectorAddress.isEmpty()) {
            response.message = "Failed to get connectorId and connectorAddress!";
            response.status = 400;
            response.data = subModel;
            return httpTools.buildResponse(response, httpResponse);
        }
        response.data = subModel;
        response.status = 200;
        return httpTools.buildResponse(response, httpResponse);
    }

    @RequestMapping(value = "/passport/{transferId}", method = {RequestMethod.GET})
    @Operation(summary = "Returns product passport by transfer process Id", responses = {
            @ApiResponse(description = "", responseCode = "200", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = PassportV1.class)))
    })
    public Response getPassport(@PathVariable("transferId") String transferId) {
        Response response = httpTools.getResponse();
        PassportV1 passportV1 = dataService.getPassportV1(transferId);
        if (passportV1 == null) {
            response.message = "Passport for transfer [" + transferId + "] not found!";
            response.status = 404;
            response.data = transferId;
            return httpTools.buildResponse(response, httpResponse);
        }
        response.data = passportV1;
        logTools.printMessage("Passport for transfer [" + transferId + "] retrieved successfully!");
        return httpTools.buildResponse(response, httpResponse);
    }
}
