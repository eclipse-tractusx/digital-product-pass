package net.catenax.ce.materialpass.http.controllers.api;

import net.catenax.ce.materialpass.managers.AssetManager;
import net.catenax.ce.materialpass.models.Response;
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

    @RequestMapping(value = "/assets/{id}", method = {RequestMethod.GET})
    public Response getAsset(@PathVariable("id") String assetId) {
        Response response = httpTools.getResponse("Asset ["+assetId+"] received");
        if(!httpTools.isInSession(httpRequest, "AssetManager")){
            httpTools.setSessionValue(httpRequest, "AssetManager", new AssetManager());
        }
        return response;
    }


}
