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

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import net.catenax.ce.productpass.exceptions.ControllerException;
import net.catenax.ce.productpass.models.dtregistry.SubModel;
import net.catenax.ce.productpass.models.http.Response;
import net.catenax.ce.productpass.models.negotiation.*;
import net.catenax.ce.productpass.models.passports.PassportV1;
import net.catenax.ce.productpass.services.AasService;
import net.catenax.ce.productpass.services.DataTransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tools.configTools;
import tools.httpTools;
import tools.logTools;
import tools.threadTools;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    public static final configTools configuration = new configTools();
    public final List<String> passportVersions = (List<String>) configuration.getConfigurationParam("passport.versions", ".", null);
    public static final String defaultProviderUrl = (String) configuration.getConfigurationParam("variables.default.providerUrl", ".", null);
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
        httpTools.redirect(httpResponse,"/passport");
        return httpTools.getResponse("Redirect to UI");
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
        if(providerUrl == null || providerUrl.equals("")){
            providerUrl = defaultProviderUrl;
        }
        Response response = httpTools.getResponse();
        ContractOffer contractOffer = null;
        try {
            contractOffer = this.getContractOfferByAssetId(assetId, providerUrl);
        } catch (ControllerException e) {
            response.message = e.getMessage();
            response.status = 500;
            response.statusText = "Server Internal Error";
            return httpTools.buildResponse(response, httpResponse);
        }
        if (contractOffer == null) {
            response.message = "Asset ID not found in any contract!";
            response.status = 404;
            response.statusText = "Not Found";
            return httpTools.buildResponse(response, httpResponse);
        }
        ;
        response.message = "Asset ID: " + assetId + " found in contractOffer [" + contractOffer.getId() + "]";
        response.data = contractOffer;
        return httpTools.buildResponse(response, httpResponse);
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
        // Initialize response
        Response response = httpTools.getResponse();
        try {
            // Configure digital twin registry query and params
            AasService.DigitalTwinRegistryQuery digitalTwinRegistry = aasService.new DigitalTwinRegistryQuery(assetId, idType, index);
            Thread digitalTwinRegistryThread = threadTools.runThread(digitalTwinRegistry);

            // Initialize variables
            Offer contractOffer = null;
            // Check if version is available
            if (!passportVersions.contains(version)) {
                response.message = "This passport version is not available at the moment!";
                response.status = 403;
                response.statusText = "FORBIDDEN";
                return httpTools.buildResponse(response, httpResponse);
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
                return httpTools.buildResponse(response, httpResponse);
            }
            if (connectorId.isEmpty() || connectorAddress.isEmpty()) {
                response.message = "Failed to get connectorId and connectorAddress!";
                response.status = 400;
                response.data = subModel;
                return httpTools.buildResponse(response, httpResponse);
            }


            /*[1]=========================================*/
            // Get catalog with all the contract offers
            try {
                contractOffer = this.getContractOfferByAssetId(assetId, connectorAddress);
            } catch (ControllerException e) {
                response.message = e.getMessage();
                response.status = 502;
                return httpTools.buildResponse(response, httpResponse);
            }

            // Check if contract offer was not received
            if (contractOffer == null) {
                response.message = "Asset ID not found in any contract!";
                response.status = 404;
                return httpTools.buildResponse(response, httpResponse);
            }


            /*[2]=========================================*/
            // Start Negotiation
            Negotiation negotiation;
            try {
                negotiation = dataService.doContractNegotiations(contractOffer);
                if (negotiation.getId() == null) {
                    response.message = "Negotiation Id not received, something went wrong";
                    response.status = 400;
                    return httpTools.buildResponse(response, httpResponse);
                }
            } catch (Exception e) {
                response.message = "Negotiation Id not received, something went wrong" + " [" + e.getMessage() + "]";
                response.status = 400;
                return httpTools.buildResponse(response, httpResponse);
            }

            /*[3]=========================================*/
            // Check for negotiation status
            try {
                negotiation = dataService.getNegotiation(negotiation.getId());
            } catch (Exception e) {
                response.message = "The negotiation for asset id failed!" + " [" + e.getMessage() + "]";
                response.status = 400;
                return httpTools.buildResponse(response, httpResponse);
            }
            if (negotiation.getState().equals("ERROR")) {
                response.message = "The negotiation for asset id failed!";
                response.status = 400;
                response.data = negotiation;
                return httpTools.buildResponse(response, httpResponse);
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
            Transfer transfer = new Transfer();
            try {
                transfer = dataService.initiateTransfer(transferRequest);
                if (transfer.getId() == null) {
                    response.message = "Transfer Id not received, something went wrong";
                    response.status = 400;
                    return httpTools.buildResponse(response, httpResponse);
                }
            } catch (Exception e) {
                response.message = e.getMessage();
                response.status = 500;
                return httpTools.buildResponse(response, httpResponse);
            }



            /*[8]=========================================*/
            // Check for transfer updates and the status
            try {
                transfer = dataService.getTransfer(transfer.getId());

                // If error return transfer message
                if (transfer.getState().equals("ERROR")) {
                    response.data = transfer;
                    response.message = "The transfer process failed!";
                    response.status = 400;
                    return httpTools.buildResponse(response, httpResponse);
                }
            } catch (Exception e) {
                response.message = e.getMessage();
                response.status = 500;
                return httpTools.buildResponse(response, httpResponse);
            }

            /*[9]=========================================*/
            // Get passport by versions
            if (version.equals("v1")) {
                // Get passport from consumer backend
                try {
                    return dataController.getPassport(transferRequest.getId());
                } catch (Exception e) {
                    logTools.printError("[" + transferRequest.getId() + "] It was not possible to retrieve passport from consumer backend in one step, waiting 5 seconds and retrying...");
                    Thread.sleep(5000);
                    return dataController.getPassport(transferRequest.getId());
                }
            }

            response.message = "Transfer process completed but passport version selected is not supported!";
            response.status = 403;
            return httpTools.buildResponse(response, httpResponse);
        } catch (InterruptedException e) {
            // Restore interrupted state...
            Thread.currentThread().interrupt();
            response.message = e.getMessage();
            response.status = 500;
            response.statusText = "INTERNAL SERVER ERROR";
            return httpTools.buildResponse(response, httpResponse);
        } catch (Exception e) {
            response.message = e.getMessage();
            response.status = 500;
            response.statusText = "INTERNAL SERVER ERROR";
            return httpTools.buildResponse(response, httpResponse);
        }

    }

}
