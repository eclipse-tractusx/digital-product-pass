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

package org.eclipse.tractusx.productpass.http.controllers.api;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.eclipse.tractusx.productpass.exceptions.ControllerException;
import org.eclipse.tractusx.productpass.models.dtregistry.SubModel;
import org.eclipse.tractusx.productpass.models.http.Response;
import org.eclipse.tractusx.productpass.models.negotiation.*;
import org.eclipse.tractusx.productpass.models.passports.Passport;
import org.eclipse.tractusx.productpass.models.passports.PassportResponse;
import org.eclipse.tractusx.productpass.models.passports.PassportV1;
import org.eclipse.tractusx.productpass.services.AasService;
import org.eclipse.tractusx.productpass.services.DataTransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import utils.ConfigUtil;
import utils.HttpUtil;
import utils.LogUtil;
import utils.ThreadUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "API-Key")
public class ApiController {
    private @Autowired HttpServletRequest httpRequest;
    private @Autowired HttpServletResponse httpResponse;
    private @Autowired DataTransferService dataService;
    private @Autowired AasService aasService;
    private @Autowired DataController dataController;

    public static final ConfigUtil configuration = new ConfigUtil();
    public final List<String> passportVersions = (List<String>) configuration.getConfigurationParam("passport.versions", ".", null);
    public static final String defaultProviderUrl = (String) configuration.getConfigurationParam("variables.default.providerUrl", ".", null);

    public static final Integer maxRetries = (Integer) configuration.getConfigurationParam("maxRetries", ".", null);

    public Offer getContractOfferByAssetId(String assetId, String providerUrl) throws ControllerException {
        try {
            Catalog catalog = dataService.getContractOfferCatalog(providerUrl);
            Map<String, Integer> offers = catalog.loadContractOffersMapByAssetId();
            if (!offers.containsKey(assetId)) {
                return null;
            }
            Integer index = offers.get(assetId);
            return catalog.getContractOffers().get(index);
        } catch (Exception e) {
            throw new ControllerException(this.getClass().getName(), e, "It was not possible to get Contract Offer for assetId [" + assetId + "]");
        }
    }
    @RequestMapping(value="/api/*", method = RequestMethod.GET)
    @Hidden         // hide this endpoint from api documentation - swagger-ui
    Response index() throws Exception{
        HttpUtil.redirect(httpResponse,"/passport");
        return HttpUtil.getResponse("Redirect to UI");
    }

