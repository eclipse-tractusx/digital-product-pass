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
        boolean error = false;
        try {
            contractOffer = this.getContractOfferByAssetId(assetId);
        } catch (ControllerException e) {
            error = true;
        }
        if(error || contractOffer == null){
            response.message = "Asset ID not found in any contract!";
            response.status = 404;
            response.statusText = "Not Found";
            return response;
        };
        response.message = "Asset ID: " + assetId+" found in contractOffer ["+contractOffer.getId()+"]";
        response.data = contractOffer;
        return response;
    }

    @RequestMapping(value = "/contracts", method = {RequestMethod.GET})
    public Response getContracts() {
        Response response = httpTools.getResponse();
        response.data = dataService.getContractOfferCatalog(null);
        return response;
    }

    @RequestMapping(value = "/negotiate/{assetId}", method = {RequestMethod.GET})
    public Response negotiate(@PathVariable("assetId") String assetId) {
        Response response = httpTools.getResponse();
        Offer contractOffer = null;
        boolean error = false;
        try {
            contractOffer = this.getContractOfferByAssetId(assetId);
        } catch (ControllerException e) {
            error = true;
        }
        if(error || contractOffer == null){
            response.message = "Asset ID not found in any contract!";
            response.status = 404;
            response.statusText = "Not Found";
            return response;
        };
        Negotiation negotiation = dataService.doContractNegotiations(contractOffer);
        if(negotiation.getId() == null){
            response.message = "Negotiation Id not received, something went wrong";
            response.status = 400;
            response.statusText = "Negotiation Failed";
            return response;
        }
        negotiation = dataService.getNegotiation(negotiation.getId());


        /// TODO: Hardcoded value
        int position = 0; // This position could be passed by parameter in the request maybe?
        String assetType = "Battery_ID_DMC_Code"; // Also hard coded

        SubModel subModel = aasService.searchSubModel(assetType, assetId, position);
        String contractId = subModel.getIdShort();
        String connectorAddress = subModel.getEndpoints().get(position).getProtocolInformation().getEndpointAddress();
        Transfer transfer = dataService.doTransferProcess(negotiation,contractId, connectorAddress,contractOffer,false);
        if(transfer.getId() == null){
            response.message = "Transfer Id not received, something went wrong";
            response.status = 400;
            response.statusText = "Transfer Failed";
            return response;
        }
        response.data = dataService.getTransfer(transfer.getId());
        return response;
    }
}
