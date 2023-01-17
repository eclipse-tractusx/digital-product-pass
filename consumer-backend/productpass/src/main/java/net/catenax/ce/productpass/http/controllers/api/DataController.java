package net.catenax.ce.productpass.http.controllers.api;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import net.catenax.ce.productpass.models.dtregistry.SubModel;
import net.catenax.ce.productpass.models.http.Response;
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
@RequestMapping("/data")
@SecurityRequirement(name = "API-Key")
public class DataController {
    private @Autowired HttpServletRequest httpRequest;
    private @Autowired HttpServletResponse httpResponse;
    private @Autowired DataTransferService dataService;
    private @Autowired VaultService vaultService;
    private @Autowired AasService aasService;

    @RequestMapping(value = "/catalog", method = {RequestMethod.GET})
    public Response getCatalog(@RequestParam(value = "providerUrl") String providerUrl) {
        Response response = httpTools.getResponse();
        response.data = dataService.getContractOfferCatalog(providerUrl);
        return httpTools.buildResponse(response, httpResponse);
    }

    @RequestMapping(value = "/submodel/{assetId}", method = {RequestMethod.GET})
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
