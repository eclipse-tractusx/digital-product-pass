package net.catenax.ce.materialpass.http.controllers.api;

import net.catenax.ce.materialpass.models.Response;
import net.catenax.ce.materialpass.services.DataTransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tools.httpTools;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/data")
public class DataController {
    private @Autowired HttpServletRequest httpRequest;
    private @Autowired HttpServletResponse httpResponse;
    private @Autowired DataTransferService dataService;
    @RequestMapping(value = "/catalog", method = {RequestMethod.GET})
    public Response getCatalog(@RequestParam(value="providerUrl") String providerUrl) {
        Response response = httpTools.getResponse();
        response.data = dataService.getContractOfferCatalog(providerUrl);
        return response;
    }
}
