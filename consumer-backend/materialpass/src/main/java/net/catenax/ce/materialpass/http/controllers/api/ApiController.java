package net.catenax.ce.materialpass.http.controllers.api;

import net.catenax.ce.materialpass.models.Response;
import net.catenax.ce.materialpass.managers.AssetManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tools.httpTools;
import tools.jsonTools;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api")
public class ApiController {
    private @Autowired HttpServletRequest httpRequest;
    private @Autowired HttpServletResponse httpResponse;
    @RequestMapping(value = "/assets/{id}", method = {RequestMethod.GET})
    public Response getAsset(@PathVariable("id") String assetId) {
        Response response = httpTools.getResponse("Asset ["+assetId+"] received");
        if(!httpTools.isInSession(httpRequest, "AssetManager")){
            httpTools.setSessionValue(httpRequest, "AssetManager", new AssetManager());
        }
        return response;
    }
    @RequestMapping(value = "/assets", method = {RequestMethod.GET})
    public Response getAssets() {
        Response response = httpTools.getResponse();
        if(!httpTools.isInSession(httpRequest, "AssetManager")){
            httpTools.setSessionValue(httpRequest, "AssetManager", new AssetManager());
        }
        AssetManager assetManager = (AssetManager) httpTools.getSessionValue(httpRequest, "AssetManager");
        response.data = jsonTools.dumpJson(assetManager.dataModel,4);

        return response;
    }

}
