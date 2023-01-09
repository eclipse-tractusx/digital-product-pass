package net.catenax.ce.productpass.http.controllers.api;

import net.catenax.ce.productpass.exceptions.ControllerException;
import net.catenax.ce.productpass.models.http.Response;
import net.catenax.ce.productpass.models.dtregistry.SubModel;
import net.catenax.ce.productpass.models.negotiation.*;
import net.catenax.ce.productpass.models.passports.PassportV1;
import net.catenax.ce.productpass.services.AasService;
import net.catenax.ce.productpass.services.DataTransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tools.configTools;
import tools.httpTools;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiController {
    private @Autowired HttpServletRequest httpRequest;
    private @Autowired HttpServletResponse httpResponse;
    private @Autowired DataTransferService dataService;
    private @Autowired AasService aasService;

    public static final configTools configuration = new configTools();
    public final List<String> passportVersions = (List<String>) configuration.getConfigurationParam("passport.versions", ".", null);

    public Offer getContractOfferByAssetId(String assetId) throws ControllerException {
        try {
            Catalog catalog = dataService.getContractOfferCatalog(null);
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

    @RequestMapping(value = "/contracts/{assetId}", method = {RequestMethod.GET})
    public Response getContract(@PathVariable("assetId") String assetId) {
        Response response = httpTools.getResponse();
        ContractOffer contractOffer = null;
        try {
            contractOffer = this.getContractOfferByAssetId(assetId);
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
    public Response getPassport(
            @PathVariable("assetId") String assetId,
            @PathVariable("version") String version,
            @RequestParam(value = "idType", required = false, defaultValue = "Battery_ID_DMC_Code") String idType,
            @RequestParam(value = "index", required = false, defaultValue = "0") Integer index
    ) {
        // Initialize response
        Response response = httpTools.getResponse();
        try {
            // Initialize variables
            Offer contractOffer = null;
            // Check if version is available
            if (!passportVersions.contains(version)) {
                response.message = "This passport version is not available at the moment!";
                response.status = 403;
                response.statusText = "FORBIDDEN";
                return httpTools.buildResponse(response, httpResponse);
            }

            /*[1]=========================================*/
            // Get catalog with all the contract offers
            try {
                contractOffer = this.getContractOfferByAssetId(assetId);
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

            /*[5]=========================================*/
            // Check data in digital twin registry
            SubModel subModel;
            String connectorId;
            String connectorAddress;
            try {
                subModel = aasService.searchSubModel(idType, assetId, index);
                connectorId = subModel.getIdShort();
                connectorAddress = subModel.getEndpoints().get(index).getProtocolInformation().getEndpointAddress();
            } catch (Exception e) {
                response.message = "Failed to get subModel from digital twin registry" + " [" + e.getMessage() + "]";
                response.status = 504;
                return httpTools.buildResponse(response, httpResponse);
            }
            if (connectorId.isEmpty() || connectorAddress.isEmpty()) {
                response.message = "Failed to get connectorId and connectorAddress!";
                response.status = 400;
                response.data = subModel;
                return httpTools.buildResponse(response, httpResponse);
            }
            /*[6]=========================================*/
            // Configure Transfer Request
            TransferRequest transferRequest = new TransferRequest(
                    DataTransferService.generateTransferId(negotiation, connectorId, connectorAddress),
                    connectorId,
                    connectorAddress,
                    negotiation.getContractAgreementId(),
                    contractOffer.getAssetId(),
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
                PassportV1 passportV1 = dataService.getPassportV1(transferRequest.getId());
                if (passportV1 == null) {
                    response.message = "Was not possible to get the passport!";
                    response.status = 400;
                    return httpTools.buildResponse(response, httpResponse);
                }
                response.data = passportV1;
                return httpTools.buildResponse(response, httpResponse);
            }

            response.message = "Transfer process completed but passport version selected is not supported!";
            response.status = 403;
            return httpTools.buildResponse(response, httpResponse);
        } catch (Exception e) {
            response.message = e.getMessage();
            response.status = 500;
            return httpTools.buildResponse(response, httpResponse);
        }
    }

}
