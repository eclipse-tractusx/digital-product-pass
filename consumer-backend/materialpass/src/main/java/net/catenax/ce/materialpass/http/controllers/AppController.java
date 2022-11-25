package net.catenax.ce.materialpass.http.controllers;

import net.catenax.ce.materialpass.models.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import tools.dateTimeTools;
import tools.httpTools;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class AppController {

    private @Autowired HttpServletRequest httpRequest;
    private @Autowired HttpServletResponse httpResponse;

    @GetMapping("/")
    public Response index(){
        Response response = httpTools.getResponse();
        response.message = "Welcome to the Catena-X Consumer Backend!";
        return response;
    }

    @GetMapping("/health")
    public Response health(){
        Response response = httpTools.getResponse(
                "RUNNING",
                200
        );
        response.data = dateTimeTools.getDateTimeFormatted(null);
        return response;
    }
}
