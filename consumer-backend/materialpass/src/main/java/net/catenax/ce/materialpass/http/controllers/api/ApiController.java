package net.catenax.ce.materialpass.http.controllers.api;

import net.catenax.ce.materialpass.exceptions.ControllerException;
import net.catenax.ce.materialpass.models.*;
import net.catenax.ce.materialpass.services.AasService;
import net.catenax.ce.materialpass.services.DataTransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import tools.httpTools;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiController {
    private @Autowired HttpServletRequest httpRequest;
    private @Autowired HttpServletResponse httpResponse;
    private @Autowired DataTransferService dataService;
    private @Autowired AasService aasService;

    public Offer getContractOfferByAssetId(String assetId) throws ControllerException {
        try {
            Catalog catalog = dataService.getContractOfferCatalog(null);
            Map<String, Integer> offers = catalog.loadContractOffersMapByAssetId();
            if (!offers.containsKey(assetId)) {
                return null;
            }
            Integer index = offers.get(assetId);
            return catalog.getContractOffers().get(index);
        }catch (Exception e) {
            throw new ControllerException(this.getClass().getName(), e, "It was not possible to get Contract Offer for assetId [" + assetId+"]");
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
        if(contractOffer == null){
            response.message = "Asset ID not found in any contract!";
            response.status = 404;
            response.statusText = "Not Found";
            return httpTools.buildResponse(response, httpResponse);
        };
        response.message = "Asset ID: " + assetId+" found in contractOffer ["+contractOffer.getId()+"]";
        response.data = contractOffer;
        return httpTools.buildResponse(response, httpResponse);
    }

    @RequestMapping(value = "/negotiate/{assetId}", method = {RequestMethod.GET})
    public Response negotiate(@PathVariable("assetId") String assetId) {
        Response response = httpTools.getResponse();
        Offer contractOffer = null;
        try {
            contractOffer = this.getContractOfferByAssetId(assetId);
        } catch (ControllerException e) {
            response.message = e.getMessage();
            response.status = 502;
            return httpTools.buildResponse(response, httpResponse);
        }
        if(contractOffer == null){
            response.message = "Asset ID not found in any contract!";
            response.status = 404;
            return httpTools.buildResponse(response, httpResponse);
        };
        Negotiation negotiation = dataService.doContractNegotiations(contractOffer);
        if(negotiation.getId() == null){
            response.message = "Negotiation Id not received, something went wrong";
            response.status = 400;
            return httpTools.buildResponse(response, httpResponse);
        }
        negotiation = dataService.getNegotiation(negotiation.getId());


        /// TODO: Hardcoded value
        int position = 0; // This position could be passed by parameter in the request maybe?
        String assetType = "Battery_ID_DMC_Code"; // Also hard coded

        SubModel subModel = aasService.searchSubModel(assetType, assetId, position);
        String connectorId = subModel.getIdShort();
        String connectorAddress = subModel.getEndpoints().get(position).getProtocolInformation().getEndpointAddress();
        Transfer transfer = dataService.initiateTransfer(negotiation, connectorId, connectorAddress, contractOffer,false);
        if(transfer.getId() == null){
            response.message = "Transfer Id not received, something went wrong";
            response.status = 400;
            return httpTools.buildResponse(response, httpResponse);
        }
        response.data = dataService.getTransfer(transfer.getId());
        return httpTools.buildResponse(response, httpResponse);
    }
}