    @RequestMapping(value = "/contracts/{assetId}", method = {RequestMethod.GET})
    @Operation(summary = "Returns contracts by asset Id", responses = {
            @ApiResponse(description = "Returns specific contract", responseCode = "200", content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ContractOffer.class)))
    })
    public Response getContract(
        @PathVariable("assetId") String assetId,
        @RequestParam(value = "providerUrl", required = false, defaultValue = "") String providerUrl
    ) {
        // Check if user is Authenticated
        if(!HttpUtil.isAuthenticated(httpRequest)){
            Response response = HttpUtil.getNotAuthorizedResponse();
            return HttpUtil.buildResponse(response, httpResponse);
        }
        if(providerUrl == null || providerUrl.equals("")){
            providerUrl = defaultProviderUrl;
        }
        Response response = HttpUtil.getResponse();
        ContractOffer contractOffer = null;
        try {
            contractOffer = this.getContractOfferByAssetId(assetId, providerUrl);
        } catch (ControllerException e) {
            response.message = e.getMessage();
            response.status = 500;
            response.statusText = "Server Internal Error";
            return HttpUtil.buildResponse(response, httpResponse);
        }
        if (contractOffer == null) {
            response.message = "Asset ID not found in any contract!";
            response.status = 404;
            response.statusText = "Not Found";
            return HttpUtil.buildResponse(response, httpResponse);
        }
        ;
        response.message = "Asset ID: " + assetId + " found in contractOffer [" + contractOffer.getId() + "]";
        response.data = contractOffer;
        return HttpUtil.buildResponse(response, httpResponse);
    }


    /**
     * @param assetId Asset id that identifies the object that has a passport
     * @param idType  Type of asset id, the name of the code in the digital twin registry
     *                Default: "Battery_ID_DMC_Code"
     * @param index   Index from the asset in the digital twin registry
     *                Default: 0
     * @return PassportV1
     */
    @RequestMapping(value = "/passport/{version}/{assetId}", method = {RequestMethod.GET})
    @Operation(summary = "Returns versioned product passport by asset Id", responses = {
            @ApiResponse(description = "", responseCode = "200", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = PassportV1.class)))
    })
    public Response getPassport(
            @PathVariable("assetId") String assetId,
            @PathVariable("version") String version,
            @RequestParam(value = "idType", required = false, defaultValue = "Battery_ID_DMC_Code") String idType,
            @RequestParam(value = "index", required = false, defaultValue = "0") Integer index
    ) {
        // Check if user is Authenticated
        if(!HttpUtil.isAuthenticated(httpRequest)){
            Response response = HttpUtil.getNotAuthorizedResponse();
            return HttpUtil.buildResponse(response, httpResponse);
        }
        // Initialize response
        Response response = HttpUtil.getResponse();
        try {
            // Configure digital twin registry query and params
            AasService.DigitalTwinRegistryQuery digitalTwinRegistry = aasService.new DigitalTwinRegistryQuery(assetId, idType, index);
            Thread digitalTwinRegistryThread = ThreadUtil.runThread(digitalTwinRegistry);

            // Initialize variables
            Offer contractOffer = null;
            // Check if version is available
            if (!passportVersions.contains(version)) {
                response.message = "This passport version is not available at the moment!";
                response.status = 403;
                response.statusText = "FORBIDDEN";
                return HttpUtil.buildResponse(response, httpResponse);
            }

            // Wait for thread to close and give a response
            digitalTwinRegistryThread.join();
            SubModel subModel;
            String connectorId;
            String connectorAddress;
            try {
                subModel = digitalTwinRegistry.getSubModel();
                connectorId = subModel.getIdShort();
                connectorAddress = subModel.getEndpoints().get(index).getProtocolInformation().getEndpointAddress();
            } catch (Exception e) {
                response.message = "Failed to get subModel from digital twin registry!";
                response.status = 504;
                return HttpUtil.buildResponse(response, httpResponse);
            }
            if (connectorId.isEmpty() || connectorAddress.isEmpty()) {
                response.message = "Failed to get connectorId and connectorAddress!";
                response.status = 400;
                response.data = subModel;
                return HttpUtil.buildResponse(response, httpResponse);
            }


            /*[1]=========================================*/
            // Get catalog with all the contract offers
            try {
                contractOffer = this.getContractOfferByAssetId(assetId, connectorAddress);
            } catch (ControllerException e) {
                response.message = e.getMessage();
                response.status = 502;
                return HttpUtil.buildResponse(response, httpResponse);
            }

            // Check if contract offer was not received
            if (contractOffer == null) {
                response.message = "Asset ID not found in any contract!";
                response.status = 404;
                return HttpUtil.buildResponse(response, httpResponse);
            }


            /*[2]=========================================*/
            // Start Negotiation
            Negotiation negotiation;
            try {
                negotiation = dataService.doContractNegotiations(contractOffer);
            } catch (Exception e) {
                response.message = "Negotiation Id not received, something went wrong" + " [" + e.getMessage() + "]";
                response.status = 400;
                return HttpUtil.buildResponse(response, httpResponse);
            }

            if (negotiation.getId() == null) {
                response.message = "Negotiation Id not received, something went wrong";
                response.status = 400;
                return HttpUtil.buildResponse(response, httpResponse);
            }

            /*[3]=========================================*/
            // Check for negotiation status
            try {
                negotiation = dataService.getNegotiation(negotiation.getId());
            } catch (Exception e) {
                response.message = "The negotiation for asset id failed!" + " [" + e.getMessage() + "]";
                response.status = 400;
                return HttpUtil.buildResponse(response, httpResponse);
            }
            if (negotiation.getState().equals("ERROR")) {
                response.message = "The negotiation for asset id failed!";
                response.status = 400;
                response.data = negotiation;
                return HttpUtil.buildResponse(response, httpResponse);
            }


            /*[6]=========================================*/
            // Configure Transfer Request
            TransferRequest transferRequest = new TransferRequest(
                    DataTransferService.generateTransferId(negotiation, connectorId, connectorAddress),
                    connectorId,
                    connectorAddress,
                    negotiation.getContractAgreementId(),
                    assetId,
                    false,
                    "HttpProxy"
            );
            /*[7]=========================================*/
            // Initiate the transfer process
            Transfer transfer = null;
            try {
                transfer = dataService.initiateTransfer(transferRequest);
            } catch (Exception e) {
                response.message = e.getMessage();
                response.status = 500;
                return HttpUtil.buildResponse(response, httpResponse);
            }
            if (transfer.getId() == null) {
                response.message = "Transfer Id not received, something went wrong";
                response.status = 400;
                return HttpUtil.buildResponse(response, httpResponse);
            }


            /*[8]=========================================*/
            // Check for transfer updates and the status
            try {
                transfer = dataService.getTransfer(transfer.getId());
            } catch (Exception e) {
                response.message = e.getMessage();
                response.status = 500;
                return HttpUtil.buildResponse(response, httpResponse);
            }

            // If error return transfer message
            if (transfer.getState().equals("ERROR")) {
                response.data = transfer;
                response.message = "The transfer process failed!";
                response.status = 400;
                return HttpUtil.buildResponse(response, httpResponse);
            }

            /*[9]=========================================*/
            // Get passport by versions

            int actualRetries = 1;
            while (actualRetries <= maxRetries) {
                try {
                    response = dataController.getPassport(transferRequest.getId(), version);
                } catch (Exception e) {
                    LogUtil.printError("[" + transferRequest.getId() + "] Waiting 5 seconds and retrying #"+actualRetries+" of "+maxRetries+"... ");
                    Thread.sleep(5000);
                }
                if(response.data!=null){
                    break;
                }
                actualRetries++;
            }

            // Correct Response
            if(response.data != null) {
                Passport passport = (Passport) response.data;
                Map<String, Object> metadata = Map.of(
                        "contractOffer", contractOffer,
                        "negotiation", negotiation,
                        "transferRequest", transferRequest,
                        "transfer", transfer
                );
                response.data = new PassportResponse(metadata, passport);
                return HttpUtil.buildResponse(response, httpResponse);
            }

            // Error or Exception response
            if(response.message == null){
                response.message = "Passport for transfer [" + transferRequest.getId() + "] not found in provider!";
                response.status = 404;
                LogUtil.printError("["+response.status+" Not Found]: "+response.message);
            }
            return HttpUtil.buildResponse(response, httpResponse);

        } catch (InterruptedException e) {
            // Restore interrupted state...
            Thread.currentThread().interrupt();
            response.message = e.getMessage();
            response.status = 500;
            response.statusText = "INTERNAL SERVER ERROR";
            return HttpUtil.buildResponse(response, httpResponse);
        } catch (Exception e) {
            response.message = e.getMessage();
            response.status = 500;
            response.statusText = "INTERNAL SERVER ERROR";
            return HttpUtil.buildResponse(response, httpResponse);
        }

    }

}
