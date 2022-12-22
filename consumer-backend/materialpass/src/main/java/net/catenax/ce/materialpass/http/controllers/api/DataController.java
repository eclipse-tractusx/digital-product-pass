package net.catenax.ce.materialpass.http.controllers.api;

import net.catenax.ce.materialpass.models.Response;
import net.catenax.ce.materialpass.services.DataTransferService;
import net.catenax.ce.materialpass.services.VaultService;
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
    private @Autowired VaultService vaultService;

    @RequestMapping(value = "/catalog", method = {RequestMethod.GET})
    public Response getCatalog(@RequestParam(value="providerUrl") String providerUrl) {
        Response response = httpTools.getResponse();
<<<<<<< HEAD
        response.data = dataService.getContractOfferCatalog(providerUrl);
=======
        if(!httpTools.isInSession(httpRequest, "AssetManager")){
            httpTools.setSessionValue(httpRequest, "AssetManager", new AssetManager());
        }
        AssetManager assetManager = (AssetManager) httpTools.getSessionValue(httpRequest, "AssetManager");
        response.data = jsonTools.dumpJson(assetManager.dataModel,4);
        assetManager.saveDataModel();
>>>>>>> 345bda961b5219ed02e168b98962b81306803480
        return response;
    }
}
