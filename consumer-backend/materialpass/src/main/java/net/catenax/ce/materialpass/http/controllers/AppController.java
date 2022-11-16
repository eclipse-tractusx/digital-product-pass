package net.catenax.ce.materialpass.http.controllers;

import net.catenax.ce.materialpass.http.models.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import tools.httpTools;

@RestController
public class AppController {

    @GetMapping("/")
    public Response index(){
        Response response = httpTools.getResponse();
        response.message = "Welcome to the Catena-X Consumer Backend!";
        return response;
    }
}
