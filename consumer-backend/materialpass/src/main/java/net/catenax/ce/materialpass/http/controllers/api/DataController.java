package net.catenax.ce.materialpass.http.controllers.api;

import net.catenax.ce.materialpass.managers.AssetManager;
import net.catenax.ce.materialpass.models.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tools.httpTools;
import tools.jsonTools;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/data")
public class DataController {
    private @Autowired HttpServletRequest httpRequest;
    private @Autowired HttpServletResponse httpResponse;

    @RequestMapping(value = "/catalog", method = {RequestMethod.GET})
    public Response getCatalog(@RequestParam(value="providerUrl") String providerUrl) {
        Response response = httpTools.getResponse();
        if(!httpTools.isInSession(httpRequest, "AssetManager")){
            httpTools.setSessionValue(httpRequest, "AssetManager", new AssetManager());
        }
        AssetManager assetManager = (AssetManager) httpTools.getSessionValue(httpRequest, "AssetManager");
        response.data = jsonTools.dumpJson(assetManager.dataModel,4);
        assetManager.saveDataModel();
        return response;
    }
}
