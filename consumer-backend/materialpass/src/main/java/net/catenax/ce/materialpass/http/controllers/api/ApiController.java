package net.catenax.ce.materialpass.http.controllers.api;

import net.catenax.ce.materialpass.managers.PassportManager;
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

@RestController
@RequestMapping("/api")
public class ApiController {
    private @Autowired HttpServletRequest httpRequest;
    private @Autowired HttpServletResponse httpResponse;
    private @Autowired DataTransferService dataService;

    @RequestMapping(value = "/passports/{id}", method = {RequestMethod.GET})
    public Response getPassport(@PathVariable("id") String passportId) {
        Response response = httpTools.getResponse();
        if(!httpTools.isInSession(httpRequest, "PassportManager")){
            httpTools.setSessionValue(httpRequest, "PassportManager", new PassportManager());
        }
        return response;
    }
    @RequestMapping(value = "/passports", method = {RequestMethod.GET})
    public Response getPassports() {
        Response response = httpTools.getResponse();
        response.data = dataService.getContractOfferCatalog(null);
        return response;
    }



}
