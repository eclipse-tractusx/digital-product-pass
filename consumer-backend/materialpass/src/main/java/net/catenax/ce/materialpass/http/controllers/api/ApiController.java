package net.catenax.ce.materialpass.http.controllers.api;

import net.catenax.ce.materialpass.models.Catalog;
import net.catenax.ce.materialpass.models.ContractOffer;
import net.catenax.ce.materialpass.models.Response;
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

    @RequestMapping(value = "/contracts/{assetId}", method = {RequestMethod.GET})
    public Response getContract(@PathVariable("assetId") String assetId) {
        Response response = httpTools.getResponse();
        Catalog catalog = dataService.getContractOfferCatalog(null);
        Map<String, Integer> offers =  catalog.loadContractOffersMapByAssetId();
        if(!offers.containsKey(assetId)){
            response.message = "Asset ID not found in any contract!";
            response.status = 404;
            response.statusText = "Not Found";
            return response;
        };

        Integer index = offers.get(assetId);
        ContractOffer contractOffer = catalog.getContractOffers().get(index);
        response.data = contractOffer;
        response.message = "Asset ID: " + assetId+" found in index [" + index+ "] of contractOffer ["+contractOffer.getId()+"]";
        return response;
    }
    @RequestMapping(value = "/contracts", method = {RequestMethod.GET})
    public Response getContracts() {
        Response response = httpTools.getResponse();
        response.data = dataService.getContractOfferCatalog(null);
        return response;
    }



}
